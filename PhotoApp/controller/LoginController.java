package controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.SerializableData;

/**
 * 
 * @author sangkyun, NIKHIL KULKARNI
 *
 */
public class LoginController {
	
	@FXML TextField username;
	@FXML TextField password;
	@FXML Button login;
	
	public static final String storeFile = "data.dat";
	public static final String storeDir = "dat";
	
	/**
	 * 
	 * @param event
	 * @throws IOException 
	 * @throws Exception
	 */
	public void handleLoginAction(ActionEvent event) throws ClassNotFoundException {
		String id = username.getText();		
		File file = new File(storeDir + File.separator + storeFile);
		
		if(!file.exists() || !file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		SerializableData data = null;
		
		try {
			data = SerializableData.readData();
			System.out.println(SerializableData.readData());
		} catch (IOException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		}
		
		try {
			if(id.isBlank()) {
				alertNoData();
			} else {
				// admin account
				if(id.equals("admin")) {
					System.out.println("admin");
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/AdminHome.fxml"));
					BorderPane root = (BorderPane)loader.load();
					
					AdminHomeController adminController = loader.getController();
					Stage stage = (Stage)login.getScene().getWindow();
					
					Scene scene = new Scene(root);
					adminController.start(stage);
					stage.setScene(scene);
					stage.show();
				// non-admin user account
				} else {
					if(data.isMember(id)) {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/NonAdminHome.fxml"));
						BorderPane root = (BorderPane)loader.load();
						NonAdminHomeController nonAdminCon = loader.<NonAdminHomeController>getController();
						nonAdminCon.setMember(data.selectOneUser(id));
						nonAdminCon.setTotalData(data);
						
						Scene scene = new Scene(root);
						Stage PrimaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
						nonAdminCon.start(PrimaryStage);
						PrimaryStage.setScene(scene);
						PrimaryStage.show();
					} else {
					}
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void alertNoData() {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("ERROR: Empty data");
		errorAlert.setHeaderText("Type the username and password.");
		errorAlert.showAndWait();
	}
}


