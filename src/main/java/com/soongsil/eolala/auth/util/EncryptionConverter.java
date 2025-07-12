package com.soongsil.eolala.auth.util;

import com.soongsil.eolala.auth.exception.AuthErrorType;
import com.soongsil.eolala.global.support.error.GlobalException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Converter
public class EncryptionConverter implements AttributeConverter<String, String> {

	private static final String SECRET_KEY = System.getenv("REFRESH_TOKEN_SECRET");
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final byte[] IV = SECRET_KEY.substring(0, 16).getBytes(StandardCharsets.UTF_8);

	private final SecretKeySpec keySpec;
	private final IvParameterSpec ivSpec;

	public EncryptionConverter() {
		byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
		this.keySpec = new SecretKeySpec(keyBytes, "AES");
		this.ivSpec = new IvParameterSpec(IV);
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (attribute == null)
			return null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
			byte[] encrypted = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new GlobalException(AuthErrorType.REFRESH_TOKEN_ENCRYPTION_FAILED, e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		if (dbData == null)
			return null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			byte[] decoded = Base64.getDecoder().decode(dbData);
			byte[] decrypted = cipher.doFinal(decoded);
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new GlobalException(AuthErrorType.REFRESH_TOKEN_DECRYPTION_FAILED, e);
		}
	}
}