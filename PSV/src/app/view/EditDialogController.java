package app.view;

import app.model.Model;
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
public class EditDialogController {

	@FXML
	private TextField accountNameField;
	@FXML
	private TextField userIDField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField remarksField;
 

	private Stage dialogStage;
	private Model Model;
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
	public void setModel(Model Model) {
		this.Model = Model;
		accountNameField.setText(Model.getAccountName());
		userIDField.setText(Model.getUserID());
		passwordField.setText(Model.getPassword());
		remarksField.setText(Model.getRemarks());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
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

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (accountNameField.getText() == null || accountNameField.getText().length() == 0) {
			errorMessage += "No valid first name!\n";
		}
		if (userIDField.getText() == null || userIDField.getText().length() == 0) {
			errorMessage += "No valid last name!\n";
		}
		if (passwordField.getText() == null || passwordField.getText().length() == 0) {
			errorMessage += "No valid password!\n";
		}

//		if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
//			errorMessage += "No valid postal code!\n";
//		} else {
//			// try to parse the postal code into an int.
//			try {
//				Integer.parseInt(postalCodeField.getText());
//			} catch (NumberFormatException e) {
//				errorMessage += "No valid postal code (must be an integer)!\n";
//			}
//		}

		if (remarksField.getText() == null || remarksField.getText().length() == 0) {
			errorMessage += "No valid remarks!\n";
		}


		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
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