package app.model;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import app.util.EncryptionUtil;

public class SymmetricKeyAdapter extends XmlAdapter<byte[], SecretKey> {

	@Override
	public SecretKey unmarshal(byte[] v) throws Exception {
		return byteArrayToSecretKey(v);
	}

	@Override
	public byte[] marshal(SecretKey v) throws Exception {
		return secretKeyToByteArray(v);
	}

	public byte[] secretKeyToByteArray(SecretKey v) {
		byte[] out = v.getEncoded();
		// System.out.println(v.getAlgorithm()+" out> "+out.length);
		switch (v.getAlgorithm()) {
		case "DES":
			out = EncryptionUtil.concateByte(new byte[] { (byte) 1 }, out);
			break;
		case "DESede":
			out = EncryptionUtil.concateByte(new byte[] { (byte) 2 }, out);
			break;
		case "AES":
			out = EncryptionUtil.concateByte(new byte[] { (byte) 3 }, out);
			break;
		default:
			break;
		}
		return out;
	}

	public SecretKey byteArrayToSecretKey(byte[] v) throws Exception {
		byte[] type = Arrays.copyOfRange(v, 0, 1);
		byte[] key = Arrays.copyOfRange(v, 1, v.length);
		return readKey(type[0], key);
	}

	public SecretKey readKey(byte type, byte[] rawkey) throws Exception {
		switch (type) {
		case 1:
			return readDESKey(rawkey);
		case 2:
			return readDESedeKey(rawkey);
		case 3:
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
}
