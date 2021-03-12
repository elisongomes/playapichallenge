package repositories;

import models.User;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserRepository extends BaseRepository {

    @Inject
    public UserRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext);
    }

    public CompletionStage<List<User>> getAll() {
        return supplyAsync(() ->
                ebeanServer.find(User.class).findList(), executionContext);
    }

    public CompletionStage<Optional<User>> getById(Long id) {
        return supplyAsync(() -> Optional.ofNullable(ebeanServer.find(User.class).setId(id).findOne()), executionContext);
    }

    public CompletionStage<Optional<User>> insert(User user) {
        return supplyAsync(() -> {
            ebeanServer.insert(user);
            return Optional.ofNullable(user);
        }, executionContext);
    }

}
