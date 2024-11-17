package br.com.casadocodigo.configuracao.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(name = "flywayBookServer")
    public Flyway flyway1(@Qualifier("dataSourceBookServer") DataSource dataSourceBookServer) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSourceBookServer)
            .locations("classpath:db/migration/bookserver")
            .load();
        flyway.migrate();
        return flyway;
    }

    @Bean(name = "flywayBookServerOauth")
    public Flyway flyway2(@Qualifier("dataSourceBookServerOauth") DataSource dataSourceBookServerOauth) {
        Flyway flyway = Flyway.configure()
            .dataSource(dataSourceBookServerOauth)
            .locations("classpath:db/migration/bookserver_oauth")
            .load();
        flyway.migrate();
        return flyway;
    }
}
