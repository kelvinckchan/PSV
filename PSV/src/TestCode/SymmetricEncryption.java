package TestCode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import app.model.UserInfoWrapper;
import app.model.SymmetricKey;
import app.model.SymmetricKeyWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SymmetricEncryption {

	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		SymmetricEncryption app = new SymmetricEncryption();
		app.run();
		System.out.println("End.");
	}

	protected final static int KeyLen_DES_56 = 56;
	protected final static int KeyLen_3DES_112 = 112;
	protected final static int KeyLen_3DES_168 = 168;
	protected final static int KeyLen_AES_128 = 128;
	protected final static int KeyLen_AES_198 = 198;
	protected final static int KeyLen_AES_256 = 256;

	protected final static String DES_ECB_PKCS5Padding = "DES/ECB/PKCS5Padding";
	protected final static String DES_CBC_PKCS5Padding = "DES/CBC/PKCS5Padding";
	protected final static String DES_CFB_PKCS5Padding = "DES/CFB/PKCS5Padding";
	protected final static String DES_OFB_PKCS5Padding = "DES/OFB/PKCS5Padding";
	protected final static String DES_CTR_PKCS5Padding = "DES/CTR/PKCS5Padding";

	protected final static String DESede_ECB_PKCS5Padding = "DESede/ECB/PKCS5Padding";
	protected final static String DESede_CBC_PKCS5Padding = "DESede/CBC/PKCS5Padding";
	protected final static String DESede_CFB_PKCS5Padding = "DESede/CFB/PKCS5Padding";
	protected final static String DESede_OFB_PKCS5Padding = "DESede/OFB/PKCS5Padding";
	protected final static String DESede_CTR_PKCS5Padding = "DESede/CTR/PKCS5Padding";

	protected final static String AES_ECB_PKCS5Padding = "AES/ECB/PKCS5Padding";
	protected final static String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
	protected final static String AES_CFB_PKCS5Padding = "AES/CFB/PKCS5Padding";
	protected final static String AES_OFB_PKCS5Padding = "AES/OFB/PKCS5Padding";
	protected final static String AES_CTR_PKCS5Padding = "AES/CTR/PKCS5Padding";

	protected final static String ECB_PKCS5Padding = "/ECB/PKCS5Padding";
	protected final static String CBC_PKCS5Padding = "/CBC/PKCS5Padding";
	protected final static String CFB_PKCS5Padding = "/CFB/PKCS5Padding";
	protected final static String OFB_PKCS5Padding = "/OFB/PKCS5Padding";
	protected final static String CTR_PKCS5Padding = "/CTR/PKCS5Padding";

	public void run() {
		String data = "#This is the secret data need to Encrypt?";
		byte[] salt3DES = EncryptionUtil.generateSalt(8);
		byte[] saltSHA256 = EncryptionUtil.generateSalt(128);
		int noIterations = 65536;

		SecretKey AES128Key = generateSymKey("AES", KeyLen_AES_128);
		System.out.println("AESKey> " + AES128Key + " : " + AES128Key.getAlgorithm());
		System.out.println("---------------------------------------------------------------------------");

		SecretKey DES56Key = generateSymKey("DES", KeyLen_DES_56);
		System.out.println("DES56Key > " + DES56Key + " : " + DES56Key.getAlgorithm());
		System.out.println("---------------------------------------------------------------------------");

		SecretKey TriDES168Key = generateSymKey("DESede", KeyLen_3DES_168);
		System.out.println("TriDES168Key > " + TriDES168Key + " : " + TriDES168Key.getAlgorithm());
		System.out.println("---------------------------------------------------------------------------");

		// byte[] AES_ECB_PKCS5PaddingEn = encrypt(AES_ECB_PKCS5Padding, AES128Key,
		// data.getBytes());
		// System.out.println("encrypt AES_ECB_PKCS5Padding> " +
		// AES_ECB_PKCS5PaddingEn);
		// byte[] AES_ECB_PKCS5PaddingDe = decrypt(AES_ECB_PKCS5Padding, AES128Key,
		// AES_ECB_PKCS5PaddingEn);
		// System.out.println("decrypt AES_ECB_PKCS5Padding> " + new
		// String(AES_ECB_PKCS5PaddingDe));

		SymmetricKey k1 = new SymmetricKey().setKeyName("k1").setKeyInfo(AES128Key.getAlgorithm()).setSeckey(AES128Key);
		symmetricKeys.add(k1);
		symmetricKeys.add(new SymmetricKey().setKeyName("k2").setKeyInfo(DES56Key.getAlgorithm()).setSeckey(DES56Key));
		symmetricKeys.add(
				new SymmetricKey().setKeyName("k3").setKeyInfo(TriDES168Key.getAlgorithm()).setSeckey(TriDES168Key));

		symmetricKeys.forEach(k -> {
			System.out.println(k.getKeyName()+ "> " + k.getSeckey());
		});

		saveSymmetricKeyToFile(new File("./testStoreSysKey.xml"));
		loadSymmetricKeyFromFile(new File("./testStoreSysKey.xml"));

		symmetricKeys.forEach(k -> {
			System.out.println(k.getKeyName() + "> " + k.getSeckey());
		});

		// try {
		// SecretKey readAES = readKey(k1.getKeyInfo(), k1.getSeckey().getEncoded());
		// System.out.println("Read AESKey> " + readAES + " : " +
		// readAES.getAlgorithm());
//		 byte[] dataByte = data.getBytes("UTF-8");
		// testENDE(ECB_PKCS5Padding, readAES, dataByte);
		// testENDE(ECB_PKCS5Padding, DES56Key, dataByte);
		// testENDE(ECB_PKCS5Padding, TriDES168Key, dataByte);
		//
		// testENDE(CBC_PKCS5Padding, AES128Key, dataByte);
		// testENDE(CBC_PKCS5Padding, DES56Key, dataByte);
		// testENDE(CBC_PKCS5Padding, TriDES168Key, dataByte);
		//
		// testENDE(CFB_PKCS5Padding, AES128Key, dataByte);
		// testENDE(CFB_PKCS5Padding, DES56Key, dataByte);
		// testENDE(CFB_PKCS5Padding, TriDES168Key, dataByte);
		//
		// testENDE(OFB_PKCS5Padding, AES128Key, dataByte);
		// testENDE(OFB_PKCS5Padding, DES56Key, dataByte);
		// testENDE(OFB_PKCS5Padding, TriDES168Key, dataByte);
		//
		// testENDE(CTR_PKCS5Padding, AES128Key, dataByte);
		// testENDE(CTR_PKCS5Padding, DES56Key, dataByte);
		// testENDE(CTR_PKCS5Padding, TriDES168Key, dataByte);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		System.out.println("rawData> " + data);

	}

	List<SymmetricKey> symmetricKeys = new ArrayList<>();

	public void saveSymmetricKeyToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SymmetricKeyWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SymmetricKeyWrapper wrapper = new SymmetricKeyWrapper();
			wrapper.setSymmetricKeys(symmetricKeys);
			m.marshal(wrapper, file);
			// Save the file path to the registry.
			// setModelFilePath(file);
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
	}

	public void loadSymmetricKeyFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SymmetricKeyWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			SymmetricKeyWrapper wrapper = (SymmetricKeyWrapper) um.unmarshal(file);
			symmetricKeys.clear();
			symmetricKeys.addAll(wrapper.getSymmetricKeys());
			// // Save the file path to the registry.
			// setModelFilePath(file);
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
	}

	public void testENDE(String method, SecretKey k, byte[] data) {
		System.out.println("---------------------------------------------------------------------------");
		byte[] en = encrypt(method, k, data);
		System.out.println("encrypt " + k.getAlgorithm() + method + "> " + en);
		byte[] de = decrypt(method, k, en);
		System.out.println("decrypt " + k.getAlgorithm() + method + "> " + new String(de));
		if (!new String(data).equals(new String(de))) {
			System.err.println("Error> " + data + " | " + de);
		}
	}

	public static SecretKey generateSymKey(String method, int keysize) {
		try {
			KeyGenerator keygenerator = KeyGenerator.getInstance(method);
			keygenerator.init(keysize);
			return keygenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] encryptDES(SecretKey sKey, byte[] data) {
		return encrypt(null, sKey, data);
	}

	public SecretKey readKey(String type, byte[] rawkey) throws Exception {
		switch (type) {
		case "DES":
			return readDESKey(rawkey);
		case "DESede":
			return readDESedeKey(rawkey);
		case "AES":
			return readAESKey(rawkey);
		default:
			return null;
		}
	}

	public SecretKey readDESKey(byte[] rawkey) throws Exception {
		SecretKeyFactory f = SecretKeyFactory.getInstance("DES");
		SecretKey skey = f.generateSecret(new DESKeySpec(rawkey));
		return skey;
	}

	public SecretKey readDESedeKey(byte[] rawkey) throws Exception {
		SecretKeyFactory f = SecretKeyFactory.getInstance("DESede");
		SecretKey skey = f.generateSecret(new DESedeKeySpec(rawkey));
		return skey;
	}

	public SecretKey readAESKey(byte[] rawkey) throws Exception {
		SecretKey skey = new SecretKeySpec(rawkey, "AES");
		return skey;
	}

	public boolean needIV(String algo) {
		if (algo.contains("CBC") || algo.contains("CFB") || algo.contains("OFB") || algo.contains("CTR"))
			return true;
		return false;
	}

	// CBC, OFB and CFB need iv
	public byte[] encrypt(String method, SecretKey sKey, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(sKey.getAlgorithm() + method);
			if (needIV(cipher.getAlgorithm())) {
				cipher.init(Cipher.ENCRYPT_MODE, sKey, EncryptionUtil.generateIV(cipher.getBlockSize()));
			} else {
				cipher.init(Cipher.ENCRYPT_MODE, sKey);
			}
			byte[] iv = cipher.getIV();
			return iv != null ? EncryptionUtil.concateByte(iv, cipher.doFinal(data)) : cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] decrypt(String method, SecretKey sKey, byte[] data) {
		try {
			Cipher cipher = Cipher.getInstance(sKey.getAlgorithm() + method);
			if (needIV(cipher.getAlgorithm())) {
				byte[] iv = Arrays.copyOfRange(data, 0, cipher.getBlockSize());
				data = Arrays.copyOfRange(data, cipher.getBlockSize(), data.length);
				cipher.init(Cipher.DECRYPT_MODE, sKey, new IvParameterSpec(iv));
			} else {
				cipher.init(Cipher.DECRYPT_MODE, sKey);
			}
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
