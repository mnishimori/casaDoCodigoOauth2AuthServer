package br.com.casadocodigo.configuracao.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(
    basePackages = "org.springframework.security.oauth2.provider.token.store.jpa", // Repositórios do OAuth2
    entityManagerFactoryRef = "oauth2EntityManagerFactory",
    transactionManagerRef = "oauth2TransactionManager")
public class DataSourceConfigBookServerOAuth {

  private final DataSourceBookServerOauthProperties dataSourceBookServerOauthProperties;

  public DataSourceConfigBookServerOAuth(DataSourceBookServerOauthProperties dataSourceBookServerOauthProperties) {
    this.dataSourceBookServerOauthProperties = dataSourceBookServerOauthProperties;
  }

  @Bean(name = "dataSourceBookServerOauth")
  public DataSource dataSourceBookServerOauth() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(dataSourceBookServerOauthProperties.getUrl());
    dataSource.setUsername(dataSourceBookServerOauthProperties.getUsername());
    dataSource.setPassword(dataSourceBookServerOauthProperties.getPassword());
    dataSource.setDriverClassName(dataSourceBookServerOauthProperties.getDriverClassName());
    return dataSource;
  }

  @Bean(name = "entityManagerFactoryBookServerOauth")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory1(
      @Qualifier("dataSourceBookServerOauth") DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan(
        "org.springframework.security.oauth2.provider.token.store.jpa"); // Pacote das entidades do banco 1
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // Dialeto para MySQL 8
    em.setJpaPropertyMap(properties);
    return em;
  }

  @Bean(name = "transactionManagerBookServerOauth")
  public JpaTransactionManager transactionManager1(
      @Qualifier("entityManagerFactoryBookServerOauth") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
