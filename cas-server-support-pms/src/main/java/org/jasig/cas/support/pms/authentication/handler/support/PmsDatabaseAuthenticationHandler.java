package org.jasig.cas.support.pms.authentication.handler.support;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.*;
import org.jasig.cas.authentication.principal.DefaultPrincipalFactory;
import org.jasig.cas.authentication.principal.PrincipalFactory;
import org.jasig.cas.support.pms.PmsPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Copyright (c) 2016 Hengte Technology Co.,Ltd.
 * All Rights Reserved.<br />
 * created on 7/15/16
 *
 * pms用户登录校验
 *
 * @author lcs
 * @version 1.0
 */
@Component("pmsDatabaseAuthenticationHandler")
public class PmsDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

	private static final String COL_USER_NAME = "USER_NAME";
	private static final String COL_PASSWORD = "PASSWORD";
	private static final String COL_SALT = "SALT";


	private static final Logger LOGGER = LoggerFactory.getLogger(PmsDatabaseAuthenticationHandler.class);

	private static final String SQL_SELECT_USER = "select " + StringUtils.join(new String[]{COL_USER_NAME, COL_PASSWORD, COL_SALT, "STATUS", "IS_DEL"}, ",") + " from sys_user where USER_NAME = ?";

	@NotNull
	@Autowired
	@Qualifier("principalFactory")
	protected PrincipalFactory principalFactory = new DefaultPrincipalFactory();

	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential transformedCredential) throws GeneralSecurityException, PreventedException {
		LOGGER.info("{}", transformedCredential.toString());

		try {
			final Map<String, Object> values = getJdbcTemplate().queryForMap(SQL_SELECT_USER, transformedCredential.getUsername());

			if (StringUtils.isBlank((String) values.get(COL_SALT))) {
				throw new FailedLoginException("旧用户数据,请联系开发打野");
			}

			final String digestedPassword = PmsPasswordEncoder.encode(transformedCredential.getPassword(), (String) values.get(COL_SALT));

			if (!values.get(COL_PASSWORD).equals(digestedPassword)) {
				LOGGER.error("密码不正确 : {}", transformedCredential.getUsername());
				throw new FailedLoginException("Password does not match value on record.");
			}

			return createHandlerResult(transformedCredential,
					this.principalFactory.createPrincipal(transformedCredential.getUsername()), null);

		} catch (final IncorrectResultSizeDataAccessException e) {
			if (e.getActualSize() == 0) {
				LOGGER.error("用户不存在 : {}", transformedCredential.getUsername());
				throw new AccountNotFoundException(transformedCredential.getUsername() + " not found with SQL query");
			} else {
				LOGGER.error("重复用户名 : {}", transformedCredential.getUsername());
				throw new FailedLoginException("Multiple records found for " + transformedCredential.getUsername());
			}
		} catch (final DataAccessException e) {
			throw new PreventedException("SQL exception while executing query for " + transformedCredential.getUsername(), e);
		}
	}

	@Autowired
	@Override
	public void setDataSource(@Qualifier("psmDatabaseDataSource") final DataSource dataSource) {
		super.setDataSource(dataSource);
	}

}
