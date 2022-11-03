package com.infocargas.freteapp.web.rest.vm.facebook;

import java.util.List;

public class FacebookActionsVM {

    private String button;

    private List<FacebookSectionsVM> sections;

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public List<FacebookSectionsVM> getSections() {
        return sections;
    }

    public void setSections(List<FacebookSectionsVM> sections) {
        this.sections = sections;
    }
}
