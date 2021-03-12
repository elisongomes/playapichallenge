package services.crm.models;

public class CRMModel {
    private String dealName;
    private String notes;
    private String personName;
    private String personEmail;
    private String organizationName;
    private String organizationSite;

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationSite() {
        return organizationSite;
    }

    public void setOrganizationSite(String organizationSite) {
        this.organizationSite = organizationSite;
    }
}
