package Key;

import java.security.KeyPair;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AsymmetricKeyAdapter extends XmlAdapter<Byte, KeyPair> {

	@Override
	public KeyPair unmarshal(Byte v) throws Exception {
		return null;
	}

	@Override
	public Byte marshal(KeyPair v) throws Exception {
		return null;
	}
}
