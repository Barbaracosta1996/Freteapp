package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.web.rest.vm.ManagedUserVM;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class CallbacksResources {
    @GetMapping("/callbacks/rd/auth")
    @ResponseStatus(HttpStatus.OK)
    public void registerAccount(){

    }
}
