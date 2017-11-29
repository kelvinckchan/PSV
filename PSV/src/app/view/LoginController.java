package app.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import TestCode.FileUtil;
import app.Main;
import app.model.Model;
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

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
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
					mainApp.showView();
				} else {
					showWrongPasswordAlertDialog();
				}
			} else {

				try {
					File pbe = new File("PBE");
					pbe.createNewFile();
					FileUtil.exportByteArrayToFile(pbe.getAbsolutePath(),
							PBEncryption.encryptPBKDF2WithHmacSHA256(pbe));
					mainApp.setPBEFilePath(pbe);
				} catch (IOException e) {
					e.printStackTrace();
				}
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