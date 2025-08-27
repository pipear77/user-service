package co.com.pragma.r2dbc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "adapters.r2dbc")
public class PostgresqlConnectionProperties {
    private String host;
    private Integer port;
    private String database;
    private String schema;
    private String username;
    private String password;
}
