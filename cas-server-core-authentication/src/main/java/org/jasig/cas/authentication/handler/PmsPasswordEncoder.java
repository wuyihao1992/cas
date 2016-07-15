package org.jasig.cas.authentication.handler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Copyright (c) 2016 Hengte Technology Co.,Ltd.
 * All Rights Reserved.<br />
 * created on 7/14/16
 *
 * MD5( BASE_64( password ) + salt )
 *
 * @author lcs
 * @version 1.0
 */

@Component("pmsPasswordEncoder")
public final class PmsPasswordEncoder implements PasswordEncoder {

	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final int HEX_RIGHT_SHIFT_COEFFICIENT = 4;
	private static final int HEX_HIGH_BITS_BITWISE_FLAG = 0x0f;

	private static final Logger LOGGER = LoggerFactory.getLogger(PmsPasswordEncoder.class);

	private static final String ENCODING_ALGORITHM = "MD5";

	private static final String CHARACTER_ENCODING = "utf-8";


	@Autowired
	private DataSource dataSource;

	@Override
	public String encode(final String password) {
		if (password == null) {
			return null;
		}

		try {
			final String salt = "";
			final String passwordBase64 = new String(Base64.encodeBase64(password.getBytes(CHARACTER_ENCODING)));
			final MessageDigest messageDigest = MessageDigest.getInstance(ENCODING_ALGORITHM);

			messageDigest.update((passwordBase64 + salt).getBytes(CHARACTER_ENCODING));

			final byte[] digest = messageDigest.digest();
			final String encodePassword = getFormattedText(digest);

			LOGGER.info(" password : {} , salt : {} , passwordBase64 : {} , encodePassowrd : {}", password, salt, passwordBase64, encodePassword);

			return encodePassword;
		} catch (final NoSuchAlgorithmException e) {

			throw new SecurityException(e);
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 *
	 * @param bytes the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(final byte[] bytes) {
		final StringBuilder buf = new StringBuilder(bytes.length * 2);

		for (int j = 0; j < bytes.length; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> HEX_RIGHT_SHIFT_COEFFICIENT) & HEX_HIGH_BITS_BITWISE_FLAG]);
			buf.append(HEX_DIGITS[bytes[j] & HEX_HIGH_BITS_BITWISE_FLAG]);
		}
		return buf.toString();
	}

}
