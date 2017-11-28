package TestCode;

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
		int noIterations = 185000;
		int hashWidth = 256;


		// SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA1,
		// SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256,
		// SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512

		Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(password, noIterations, hashWidth);
		encoder.setAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		String encryptedData = encoder.encode(data);

		Pbkdf2PasswordEncoder decoder = new Pbkdf2PasswordEncoder(password, noIterations, hashWidth);
		decoder.setAlgorithm(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		System.out.println("encryptedData> " + encryptedData + " \nMatch?>" + decoder.matches(data, encryptedData));

		// byte[] encryptedData = encrypt(data.getBytes(), password.toCharArray(), salt,
		// noIterations);
		// System.out.println("encryptedData> " + encryptedData);
	}

	public boolean encrypt() {
		Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
		String result = encoder.encode("myPassword");
		return encoder.matches("myPassword", result);
	}

}
