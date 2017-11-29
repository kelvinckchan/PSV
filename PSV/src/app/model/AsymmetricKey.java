package app.model;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AsymmetricKey   {
	private StringProperty keyName;
	private StringProperty keyInfo;
	private StringProperty Type;
	private KeyPair keyPair;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public AsymmetricKey() {
		this(null, null, null, null, null, null);
	}

	public AsymmetricKey(String keyName, String keyInfo, String Type, KeyPair keyPair, PublicKey publicKey,
			PrivateKey privateKey) {
		this.keyName = new SimpleStringProperty(keyName);
		this.keyInfo = new SimpleStringProperty(keyInfo);
		this.keyPair = keyPair;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.Type = new SimpleStringProperty(Type);
	}

	public AsymmetricKey(String keyName, String keyInfo) {
		this(keyName, keyInfo, null, null, null, null);
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

	public StringProperty TypeProperty() {
		return Type;
	}

	@XmlElement(name = "Type")
	public String getType() {
		return Type.get();
	}

	private void setType(String Type) {
		this.Type.set(Type);
	}

	@XmlElement(name = "keyPair")
	@XmlJavaTypeAdapter(AsymmetricKeyPairAdapter.class)
	public KeyPair getKeyPair() {
		return keyPair;
	}

	public AsymmetricKey setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
		this.setType("KeyPair");
		return this;
	}

	@XmlElement(name = "publicKey")
	@XmlJavaTypeAdapter(AsymmetricPublicKeyAdapter.class)
	public PublicKey getPublicKey() {
		return publicKey;
	}

	public AsymmetricKey setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
		this.setType(this.privateKey == null ? "PublicKey" : "KeyPair");
		return this;
	}

	@XmlElement(name = "privateKey")
	@XmlJavaTypeAdapter(AsymmetricPrivateKeyAdapter.class)
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public AsymmetricKey setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
		this.setType(this.publicKey == null ? "PrivateKey" : "KeyPair");
		return this;
	}

}
