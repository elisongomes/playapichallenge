package services.crm.adapters.pipedrive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import services.crm.adapters.BaseCRMAdapter;
import services.crm.adapters.CRMInterface;

import java.util.*;

public class Pipedrive extends BaseCRMAdapter implements CRMInterface {

    public final String apiToken;
    public final String apiUrl;

    public static final String DEALS = "/deals";
    public static final String PERSONS = "/persons";
    public static final String NOTES = "/notes";
    public static final String ORGANIZATIONS = "/organizations";
    public static final String ORGANIZATION_FIELDS = "/organizationFields";

    public Pipedrive(String apiToken, String apiUrl) {
        this.apiToken = apiToken;
        this.apiUrl = apiUrl;
    }

    @Override
    public int createDeal(String name, int organizationId) {
        Map<Object, Object> params = new HashMap<>();
        params.put("title", name);
        params.put("org_id", organizationId);

        JsonNode response = this.ofString(this.httpClient("POST", this.getUrl(DEALS), params));
        if (response != null) {
            return response.get("data").get("id").asInt();
        }

        return 0;
    }

    @Override
    public int createPerson(String name, String email, int organizationId) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("org_id", organizationId);

        JsonNode response = this.ofString(this.httpClient("POST", this.getUrl(PERSONS), params));
        if (response != null) {
            return response.get("data").get("id").asInt();
        }

        return 0;
    }

    @Override
    public int createNote(String content, int dealId, int personId, int organizationId) {
        Map<Object, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("deal_id", dealId);
        params.put("person_id", personId);
        params.put("org_id", organizationId);

        JsonNode response = this.ofString(this.httpClient("POST", this.getUrl(NOTES), params));
        if (response != null) {
            return response.get("data").get("id").asInt();
        }
        return 0;
    }

    @Override
    public int createOrganization(String name, String site) {
        int organizationId = 0;
        Map<Object, Object> params;
        /**
         * Verify if exist 'site' field on organizationFields
         */
        String siteFieldKey = "";
        JsonNode response = this.ofString(this.httpClient("GET", this.getUrl(ORGANIZATION_FIELDS)));
        for (int i = 0, ci = response.get("data").size(); i < ci; i++) {
            if (response.get("data").get(i).get("name").asText().toLowerCase().equals("site")) {
                siteFieldKey = response.get("data").get(i).get("key").asText();
                break;
            }
        }
        /**
         * Create 'site' field on organizationFields
         */
        if (siteFieldKey.equals("")) {
            params = new HashMap<>();
            params.put("name", "Site");
            params.put("field_type", "varchar");
            response = this.ofString(this.httpClient("POST", this.getUrl(ORGANIZATION_FIELDS), params));
            siteFieldKey = response.get("data").get("key").asText();
        }

        /**
         * Create Organization
         */
        params = new HashMap<>();
        params.put("name", name);
        response = this.ofString(this.httpClient("POST", this.getUrl(ORGANIZATIONS), params));
        if (response != null) {
            organizationId = response.get("data").get("id").asInt();
            /**
             * Add 'Site' field to current Organization
             */
            params = new HashMap<>();
            params.put(siteFieldKey, site);
            response = this.ofString(this.httpClient("PUT", this.getUrl(ORGANIZATIONS + "/" + organizationId), params));
        }

        return organizationId;
    }

    private String getUrl(String uri) {
        return this.apiUrl + uri + "?api_token=" + this.apiToken;
    }
}
