package org.jasig.cas.support.pms.authentication.handler.support;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.principal.DefaultPrincipalFactory;
import org.jasig.cas.authentication.principal.PrincipalFactory;
import org.jasig.cas.support.pms.PmsPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Copyright (c) 2016 Hengte Technology Co.,Ltd.
 * All Rights Reserved.<br />
 * created on 7/15/16
 *
 * @author lcs
 * @version 1.0
 */
@Component("pmsDatabaseAuthenticationHandler")
public class PmsDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsDatabaseAuthenticationHandler.class);
	@NotNull
	@Autowired
	@Qualifier("principalFactory")
	protected PrincipalFactory principalFactory = new DefaultPrincipalFactory();

	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential transformedCredential) throws GeneralSecurityException, PreventedException {
		LOGGER.info("{}", transformedCredential);
		return new DefaultHandlerResult(this, new BasicCredentialMetaData(transformedCredential), this.principalFactory.createPrincipal(""), null);
	}

}
