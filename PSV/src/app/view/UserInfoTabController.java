package app.view;

import app.Main;
import app.model.UserInfo;
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

public class UserInfoTabController {

	private ObservableList<String> comboBoxList = FXCollections.observableArrayList("DES", "3DES", "AES");

	@FXML
	private TableView<UserInfo> ModelTable;
	@FXML
	private TableColumn<UserInfo, String> accountNameColumn;
	@FXML
	private TableColumn<UserInfo, String> userIDColumn;
	@FXML
	private ComboBox<String> testComboBox;

	@FXML
	private Label accountNameLabel;
	@FXML
	private Label userIDLabel;
	@FXML
	private Label passwordLabel;
	@FXML
	private Label remarksLabel;

	// Reference to the main application.
	private Main mainApp;

	public UserInfoTabController() {
	}

	@FXML
	private void initialize() {
		accountNameColumn.setCellValueFactory(cellData -> cellData.getValue().accountNameProperty());
		userIDColumn.setCellValueFactory(cellData -> cellData.getValue().userIDProperty());

		testComboBox.setItems(comboBoxList);

		showModelDetails(null);

		ModelTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showModelDetails(newValue));
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;

		ModelTable.setItems(mainApp.getUserInfoData());
	}

	private void showModelDetails(UserInfo Model) {
		if (Model != null) {
			accountNameLabel.setText(Model.getAccountName());
			userIDLabel.setText(Model.getUserID());
			passwordLabel.setText(Model.getPassword());
			remarksLabel.setText(Model.getRemarks());

		} else {
			accountNameLabel.setText("");
			userIDLabel.setText("");
			passwordLabel.setText("");
			remarksLabel.setText("");
		}
	}

	@FXML
	private void handleDeleteModel() {
		int selectedIndex = ModelTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			ModelTable.getItems().remove(selectedIndex);
			ModelTable.getSelectionModel().clearSelection();
		} else {
			showNothingSelectedAlertDialog();
		}
	}

	@FXML
	private void handleNewModel() {
		UserInfo tempModel = new UserInfo();
		boolean okClicked = mainApp.showModelEditDialog(tempModel);
		if (okClicked) {
			mainApp.getUserInfoData().add(tempModel);
		}
	}

	@FXML
	private void handleEditModel() {
		UserInfo selectedModel = ModelTable.getSelectionModel().getSelectedItem();
		if (selectedModel != null) {
			boolean okClicked = mainApp.showModelEditDialog(selectedModel);
			if (okClicked) {
				showModelDetails(selectedModel);
			}

		} else {
			showNothingSelectedAlertDialog();
		}
	}

	private void showNothingSelectedAlertDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("No Selection");
		alert.setHeaderText(null);
		alert.setContentText("Please select a Model in the table.");
		alert.showAndWait();
	}
}