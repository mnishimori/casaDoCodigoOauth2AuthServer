package br.com.casadocodigo.configuracao.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(
    basePackages = "br.com.casadocodigo.*", // Pacotes dos repositórios do banco 1
    entityManagerFactoryRef = "entityManagerFactoryBookServer",
    transactionManagerRef = "transactionManagerBookServer"
)
public class DataSourceConfigBookServer {

  private final DataSourceBookServerProperties dataSourceBookServerProperties;

  public DataSourceConfigBookServer(DataSourceBookServerProperties dataSourceBookServerProperties) {
    this.dataSourceBookServerProperties = dataSourceBookServerProperties;
  }

  @Primary
  @Bean(name = "dataSourceBookServer")
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSourceBookServer() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(dataSourceBookServerProperties.getUrl());
    dataSource.setUsername(dataSourceBookServerProperties.getUsername());
    dataSource.setPassword(dataSourceBookServerProperties.getPassword());
    dataSource.setDriverClassName(dataSourceBookServerProperties.getDriverClassName());
    return dataSource;
  }

  @Primary
  @Bean(name = "entityManagerFactoryBookServer")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory1(
      @Qualifier("dataSourceBookServer") DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("br.com.casadocodigo.*"); // Pacote das entidades do banco 1
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    Map<String, Object> properties = new HashMap<>();
    properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // Dialeto para MySQL 8
    em.setJpaPropertyMap(properties);
    return em;
  }

  @Primary
  @Bean(name = "transactionManagerBookServer")
  public JpaTransactionManager transactionManager1(
      @Qualifier("entityManagerFactoryBookServer") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
