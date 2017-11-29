package Key;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.security.KeyPair;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AsymmetricKeyPairAdapter extends XmlAdapter<byte[], KeyPair> {

	@Override
	public KeyPair unmarshal(byte[] v) throws Exception {
		return readKeyPairFromByteArray(v);
	}

	@Override
	public byte[] marshal(KeyPair v) throws Exception {
		return storeKeyPairToByteArray(v);
	}

	public byte[] storeKeyPairToByteArray(KeyPair kp) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(kp);
		byte[] kpb = b.toByteArray();
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

}
