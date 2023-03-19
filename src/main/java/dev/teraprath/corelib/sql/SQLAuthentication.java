package dev.teraprath.corelib.sql;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public record SQLAuthentication(String host, int port, String user, String password, String database) {

    public SQLAuthentication(@Nonnull String host, @Nonnegative int port, @Nonnull String user, @Nonnull String password, @Nonnull String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

}
