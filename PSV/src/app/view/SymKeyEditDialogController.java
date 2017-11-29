package app.view;

import app.model.SymmetricKey;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SymKeyEditDialogController {

	@FXML
	private TextField keyNameField;
	@FXML
	private TextField keyInfoField;
 

	private Stage dialogStage;
	private SymmetricKey Key;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setModel(SymmetricKey Key) {
		this.Key = Key;
		keyNameField.setText(Key.getKeyName());
		keyInfoField.setText(Key.getKeyInfo());
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			Key.setKeyName(keyNameField.getText());
			Key.setKeyInfo(keyInfoField.getText());

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

		if (keyNameField.getText() == null || keyNameField.getText().length() == 0) {
			errorMessage += "No valid key name!\n";
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