package com.infocargas.freteapp.service.dto;

import java.util.Map;

public class ClickaTellTemplates {

    /**
     * Name of the template created in Meta.
     * This template need be created and approved by Meta
     */
    private String templateName;

    private Map<String, String> headers;
    private Map<String, String> body;
    private Map<String, String> buttons;
}
