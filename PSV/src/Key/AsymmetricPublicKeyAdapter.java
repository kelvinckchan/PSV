package Key;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AsymmetricPublicKeyAdapter extends XmlAdapter<byte[], PublicKey> {

	@Override
	public PublicKey unmarshal(byte[] v) throws Exception {
		return readPublicKeyFromByteArray(v);
	}

	@Override
	public byte[] marshal(PublicKey v) throws Exception {
		return storePublicKeyToByteArray(v);
	}

	public byte[] storePublicKeyToByteArray(PublicKey pk) throws IOException {
		return pk.getEncoded();
	}

	public PublicKey readPublicKeyFromByteArray(byte[] pkb) throws IOException {
		try {
			return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pkb));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
