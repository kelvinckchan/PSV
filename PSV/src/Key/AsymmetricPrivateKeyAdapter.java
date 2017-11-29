package Key;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AsymmetricPrivateKeyAdapter extends XmlAdapter<byte[], PrivateKey> {

	@Override
	public PrivateKey unmarshal(byte[] v) throws Exception {
		return readPrivateKeyFromByteArray(v);
	}

	@Override
	public byte[] marshal(PrivateKey v) throws Exception {
		return storePrivateKeyToByteArray(v);
	}

	public byte[] storePrivateKeyToByteArray(PrivateKey pk) throws IOException {
		return pk.getEncoded();
	}

	public PrivateKey readPrivateKeyFromByteArray(byte[] pkb) throws IOException {
		try {
			return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(pkb));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
