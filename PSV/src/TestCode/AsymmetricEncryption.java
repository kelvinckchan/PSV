package TestCode;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.junit.jupiter.api.Test;

import Key.AsymmetricKey;
import Key.AsymmetricKeyWrapper;

public class AsymmetricEncryption {

	public static void main(String[] args) {
		AsymmetricEncryption app = new AsymmetricEncryption();
		app.run();
		System.err.println("AAsymmetricEncryption Done");
	}

	private void run() {
		KeyPair kp;
		PublicKey publicKey;
		PrivateKey privateKey;
		System.out.println("AAsymmetricEncryption");
		String data = "#¤Ï¤¤This is the secret data need to Encrypt?";
		// 1024/2048/4096-bit RSA key
		kp = generateKeyPair(2048);
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		// System.out.println("publicKey> " + publicKey + "\nprivateKey> " +
		// privateKey);

		saveKeyPair(kp);
		try {
			PublicKey readpublicKey = (PublicKey) readKeyFromFile("public.key", "Public");
			PrivateKey readprivateKey = (PrivateKey) readKeyFromFile("private.Key", "Private");
			// System.out.println("publicKey> " + readpublicKey + "\nprivateKey> " +
			// readprivateKey);
			byte[] dataByte = data.getBytes("UTF-8");

			// testENDE(dataByte, publicKey, privateKey);
			// testENDE(dataByte, publicKey, readprivateKey);
			// testENDE(dataByte, privateKey, publicKey);
			// testENDE(dataByte, privateKey, readpublicKey);
			// testENDE(dataByte, readpublicKey, readprivateKey);
			// testENDE(dataByte, readprivateKey, readpublicKey);
			// System.out.println("**********************************************************");
			//
			System.out.println("kp> " + kp);
			byte[] Storedkp = storeKeyPairToByteArray(kp);
			KeyPair readkp = readKeyPairFromByteArray(Storedkp);
			System.out.println("read kp> " + readkp);

			// Path filePath = Paths.get("testStoreAsyKey.xml");
			// String path = filePath.toAbsolutePath().toString();
			// String sigPath = path.replace(path.substring(path.lastIndexOf(".")), ".sig");
			// System.out.println("read sigPath> " + sigPath);
			//
			byte[] readdata = Files.readAllBytes(Paths.get("testStoreAsyKey.xml"));

			String SigningMethod = "SHA1withRSA";
			SigningMethod = "MD5withRSA";
			
			byte[] s = sign(readdata,SigningMethod, readkp.getPrivate());
			System.out.println("dataByte> " + readdata);
			System.out.println("Signed> " + s);
			generateSigFile(s, Paths.get("testStoreAsyKey.xml"));
			byte[] reads = readSigFile(Paths.get("testStoreAsyKey.sig"));
			System.out.println("read Signed> " + reads);
			boolean isCorrect = verify(readdata,SigningMethod, reads, readkp.getPublic());
			System.out.println("Signature correct: " + isCorrect);

			// testENDE(dataByte, readpub, readpri);
			// testENDE(dataByte, readpri, readpub);
			// testENDE(dataByte, publicKey, readpri);
			// testENDE(dataByte, readpri, publicKey);
			// testENDE(dataByte, privateKey, readpub);
			// testENDE(dataByte, readpub, privateKey);

			// asymmetricKeys
			// .add(new
			// AsymmetricKey().setKeyName("KeyPair1").setKeyInfo("RSA2048").setKeyPair(generateKeyPair(2048)));

			// KeyPair temp = generateKeyPair(2048);
			// asymmetricKeys.add(new
			// AsymmetricKey().setKeyName("KeyPair1").setKeyInfo("RSA2048KeyPair")
			// .setPublicKey(temp.getPublic()).setPrivateKey(temp.getPrivate()));
			//
			// asymmetricKeys.add(new
			// AsymmetricKey().setKeyName("PublicKey2").setKeyInfo("RSA2048PublicKey")
			// .setPublicKey(generateKeyPair(2048).getPublic()));
			// asymmetricKeys.add(new
			// AsymmetricKey().setKeyName("PrivateKey3").setKeyInfo("RSA1024PrivateKey")
			// .setPrivateKey(generateKeyPair(1024).getPrivate()));

			// asymmetricKeys
			// .add(new
			// AsymmetricKey().setKeyName("k2").setKeyInfo("RSA4096").setKeyPair(generateKeyPair(4096)));
			// asymmetricKeys
			// .add(new
			// AsymmetricKey().setKeyName("k3").setKeyInfo("RSA1024").setKeyPair(generateKeyPair(1024)));
			// asymmetricKeys
			// .add(new
			// AsymmetricKey().setKeyName("k4").setKeyInfo("RSA2048").setKeyPair(generateKeyPair(2048)));

//			printList();

			// PublicKey ip = asymmetricKeys.get(0).getKeyPair().getPublic();
			// saveAsymmetricKeyToFile(new File("testStoreAsyKey.xml"));
			// loadAsymmetricKeyFromFile(new File("testStoreAsyKey.xml"));

//			printList();

			// testENDE(dataByte, ip, asymmetricKeys.get(0).getKeyPair().getPrivate());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testENDE(byte[] dataByte, Key enKey, Key deKey) throws UnsupportedEncodingException {
		byte[] en = rsaEncrypt(dataByte, enKey);
		byte[] de = rsaDecrypt(en, deKey);
		System.out.println("en> " + en + "\nde> " + new String(de, "UTF-8"));
		System.out.println("---------------------------------------------------------------------------");
		assertEquals(new String(dataByte, "UTF-8"), new String(de, "UTF-8"));
	}

	public void printList() {
		System.out.println("**********************************************************");
		asymmetricKeys.forEach(k -> {
			System.out.println(k.getKeyName() + "> " + k.getKeyInfo() + " > " + k.getType());
			System.out.println("Pair > " + k.getKeyPair());
			System.out.println("Pub > " + k.getPublicKey());
			System.out.println("Pri > " + k.getPrivateKey());
			System.out.println("^^^^^^");
		});
	}

	List<AsymmetricKey> asymmetricKeys = new ArrayList<>();

	public void saveAsymmetricKeyToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(AsymmetricKeyWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			AsymmetricKeyWrapper wrapper = new AsymmetricKeyWrapper();
			wrapper.setAsymmetricKeys(asymmetricKeys);
			m.marshal(wrapper, file);
			// Save the file path to the registry.
			// setModelFilePath(file);
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
	}

	public void loadAsymmetricKeyFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(AsymmetricKeyWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			AsymmetricKeyWrapper wrapper = (AsymmetricKeyWrapper) um.unmarshal(file);
			asymmetricKeys.clear();
			asymmetricKeys.addAll(wrapper.getAsymmetricKeys());
			// // Save the file path to the registry.
			// setModelFilePath(file);
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
	}

	public boolean verifyCheckSum(String mode, File file) throws IOException {
		String format = "";
		if (mode.equals("MD5")) {
			format = ".md5";
		} else if (mode.equals("SHA-1")) {
			format = ".sha1";
		}
		String existCheckSumPath = FileUtil.changeExtension(file.getAbsolutePath(), format);
		String newCheckSum = new HexBinaryAdapter().marshal(getCheckSum(mode, Files.readAllBytes(file.toPath())));
		String existCheckSum = new HexBinaryAdapter().marshal(Files.readAllBytes(Paths.get(existCheckSumPath)));
		return newCheckSum.equals(existCheckSum);
	}

	public void generateCheckSun(String mode, File file) throws IOException {
		String format = "";
		if (mode.equals("MD5")) {
			format = ".md5";
		} else if (mode.equals("SHA-1")) {
			format = ".sha1";
		}
		String checkSumPath = FileUtil.changeExtension(file.getAbsolutePath(), format);
		Files.write(Paths.get(checkSumPath), getCheckSum(mode, Files.readAllBytes(file.toPath())));
	}

	// 
	public void generateDigitalSignatureFile(byte[] data, String HashMode, String method, PrivateKey privateKey,
			String path) {
		try {
			byte[] HashValue = getCheckSum(HashMode, data);
			byte[] SignedValue = sign(HashValue, method, privateKey);
			FileUtil.exportByteArrayToFile(path, SignedValue);

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	}

	public boolean verifyDigitalSignatureFile(String path, PublicKey publicKey) {

		return false;
	}

	public byte[] getCheckSum(String mode, byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance(mode);
			md.update(data);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] storeKeyPairToByteArray(KeyPair kp) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(kp);
		byte[] kpb = b.toByteArray();

		// Files.write(new File("KeyPair.key").toPath(), kpb);
		return kpb;
	}

	public KeyPair readKeyPairFromByteArray(byte[] kpb) throws IOException {
		ByteArrayInputStream bi = new ByteArrayInputStream(kpb);
		ObjectInputStream oi = new ObjectInputStream(bi);
		try {
			KeyPair kp = (KeyPair) oi.readObject();

			return kp;
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

	public static byte[] sign(byte[] data, String method, PrivateKey privateKey) throws InvalidKeyException {
		try {
			Signature privateSignature = Signature.getInstance(method);
			privateSignature.initSign(privateKey);
			privateSignature.update(data);
			return privateSignature.sign();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void generateSigFile(byte[] sign, Path filePath) {
		String path = filePath.toAbsolutePath().toString();
		String sigPath = path.replace(path.substring(path.lastIndexOf(".")), ".sig");
		FileUtil.exportByteArrayToFile(sigPath, sign);
	}

	public byte[] readSigFile(Path filePath) {
		// try {
		// //read .sig with same name
		//// String path = filePath.toAbsolutePath().toString();
		//// String sigPath = path.replace(path.substring(path.lastIndexOf(".")),
		// ".sig");
		// return Files.readAllBytes(filePath);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;
		return FileUtil.importByteArrayFromFile(filePath.toAbsolutePath().toString());
	}

	public static boolean verify(byte[] data, String method, byte[] signature, PublicKey publicKey)
			throws InvalidKeyException, SignatureException {
		try {
			Signature publicSignature = Signature.getInstance(method);
			publicSignature.initVerify(publicKey);
			publicSignature.update(data);
			return publicSignature.verify(signature);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
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

}
