package com.infocargas.freteapp.proxy;

import com.infocargas.freteapp.response.RDContactDTO;
import com.infocargas.freteapp.response.RDContactResponse;
import com.infocargas.freteapp.response.RDEventsDTO;
import com.infocargas.freteapp.response.RDToken;
import com.infocargas.freteapp.web.rest.vm.RDStationAuth;
import com.infocargas.freteapp.web.rest.vm.RDStationRefresh;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "rdstation", url = "https://api.rd.services")
public interface RDStationProxy {
    @PostMapping("/auth/token")
    ResponseEntity<RDToken> authRDStation(@RequestBody RDStationAuth rdStationAuth);

    @PostMapping("/auth/token")
    ResponseEntity<RDToken> refreshRDStation(@RequestBody RDStationRefresh rdStationAuth);

    @PostMapping(value = "/platform/events")
    ResponseEntity<RDContactResponse> createContact(
        @RequestHeader(value = "Authorization") String details,
        @RequestBody RDEventsDTO eventsDTO
    );
}
