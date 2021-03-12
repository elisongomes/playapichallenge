package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.UserRepository;
import utils.Util;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserController extends Controller {

    private final UserRepository userRepository;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public UserController(UserRepository userRepository,
                           HttpExecutionContext httpExecutionContext) {
        this.userRepository = userRepository;
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> listAll() {
        return userRepository.getAll().thenApplyAsync(list -> {
            JsonNode userJson = Json.toJson(list);
            return ok(userJson);
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> getById(Long id) {
        return userRepository.getById(id).thenApplyAsync(user -> {
            Optional<User> userOptional = user;
            return userOptional.map(d -> {
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
        return userRepository.insert(Json.fromJson(json, User.class)).thenApplyAsync(user -> {
            Optional<User> userOptional = user;
            return userOptional.map(d -> {
                return ok(Json.toJson(d));
            }).orElse(status(400, Util.jsonResponse("Could not create user", false)));
        }, httpExecutionContext.current());
    }

}
