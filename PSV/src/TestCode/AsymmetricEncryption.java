package TestCode;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.*;

import org.junit.jupiter.api.Test;

public class AsymmetricEncryption {

	public static void main(String[] args) {
		AsymmetricEncryption app = new AsymmetricEncryption();
		app.run();
		System.out.println("AsymmetricEncryption Done");
	}

	KeyPair kp;
	PublicKey publicKey;
	PrivateKey privateKey;

	private void run() {
		System.out.println("AsymmetricEncryption");
		String data = "#¤Ï¤¤This is the secret data need to Encrypt?";

		kp = generateKeyPair(2048);
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		System.out.println("publicKey> " + publicKey + "\nprivateKey> " + privateKey);

		saveKeyPair(kp);
		try {
			PublicKey readpublicKey = (PublicKey) readKeyFromFile("public.key", "Public");
			PrivateKey readprivateKey = (PrivateKey) readKeyFromFile("private.Key", "Private");
			System.out.println("publicKey> " + readpublicKey + "\nprivateKey> " + readprivateKey);
			byte[] dataByte = data.getBytes("UTF-8");

			testENDE(dataByte, publicKey, privateKey);
			testENDE(dataByte, publicKey, readprivateKey);
			testENDE(dataByte, privateKey, publicKey);
			testENDE(dataByte, privateKey, readpublicKey);
			testENDE(dataByte, readpublicKey, readprivateKey);
			testENDE(dataByte, readprivateKey, readpublicKey);
			System.out.println("**********************************************************");

			byte[] Storedkp = storeKeyPairToByteArray(kp);
			KeyPair readkp = readKeyPairFromByteArray(Storedkp);
			PublicKey readpub = readkp.getPublic();
			PrivateKey readpri = readkp.getPrivate();
			testENDE(dataByte, readpub, readpri);
			testENDE(dataByte, readpri, readpub);
			testENDE(dataByte, publicKey, readpri);
			testENDE(dataByte, readpri, publicKey);
			testENDE(dataByte, privateKey, readpub);
			testENDE(dataByte, readpub, privateKey);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testENDE(byte[] dataByte, Key pub, Key pri) throws UnsupportedEncodingException {
		byte[] en = rsaEncrypt(dataByte, pub);
		byte[] de = rsaDecrypt(en, pri);
		System.out.println("en> " + en + "\nde> " + new String(de, "UTF-8"));
		System.out.println("---------------------------------------------------------------------------");
		assertEquals(new String(dataByte, "UTF-8"), new String(de, "UTF-8"));
	}

	public byte[] storeKeyPairToByteArray(KeyPair kp) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(kp);
		return b.toByteArray();
	}

	public KeyPair readKeyPairFromByteArray(byte[] kpb) throws IOException {
		ByteArrayInputStream bi = new ByteArrayInputStream(kpb);
		ObjectInputStream oi = new ObjectInputStream(bi);
		try {
			return (KeyPair) oi.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public KeyPair generateKeyPair(int keySize) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(keySize);
			KeyPair kp = kpg.genKeyPair();
			return kp;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), "UTF-8");
	}

	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes("UTF-8"));

		byte[] signature = privateSignature.sign();

		return Base64.getEncoder().encodeToString(signature);
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes("UTF-8"));

		byte[] signatureBytes = Base64.getDecoder().decode(signature);

		return publicSignature.verify(signatureBytes);
	}

	// http://niels.nu/blog/2016/java-rsa.html
	public void test() throws Exception {
		KeyPair pair = generateKeyPair(2048);
		String signature = sign("foobar", pair.getPrivate());
		// Let's check the signature
		boolean isCorrect = verify("foobar", signature, pair.getPublic());
		System.out.println("Signature correct: " + isCorrect);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void saveKeyPair(KeyPair kp) {
		try {
			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
			saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
			saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
	}

	public void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}

	// RSAPrivateKeySpec rsaPublickeySpec;
	// RSAPublicKeySpec rsaPrivatekeySpec;

	public Key readKeyFromFile(String keyFileName, String Type) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			KeyFactory keyFact = KeyFactory.getInstance("RSA");
			if (Type.equals("Private")) {
				return keyFact.generatePrivate(new RSAPrivateKeySpec(m, e));
			} else if (Type.equals("Public")) {
				return keyFact.generatePublic(new RSAPublicKeySpec(m, e));
			}
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
		return null;
	}

	public byte[] rsaEncrypt(byte[] data, Key key) {
		return rsaProcess(Cipher.ENCRYPT_MODE, data, key);
	}

	public byte[] rsaDecrypt(byte[] data, Key key) {
		return rsaProcess(Cipher.DECRYPT_MODE, data, key);
	}

	public byte[] rsaProcess(int mode, byte[] data, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(mode, key);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// public byte[] rsaEncrypt(byte[] data, Key key) {
	// try {
	// Cipher cipher = Cipher.getInstance("RSA");
	// cipher.init(Cipher.ENCRYPT_MODE, key);
	// return cipher.doFinal(data);
	// } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	// e.printStackTrace();
	// } catch (InvalidKeyException e) {
	// e.printStackTrace();
	// } catch (IllegalBlockSizeException e) {
	// e.printStackTrace();
	// } catch (BadPaddingException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	// public byte[] rsaDecrypt(byte[] data, Key key)
	// throws IllegalBlockSizeException, BadPaddingException,
	// InvalidKeySpecException, NoSuchAlgorithmException,
	// IOException, NoSuchPaddingException, InvalidKeyException {
	// Cipher cipher = Cipher.getInstance("RSA");
	// cipher.init(Cipher.DECRYPT_MODE, key);
	// return cipher.doFinal(data);
	// }
}
