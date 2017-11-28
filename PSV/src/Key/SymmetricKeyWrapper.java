package Key;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "symmetricKeys")
public class SymmetricKeyWrapper {

	private List<SymmetricKey> symmetricKeys;

	@XmlElement(name = "symmetricKey")
	public List<SymmetricKey> getSymmetricKeys() {
		return symmetricKeys;
	}

	public void setSymmetricKeys(List<SymmetricKey> symmetricKeys) {
		this.symmetricKeys = symmetricKeys;
	}
}