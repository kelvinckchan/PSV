package TestCode;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Objects;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PBEncryption {

	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		PBEncryption app = new PBEncryption();
		try {
			app.run();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void run() throws UnsupportedEncodingException {
		String data = "This is the secret data need to Encrypt";
		String password = "123456";
		byte[] salt = generateSalt();
		int noIterations = 65536;
		int keyLength = 256;

		System.out.println("rawData> " + data);
		byte[] encryptedData = encryptPBEWithMD5AndTripleDES(data.getBytes(), password.toCharArray(), salt, noIterations);
		System.out.println("encryptPBEWithMD5AndTripleDES> " + encryptedData);

		encryptedData = encryptPBKDF2WithHmacSHA256(data.getBytes(), password.toCharArray(), salt, noIterations,
				keyLength);
		System.out.println("encryptPBKDF2WithHmacSHA256> " + encryptedData);

		byte[] decryptedData = decryptPBKDF2WithHmacSHA256(encryptedData, iv);
		System.out.println("decryptPBKDF2WithHmacSHA256> " + new String(decryptedData));
	}

	public byte[] generateSalt() {
		Random r = new SecureRandom();
		byte[] salt = new byte[20];
		r.nextBytes(salt);
		return salt;
	}

	public byte[] encryptPBEWithMD5AndTripleDES(byte[] data, char[] password, byte[] salt, int noIterations) {
		try {
			String method = "PBEWithMD5AndTripleDES";
			SecretKeyFactory kf = SecretKeyFactory.getInstance(method);
			PBEKeySpec keySpec = new PBEKeySpec(password);
			SecretKey key = kf.generateSecret(keySpec);
			Cipher ciph = Cipher.getInstance(method);
			ciph.init(Cipher.ENCRYPT_MODE, key);
			PBEParameterSpec params = new PBEParameterSpec(salt, noIterations);
			return ciph.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

	SecretKey secretkey;
	byte[] iv;

	public byte[] encryptPBKDF2WithHmacSHA256(byte[] data, char[] password, byte[] salt, int noIterations,
			int keyLength) {
		try {
			/* Derive the key, given password and salt. */
			String method = "PBKDF2WithHmacSHA256";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
			PBEKeySpec spec = new PBEKeySpec(password, salt, noIterations, keyLength);
			SecretKey tmp = factory.generateSecret(spec);
			secretkey = new SecretKeySpec(tmp.getEncoded(), "AES");

			/* Encrypt the message. */
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretkey);
			AlgorithmParameters params = cipher.getParameters();
			iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] ciphertext = cipher.doFinal(data);

			return ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

	Cipher cipher;

	public boolean match2(byte[] rawData, byte[] encryptedData, Cipher cipher, SecretKey secretkey) {
		try {

			/* Encrypt the message. */
			cipher.init(Cipher.ENCRYPT_MODE, secretkey);
			byte[] ciphertext = cipher.doFinal(rawData);
			System.out.printf("rawData>%s, encryptedData>%s, ciphertext>%s\n", new String(rawData), encryptedData,
					ciphertext);

			return Objects.equals(ciphertext, rawData);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}

	}

	public byte[] decryptPBKDF2WithHmacSHA256(byte[] ciphertext, byte[] iv) {
		try {
			/* Decrypt the message, given derived key and initialization vector. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretkey, new IvParameterSpec(iv));
			byte[] plaintext = cipher.doFinal(ciphertext);

			return plaintext;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

}
