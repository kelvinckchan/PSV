package TestCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.spec.IvParameterSpec;

public class EncryptionUtil {
	public static byte[] generateSalt(int saltSize) {
		Random r = new SecureRandom();
		byte[] salt = new byte[saltSize];
		r.nextBytes(salt);
		return salt;
	}

	public static byte[] concateByte(byte[]... b) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (byte[] byt : b)
			try {
				outputStream.write(byt);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return outputStream.toByteArray();
	}

	public static IvParameterSpec generateIV(int ivSize) {
		byte iv[] = new byte[ivSize];
		SecureRandom ivsecRandom = new SecureRandom();
		ivsecRandom.nextBytes(iv);
		return new IvParameterSpec(iv);
	}
}
