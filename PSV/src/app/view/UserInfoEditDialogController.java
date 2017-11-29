package app.view;

import app.model.UserInfo;
import app.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a Model.
 * 
 * @author Marco Jakob
 */
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

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the Model to be edited in the dialog.
	 * 
	 * @param Model
	 */
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