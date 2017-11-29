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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TabpanelController {

	private Main mainApp;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab userInfoTab;
	@FXML
	private UserInfoTabController userInfoTabController;

	public TabpanelController() {
	}

	@FXML
	private void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/userInfoTab.fxml"));
			userInfoTab.setContent(loader.load());;
			userInfoTabController = loader.getController();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
		userInfoTabController.setMainApp(mainApp);
		
	}

}