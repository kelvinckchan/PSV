package TestCode;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class InfoEncryption {

	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		InfoEncryption app = new InfoEncryption();
		app.run();

	}

	public void run() {
		String data = "Account Name:";
		String password = "123456";
		byte[] salt = generateSalt();
		int noIterations = 1500;
		byte[] encryptedData = encrypt(data.getBytes(), password.toCharArray(), salt, noIterations);
		System.out.println("encryptedData> " + encryptedData);
		encryptedData = encrypt2(data.getBytes(), password.toCharArray(), salt, noIterations);
		System.out.println("encryptedData> " + encryptedData);
	}

	public byte[] generateSalt() {
		Random r = new SecureRandom();
		byte[] salt = new byte[20];
		r.nextBytes(salt);
		return salt;
	}

	public static byte[] encrypt(byte[] data, char[] password, byte[] salt, int noIterations) {
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

	public static byte[] encrypt2(byte[] data, char[] password, byte[] salt, int noIterations) {
		try {
			String method = "PBKDF2WithHmacSHA256";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
			PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));

			return ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

}
