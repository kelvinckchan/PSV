package app.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import app.Main;
import app.model.UserInfo;
import app.util.EncryptionUtil;
import app.util.FileUtil;
import app.util.PBEncryption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LoginController {

	private Main mainApp;
	@FXML
	private TextField PBEpassword;

	public LoginController() {
	}

	@FXML
	private void initialize() {
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handleSubmitPassword() {
		if (PBEpassword.getText() != null && PBEpassword.getText().length() > 0) {
			PBEncryption.setPBEpassword(PBEpassword.getText());
			System.out.println("set [" + PBEpassword.getText() + "]");
			File file = mainApp.getPBEFilePath();
			if (file != null && file.exists()) {
				byte[] decrypted = PBEncryption.decryptPBKDF2WithHmacSHA256(file);
				if (decrypted != null) {
					FileUtil.exportByteArrayToFile(file.getAbsolutePath(),
							PBEncryption.encryptPBKDF2WithHmacSHA256(EncryptionUtil.generateSalt(256)));
					mainApp.showView();
				} else {
					showWrongPasswordAlertDialog();
				}
			} else {
				File pbe = new File("PBE");
				FileUtil.exportByteArrayToFile(pbe.getAbsolutePath(),
						PBEncryption.encryptPBKDF2WithHmacSHA256(EncryptionUtil.generateSalt(256)));
				mainApp.setPBEFilePath(pbe);
				mainApp.showView();
			}

		} else {
			System.out.println("Null!" + PBEpassword.getText());
			showNothingEnteredAlertDialog();
		}
	}

	// File tempFile = File.createTempFile(file.getName(), ".tmp", null);
	// FileOutputStream fos = new FileOutputStream(tempFile);
	// fos.write(decrypted);
	// mainApp.loadModelDataFromFile(file);

	private void showWrongPasswordAlertDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Wrong Password!");
		alert.setHeaderText(null);
		alert.setContentText("The PBE Password is Wrong!");
		alert.showAndWait();
	}

	private void showNothingEnteredAlertDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Null PassWord!");
		alert.setHeaderText(null);
		alert.setContentText("Null PassWord!");
		alert.showAndWait();
	}
}