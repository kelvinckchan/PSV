package TestCode;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

public class SpringEncryption {

	public static void main(String[] args) {

		SpringEncryption app = new SpringEncryption();
		app.run();

	}

	public void run() {
		String data = "Account Name:";
		String password = "123456";
		byte[] salt = generateSalt();
		int noIterations = 1500;
		// SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA1,
		// SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256,
		// SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512
		Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
		encoder.setAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
		String result = encoder.encode(password);
		System.out.println("encryptedData> " + result + " \nMatch?>" + encoder.matches(password, result));
		// byte[] encryptedData = encrypt(data.getBytes(), password.toCharArray(), salt,
		// noIterations);
		// System.out.println("encryptedData> " + encryptedData);
	}

	public byte[] generateSalt() {
		Random r = new SecureRandom();
		byte[] salt = new byte[20];
		r.nextBytes(salt);
		return salt;
	}

	public boolean encrypt() {
		Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
		String result = encoder.encode("myPassword");
		return encoder.matches("myPassword", result);
	}

}
