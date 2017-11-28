package Key;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "aysmmetricKeys")
public class AsymmetricKeyWrapper {

	private List<AsymmetricKey> aysmmetricKeys;

	@XmlElement(name = "aysmmetricKey")
	public List<AsymmetricKey> getAsymmetricKeys() {
		return aysmmetricKeys;
	}

	public void setAsymmetricKeys(List<AsymmetricKey> aysmmetricKeys) {
		this.aysmmetricKeys = aysmmetricKeys;
	}
}