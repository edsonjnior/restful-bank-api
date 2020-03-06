package com.github.edsonjnior.bankapi.security;

import com.github.edsonjnior.bankapi.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(usersService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("myapp")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("all")
                .refreshTokenValiditySeconds(3 * 24 * 60 * 60) // 3 days = 259200‬
                .resourceIds(ResourceServerConfig.RESOURCE_ID)
                .secret(passwordEncoder.encode("myappsecret"))
                .accessTokenValiditySeconds(24 * 60 * 60); // 1 day = 86400
    }

    @Bean
    public TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());

        return tokenServices;
    }
}
