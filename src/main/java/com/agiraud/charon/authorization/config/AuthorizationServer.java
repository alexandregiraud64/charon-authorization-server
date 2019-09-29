package com.agiraud.charon.authorization.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.Assert;

public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
	/* ************************************************************************* */
	// ATTRIBUTES
	/* ************************************************************************* */
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManagerBean;

	@Autowired
	private ClientDetailsService clientDetailsServiceImpl;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private ApprovalStore approvalStore;

	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private ApprovalStoreUserApprovalHandler approvalStoreUserApprovalHandler;
	
	/* ************************************************************************* */
	// POST CONSTRUCT
	/* ************************************************************************* */
	@PostConstruct
	public void init() {
		Assert.notNull(userDetailsService, "userDetailsService can not be null");
		Assert.notNull(authenticationManagerBean, "authenticationManagerBean can not be null");
		Assert.notNull(clientDetailsServiceImpl, "clientDetailsServiceImpl can not be null");
		Assert.notNull(tokenStore, "tokenStore can not be null");
		Assert.notNull(approvalStore, "approvalStore can not be null");
		Assert.notNull(authorizationCodeServices, "authorizationCodeServices can not be null");
		Assert.notNull(authenticationProvider, "authenticationProvider can not be null");
	}

	/* ************************************************************************* */
	// SETTERS AND GETTERS
	/* ************************************************************************* */
	public void setApprovalStoreUserApprovalHandler(ApprovalStoreUserApprovalHandler approvalStoreUserApprovalHandler) {
		this.approvalStoreUserApprovalHandler = approvalStoreUserApprovalHandler;
	}
	
	public ApprovalStore getApprovalStore() {
		return approvalStore;
	}

	public ClientDetailsService getClientDetailsService() {
		return clientDetailsServiceImpl;
	}

	/* ************************************************************************* */
	// OVERRIDE
	/* ************************************************************************* */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
			.withClientDetails(clientDetailsServiceImpl);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore)
			.authenticationManager(authenticationManagerBean)
			.userDetailsService(userDetailsService)
		    .approvalStore(approvalStore)
		    .userApprovalHandler(approvalStoreUserApprovalHandler)
		    .authorizationCodeServices(authorizationCodeServices);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
			.passwordEncoder(passwordEncoder);
	}
	
}
