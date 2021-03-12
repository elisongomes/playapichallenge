package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Lead;
import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.LeadRepository;
import utils.Util;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class LeadController extends Controller {

    private final LeadRepository leadRepository;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public LeadController(LeadRepository leadRepository,
                          HttpExecutionContext httpExecutionContext) {
        this.leadRepository = leadRepository;
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> listAll() {
        return leadRepository.getAll().thenApplyAsync(list -> {
            JsonNode leadJson = Json.toJson(list);
            return ok(leadJson);
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> getById(Long id) {
        return leadRepository.getById(id).thenApplyAsync(lead -> {
            Optional<Lead> leadOptional = lead;
            return leadOptional.map(d -> {
                return ok(Json.toJson(d));
            }).orElse(status(204));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return supplyAsync(() -> {
                return badRequest(Util.jsonResponse("Json data not found", false));
            }, httpExecutionContext.current());
        }
        return leadRepository.insert(Json.fromJson(json, Lead.class), json.get("user_id").asInt()).thenApplyAsync(lead -> {
            Optional<Lead> leadOptional = lead;
            return leadOptional.map(d -> {
                return ok(Json.toJson(d));
            }).orElse(status(400, Util.jsonResponse(leadRepository.getMessage(), false)));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> finish(Long id, Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return supplyAsync(() -> {
                return badRequest(Util.jsonResponse("Json data not found", false));
            }, httpExecutionContext.current());
        }
        return leadRepository.finish(id, json.get("status").asText()).thenApplyAsync(lead -> {
            Optional<Lead> leadOptional = lead;
            return leadOptional.map(d -> {
                return ok(Json.toJson(d));
            }).orElse(status(400, Util.jsonResponse(leadRepository.getMessage(), false)));
        }, httpExecutionContext.current());
    }
}
