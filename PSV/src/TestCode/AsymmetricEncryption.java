package TestCode;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;

public class AsymmetricEncryption {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		AsymmetricEncryption app = new AsymmetricEncryption() ;
		app.generateKeyPairs();
	}

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
			// TODO Auto-generated catch block
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
}
