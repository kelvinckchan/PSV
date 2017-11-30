package app.view;

import java.io.File;

import app.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class RootLayoutController {

	private Main mainApp;

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handleNew() {
		mainApp.getUserInfoData().clear();
		mainApp.setModelFilePath(null);
	}
	@FXML
	private void handleReadUserInfo() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			mainApp.loadUserInfoFromFile(file);
		}
	}

	@FXML
	private void handleSaveUserInfo() {
		File modelFile = mainApp.getModelFilePath();
		if (modelFile != null) {
			mainApp.saveUserInfoToFile(modelFile);
		} else {
			handleSaveAs();
		}
	}
	@FXML
	private void handleReadSymKey() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			mainApp.loadSymKeyFromFile(file);
		}
	}
	@FXML
	private void handleSaveSymKey() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveSymKeyToFile(file);
		}
	}
	
	@FXML
	private void handleReadAsymKey() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			mainApp.loadAsymKeyFromFile(file);
		}
	}
	@FXML
	private void handleSaveAsymKey() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveAsymKeyToFile(file);
		}
	}

	

	@FXML
	private void handleSaveAs() {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveUserInfoToFile(file);
		}
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}
}