package controller;

import java.io.File;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.SerializableData;
import model.User;

public class AlbumDetailController implements Logout {
	@FXML ListView<Photo> photoList;
	@FXML Text albumName;
	
	private Album album;
	private List<Photo> photos;
	private ObservableList<Photo> obsList;
	private User user;
	private SerializableData data;
	
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		
		albumName.setText(album.getName());
		photos = album.getPhoto();
		obsList = FXCollections.observableArrayList(photos);
		
		photoList.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			
			@Override
			public ListCell<Photo> call(ListView<Photo> arg0) {
				// TODO Auto-generated method stub
				return new Cell();
			}
		});
		
		photoList.setItems(obsList);
		
		if(!photos.isEmpty()) {
			photoList.getSelectionModel().select(0);
		}
		
		photoList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				
				if(!photos.isEmpty()) {
					if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/PhotoDetail.fxml"));
						BorderPane root = null;
						try {
							root = (BorderPane)loader.load();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						PhotoDetailController con = loader.<PhotoDetailController>getController();
						Scene scene = new Scene(root);
						con.setPhotoIndex(album.getPhotoIndex(photoList.getSelectionModel().getSelectedItem()));
						con.setAlbum(album);
						con.setMember(user);
						con.setTotalData(data);
						con.start(primaryStage);
						primaryStage.setScene(scene);
						primaryStage.show();
						
					}
				}
			}
		});
	}
	
	private class Cell extends ListCell<Photo>{
		ImageView imageView = new ImageView();
		Text caption = new Text();
		AnchorPane apane = new AnchorPane();
		StackPane spane = new StackPane();
		
		public Cell(){
			super();
			imageView.setFitWidth(55.0);
			imageView.setFitHeight(55.0);
			imageView.setPreserveRatio(true);
			StackPane.setAlignment(imageView, Pos.CENTER);
			
			spane.getChildren().add(imageView);
			spane.setPrefHeight(65.0);
			spane.setPrefWidth(55.0);
			
			AnchorPane.setLeftAnchor(spane, 0.0);
			AnchorPane.setLeftAnchor(caption, 55.0);
			AnchorPane.setTopAnchor(caption, 0.0);
			apane.getChildren().addAll(spane, caption);
			apane.setPrefHeight(65.0);
			setGraphic(apane);
		}
		
		@Override
		public void updateItem(Photo photo, boolean empty) {
			
			super.updateItem(photo, empty);
			
			if(photo == null) {
				imageView.setImage(null);
				caption.setText("");
			}
			
			if(photo != null) {
				imageView.setImage(new Image(photo.getFile().toURI().toString()));
				
				if(!(photo.getCaption().isEmpty())) {
					caption.setText(photo.getCaption());
				} else {
					caption.setText("");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleAddPhoto(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JPG", "*.jpg"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("PNG", "*.png"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("All files", "*.*"));
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		File file = fileChooser.showOpenDialog(stage);
		Photo temp = null;
		boolean found = false;
		
		
		if(file == null) {
			return;
		} else {
			if(found) {
				//album.addOnePhoto(temp);
				//obsList.add(temp);
			} else {
				Photo newPhoto = new Photo(file);
				album.addOnePhoto(newPhoto);
				obsList.add(newPhoto);
			}
			SerializableData.writeData(data);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void hadleDeletePhoto(ActionEvent event) throws IOException {
		
		if(photos.isEmpty()) {
			return;
		}
		
		if(alertConfirmation("deleting")) {
			int index = album.getPhotoIndex(photoList.getSelectionModel().getSelectedItem());
			obsList.remove(photos.get(index));
			photoList.setItems(obsList);
			album.removePhoto(index);
			SerializableData.writeData(data);
		}
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
		alert.setContentText("Are you sure about " + actionType + "this song?");
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleBack(ActionEvent event) throws IOException {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/NonAdminHome.fxml"));
			BorderPane root = (BorderPane)loader.load();
			
			NonAdminHomeController nonAdminCon = loader.<NonAdminHomeController>getController();
			nonAdminCon.setMember(user);
			nonAdminCon.setTotalData(data);
			
			Scene scene = new Scene(root);
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			nonAdminCon.start(stage);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException 
	 */
	public void handleCopyPhoto(ActionEvent event) throws IOException {
		
		if(photos.isEmpty()) {
			return;
		}
		
		int index = album.getPhotoIndex(photoList.getSelectionModel().getSelectedItem());
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Move the photo to another album");
		dialog.setResizable(true);
		   
		Text moveText = new Text("Album : ");
		List<String> albumNameList = new ArrayList<String>();
		
		for(int i = 0; i < user.getAlbums().size(); i++) {
			if(!user.getAlbums().get(i).getName().equals(album.getName())) {
				albumNameList.add(user.getAlbums().get(i).getName());
			}
		}
		
		ObservableList<String> albumOption = FXCollections.observableArrayList(albumNameList);
		ComboBox<String> cBox = new ComboBox<String>(albumOption);
		
		GridPane grid = new GridPane();
		grid.add(moveText, 1, 1);
		grid.add(cBox, 1, 2);
		   
		dialog.getDialogPane().setContent(grid);
		   
		ButtonType buttonTypeOk = new ButtonType("Copy", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		Optional<Album> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			String albumName = cBox.getSelectionModel().getSelectedItem();
			
			for(int i = 0; i < user.getAlbums().size(); i++) {
				// find the album that has the same name as the album name selected  in the combo box 
				if(user.getAlbums().get(i).getName().equals(albumName)) {
					// copy the photo
					user.getAlbums().get(i).addOnePhoto(album.getPhoto().get(index));
					SerializableData.writeData(data);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleMovePhoto(ActionEvent event) throws IOException {
		
		if(photos.isEmpty()) {
			return;
		}
		
		int index = album.getPhotoIndex(photoList.getSelectionModel().getSelectedItem());
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Move the photo to another album");
		dialog.setResizable(true);
		   
		Text moveText = new Text("Album : ");
		List<String> albumNameList = new ArrayList<String>();
		
		for(int i = 0; i < user.getAlbums().size(); i++) {
			if(!user.getAlbums().get(i).getName().equals(album.getName())) {
				albumNameList.add(user.getAlbums().get(i).getName());
			}
		}
		
		ObservableList<String> albumOption = FXCollections.observableArrayList(albumNameList);
		ComboBox<String> cBox = new ComboBox<String>(albumOption);
		
		GridPane grid = new GridPane();
		grid.add(moveText, 1, 1);
		grid.add(cBox, 1, 2);
		   
		dialog.getDialogPane().setContent(grid);
		   
		ButtonType buttonTypeOk = new ButtonType("Move", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		Optional<Album> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			String albumName = cBox.getSelectionModel().getSelectedItem();
			
			for(int i = 0; i < user.getAlbums().size(); i++) {
				// find the album that has the same name as the album name selected  in the combo box 
				if(user.getAlbums().get(i).getName().equals(albumName)) {
					// copy the photo
					user.getAlbums().get(i).addOnePhoto(album.getPhoto().get(index));
					// delete photo in the original album
					obsList.remove(photos.get(index));
					photoList.setItems(obsList);
					album.removePhoto(index);
					
					SerializableData.writeData(data);
					return;
				}
			}
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
	 * @param member
	 */
	public void setMember(User member) {
		// TODO Auto-generated method stub
		this.user = member;
	}
	
	/**
	 * 
	 * @param album
	 */
	public void setAlbum(Album album) {
		// TODO Auto-generated method stub
		this.album = album;
	}
	
	/**
	 * 
	 * @param data
	 */
	public void setTotalData(SerializableData data) {
		this.data = data;
	}
}
