package services.crm;

import com.typesafe.config.*;
import services.crm.adapters.CRMInterface;
import services.crm.models.CRMModel;

public class CRMService {

    private CRMInterface crmAdapter;

    public CRMService() {
        this.factoryInterface();
    }

    private void factoryInterface() {
        Config conf = ConfigFactory.load();
        String apiAdapter = conf.getString("crm.api.adapter").toLowerCase();
        String apiToken = conf.getString("crm.api.token");
        String apiUrl = conf.getString("crm.api.url");

        switch (apiAdapter) {
            case "pipedrive":
                this.crmAdapter = new services.crm.adapters.pipedrive.Pipedrive(apiToken, apiUrl);
//            case "RD": this.crmAdapter = new services.crm.adapters.rd.RD();
//            case "HS": this.crmAdapter = new services.crm.adapters.hubsopt.Hubspot();
        }
    }

    public void create(CRMModel crmModel) {
        int dealId = 0;
        int organizationId = 0;
        int personId = 0;
        if (crmModel.getOrganizationName() != null) {
            organizationId = this.crmAdapter.createOrganization(crmModel.getOrganizationName(), crmModel.getOrganizationSite());
        }
        if (crmModel.getDealName() != null) {
            dealId = this.crmAdapter.createDeal(crmModel.getDealName(), organizationId);
        }
        if (crmModel.getPersonName() != null) {
            personId = this.crmAdapter.createPerson(crmModel.getPersonName(), crmModel.getPersonEmail(), organizationId);
        }
        if (crmModel.getNotes() != null) {
            this.crmAdapter.createNote(crmModel.getNotes(), dealId, personId, organizationId);
        }
    }
}
