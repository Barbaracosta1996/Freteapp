package com.infocargas.freteapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class ClickaTellService {
    Logger log = LoggerFactory.getLogger(ClickaTellService.class);

    private String url ="https://platform.clickatell.com/messages/http/send?apiKey=%s&to=%s&content=%s";

    public void sendSms(String message, String... phoneNumbers) {

        try {
            if (phoneNumbers == null) {
                return;
            }

            RestTemplate restTemplate = new RestTemplate();
            Arrays.stream(phoneNumbers).map(s -> s.replaceAll("\\D", "")).forEach(phoneNumber -> {
                restTemplate.getForEntity(String.format(url, "V8gPLfPGTaqVuKYIOjP9Bw==", phoneNumber, message), String.class);
            });
        }catch (Exception e){
            log.error("Erro ao enviar SMS ", e);
        }
    }

}
