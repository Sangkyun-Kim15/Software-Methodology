package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private List<Album> albums;
	
	public User(String username) {
		this.username = username;
		albums = new ArrayList<Album>();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public List<Album> getAlbums() {
		return albums;
	}
	
	public void addAlbum(Album album) {
		this.albums.add(album);
	}
	
	public void deleteAlbum(Album album) {
		// TODO Auto-generated method stub
		this.albums.remove(album);
	}
	
	@Override
	public String toString() {
		return "Member [username=" + username + ", albums=" + albums + "]";
	}
}
