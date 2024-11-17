package br.com.casadocodigo.configuracao.oauth;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class ConfiguracaoOAuth2 {

  public static final String RESOURCE_ID = "books";

  @EnableResourceServer
  protected static class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
      resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
          .anyRequest().authenticated().and()
          .requestMatchers().antMatchers("/api/v2/**");
    }
  }

  @EnableAuthorizationServer
  protected static class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    @Qualifier("dataSourceBookServerOauth")
    private DataSource dataSource;

    @Bean
    public TokenStore tokenStore() {
      return new JdbcTokenStore(this.dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
      return new JdbcApprovalStore(this.dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients.jdbc(this.dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(this.clientDetailsService);

      endpoints
          .authenticationManager(this.authenticationManager)
          .requestFactory(requestFactory)
          .approvalStore(approvalStore())
          .tokenStore(tokenStore());
    }

    /*
    Configuração em memória do client
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients
          .inMemory()
          .withClient("cliente-app")
          .secret("$2a$10$sTEY0A3Z3MdRZrJSRRDvhuOzm1q2gE.BKtX91MEvsm5XJtWPJZODu")
          .authorizedGrantTypes("password", "authorization_code", "refresh_token")
          .accessTokenValiditySeconds(120)
          .scopes("read", "write")
          .redirectUris("http://localhost:9000/callback", "http://localhost:9000/integracao/callback")
          .resourceIds(RESOURCE_ID)
          .and()
          .withClient("cliente-admin")
          .secret("$2a$10$q5B3yLhM.H8gdwNmLxtQie.wga.noGT7x2AF3foEdnJKu1aJJ.iwW")
          .authorizedGrantTypes("client_credentials")
          .scopes("read")
          .resourceIds(RESOURCE_ID);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints
          .authenticationManager(this.authenticationManager)
          .userDetailsService(this.userDetailsService);
    }
     */
  }
}
