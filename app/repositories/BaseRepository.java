package repositories;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import play.db.ebean.EbeanConfig;

public class BaseRepository {
    protected final EbeanServer ebeanServer;
    protected final DatabaseExecutionContext executionContext;

    protected String message;

    public BaseRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public String getMessage() {
        return message;
    }
}
