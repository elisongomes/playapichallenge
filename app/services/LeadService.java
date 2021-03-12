package services;

import models.Lead;
import services.crm.CRMService;
import services.crm.models.CRMModel;

public class LeadService {
    public LeadService() {

    }
    public void sendToCRM(Lead lead) {
        CRMModel crmModel = new CRMModel();
        crmModel.setDealName(lead.name);
        crmModel.setNotes(lead.notes);
        crmModel.setPersonName(lead.name);
        crmModel.setPersonEmail(lead.email);
        crmModel.setOrganizationName(lead.company);
        crmModel.setOrganizationSite(lead.site);

        CRMService crmService = new CRMService();
        crmService.create(crmModel);
    }
}
