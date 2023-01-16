package com.infocargas.freteapp.service.schedule;

import com.google.gson.Gson;
import com.infocargas.freteapp.controller.FacebookController;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.WhatsAppType;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import com.infocargas.freteapp.service.PerfilService;
import com.infocargas.freteapp.service.RotasOfertasService;
import com.infocargas.freteapp.service.WhatsMessageBatchService;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.dto.WhatsMessageBatchDTO;
import com.infocargas.freteapp.service.dto.routes.GoogleLegs;
import com.infocargas.freteapp.service.dto.routes.GoogleRoutes;
import com.infocargas.freteapp.utils.GeoUtils;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class FindNearRouteSchedule {

    private final Logger log = LoggerFactory.getLogger(FindNearRouteSchedule.class);

    private final RotasOfertasService ofertasService;
    private final FacebookController facebookController;

    private final PerfilService perfilService;

    private final WhatsMessageBatchService whatsMessageBatchService;

    public FindNearRouteSchedule(
        RotasOfertasService ofertasService,
        FacebookController facebookController,
        PerfilService perfilService,
        WhatsMessageBatchService whatsMessageBatchService
    ) {
        this.ofertasService = ofertasService;
        this.facebookController = facebookController;
        this.perfilService = perfilService;
        this.whatsMessageBatchService = whatsMessageBatchService;
    }

    @Scheduled(fixedDelayString = "PT35M")
    public void updateWhatsStatus() {
        var ofertas = ofertasService.findByExpired(StatusOferta.AGUARDANDO_PROPOSTA);

        ofertas.forEach(data -> {
            data.getOfertas().setStatus(StatusOferta.CANCELED);
            ofertasService.save(data);
        });
    }

    @Scheduled(fixedDelayString = "PT35M")
    public void updateStatusOferta() {
        var whatsStatus = this.whatsMessageBatchService.findByRouteStatus(WhatsStatus.OPEN);
        whatsStatus.forEach(whats -> {
            whats.setStatus(WhatsStatus.CLOSED);
            whatsMessageBatchService.save(whats);
        });
    }

    @Scheduled(fixedDelayString = "PT02M")
    public void getNearRoute() {
        log.info("Start scanning nears route to user.....");

        Gson g = new Gson();
        List<RotasOfertasDTO> ofertasList = ofertasService.findAllByStatusAndDate(StatusOferta.AGUARDANDO_PROPOSTA);

        for (RotasOfertasDTO rotasOfertas : ofertasList) {
            List<OfertasDTO> selected = new ArrayList<>();

            GoogleRoutes[] gRoutesArr = g.fromJson(rotasOfertas.getRotas(), GoogleRoutes[].class);

            GoogleRoutes gRoutes = gRoutesArr[0];

            var perfilId = rotasOfertas.getOfertas().getPerfil().getId();

            var whatsService = whatsMessageBatchService.findByNearRouteStatus(perfilId.intValue(), WhatsStatus.OPEN);

            if (whatsService.size() > 0) {
                continue;
            }

            List<RotasOfertasDTO> allRoutes = ofertasList
                .stream()
                .filter(o ->
                    !Objects.equals(o.getOfertas().getPerfil().getId(), perfilId) &&
                    !o.getOfertas().getTipoOferta().equals(rotasOfertas.getOfertas().getTipoOferta())
                )
                .toList();

            allRoutes.forEach(oferta -> {
                if (oferta.getId().equals(rotasOfertas.getId())) {
                    return;
                }

                if (!oferta.getOfertas().getStatus().equals(StatusOferta.AGUARDANDO_PROPOSTA)) {
                    return;
                }

                GoogleRoutes[] gRoutesOferta = g.fromJson(oferta.getRotas(), GoogleRoutes[].class);

                GoogleLegs googleLegs = gRoutes.getLegs().get(0);

                AtomicReference<Double> origem = new AtomicReference<>();
                AtomicReference<Double> destino = new AtomicReference<>();

                googleLegs
                    .getSteps()
                    .forEach(googleSteps -> {
                        if (origem.get() == null) {
                            gRoutesOferta[0].getLegs()
                                .forEach(distance -> {
                                    double result1 = GeoUtils.geoDistanceInKm(
                                        googleSteps.getStart_location(),
                                        distance.getStart_location()
                                    );
                                    if (result1 > 0.0 && result1 <= 100.0) {
                                        origem.set(result1);
                                    }
                                });
                        }

                        if (destino.get() == null) {
                            gRoutesOferta[0].getLegs()
                                .forEach(distance -> {
                                    double result2 = GeoUtils.geoDistanceInKm(googleSteps.getEnd_location(), distance.getEnd_location());
                                    if (result2 > 0.0 && result2 <= 100.0) {
                                        destino.set(result2);
                                    }
                                });
                        }
                    });

                if ((origem.get() != null && origem.get() <= 100) && (destino.get() != null && destino.get() <= 100)) {
                    selected.add(oferta.getOfertas());
                }
            });

            if (selected.size() > 0) {
                var perfil = perfilService.findOne(rotasOfertas.getOfertas().getPerfil().getId());

                if (perfil.isPresent()) {
                    rotasOfertas.getOfertas().setPerfil(perfil.get());

                    var response = facebookController.createAlertTransportIndication(rotasOfertas.getOfertas(), selected.size());

                    if (response.getError() == null) {
                        WhatsMessageBatchDTO whatsMessageBatch = new WhatsMessageBatchDTO();
                        whatsMessageBatch.setTipo(WhatsAppType.INDICATION_ALERT_TRANSPORT);
                        whatsMessageBatch.setPerfilID(rotasOfertas.getOfertas().getPerfil().getId().intValue());
                        whatsMessageBatch.setOfertaId(rotasOfertas.getOfertas().getId());
                        whatsMessageBatch.setStatus(WhatsStatus.OPEN);
                        whatsMessageBatch.setTipoOferta(rotasOfertas.getOfertas().getTipoOferta());
                        whatsMessageBatch.setWaidTo(response.getMessages().get(0).getId());
                        whatsMessageBatch.setNotificationDate(ZonedDateTime.now());
                        this.whatsMessageBatchService.save(whatsMessageBatch);
                    } else {
                        log.error(response.getError().getCode());
                        log.error(response.getError().getType());
                        log.error(response.getError().getMessage());
                    }
                }
            }
        }

        log.info("Stop scanning nears route to users.....");
    }
    //    public ResponseEntity<List<OfertasDTO>> getNearRoute(@PathVariable Long id) {
    //        Optional<RotasOfertasDTO> dto = ofertasService.findByIdOferta(id);
    //        List<OfertasDTO> selected = new ArrayList<>();
    //        Gson g = new Gson();
    //
    //        if (dto.isPresent()) {
    //            RotasOfertasDTO rotasOfertasDTO = dto.get();
    //            GoogleRoutes[] gRoutesArr = g.fromJson(rotasOfertasDTO.getRotas(), GoogleRoutes[].class);
    //
    //            GoogleRoutes gRoutes = gRoutesArr[0];
    //
    //            List<RotasOfertasDTO> allRoutes = ofertasService.findAllNotPerfilId(
    //                rotasOfertasDTO.getOfertas().getPerfil().getId(),
    //                rotasOfertasDTO.getOfertas().getTipoOferta() == TipoOferta.CARGA ? TipoOferta.VAGAS : TipoOferta.CARGA,
    //                StatusOferta.AGUARDANDO_PROPOSTA
    //            );
    //
    //            allRoutes.forEach(oferta -> {
    //                if (oferta.getId().equals(rotasOfertasDTO.getId())) {
    //                    return;
    //                }
    //
    //                GoogleRoutes[] gRoutesOferta = g.fromJson(oferta.getRotas(), GoogleRoutes[].class);
    //
    //                GoogleLegs googleLegs = gRoutes.getLegs().get(0);
    //
    //                AtomicReference<Double> origem = new AtomicReference<>();
    //                AtomicReference<Double> destino = new AtomicReference<>();
    //
    //                googleLegs
    //                    .getSteps()
    //                    .forEach(googleSteps -> {
    //                        if (origem.get() == null) {
    //                            double result1 = GeoUtils.geoDistanceInKm(
    //                                googleSteps.getStart_location(),
    //                                gRoutesOferta[0].getLegs().get(0).getStart_location()
    //                            );
    //                            double result2 = GeoUtils.geoDistanceInKm(
    //                                googleSteps.getEnd_location(),
    //                                gRoutesOferta[0].getLegs().get(0).getStart_location()
    //                            );
    //
    //                            if (result1 > 0.0 && result1 <= 100.0) {
    //                                origem.set(result1);
    //                            } else if (result2 > 0.0 && result2 <= 100) {
    //                                origem.set(result2);
    //                            }
    //                        }
    //
    //                        if (destino.get() == null) {
    //                            double result1 = GeoUtils.geoDistanceInKm(
    //                                googleSteps.getStart_location(),
    //                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
    //                            );
    //                            double result2 = GeoUtils.geoDistanceInKm(
    //                                googleSteps.getEnd_location(),
    //                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
    //                            );
    //
    //                            if (result1 > 0.0 && result1 <= 100.0) {
    //                                destino.set(result1);
    //                            } else if (result2 > 0.0 && result2 <= 100.0) {
    //                                destino.set(result2);
    //                            }
    //                        }
    //                    });
    //
    //                if ((origem.get() != null && origem.get() <= 100) && (destino.get() != null && destino.get() <= 100)) {
    //                    selected.add(oferta.getOfertas());
    //                }
    //            });
    //        }
    //
    //        return ResponseEntity.ok(selected);
    //    }
}
