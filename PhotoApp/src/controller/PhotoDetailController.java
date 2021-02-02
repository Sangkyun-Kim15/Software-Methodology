package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.SerializableData;
import model.Tag;
import model.User;

public class PhotoDetailController implements Logout {
	@FXML ImageView photoView;
	@FXML ListView<Tag> tagList;
	@FXML Text date;
	@FXML TextArea captionTextArea;
	@FXML Button prev;
	@FXML Button next;
	
	private SerializableData data;
	private ObservableList<Tag> obsList;
	private User user;
	private Album album;
	private List<Photo> photos;
	private int pohtoIndex;
	private String prevPage = "";
	private List<Tag> searchTagList;
	private LocalDate fromDate;
	private LocalDate toDate;
	
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		if(!prevPage.equals("searchResult")) {
			photos = album.getPhoto();
		}
		obsList = FXCollections.observableArrayList(photos.get(pohtoIndex).getTag());
		tagList.setItems(obsList);
		photoView.setImage(new Image(photos.get(pohtoIndex).getFile().toURI().toString()));
		date.setText(photos.get(pohtoIndex).getDate());
		captionTextArea.setText(photos.get(pohtoIndex).getCaption());
		
		if (pohtoIndex == 0) {
			prev.setDisable(true);
		}
		if(pohtoIndex == photos.size() - 1) {
			next.setDisable(true);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleDeleteTag(ActionEvent event) throws IOException {
		if(photos.get(pohtoIndex).getTag().isEmpty()) {
			return;
		}
		if(alertConfirmation("deleting")) {
			System.out.println("Delete Tag function");
			
			for(int i = 0; i < photos.get(pohtoIndex).getTag().size(); i++) {
				
				if(photos.get(pohtoIndex).getOneTag(i).equals(tagList.getSelectionModel().getSelectedItem())) {
					obsList.remove(photos.get(pohtoIndex).getOneTag(i));
					photos.get(pohtoIndex).deleteTag(i);
					tagList.setItems(obsList);
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
	public void handleAddTag(ActionEvent event) throws IOException {
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Insert Tag");
		dialog.setHeaderText("Insert a new Tag.");
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
			Photo temp = photos.get(pohtoIndex);
			temp.addTag(keyTextField.getText(), valueTextField.getText());
			
			obsList.clear();
			obsList.addAll(temp.getTag());
			SerializableData.writeData(data);
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void handleEditCaption(ActionEvent event) throws IOException {
		Dialog<Album> dialog = new Dialog<>();
		dialog.setTitle("Insert Caption");
		dialog.setHeaderText("Insert a new Caption.");
		dialog.setResizable(true);
		
		Text captionText = new Text("Caption: ");
		TextField captionTextField = new TextField();
		
		GridPane grid = new GridPane();
		grid.add(captionText, 1, 1);
		grid.add(captionTextField, 2, 1);
		
		dialog.getDialogPane().setContent(grid);
		ButtonType buttonTypeOk = new ButtonType("done", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		Optional<Album> result = dialog.showAndWait();
		
		if(result.isPresent() && !captionTextField.getText().isBlank()) {
			Photo temp = photos.get(pohtoIndex);
			temp.setCaption(captionTextField.getText());
			
			captionTextArea.clear();
			captionTextArea.setText(photos.get(pohtoIndex).getCaption());
			
			SerializableData.writeData(data);
			
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handlePrevPhoto(ActionEvent event) {
		if(!prevPage.equals("searchResult")) {
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
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			con.setPhotoIndex(pohtoIndex - 1);
			con.setAlbum(album);
			con.setMember(user);
			con.setTotalData(data);
			con.start(stage);
			stage.setScene(scene);
			stage.show();
			
		} else {
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
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			con.setPhotoIndex(pohtoIndex - 1);
			con.setSearchResult(photos);
			con.setPrevPage("searchResult");
			con.setSearchTagList(searchTagList);
			con.setFromDate(fromDate);
			con.setToDate(toDate);
			// con.setPhotoIndex(album.getPhotoIndex(photoList.getSelectionModel().getSelectedItem()));
			con.setAlbum(album);
			con.setMember(user);
			con.setTotalData(data);
			con.start(stage);
			stage.setScene(scene);
			stage.show();
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handleNextPhoto(ActionEvent event) {
		if(!prevPage.equals("searchResult")) {
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
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			con.setPhotoIndex(pohtoIndex + 1);
			con.setAlbum(album);
			con.setMember(user);
			con.setTotalData(data);
			con.start(stage);
			stage.setScene(scene);
			stage.show();
			
		} else {
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
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			con.setPhotoIndex(pohtoIndex + 1);
			con.setSearchResult(photos);
			con.setPrevPage("searchResult");
			con.setSearchTagList(searchTagList);
			con.setFromDate(fromDate);
			con.setToDate(toDate);
			con.setAlbum(album);
			con.setMember(user);
			con.setTotalData(data);
			con.start(stage);
			stage.setScene(scene);
			stage.show();
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
	 * @param album
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}

	/**
	 * 
	 * @param photoIndex
	 */
	public void setPhotoIndex(int photoIndex) {
		// TODO Auto-generated method stub
		this.pohtoIndex = photoIndex;
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
	 * @param event
	 * @throws IOException
	 */
	public void handleBack(ActionEvent event) throws IOException {
		
		try {
			if(!prevPage.equals("searchResult")) {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/AlbumDetail.fxml"));
				BorderPane root = (BorderPane)loader.load();
				
				AlbumDetailController albumDetailCon = loader.<AlbumDetailController>getController();
				
				Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				albumDetailCon.setAlbum(album);
				albumDetailCon.setTotalData(data);
				albumDetailCon.setMember(user);
				albumDetailCon.start(stage);
				
				stage.setScene(scene);
				stage.show();
				
			} else {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/searchResult.fxml"));
				BorderPane root = (BorderPane)loader.load();
				searchResultController con = loader.<searchResultController>getController();
				Scene scene = new Scene(root);
				Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				con.setTagList(searchTagList);
				con.setFromDate(fromDate);
				con.setTodate(toDate);
				con.setTotalData(data);
				con.setMember(user);
				con.start(stage);
				stage.setScene(scene);
				stage.show();
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
		alert.setContentText("Are you sure about " + actionType);
		
		if(alert.showAndWait().get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param searchResult
	 */
	public void setSearchResult(List<Photo> searchResult) {
		// TODO Auto-generated method stub
		this.photos = searchResult;
	}

	/**
	 * 
	 * @param input
	 */
	public void setPrevPage(String input) {
		// TODO Auto-generated method stub
		this.prevPage = input;
	}

	/**
	 * 
	 * @param searchTagList
	 */
	public void setSearchTagList(List<Tag> searchTagList) {
		// TODO Auto-generated method stub
		this.searchTagList = searchTagList;
	}

	/**
	 * 
	 * @param fromDate
	 */
	public void setFromDate(LocalDate fromDate) {
		// TODO Auto-generated method stub
		this.fromDate = fromDate;
	}

	/**
	 * 
	 * @param toDate
	 */
	public void setToDate(LocalDate toDate) {
		// TODO Auto-generated method stub
		this.toDate = toDate;
	}
}
