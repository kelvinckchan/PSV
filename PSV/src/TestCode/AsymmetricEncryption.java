package TestCode;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.*;
import java.util.Base64;

import javax.crypto.Cipher;

public class AsymmetricEncryption {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		AsymmetricEncryption app = new AsymmetricEncryption();
		app.generateKeyPairs();
	}

	public void openKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("JKS");
		// get user password and file input stream
		char[] password = "".toCharArray();
		FileInputStream fis = new FileInputStream("keyStoreName");
		ks.load(fis, password);
		fis.close();

		// // get my private key
		// KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
		// ks.getEntry("privateKeyAlias", password);
		// PrivateKey myPrivateKey = pkEntry.getPrivateKey();
		//
		// // save my secret key
		// javax.crypto.SecretKey mySecretKey;
		// KeyStore.SecretKeyEntry skEntry =
		// new KeyStore.SecretKeyEntry(mySecretKey);
		// ks.setEntry("secretKeyAlias", skEntry, password);

		// store away the keystore
		java.io.FileOutputStream fos = new java.io.FileOutputStream("newKeyStoreName");
		ks.store(fos, password);
		fos.close();

	}

	public static KeyPair generateKeyPair(int keySize) throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		// generator.initialize(2048, new SecureRandom());
		generator.initialize(keySize, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();
		pair.getPrivate();
		return pair;
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
//http://niels.nu/blog/2016/java-rsa.html
	public void test() throws Exception {
		KeyPair pair = generateKeyPair(2048);

		String signature = sign("foobar", pair.getPrivate());

		// Let's check the signature
		boolean isCorrect = verify("foobar", signature, pair.getPublic());
		System.out.println("Signature correct: " + isCorrect);
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes("UTF-8"));

		byte[] signatureBytes = Base64.getDecoder().decode(signature);

		return publicSignature.verify(signatureBytes);
	}
	public static KeyPair getKeyPairFromKeyStore() throws Exception {
	    InputStream ins = AsymmetricEncryption.class.getResourceAsStream("/keystore.jks");

	    KeyStore keyStore = KeyStore.getInstance("JCEKS");
	    keyStore.load(ins, "s3cr3t".toCharArray());   //Keystore password
	    KeyStore.PasswordProtection keyPassword =       //Key password
	            new KeyStore.PasswordProtection("s3cr3t".toCharArray());

	    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("mykey", keyPassword);

	    java.security.cert.Certificate cert = keyStore.getCertificate("mykey");
	    PublicKey publicKey = cert.getPublicKey();
	    PrivateKey privateKey = privateKeyEntry.getPrivateKey();

	    return new KeyPair(publicKey, privateKey);
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void generateKeyPairs() throws NoSuchAlgorithmException {

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(4096);
		KeyPair kp = kpg.genKeyPair();
		Key publicKey = kp.getPublic();
		Key privateKey = kp.getPrivate();
		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pub = null;
		RSAPrivateKeySpec priv = null;
		try {
			pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
			priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		try {
			saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
			saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void readFromFile(String fileName) {
		FileInputStream keyfis;
		try {
			keyfis = new FileInputStream(fileName);
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(encKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			// new BigInteger (1, b64dec.decodeBuffer(modulusBase64)), new BigInteger(1,
			// b64dec.decodeBuffer(exponentBase64)));
			// keyfis.close();
			// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// RSAPublicKeySpec pubKeySpec = keyFactory.getKeySpec(encKey,
			// RSAPublicKeySpec.class);
			// PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
