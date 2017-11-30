package app.view;

import app.model.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserInfoEditDialogController {

	@FXML
	private TextField accountNameField;
	@FXML
	private TextField userIDField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField remarksField;
 

	private Stage dialogStage;
	private UserInfo Model;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setModel(UserInfo Model) {
		this.Model = Model;
		accountNameField.setText(Model.getAccountName());
		userIDField.setText(Model.getUserID());
		passwordField.setText(Model.getPassword());
		remarksField.setText(Model.getRemarks());
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			Model.setAccountName(accountNameField.getText());
			Model.setUserID(userIDField.getText());
			Model.setPassword(passwordField.getText());
			Model.setRemarks(remarksField.getText());

			okClicked = true;
			dialogStage.close();
		}
	}

 
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

 
	private boolean isInputValid() {
		String errorMessage = "";

		if (accountNameField.getText() == null || accountNameField.getText().length() == 0) {
			errorMessage += "No valid acc name!\n";
		}
		if (userIDField.getText() == null || userIDField.getText().length() == 0) {
			errorMessage += "No valid user ID!\n";
		}
		if (passwordField.getText() == null || passwordField.getText().length() == 0) {
			errorMessage += "No valid password!\n";
		}

		if (remarksField.getText() == null || remarksField.getText().length() == 0) {
			errorMessage += "No valid remarks!\n";
		}


		if (errorMessage.length() == 0) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText(null);
			alert.setContentText("Please correct invalid fields\n"+errorMessage);
			alert.showAndWait();
			return false;
		}
	}
}