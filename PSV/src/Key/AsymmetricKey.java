package Key;

import java.security.KeyPair;

import javafx.beans.property.StringProperty;

public class AsymmetricKey {

	private StringProperty keyName;
	private StringProperty keyInfo;
	private StringProperty keyPairString;
	private KeyPair keyPair;

	public StringProperty keyNameProperty() {
		return keyName;
	}

	public String getKeyName() {
		return keyName.get();
	}

	public void setkeyName(String keyName) {
		this.keyName.set(keyName);
	}

	public StringProperty keyInfoProperty() {
		return keyInfo;
	}

	public String getKeyInfo() {
		return keyInfo.get();
	}

	public void setKeyInfo(String keyInfo) {
		this.keyInfo.set(keyInfo);
	}

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

	public String getKeyPairString() {
		return keyPairString.get();
	}

	private void setKeyPairString(String keyPairString) {
		this.keyPairString.set(keyPairString);
	}

}
