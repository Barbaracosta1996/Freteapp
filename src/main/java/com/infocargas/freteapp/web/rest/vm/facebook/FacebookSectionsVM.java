package com.infocargas.freteapp.web.rest.vm.facebook;

import java.util.List;

public class FacebookSectionsVM {

    private String title;

    private List<FacebookRowsVM> rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FacebookRowsVM> getRows() {
        return rows;
    }

    public void setRows(List<FacebookRowsVM> rows) {
        this.rows = rows;
    }
}
