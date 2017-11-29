package Key;

import java.security.KeyPair;

import javax.xml.bind.annotation.XmlElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AsymmetricKey {

	private StringProperty keyName;
	private StringProperty keyInfo;
	private StringProperty keyPairString;
	private KeyPair keyPair;
	
	public AsymmetricKey() {
		this(null, null, null);
	}

	public AsymmetricKey(String keyName, String keyInfo, KeyPair keyPair) {
		this.keyName = new SimpleStringProperty(keyName);
		this.keyInfo = new SimpleStringProperty(keyInfo);
		this.keyPairString = new SimpleStringProperty(keyPair.toString());
		this.keyPair = keyPair;
	}

	public StringProperty keyNameProperty() {
		return keyName;
	}

	@XmlElement(name = "keyName")
	public String getKeyName() {
		return keyName.get();
	}

	public AsymmetricKey setKeyName(String keyName) {
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

	public AsymmetricKey setKeyInfo(String keyInfo) {
		this.keyInfo.set(keyInfo);
		return this;
	}

	@XmlElement(name = "keyPair")
	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
		setKeyPairString(keyPair.toString());
	}

	public StringProperty keyPairStringProperty() {
		return keyPairString;
	}

	@XmlElement(name = "keyPairString")
	public String getKeyPairString() {
		return keyPairString.get();
	}

	private void setKeyPairString(String keyPairString) {
		this.keyPairString.set(keyPairString);
	}

}
