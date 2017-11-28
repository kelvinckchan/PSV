package TestCode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class TestKeyStore {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		TestKeyStore app = new TestKeyStore();
		try {
			app.createNewKeyStore();
			KeyPair kp = app.generateKeyPair(2048);
			app.storeToKeyStore(ks, "key1", password, kp.getPrivate(), keyStoreFileName);
		} catch (KeyStoreException | CertificateException | IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static KeyPair getKeyPairFromKeyStore() throws Exception {
		InputStream ins = AsymmetricEncryption.class.getResourceAsStream("/keystore.jks");

		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		keyStore.load(ins, "s3cr3t".toCharArray()); // Keystore password
		KeyStore.PasswordProtection keyPassword = // Key password
				new KeyStore.PasswordProtection("s3cr3t".toCharArray());

		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("mykey", keyPassword);

		java.security.cert.Certificate cert = keyStore.getCertificate("mykey");
		PublicKey publicKey = cert.getPublicKey();
		PrivateKey privateKey = privateKeyEntry.getPrivateKey();

		return new KeyPair(publicKey, privateKey);
	}

	public KeyPair generateKeyPair(int keySize) throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(keySize, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();
		return pair;
	}

	public void storeToKeyStore(KeyStore ks, String alias, char[] passwordForKeyCharArray, Key key,
			String filePathToStore)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		Certificate[] cert = ks.getCertificateChain("");
		ks.setKeyEntry(alias, key, passwordForKeyCharArray, cert);
		OutputStream writeStream = new FileOutputStream(filePathToStore);
		ks.store(writeStream, passwordForKeyCharArray);
		writeStream.close();
	}

	static KeyStore ks;
	static char[] password = "123456".toCharArray();
	static String keyStoreFileName = "NewKeyStore.jks";

	public void createNewKeyStore()
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		ks = KeyStore.getInstance("JKS");
		// store away the keystore
		java.io.FileOutputStream fos = new java.io.FileOutputStream(keyStoreFileName);
		ks.load(null, null);
		ks.store(fos, password);
		fos.close();
	}

	public void openKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance("JKS");
		// get user password and file input stream
		char[] password = "".toCharArray();
		FileInputStream fis = new FileInputStream(keyStoreFileName);
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
		// java.io.FileOutputStream fos = new
		// java.io.FileOutputStream(keyStoreFileName);
		// ks.store(fos, password);
		// fos.close();

	}
}
