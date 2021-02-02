package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import model.Photo;
import model.SerializableData;
import model.Tag;
import model.User;

public class searchResultController implements Logout {
@FXML ListView<Photo> photoList;
	
	private Album album;
	private List<Photo> photo;
	private ObservableList<Photo> obsList;
	private User user;
	private SerializableData data;
	private List<Tag> searchTagList;
	private LocalDate fromDate;
	private LocalDate toDate;
	private List<Photo> searchResult = new ArrayList<Photo>();
	
	/**
	 * 
	 * @param stage
	 */
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub
		
		searchPhoto();
		
		obsList = FXCollections.observableArrayList(searchResult);
		
		photoList.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>() {
			
			@Override
			public ListCell<Photo> call(ListView<Photo> arg0) {
				// TODO Auto-generated method stub
				return new Cell();
			}
		});
		
		photoList.setItems(obsList);
		
		photoList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				
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
					con.setPhotoIndex(searchResult.indexOf(photoList.getSelectionModel().getSelectedItem()));
					con.setSearchResult(searchResult);
					con.setPrevPage("searchResult");
					con.setSearchTagList(searchTagList);
					con.setFromDate(fromDate);
					con.setToDate(toDate);
					// con.setPhotoIndex(album.getPhotoIndex(photoList.getSelectionModel().getSelectedItem()));
					con.setAlbum(album);
					con.setMember(user);
					con.setTotalData(data);
					con.start(primaryStage);
					primaryStage.setScene(scene);
					primaryStage.show();
				}
			}
		});
	}
	
	private void searchPhoto() {
		// TODO Auto-generated method stub
		List<Photo> allPhoto = getAllPhoto();
		LocalDate beginDate = null;
		LocalDate endDate = null;
		
		
		if(searchTagList.isEmpty() && fromDate == null && toDate == null) {
			searchResult = allPhoto;
			return;
		} else if(searchTagList.isEmpty() && fromDate != null && toDate == null) {
			beginDate = fromDate;
			endDate = LocalDate.MAX;
		} else if(searchTagList.isEmpty() && fromDate != null && toDate != null) {
			beginDate = fromDate;
			endDate = toDate;
		} else if(searchTagList.isEmpty() && fromDate == null && toDate != null) {
			beginDate = LocalDate.MIN;
			endDate = toDate;
		} else if(!searchTagList.isEmpty() && fromDate == null && toDate == null) {
			beginDate = LocalDate.MIN;
			endDate = LocalDate.MAX;
		} else if(!searchTagList.isEmpty() && fromDate != null && toDate == null) {
			beginDate = fromDate;
			endDate = LocalDate.MAX;
		} else if(!searchTagList.isEmpty() && fromDate != null && toDate != null) {
			beginDate = fromDate;
			endDate = toDate;
		} else if(!searchTagList.isEmpty() && fromDate == null && toDate != null) {
			beginDate = LocalDate.MIN;
			endDate = toDate;
		}
		
		for(int i = 0; i < allPhoto.size(); i++) {
			LocalDate photoDate = allPhoto.get(i).getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			if(searchTagList.isEmpty()) {
				if((photoDate.isEqual(endDate)) 
						|| (photoDate.isEqual(beginDate)) 
						|| (photoDate.isBefore(endDate) 
								&& photoDate.isAfter(beginDate))) {
					searchResult.add(allPhoto.get(i));
				}
			} else {
				if((allPhoto.get(i).matchTag(searchTagList)) && ((photoDate.isEqual(endDate)) 
						|| (photoDate.isEqual(beginDate)) 
						|| (photoDate.isBefore(endDate) 
								&& photoDate.isAfter(beginDate)))) {
					searchResult.add(allPhoto.get(i));
				}
			}
		}
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
	
	private List<Photo> getAllPhoto() {
		// TODO Auto-generated method stub
		List<Photo> result = new ArrayList<Photo>();
		
		for(int i = 0; i < user.getAlbums().size(); i++) {
			for(int j = 0; j < user.getAlbums().get(i).getPhotoNum(); j++) {
				if(!result.contains(user.getAlbums().get(i).getPhoto().get(j))) {
					result.add(user.getAlbums().get(i).getPhoto().get(j));
				}
			}
		}
		return result;
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
			user.getAlbums().add(newAlbum);
			
			for(int i = 0; i < searchResult.size(); i++) {
				newAlbum.addOnePhoto(searchResult.get(i));
			}
			SerializableData.writeData(data);
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	public void handleBack(ActionEvent event) {
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
	 * @throws Exception
	 */
	public void handleLogout(ActionEvent event) throws Exception {
		logout(event);
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
	 * @param member
	 */
	public void setMember(User user) {
		// TODO Auto-generated method stub
		this.user = user;
	}

	/**
	 * 
	 * @param searchTagList
	 */
	public void setTagList(List<Tag> searchTagList) {
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
	public void setTodate(LocalDate toDate) {
		// TODO Auto-generated method stub
		this.toDate = toDate;
	}
}
