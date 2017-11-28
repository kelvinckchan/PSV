package Key;

import javax.crypto.SecretKey;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SymmetricKey {
	private final StringProperty keyName;
	private final StringProperty keyInfo;
	private SecretKey secKey;

	public SymmetricKey() {
		this(null, null, null);
	}

	public SymmetricKey(String keyName, String keyInfo, SecretKey seckey) {
		this.keyName = new SimpleStringProperty(keyName);
		this.keyInfo = new SimpleStringProperty(keyInfo);
		this.secKey = seckey;

	}

	public StringProperty keyNameProperty() {
		return keyName;
	}

	@XmlElement(name = "keyName")
	public String getKeyName() {
		return keyName.get();
	}

	public SymmetricKey setkeyName(String keyName) {
		this.keyName.set(keyName);
		return this;
	}

	public StringProperty keyInfoProperty() {
		return keyInfo;
	}

	@XmlElement(name = "keyInfo")
	public String getKeyInfo() {
		return keyInfo.get();
	}

	public SymmetricKey setKeyInfo(String keyInfo) {
		this.keyInfo.set(keyInfo);
		return this;
	}
	

	@XmlElement(name = "secKey")
	@XmlJavaTypeAdapter(SymmetricKeyAdapter.class)
	public SecretKey getSeckey() {
		return secKey;
	}

	public SymmetricKey setSeckey(SecretKey seckey) {
		this.secKey = seckey;
		return this;
	}

}
