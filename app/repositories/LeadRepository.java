package repositories;

import models.Lead;
import models.LeadStatus;
import models.User;
import play.db.ebean.EbeanConfig;
import services.LeadService;
import utils.Util;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class LeadRepository extends BaseRepository {

    @Inject
    public LeadRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext);
    }

    public CompletionStage<List<Lead>> getAll() {
        return supplyAsync(() ->
                ebeanServer.find(Lead.class).findList(), executionContext);
    }

    public CompletionStage<Optional<Lead>> getById(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(Lead.class).setId(id).findOne()), executionContext);
    }

    public CompletionStage<Optional<Lead>> insert(Lead lead, int userId) {
        return supplyAsync(() -> {
            /**
             * Validate e-mail
             */
            if (!Util.validateEmail(lead.email)) {
                this.message = "Please enter an valid e-mail";
                return Optional.empty();
            }
            /**
             * Check if Lead e-mail already exists
             */
            Lead existentLead = ebeanServer.find(Lead.class).where().eq("email", lead.email).findOne();
            if (existentLead == null) {
                lead.status.clear();
                LeadStatus leadStatus = new LeadStatus();
                leadStatus.status = "OPEN";
                leadStatus.finalized_at = null;
                leadStatus.user = ebeanServer.find(User.class).setId(userId).findOne();
                lead.status.add(leadStatus);

                ebeanServer.insert(lead);
                return Optional.ofNullable(lead);
            } else {
                /**
                 * Load current status of existent lead
                 */
                LeadStatus leadStatus = ebeanServer.find(LeadStatus.class)
                        .where().eq("lead_id", existentLead.id)
                        .orderBy("created_at desc")
                        .findOne();
                /**
                 * Check if status of existent lead not is OPEN
                 */
                if (!leadStatus.status.equals("OPEN")) {
                    existentLead.name = lead.name;
                    existentLead.company = lead.company;
                    existentLead.site = lead.site;
                    existentLead.phones = lead.phones;
                    existentLead.notes = lead.notes;
                    ebeanServer.save(existentLead);
                    return Optional.of(existentLead);
                } else {
                    this.message = "Lead e-mail already exists with OPEN status";
                    return Optional.empty();
                }
            }
        }, executionContext);
    }

    public CompletionStage<Optional<Lead>> finish(Long id, String status) {
        return supplyAsync(() -> {
            /**
             * Validate status
             */
            if (!status.equals("WON") && !status.equals("LOST")) {
                this.message = "The accepted status value are [WON or LOST]";
                return Optional.empty();
            }
            /**
             * Check if Lead e-mail already exists
             */
            Lead lead = ebeanServer.find(Lead.class).setId(id).findOne();
            if (lead == null) {
                this.message = "Lead not found";
                return Optional.empty();
            } else {
                /**
                 * Load current status of existent lead
                 */
                LeadStatus leadStatus = ebeanServer.find(LeadStatus.class)
                        .where().eq("lead_id", lead.id)
                        .orderBy("created_at desc")
                        .findOne();
                /**
                 * Check if status of existent lead not is OPEN
                 */
                if (leadStatus.status.equals("OPEN")) {
                    leadStatus.status = status;
                    leadStatus.finalized_at = new Timestamp(System.currentTimeMillis());;
                    ebeanServer.save(leadStatus);
                    /**
                     * Send lead to CRM
                     */
                    if (status.equals("WON")) {
                        LeadService leadService = new LeadService();
                        leadService.sendToCRM(lead);
                    }
                    return Optional.of(lead);
                } else {
                    this.message = "Lead not OPEN for finalization";
                    return Optional.empty();
                }
            }
        }, executionContext);
    }
}
