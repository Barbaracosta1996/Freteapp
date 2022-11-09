package com.infocargas.freteapp.proxy;

import static com.infocargas.freteapp.config.Constants.FACEBOOK_TOKEN;

import com.infocargas.freteapp.response.facebook.FacebookSendResponse;
import com.infocargas.freteapp.web.rest.vm.facebook.FacebookMessageVM;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "facebook", url = "https://graph.facebook.com", path = "/v15.0")
public interface FacebookApiProxy {
    @PostMapping(value = "/100355506229783/messages")
    FacebookSendResponse createMessage(
        @RequestHeader(value = "messaging_product") String messagingProduct,
        @RequestHeader(value = "Authorization") String token,
        @RequestBody FacebookMessageVM messageVM
    );

    @PostMapping(value = "/101799009411734/messages")
    FacebookSendResponse createMessageHom(
        @RequestHeader(value = "messaging_product") String messagingProduct,
        @RequestHeader(value = "Authorization") String token,
        @RequestBody FacebookMessageVM messageVM
    );
}
