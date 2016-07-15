package org.jasig.cas.support.pms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Copyright (c) 2016 Hengte Technology Co.,Ltd.
 * All Rights Reserved.<br />
 * created on 7/14/16
 *
 * MD5( password  + salt )
 *
 * @author lcs
 * @version 1.0
 */

public final class PmsPasswordEncoder {

	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final int HEX_RIGHT_SHIFT_COEFFICIENT = 4;
	private static final int HEX_HIGH_BITS_BITWISE_FLAG = 0x0f;

	private static final Logger LOGGER = LoggerFactory.getLogger(PmsPasswordEncoder.class);

	private static final String ENCODING_ALGORITHM = "MD5";

	private static final String CHARACTER_ENCODING = "utf-8";

	public static String encode(final String password , final String salt) {
		if (password == null) {
			return null;
		}
		try {

			final MessageDigest messageDigest = MessageDigest.getInstance(ENCODING_ALGORITHM);

			messageDigest.update((password + salt).getBytes(CHARACTER_ENCODING));

			final byte[] digest = messageDigest.digest();
			final String encodePassword = getFormattedText(digest);

			LOGGER.info(" password : {} , salt : {} , encodePassowrd : {}", password, salt, encodePassword);

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
