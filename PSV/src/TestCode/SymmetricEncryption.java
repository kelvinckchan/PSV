package TestCode;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
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
import javax.crypto.spec.SecretKeySpec;

public class SymmetricEncryption {

	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		SymmetricEncryption app = new SymmetricEncryption();
		try {
			app.run();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
//	 ECB mode, CBC mode, CFB mode, OFB mode and CTR mode. 
	
	public void run() throws UnsupportedEncodingException {
		String data = "This is the secret data need to Encrypt";
		char[] password = "123456".toCharArray();

		System.out.println("rawData> " + data);

		salt = generateSalt();
		randomIvSpec = generateIV();
		skey = generateSKey(password);
		byte[] en = encrypt(data.getBytes());
		System.out.println("encryptedData> " + en);

		byte[] de = decrypt(en);
		System.out.println("decryptedData> " + new String(de));
		skey = generateSKey("Wrongpassword".toCharArray());
		de = decrypt(en);
		if (de != null) {
			System.out.println("decryptedData> " + new String(de));
		} else {
			System.out.println("Wrong Password.");
		}
		System.out.println("End.");
	}

	int iterCount = 12288;
	int derivedKeyLength = 256;
	byte[] salt;

	public byte[] generateSalt() {
		Random r = new SecureRandom();
		byte[] salt = new byte[128];
		r.nextBytes(salt);
		return salt;
	}

	SecretKey skey;

	public SecretKey generateSKey(char[] user_entered_password) {
		try {
			KeySpec spec = new PBEKeySpec(user_entered_password, salt, iterCount, derivedKeyLength);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecretKey skey = f.generateSecret(spec);
			return new SecretKeySpec(skey.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	IvParameterSpec randomIvSpec;

	public IvParameterSpec generateIV() {
		byte iv[] = new byte[16];
		SecureRandom ivsecRandom = new SecureRandom();
		ivsecRandom.nextBytes(iv); // self-seeded randomizer to generate IV
		IvParameterSpec randomIvSpec = new IvParameterSpec(iv);
		return randomIvSpec;
	}

	public byte[] encrypt(byte[] data) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, skey, randomIvSpec);
			return c.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] decrypt(byte[] ciphertext) {
		/* Decrypt the message, given derived key and initialization vector. */
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey, randomIvSpec);
			return cipher.doFinal(ciphertext);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
