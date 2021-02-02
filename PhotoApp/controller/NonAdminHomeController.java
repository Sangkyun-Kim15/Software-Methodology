package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.SerializableData;
import model.Tag;
import model.User;

public class NonAdminHomeController implements Logout{
	@FXML private ListView<Album> albumList;
	@FXML TextField tag;
	@FXML DatePicker fromDate;
	@FXML DatePicker toDate;
	@FXML Button renameBtn;
	@FXML Button deleteBtn;
	
	private ObservableList<Album> obsList;
	private List<Album> albums = new ArrayList<Album>();
	private User user;
	private SerializableData data;
	private List<Tag> searchTagList = new ArrayList<Tag>(); 
	 
	/**
	 * 
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		albums = user.getAlbums();
		obsList = FXCollections.observableArrayList(albums);
		
		albumList.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>() {
			
			@Override
			public ListCell<Album> call(ListView<Album> arg0) {
				// TODO Auto-generated method stub
				return new Cell();
			}
		});
		
		albumList.setItems(obsList);
		albumList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				int albumIndex = albumList.getSelectionModel().getSelectedIndex();
				
				if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/AlbumDetail.fxml"));
					BorderPane root = null;
					try {
						root = (BorderPane)loader.load();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AlbumDetailController albumDetailCon = loader.<AlbumDetailController>getController();
					Scene scene = new Scene(root);
					albumDetailCon.setAlbum(albums.get(albumIndex));
					albumDetailCon.setTotalData(data);
					albumDetailCon.setMember(user);
					albumDetailCon.start(primaryStage);
					primaryStage.setScene(scene);
					primaryStage.show();
				}
			}
			
		});
		
		tag.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
					Dialog<Album> dialog = new Dialog<>();
					dialog.setTitle("Add Tag for search");
					dialog.setHeaderText("Add a Tag.");
					dialog.setResizable(true);
					
					Text key = new Text("Type: ");
					TextField keyTextField = new TextField();
					Text value = new Text("value: ");
					TextField valueTextField = new TextField();
					
					GridPane grid = new GridPane();
					grid.add(key, 1, 1);
					grid.add(keyTextField, 2, 1);
					grid.add(value, 1, 2);
					grid.add(valueTextField, 2, 2);
					
					dialog.getDialogPane().setContent(grid);
					
					ButtonType buttonTypeOk = new ButtonType("done", ButtonData.OK_DONE);
					dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
					
					Optional<Album> result = dialog.showAndWait();
					
					if (result.isPresent() && !keyTextField.getText().isBlank() && !valueTextField.getText().isBlank()) {
						if(searchTagList.size() == 2) {
							System.out.println("more than 2 tags for searching is forbidden");
						} else {
							searchTagList.add(new Tag(keyTextField.getText(), valueTextField.getText()));
							System.out.println(searchTagList.toString());
						}
					}
				}
			}
		});
	}
	
	private class Cell extends ListCell<Album>{
		ImageView imageView = new ImageView();
		AnchorPane apane = new AnchorPane();
		StackPane spane = new StackPane();
		Text dateRange = new Text();
		Text name = new Text();
		Text photoNum = new Text();
		
		public Cell() {
			super();
			imageView.setFitWidth(55.0);
			imageView.setFitHeight(55.0);
			imageView.setPreserveRatio(true);
			StackPane.setAlignment(imageView, Pos.CENTER);
			
			spane.getChildren().add(imageView);
			spane.setPrefHeight(65.0);
			spane.setPrefWidth(55.0);
			
			AnchorPane.setLeftAnchor(spane, 0.0);
			AnchorPane.setLeftAnchor(name, 100.0);
			AnchorPane.setTopAnchor(name, 0.0);
			
			AnchorPane.setLeftAnchor(dateRange, 100.0);
			AnchorPane.setTopAnchor(dateRange, 15.0);
			AnchorPane.setLeftAnchor(photoNum, 100.0);
			AnchorPane.setTopAnchor(photoNum, 30.0);
			
			apane.getChildren().addAll(spane, dateRange, name, photoNum);
			apane.setPrefHeight(65.0);
			setGraphic(apane);
		}
		
		@Override
		public void updateItem(Album album, boolean empty) {
			
			super.updateItem(album, empty);
			if(album == null) {
				imageView.setImage(null);
				name.setText("");
				dateRange.setText("");
				photoNum.setText("");
			} else {
				if(album.getPhoto().isEmpty()) {
					imageView.setImage(null);
					name.setText(album.getName());
					dateRange.setText(album.getDateRange());
					photoNum.setText("" + album.getPhotoNum());
				} else {
					// display the latest updated photo
					imageView.setImage(new Image(album.getPhoto().get(album.getPhoto().size() - 1).getFile().toURI().toString()));
					name.setText(album.getName());
					dateRange.setText(album.getDateRange());
					photoNum.setText("" + album.getPhotoNum());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param member
	 */
	public void setMember(User user) {
		this.user = user;
	}
	
	/**
	 * 
	 * @param data
	 */
	public void setTotalData(SerializableData data) {
		this.data = data;
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleRename(ActionEvent event) throws IOException {
		
		if(albums.isEmpty()) {
			return;
		}
		
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Change the album name");
		dialog.setResizable(true);
		
		Text albumname = new Text("Album name: ");
		TextField albumnameTextField = new TextField();
		
		GridPane grid = new GridPane();
		grid.add(albumname, 1, 1);
		grid.add(albumnameTextField, 2, 1);
		
		dialog.getDialogPane().setContent(grid);
		
		ButtonType btnType = new ButtonType("done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(btnType);
		
		dialog.setResultConverter(ButtonType -> {
			return albumnameTextField.getText();
		});
		
		Optional<String> result = dialog.showAndWait();
		
		if(result.isPresent() && !(result.get().isBlank())) {
			for(int i = 0; i < albums.size(); i++) {
				if(albumnameTextField.getText().equals(albums.get(i).getName())) {
					return;
				}
			}
			albums.get(albumList.getSelectionModel().getSelectedIndex()).setName(albumnameTextField.getText().trim());
			obsList.setAll(albums);
			SerializableData.writeData(data);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleDelete(ActionEvent event) throws IOException {
		
		if(albums.isEmpty()) {
			return;
		}
		
		if(alertConfirmation("deleting")) {
			int index = albumList.getSelectionModel().getSelectedIndex();
			obsList.remove(index);
			user.deleteAlbum(albums.get(index));
			
			SerializableData.writeData(data);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleAddAlbum(ActionEvent event) throws IOException {
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Add a New Album");
		dialog.setResizable(true);
		
		Text albumname = new Text("Album name: ");
		TextField albumnameTextField = new TextField();
		
		GridPane grid = new GridPane();
		grid.add(albumname, 1, 1);
		grid.add(albumnameTextField, 2, 1);
		
		dialog.getDialogPane().setContent(grid);
		
		ButtonType btnType = new ButtonType("done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(btnType);
		
		dialog.setResultConverter(ButtonType -> {
			if(!albumnameTextField.getText().isBlank()) {
				return new Album(albumnameTextField.getText().trim());
			} else {
				return null;
			}
		});
		
		Optional<Album> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			Album newAlbum = (Album)result.get();
			
			for(int i = 0; i < albums.size(); i++) {
				if(albumnameTextField.getText().equals(albums.get(i).getName())) {
					return;
				}
			}
			obsList.add(newAlbum);
			albums.add(newAlbum);
			
			SerializableData.writeData(data);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void handleLogout(ActionEvent event) throws Exception {
		logout(event);
	}
	
	/**
	 * 
	 * @param actionType
	 * @return
	 */
	public boolean alertConfirmation(String actionType) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation: " + actionType);
		alert.setHeaderText("Check the Confirmation message");
		alert.setContentText("Are you sure about " + actionType);
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}
	
	public void alertInvalidInput() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Invalid Input");
		alert.setHeaderText("Please select valid date range.");
		alert.setContentText("Search condition will be reset.");
		alert.showAndWait();
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handleReset(ActionEvent event) {
		if(alertConfirmation("reset the search condition")) {
			searchTagList.clear();
			fromDate.setValue(null);
			toDate.setValue(null);
			System.out.println(searchTagList.toString());
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handleSearch(ActionEvent event) {
		
		if(fromDate.getValue() != null && toDate.getValue() != null) {
			if(!(fromDate.getValue().isBefore(toDate.getValue()) || fromDate.getValue().equals(toDate.getValue()))) {
				alertInvalidInput();
				searchTagList.clear();
				fromDate.setValue(null);
				toDate.setValue(null);
				return;
			}
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/searchResult.fxml"));
		BorderPane root = null;
		try {
			root = (BorderPane)loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		searchResultController con = loader.<searchResultController>getController();
		Scene scene = new Scene(root);
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		con.setTagList(searchTagList);
		con.setFromDate(fromDate.getValue());
		con.setTodate(toDate.getValue());
		con.setTotalData(data);
		con.setMember(user);
		con.start(stage);
		stage.setScene(scene);
		stage.show();
	}
}
