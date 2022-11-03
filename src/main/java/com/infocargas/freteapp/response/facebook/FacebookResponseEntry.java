package com.infocargas.freteapp.response.facebook;

import java.util.List;

public class FacebookResponseEntry {

    private String id;
    private List<FacebookResponseChange> changes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FacebookResponseChange> getChanges() {
        return changes;
    }

    public void setChanges(List<FacebookResponseChange> changes) {
        this.changes = changes;
    }
}
