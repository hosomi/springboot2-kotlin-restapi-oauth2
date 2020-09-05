package com.hosomi.restapi.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore
import javax.sql.DataSource

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer
                .checkTokenAccess("isAuthenticated()")
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
            .withClient("client")
            .secret(passwordEncoder.encode("secret"))
            .authorizedGrantTypes("refresh_token", "password", "client_credentials")
            .scopes("user_info")
            .autoApprove(true)
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
    }
}