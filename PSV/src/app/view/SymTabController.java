package app.view;

import javax.crypto.SecretKey;

import TestCode.SymmetricEncryption;
import app.Main;
import app.model.SymmetricKey;
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

public class SymTabController {

	private ObservableList<String> keyTypCbxList = FXCollections.observableArrayList("DES", "3DES", "AES");
	private ObservableList<String> AESkeyLengthCbxList = FXCollections.observableArrayList("128", "198", "256");
	private ObservableList<String> DESkeyLengthCbxList = FXCollections.observableArrayList("56");
	private ObservableList<String> DESedekeyLengthCbxList = FXCollections.observableArrayList("112", "168");
	@FXML
	private TableView<SymmetricKey> SymmetricKeyTable;
	@FXML
	private TableColumn<SymmetricKey, String> keyNameColumn;
	@FXML
	private TableColumn<SymmetricKey, String> keyInfoColumn;
	@FXML
	private ComboBox<String> keyTypCbx;
	@FXML
	private ComboBox<String> keyLengthCbx;

	// @FXML
	// private Label accountNameLabel;
	// @FXML
	// private Label userIDLabel;
	// @FXML
	// private Label passwordLabel;
	// @FXML
	// private Label remarksLabel;
	//
	// // Reference to the main application.
	private Main mainApp;

	/**
	 * The constructor. The constructor is called before the initialize() method.
	 */
	public SymTabController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Initialize the SymmetricKey table with the two columns.
		keyNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyNameProperty());
		keyInfoColumn.setCellValueFactory(cellData -> cellData.getValue().keyInfoProperty());

		keyTypCbx.setItems(keyTypCbxList);
		keyTypCbx.valueProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == null) {
				keyLengthCbx.getItems().clear();
			} else if (newValue.equals("AES")) {
				keyLengthCbx.setItems(AESkeyLengthCbxList);
			} else if (newValue.equals("DES")) {
				keyLengthCbx.setItems(DESkeyLengthCbxList);
			} else if (newValue.equals("3DES")) {
				keyLengthCbx.setItems(DESedekeyLengthCbxList);
			}
		});

		// Clear SymmetricKey details.
		// showModelDetails(null);

		// Listen for selection changes and show the SymmetricKey details when changed.
		// SymmetricKeyTable.getSelectionModel().selectedItemProperty()
		// .addListener((observable, oldValue, newValue) -> showModelDetails(newValue));
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		SymmetricKeyTable.setItems(mainApp.getSymmetricKeyData());
	}

	// private void showModelDetails(SymmetricKey SymmetricKey) {
	// if (SymmetricKey != null) {
	// // Fill the labels with info from the SymmetricKey object.
	// accountNameLabel.setText(SymmetricKey.getAccountName());
	// userIDLabel.setText(SymmetricKey.getUserID());
	// passwordLabel.setText(SymmetricKey.getPassword());
	// remarksLabel.setText( SymmetricKey.getRemarks());
	//
	// } else {
	// // SymmetricKey is null, remove all the text.
	// accountNameLabel.setText("");
	// userIDLabel.setText("");
	// passwordLabel.setText("");
	// remarksLabel.setText("");
	// }
	// }

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeleteKey() {
		int selectedIndex = SymmetricKeyTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			SymmetricKeyTable.getItems().remove(selectedIndex);
			SymmetricKeyTable.getSelectionModel().clearSelection();
		} else {
			// Nothing selected.
			showNothingSelectedAlertDialog();
		}
	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit details
	 * for a new SymmetricKey.
	 */
	@FXML
	private void handleNewKey() {

		if (keyTypCbx.getSelectionModel().isEmpty() || keyLengthCbx.getSelectionModel().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Spec not Complete");
			alert.setHeaderText(null);
			alert.setContentText("Please select both Key type and lenght.");
			alert.showAndWait();
		} else {
			SecretKey sk = SymmetricEncryption.generateSymKey(keyTypCbx.getValue(),
					Integer.parseInt(keyLengthCbx.getValue()));
			SymmetricKey tempKey = new SymmetricKey().setSeckey(sk);
			boolean okClicked = mainApp.showKeyEditDialog(tempKey);

			if (okClicked) {
				mainApp.getSymmetricKeyData().add(tempKey);
			}
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit details
	 * for the selected SymmetricKey.
	 */
	@FXML
	private void handleEditKey() {
		SymmetricKey selectedKey = SymmetricKeyTable.getSelectionModel().getSelectedItem();
		if (selectedKey != null) {
			boolean okClicked = mainApp.showKeyEditDialog(selectedKey);
			if (okClicked) {
				// showKeyDetails(selectedKey);
			}

		} else {
			// Nothing selected.
			showNothingSelectedAlertDialog();
		}
	}

	private void showNothingSelectedAlertDialog() {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("No Selection");
		// alert.setHeaderText("No SymmetricKey Selected");
		alert.setHeaderText(null);
		alert.setContentText("Please select a SymmetricKey in the table.");
		alert.showAndWait();
	}
}