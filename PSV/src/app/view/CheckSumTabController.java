package app.view;

import java.io.File;

import javax.crypto.SecretKey;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import app.Main;
import app.model.SymmetricKey;
import app.util.CheckSum;
import app.util.FileUtil;
import app.util.SymmetricEncryption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CheckSumTabController {
	private ObservableList<String> hashTypCbxList = FXCollections.observableArrayList("MD5", "SHA-1");
	@FXML
	private ComboBox<String> hashTypCbx;
	@FXML
	private TextField checkSumField;

	private Main mainApp;

	public CheckSumTabController() {
	}

	@FXML
	private void initialize() {
		hashTypCbx.setItems(hashTypCbxList);
		checkSumField.setEditable(false);
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void handleCheckSum() {
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null && !hashTypCbx.getSelectionModel().isEmpty()) {

			byte[] csbyte = CheckSum.getCheckSum(hashTypCbx.getValue(), file);
			String csString = new HexBinaryAdapter().marshal(csbyte);

			checkSumField.setText(csString);

		} else {
			showNothingSelectedAlertDialog();
		}
	}


	private void showNothingSelectedAlertDialog() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("No Selection");
		// alert.setHeaderText("No AsymmetricKey Selected");
		alert.setHeaderText(null);
		alert.setContentText("Please select a Hash algorithm.");
		alert.showAndWait();
	}

}