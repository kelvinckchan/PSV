package app.view;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import javax.crypto.SecretKey;

import app.Main;
import app.model.AsymmetricKey;
import app.util.AsymmetricEncryption;
import app.util.FileUtil;
import app.util.SymmetricEncryption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AsymTabController {

	private ObservableList<String> hashTypCbxList = FXCollections.observableArrayList("MD5withRSA", "SHA1withRSA");
	private ObservableList<String> RSAkeyLengthCbxList = FXCollections.observableArrayList("1024", "2048", "4096");

	private ObservableList<String> exportTypeCbxList = FXCollections.observableArrayList("KeyPair", "PublicKey",
			"PrivateKey");

	@FXML
	private TableView<AsymmetricKey> asymmetricKeyTable;
	@FXML
	private TableColumn<AsymmetricKey, String> keyNameColumn;
	@FXML
	private TableColumn<AsymmetricKey, String> keyInfoColumn;
	@FXML
	private TableColumn<AsymmetricKey, String> keyTypeColumn;
	@FXML
	private ComboBox<String> hashTypCbx;
	@FXML
	private ComboBox<String> keyLengthCbx;
	@FXML
	private ComboBox<String> exportTypeCbx;
	@FXML
	private ComboBox<String> importTypeCbx;
	@FXML
	private Button verifyBtn;
	@FXML
	private Button signBtn;

	private Main mainApp;

	public AsymTabController() {
	}

	@FXML
	private void initialize() {
		// Initialize the AsymmetricKey table with the two columns.
		keyNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyNameProperty());
		keyInfoColumn.setCellValueFactory(cellData -> cellData.getValue().keyInfoProperty());
		keyTypeColumn.setCellValueFactory(cellData -> cellData.getValue().TypeProperty());
		keyLengthCbx.setItems(RSAkeyLengthCbxList);
		hashTypCbx.setItems(hashTypCbxList);
		exportTypeCbx.setItems(exportTypeCbxList);
		// importTypeCbx.setItems(exportTypeCbxList);

		asymmetricKeyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.getKeyPair() == null) {
				signBtn.setDisable(newValue.getPrivateKey() == null ? true : false);
				verifyBtn.setDisable(newValue.getPrivateKey() == null ? true : false);

				if (newValue.getPublicKey() != null && newValue.getPrivateKey() != null)
					exportTypeCbx.setItems(FXCollections.observableArrayList("KeyPair", "PublicKey", "PrivateKey"));

				if (newValue.getPublicKey() != null && newValue.getPrivateKey() == null)
					exportTypeCbx.setItems(FXCollections.observableArrayList("PublicKey"));

				if (newValue.getPublicKey() == null && newValue.getPrivateKey() != null)
					exportTypeCbx.setItems(FXCollections.observableArrayList("PrivateKey"));

			} else {
				signBtn.setDisable(false);
				verifyBtn.setDisable(false);
				exportTypeCbx.setItems(FXCollections.observableArrayList("KeyPair", "PublicKey", "PrivateKey"));
			}

		});
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		asymmetricKeyTable.setItems(mainApp.getAsymmetricKeyData());
	}

	@FXML
	private void handlExport() {
		AsymmetricKey selectedKey = asymmetricKeyTable.getSelectionModel().getSelectedItem();
		if (selectedKey != null && !exportTypeCbx.getSelectionModel().isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
			PublicKey pub = selectedKey.getPublicKey() != null ? selectedKey.getPublicKey()
					: selectedKey.getKeyPair().getPublic();
			PrivateKey pri = selectedKey.getPrivateKey() != null ? selectedKey.getPrivateKey()
					: selectedKey.getKeyPair().getPrivate();
			if (exportTypeCbx.getValue().equals("KeyPair")) {
				AsymmetricEncryption.savePublicKey(pub, file);
				file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
				AsymmetricEncryption.savePrivateKey(pri, file);
			} else if (exportTypeCbx.getValue().equals("PublicKey")) {
				AsymmetricEncryption.savePublicKey(pub, file);
			} else if (exportTypeCbx.getValue().equals("PrivateKey")) {
				AsymmetricEncryption.savePrivateKey(pri, file);
			}
		}
	}

	@FXML
	private void handlImport() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		PublicKey pub = null;
		PrivateKey pri = null;
		if (!exportTypeCbx.getSelectionModel().isEmpty()) {
			if (exportTypeCbx.getValue().equals("KeyPair")) {
				pub = (PublicKey) AsymmetricEncryption.readKeyFromFile(file.getAbsolutePath(), "Public");
				file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
				pri = (PrivateKey) AsymmetricEncryption.readKeyFromFile(file.getAbsolutePath(), "Private");

			} else if (exportTypeCbx.getValue().equals("PublicKey")) {
				pub = (PublicKey) AsymmetricEncryption.readKeyFromFile(file.getAbsolutePath(), "Public");
			} else if (exportTypeCbx.getValue().equals("PrivateKey")) {
				pri = (PrivateKey) AsymmetricEncryption.readKeyFromFile(file.getAbsolutePath(), "Private");
			}
			AsymmetricKey tempKey = null;
			if (pub != null || pri != null) {
				if (pub != null)
					tempKey = new AsymmetricKey().setPublicKey(pub);
				if (pri != null)
					tempKey.setPrivateKey(pri);

				boolean okClicked = mainApp.showAsymKeyEditDialog(tempKey);

				if (okClicked) {
					mainApp.getAsymmetricKeyData().add(tempKey);
				}
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Key import Error!");
				alert.setHeaderText(null);
				alert.setContentText("Key import Error!");
				alert.showAndWait();
			}
		}

	}

	@FXML
	private void handleVerify() {

		AsymmetricKey selectedKey = asymmetricKeyTable.getSelectionModel().getSelectedItem();
		if (selectedKey != null && !hashTypCbx.getSelectionModel().isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
			if (file != null) {
				PublicKey pub = selectedKey.getPublicKey() != null ? selectedKey.getPublicKey()
						: selectedKey.getKeyPair().getPublic();
				fileChooser = new FileChooser();
				// FileChooser.ExtensionFilter extFilter = new
				// FileChooser.ExtensionFilter("Signature files (*.sig)",
				// "*.sig");
				// fileChooser.getExtensionFilters().add(extFilter);
				File sig = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

				try {
					if (AsymmetricEncryption.verify(file, hashTypCbx.getValue(), sig, pub)) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(mainApp.getPrimaryStage());
						alert.setTitle("Verified!");
						alert.setHeaderText(null);
						alert.setContentText("Matched!");
						alert.showAndWait();
					} else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.initOwner(mainApp.getPrimaryStage());
						alert.setTitle("Fail to Verified, not match!");
						alert.setHeaderText(null);
						alert.setContentText("Verified Failed! Change Key or Algorithm!");
						alert.showAndWait();
					}
				} catch (InvalidKeyException | SignatureException e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Fail to Verified, not match!");
					alert.setHeaderText(null);
					alert.setContentText("Verified Failed! Change Key or Algorithm!");
					alert.showAndWait();
				}
			}

		} else {
			showNothingSelectedAlertDialog();
		}
	}

	@FXML
	private void handleSign() {
		AsymmetricKey selectedKey = asymmetricKeyTable.getSelectionModel().getSelectedItem();
		if (selectedKey != null && !hashTypCbx.getSelectionModel().isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
			if (file != null) {
				try {
					PrivateKey pri = selectedKey.getPrivateKey() != null ? selectedKey.getPrivateKey()
							: selectedKey.getKeyPair().getPrivate();
					fileChooser = new FileChooser();
					File sig = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

					AsymmetricEncryption.generateDigitalSignatureFile(file, hashTypCbx.getValue(), pri,
							sig.getAbsolutePath());

					showSuccessDialog();
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.initOwner(mainApp.getPrimaryStage());
					alert.setTitle("Fail");
					// alert.setHeaderText("No AsymmetricKey Selected");
					alert.setHeaderText(null);
					alert.setContentText("Decryption Failed! Change Key or Algorithm!");
					alert.showAndWait();
				}
			}
		} else {
			showNothingSelectedAlertDialog();
		}
	}

	@FXML
	private void handleDeleteKey() {
		int selectedIndex = asymmetricKeyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			asymmetricKeyTable.getItems().remove(selectedIndex);
			asymmetricKeyTable.getSelectionModel().clearSelection();
		} else {
			showNothingSelectedAlertDialog();
		}
	}

	@FXML
	private void handleNewKey() {

		if (keyLengthCbx.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Spec not Complete");
			alert.setHeaderText(null);
			alert.setContentText("Please select key length.");
			alert.showAndWait();
		} else {

			KeyPair kp = AsymmetricEncryption.generateKeyPair(Integer.parseInt(keyLengthCbx.getValue()));

			if (kp != null) {
				AsymmetricKey tempKey = new AsymmetricKey().setPrivateKey(kp.getPrivate()).setPublicKey(kp.getPublic());
				boolean okClicked = mainApp.showAsymKeyEditDialog(tempKey);

				if (okClicked) {
					mainApp.getAsymmetricKeyData().add(tempKey);
				}
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.initOwner(mainApp.getPrimaryStage());
				alert.setTitle("Key Generation Error!");
				alert.setHeaderText(null);
				alert.setContentText("Key Generation Error!");
				alert.showAndWait();
			}
		}

	}
	@FXML
	private void handleEditKey() {
		AsymmetricKey selectedKey = asymmetricKeyTable.getSelectionModel().getSelectedItem();
		if (selectedKey != null) {
			boolean okClicked = mainApp.showAsymKeyEditDialog(selectedKey);
			if (okClicked) {
				// showKeyDetails(selectedKey);
			}

		} else {
			showNothingSelectedAlertDialog();
		}
	}

	private void showSuccessDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Done. ");
		// alert.setHeaderText("No AsymmetricKey Selected");
		alert.setHeaderText(null);
		alert.setContentText("Done! Please check.");
		alert.showAndWait();
	}

	private void showNothingSelectedAlertDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("No Selection");
		// alert.setHeaderText("No AsymmetricKey Selected");
		alert.setHeaderText(null);
		alert.setContentText("Please select a AsymmetricKey in the table.");
		alert.showAndWait();
	}
}