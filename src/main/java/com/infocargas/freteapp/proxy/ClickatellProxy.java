package com.infocargas.freteapp.proxy;

import com.infocargas.freteapp.service.dto.ClickatellMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "clickatell", url = "https://platform.clickatell.com/")
public interface ClickatellProxy {
    @PostMapping("/v1/message")
    void createWhatsappMessage(
        @RequestHeader(value = "Authorization", defaultValue = "V8gPLfPGTaqVuKYIOjP9Bw==") String token,
        @RequestBody ClickatellMessageDTO message
    );
}
