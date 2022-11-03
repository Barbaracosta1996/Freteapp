package com.infocargas.freteapp.service.dto;

public class ClickTellPayload {

    /**
     * Phone to will send the message
     */
    private String to;
    /**
     * Type channel of message whatsapp or sms
     */
    private String channel;
    /**
     * One single message without template or media.
     */
    private String content;

    /**
     * Templates apprroved by Meta to use in WhatsApp API
     */
    private ClickaTellTemplates template;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ClickaTellTemplates getTemplate() {
        return template;
    }

    public void setTemplate(ClickaTellTemplates template) {
        this.template = template;
    }
}
