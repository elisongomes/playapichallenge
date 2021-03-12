package services.crm.adapters;

public interface CRMInterface {
    public int createDeal(String name, int organizationId);
    public int createPerson(String name, String email, int organizationId);
    public int createNote(String content, int dealId, int personId, int organizationId);
    public int createOrganization(String name, String site);
}
