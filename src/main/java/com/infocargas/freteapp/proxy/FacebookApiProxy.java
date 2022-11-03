package com.infocargas.freteapp.proxy;

import static com.infocargas.freteapp.config.Constants.FACEBOOK_TOKEN;

import com.infocargas.freteapp.response.facebook.FacebookSendResponse;
import com.infocargas.freteapp.web.rest.vm.facebook.FacebookMessageVM;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "facebook", url = "https://graph.facebook.com", path = "/v15.0/100355506229783")
public interface FacebookApiProxy {
    @PostMapping(value = "/messages")
    FacebookSendResponse createMessage(
        @RequestHeader(value = "messaging_product") String messagingProduct,
        @RequestHeader(value = "Authorization") String token,
        @RequestBody FacebookMessageVM messageVM
    );
}
