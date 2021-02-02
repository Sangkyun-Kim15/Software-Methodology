package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Photo> photos;
	private String name;
	private Photo firstPhoto;
	private Photo LastPhoto;
	
	public Album(String name) {
		this.name = name;
		this.photos = new ArrayList<Photo>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Photo> getPhoto() {
		// TODO Auto-generated method stub
		return this.photos;
	}
	
	public void addOnePhoto(Photo photo) {
		this.photos.add(photo);
		setFirstPhoto();
		setLastPhoto();
	}
	
	public int getPhotoIndex(Photo selectedItem) {
		// TODO Auto-generated method stub
		for(int i = 0; i < photos.size(); i++) {
			if(selectedItem.equals(photos.get(i))) {
				return i;
			}
		}
		return 0;
	}
	
	public Photo getFirstPhoto() {
		return firstPhoto;
	}

	public void setFirstPhoto() {
		if(photos.size() == 0) {
			return;
		}
		Photo first = photos.get(0);
		
		if(photos.isEmpty()) {
		} else {
			for(int i = 0; i < photos.size(); i++) {
				if(photos.get(i).getCalendar().compareTo(first.getCalendar()) > 0) {
					first = photos.get(i);
				}
			}
			firstPhoto = first;
		}
	}
	
	public Photo getLastPhoto() {
		return LastPhoto;
	}

	public void setLastPhoto() {
		if(photos.size() == 0) {
			return;
		}
		Photo last = photos.get(0);
		
		if(photos.isEmpty()) {
		} else {
			for(int i = 0; i < photos.size(); i++) {
				if(photos.get(i).getCalendar().compareTo(last.getCalendar()) < 0) {
					last = photos.get(i);
				}
			}
			LastPhoto = last;
		}
	}
	
	public String getDateRange() {
		// TODO Auto-generated method stub
		String firstDate;
		String lastDate;
		
		if(firstPhoto == null) {
			firstDate = "none";
		} else {
			firstDate = firstPhoto.getDate();
		}
		
		if(LastPhoto == null) {
			lastDate = "none";
		} else {
			lastDate = LastPhoto.getDate();
		}
	
		return firstDate + " ~ " + lastDate;
	}
	
	public void removePhoto(int index) {
		// TODO Auto-generated method stub
		photos.remove(index);
		setFirstPhoto();
		setLastPhoto();
	}

	public int getPhotoNum() {
		// TODO Auto-generated method stub
		return this.photos.size();
	}
}
