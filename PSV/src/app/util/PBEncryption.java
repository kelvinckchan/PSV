package app.util;

import java.io.File;
import java.security.AlgorithmParameters;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import TestCode.EncryptionUtil;
import TestCode.FileUtil;

public class PBEncryption {

	public static void main(String[] args) {
		PBEncryption app = new PBEncryption();
		app.run();
	}

	private static char[] PBEpassword = null;
	final static int noIterations = 65536;
	final static int keyLength = 256;

	public PBEncryption() {
	}

	public static void setPBEpassword(String p) {
		PBEpassword = p.toCharArray();
	}

	public void run() {

	}

	public void testPBKDF2ENED(byte[] data, char[] password) {
		byte[] en = encryptPBKDF2WithHmacSHA256(data, password);
		byte[] de = decryptPBKDF2WithHmacSHA256(en, password);
		System.out.println("encrypt PBKDF2WithHmacSHA256> " + en);
		System.out.println("decrypt PBKDF2WithHmacSHA256> " + new String(de));
	}

	public static byte[] encryptPBKDF2WithHmacSHA256(byte[] data, char[] password) {
		return encryptPBKDF2WithHmacSHA256(data, password, EncryptionUtil.generateSalt(128), noIterations, keyLength);
	}

	public static byte[] decryptPBKDF2WithHmacSHA256(byte[] data, char[] password) {
		return decryptPBKDF2WithHmacSHA256(data, password, noIterations, keyLength);
	}

	public static byte[] encryptPBKDF2WithHmacSHA256(byte[] data, char[] password, byte[] salt, int noIterations,
			int keyLength) {
		try {
			/* Derive the key, given password and salt. */
			String method = "PBKDF2WithHmacSHA256";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
			PBEKeySpec spec = new PBEKeySpec(password, salt, noIterations, keyLength);
			SecretKey secretkey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

			/* Encrypt the message. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			cipher.init(Cipher.ENCRYPT_MODE, secretkey, new IvParameterSpec(iv));
			byte[] cipherText = cipher.doFinal(data);
			System.out.println("encryptPBKDF2WithHmacSHA256.");
			return EncryptionUtil.concateByte(salt, iv, cipherText);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decryptPBKDF2WithHmacSHA256(byte[] data, char[] password, int noIterations, int keyLength) {
		try {
			byte[] salt = Arrays.copyOfRange(data, 0, 128);
			byte[] iv = Arrays.copyOfRange(data, 128, 128 + 16);
			byte[] ciphertext = Arrays.copyOfRange(data, 128 + 16, data.length);

			String method = "PBKDF2WithHmacSHA256";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
			PBEKeySpec spec = new PBEKeySpec(password, salt, noIterations, keyLength);
			SecretKey secretkey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

			/* Decrypt the message, given derived key and initialization vector. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretkey, new IvParameterSpec(iv));
			byte[] plaintext = cipher.doFinal(ciphertext);
			System.out.println("decryptPBKDF2WithHmacSHA256.");
			return plaintext;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decryptPBKDF2WithHmacSHA256(File file) {
		return decryptPBKDF2WithHmacSHA256(FileUtil.importByteArrayFromFile(file), PBEpassword);
	}

	public static byte[] encryptPBKDF2WithHmacSHA256(File pbe) {
		return encryptPBKDF2WithHmacSHA256(FileUtil.importByteArrayFromFile(pbe), PBEpassword);
	}

	public static byte[] encryptPBKDF2WithHmacSHA256(byte[] byteArray) {
		return encryptPBKDF2WithHmacSHA256(byteArray, PBEpassword);
	}

}
