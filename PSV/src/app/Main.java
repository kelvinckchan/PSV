package app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import app.model.AsymmetricKey;
import app.model.AsymmetricKeyWrapper;
import app.model.SymmetricKey;
import app.model.SymmetricKeyWrapper;
import app.model.UserInfo;
import app.model.UserInfoWrapper;
import app.util.FileUtil;
import app.util.PBEncryption;
import app.view.UserInfoEditDialogController;
import app.view.SymKeyEditDialogController;
import app.view.AsymKeyEditDialogController;
import app.view.LoginController;
import app.view.RootLayoutController;
import app.view.TabpanelController;
import app.view.UserInfoTabController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("The App");
		initRootLayout();
		showLogin();
	}

	public void initRootLayout() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("./style/application.css").toExternalForm());
			primaryStage.setScene(scene);
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Login.fxml"));
			AnchorPane loginPane = (AnchorPane) loader.load();
			rootLayout.setCenter(loginPane);
			LoginController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showView() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Tabpanel.fxml"));
			AnchorPane Tabpanel = (AnchorPane) loader.load();
			rootLayout.setCenter(Tabpanel);
			TabpanelController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showModelEditDialog(UserInfo userInfo) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/UserInfoEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit UserInfo");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			UserInfoEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setModel(userInfo);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showAsymKeyEditDialog(AsymmetricKey selectedKey) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AsymKeyEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Sym Key");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource("./style/application.css").toExternalForm());
			dialogStage.setScene(scene);

			AsymKeyEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setModel(selectedKey);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showSymKeyEditDialog(SymmetricKey selectedKey) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SymKeyEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Sym Key");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource("./style/application.css").toExternalForm());
			dialogStage.setScene(scene);

			SymKeyEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setModel(selectedKey);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public File getPBEFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("PBEPath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	public void setPBEFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("PBEPath", file.getPath());
			primaryStage.setTitle("PSV - Encrypted");
		} else {
			prefs.remove("PBEPath");
			primaryStage.setTitle("PSV");
		}
	}

	public void loadUserInfoFromFile(File file) {
		try {

			JAXBContext context = JAXBContext.newInstance(UserInfoWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			UserInfoWrapper wrapper = (UserInfoWrapper) um
					.unmarshal(new ByteArrayInputStream(PBEncryption.decryptPBKDF2WithHmacSHA256(file)));

			userInfoData.clear();
			userInfoData.addAll(wrapper.getUserInfos());

			setModelFilePath(file);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public void loadSymKeyFromFile(File file) {
		try {

			JAXBContext context = JAXBContext.newInstance(SymmetricKeyWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			SymmetricKeyWrapper wrapper = (SymmetricKeyWrapper) um
					.unmarshal(new ByteArrayInputStream(PBEncryption.decryptPBKDF2WithHmacSHA256(file)));

			symmetricKeyData.clear();
			symmetricKeyData.addAll(wrapper.getSymmetricKeys());

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public void loadAsymKeyFromFile(File file) {
		try {

			JAXBContext context = JAXBContext.newInstance(AsymmetricKeyWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			AsymmetricKeyWrapper wrapper = (AsymmetricKeyWrapper) um
					.unmarshal(new ByteArrayInputStream(PBEncryption.decryptPBKDF2WithHmacSHA256(file)));

			asymmetricKeyData.clear();
			asymmetricKeyData.addAll(wrapper.getAsymmetricKeys());

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public void saveSymKeyToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SymmetricKeyWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			SymmetricKeyWrapper wrapper = new SymmetricKeyWrapper();
			wrapper.setSymmetricKeys(symmetricKeyData);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			m.marshal(wrapper, out);

			FileUtil.exportByteArrayToFile(file.getAbsolutePath(),
					PBEncryption.encryptPBKDF2WithHmacSHA256(out.toByteArray()));

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			alert.showAndWait();
		}
	}

	public void saveAsymKeyToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(AsymmetricKeyWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			AsymmetricKeyWrapper wrapper = new AsymmetricKeyWrapper();
			wrapper.setAsymmetricKeys(asymmetricKeyData);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			m.marshal(wrapper, out);

			FileUtil.exportByteArrayToFile(file.getAbsolutePath(),
					PBEncryption.encryptPBKDF2WithHmacSHA256(out.toByteArray()));

		} catch (Exception e) { 
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			alert.showAndWait();
		}
	}

	public void saveUserInfoToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(UserInfoWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			UserInfoWrapper wrapper = new UserInfoWrapper();
			wrapper.setUserInfos(userInfoData);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			m.marshal(wrapper, out);

			FileUtil.exportByteArrayToFile(file.getAbsolutePath(),
					PBEncryption.encryptPBKDF2WithHmacSHA256(out.toByteArray()));

			setModelFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());
			alert.showAndWait();
		}
	}

	private ObservableList<UserInfo> userInfoData = FXCollections.observableArrayList();
	private ObservableList<SymmetricKey> symmetricKeyData = FXCollections.observableArrayList();
	private ObservableList<AsymmetricKey> asymmetricKeyData = FXCollections.observableArrayList();

	public ObservableList<UserInfo> getUserInfoData() {
		return userInfoData;
	}

	public ObservableList<SymmetricKey> getSymmetricKeyData() {
		return symmetricKeyData;
	}

	public ObservableList<AsymmetricKey> getAsymmetricKeyData() {
		return asymmetricKeyData;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		launch(args);
	}

	public Main() {
	}

	public void setModelFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle("PSV - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("PSV");
		}
	}

	public File getModelFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

}
