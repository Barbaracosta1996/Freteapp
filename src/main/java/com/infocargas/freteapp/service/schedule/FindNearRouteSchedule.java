package com.infocargas.freteapp.service.schedule;

import com.google.gson.Gson;
import com.infocargas.freteapp.controller.FacebookController;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.service.PerfilService;
import com.infocargas.freteapp.service.RotasOfertasService;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.dto.routes.GoogleLegs;
import com.infocargas.freteapp.service.dto.routes.GoogleRoutes;
import com.infocargas.freteapp.utils.GeoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FindNearRouteSchedule {

    private final Logger log = LoggerFactory.getLogger(FindNearRouteSchedule.class);

    private final RotasOfertasService ofertasService;
    private final FacebookController facebookController;

    private final PerfilService perfilService;

    public FindNearRouteSchedule(RotasOfertasService ofertasService, FacebookController facebookController, PerfilService perfilService) {
        this.ofertasService = ofertasService;
        this.facebookController = facebookController;
        this.perfilService = perfilService;
    }

    @Scheduled(fixedDelayString = "PT015M")
    public void getNearRoute() {
        log.info("Start scanning nears route to users.....");

        Gson g = new Gson();
        List<RotasOfertasDTO> ofertasList = ofertasService.findAll();

        for (RotasOfertasDTO dto : ofertasList) {
            List<OfertasDTO> selected = new ArrayList<>();

            GoogleRoutes[] gRoutesArr = g.fromJson(dto.getRotas(), GoogleRoutes[].class);

            GoogleRoutes gRoutes = gRoutesArr[0];

            List<RotasOfertasDTO> allRoutes = ofertasService.findAllNotPerfilId(
                dto.getOfertas().getPerfil().getId(),
                dto.getOfertas().getTipoOferta() == TipoOferta.CARGA ? TipoOferta.VAGAS : TipoOferta.CARGA,
                dto.getOfertas().getDataFechamento(),
                StatusOferta.AGUARDANDO_PROPOSTA
            );

            allRoutes.forEach(oferta -> {
                if (oferta.getId().equals(dto.getId())) {
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
                            double result1 = GeoUtils.geoDistanceInKm(
                                googleSteps.getStart_location(),
                                gRoutesOferta[0].getLegs().get(0).getStart_location()
                            );
                            double result2 = GeoUtils.geoDistanceInKm(
                                googleSteps.getEnd_location(),
                                gRoutesOferta[0].getLegs().get(0).getStart_location()
                            );

                            if (result1 > 0.0 && result1 <= 100.0) {
                                origem.set(result1);
                            } else if (result2 > 0.0 && result2 <= 100) {
                                origem.set(result2);
                            }
                        }

                        if (destino.get() == null) {
                            double result1 = GeoUtils.geoDistanceInKm(
                                googleSteps.getStart_location(),
                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
                            );
                            double result2 = GeoUtils.geoDistanceInKm(
                                googleSteps.getEnd_location(),
                                gRoutesOferta[0].getLegs().get(0).getEnd_location()
                            );

                            if (result1 > 0.0 && result1 <= 100.0) {
                                destino.set(result1);
                            } else if (result2 > 0.0 && result2 <= 100.0) {
                                destino.set(result2);
                            }
                        }
                    });

                if ((origem.get() != null && origem.get() <= 100) && (destino.get() != null && destino.get() <= 100)) {
                    selected.add(oferta.getOfertas());
                }
            });

            if (selected.size() > 0) {
                dto.getOfertas().setPerfil(perfilService.findOne(dto.getOfertas().getPerfil().getId()).get());
                facebookController.sendNearsRouteNotifcation(selected, dto);
            }
        }

        log.info("Stop scanning nears route to users.....");
    }
}
