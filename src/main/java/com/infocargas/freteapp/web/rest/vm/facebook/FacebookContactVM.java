package com.infocargas.freteapp.web.rest.vm.facebook;

import java.util.List;
import java.util.Map;

public class FacebookContactVM {

    private Map<String, String> addresses;
    private List<Map<String, String>> emails;
    private Map<String, String> name;
    private Map<String, String> org;
    private List<Map<String, String>> phones;

    public Map<String, String> getAddresses() {
        return addresses;
    }

    public void setAddresses(Map<String, String> addresses) {
        this.addresses = addresses;
    }

    public List<Map<String, String>> getEmails() {
        return emails;
    }

    public void setEmails(List<Map<String, String>> emails) {
        this.emails = emails;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getOrg() {
        return org;
    }

    public void setOrg(Map<String, String> org) {
        this.org = org;
    }

    public List<Map<String, String>> getPhones() {
        return phones;
    }

    public void setPhones(List<Map<String, String>> phones) {
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookContactVM)) return false;

        FacebookContactVM that = (FacebookContactVM) o;

        if (getAddresses() != null ? !getAddresses().equals(that.getAddresses()) : that.getAddresses() != null) return false;
        if (getEmails() != null ? !getEmails().equals(that.getEmails()) : that.getEmails() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getOrg() != null ? !getOrg().equals(that.getOrg()) : that.getOrg() != null) return false;
        return getPhones() != null ? getPhones().equals(that.getPhones()) : that.getPhones() == null;
    }

    @Override
    public int hashCode() {
        int result = getAddresses() != null ? getAddresses().hashCode() : 0;
        result = 31 * result + (getEmails() != null ? getEmails().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getOrg() != null ? getOrg().hashCode() : 0);
        result = 31 * result + (getPhones() != null ? getPhones().hashCode() : 0);
        return result;
    }
}
