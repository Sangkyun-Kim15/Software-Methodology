package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SerializableData;
import model.User;

public class AdminHomeController implements Logout {
	@FXML ListView<User> userList;
	@FXML Button logout;
	@FXML Button add;
	@FXML Button delete;
	
	private ObservableList<User> obsList;
	private List<User> users = new ArrayList<User>();
	private SerializableData data;
	
	public void start(Stage primaryStage) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		data = SerializableData.readData();
		users = data.getUsers();
		obsList = FXCollections.observableArrayList(users);
		
		// no show admin account
		obsList.remove(0);
		
		userList.setItems(obsList);
		
	}
	
	public void handleLogout(ActionEvent event) throws Exception {
		logout(event);
	}
	
	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void handleAdd(ActionEvent event) throws Exception {
		Dialog<User> dialog = new Dialog<>();
		dialog.setTitle("Add a New Member");
		dialog.setResizable(true);
		
		Text usernameText = new Text("Username: ");
		TextField usernameTextField = new TextField();
		
		GridPane grid = new GridPane();
		grid.add(usernameText, 1, 1);
		grid.add(usernameTextField, 2, 1);
		
		dialog.getDialogPane().setContent(grid);
		
		ButtonType btnType = new ButtonType("done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(btnType);
		
		dialog.setResultConverter(ButtonType -> {
			return new User(usernameTextField.getText());
		});
		
		Optional<User> result = dialog.showAndWait();
		
		if(result.isPresent() && !usernameTextField.getText().isBlank()) {
			User newMember = (User)result.get();
			
			obsList.add(newMember);
			userList.setItems(obsList.sorted());
			users.add(newMember);
			
			SerializableData.writeData(data);
		}
	}
	public void handleDelete(ActionEvent event) throws Exception {
		if(users.isEmpty()) {
			return;
		}
		User input = userList.getSelectionModel().getSelectedItem();
		obsList.remove(input);
		userList.setItems(obsList.sorted());
		users.remove(input);
		SerializableData.writeData(data);
	}
}
