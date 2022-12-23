package com.infocargas.freteapp.web.rest;

import com.google.gson.Gson;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.service.ClickaTellService;
import com.infocargas.freteapp.service.RotasOfertasService;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.dto.routes.GoogleLegs;
import com.infocargas.freteapp.service.dto.routes.GoogleRoutes;
import com.infocargas.freteapp.utils.GeoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.ws.rs.QueryParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GeoOfertaResource {

    private final RotasOfertasService ofertasService;

    public GeoOfertaResource(RotasOfertasService ofertasService) {
        this.ofertasService = ofertasService;
    }

    /**
     * {@code GET  /frotas/:id} : get the "id" frota.
     *
     * @param id the id of the frotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/geo-ofertas/{id}")
    public ResponseEntity<List<OfertasDTO>> getNearRoute(@PathVariable Long id) {
        Optional<RotasOfertasDTO> dto = ofertasService.findByIdOferta(id);
        List<OfertasDTO> selected = new ArrayList<>();
        Gson g = new Gson();

        if (dto.isPresent()) {
            RotasOfertasDTO rotasOfertasDTO = dto.get();
            GoogleRoutes[] gRoutesArr = g.fromJson(rotasOfertasDTO.getRotas(), GoogleRoutes[].class);

            GoogleRoutes gRoutes = gRoutesArr[0];

            List<RotasOfertasDTO> allRoutes = ofertasService.findAllNotPerfilId(
                rotasOfertasDTO.getOfertas().getPerfil().getId(),
                rotasOfertasDTO.getOfertas().getTipoOferta() == TipoOferta.CARGA ? TipoOferta.VAGAS : TipoOferta.CARGA,
                StatusOferta.AGUARDANDO_PROPOSTA
            );

            allRoutes.forEach(oferta -> {
                if (oferta.getId().equals(rotasOfertasDTO.getId())) {
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
        }

        return ResponseEntity.ok(selected);
    }

    /**
     * {@code GET  /frotas/:id} : get the "id" frota.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/geo-ofertas")
    public ResponseEntity<Double> getFrota(
        @QueryParam("latFirst") Double latFirst,
        @QueryParam("lngFirst") Double lngFirst,
        @QueryParam("latSecond") Double latSecond,
        @QueryParam("lngSecond") Double lngSecond
    ) {
        Double distance = GeoUtils.geoDistanceInKm(latFirst, lngFirst, latSecond, lngSecond);
        return ResponseEntity.ok(distance);
    }
}
