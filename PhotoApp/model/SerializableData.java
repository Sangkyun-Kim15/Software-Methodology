package model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> users;
	public static final String storeFile = "data.dat";
	public static final String storeDir = "dat";
	
	
	public SerializableData() {
		users = new ArrayList<User>();
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void addUsers(User user) {
		this.users.add(user);
	}
	
	public void deleteMember(User member) {
		this.users.remove(member);
	}
	
	public boolean isMember(String id) {
		// TODO Auto-generated method stub
		for(int i = 0; i < users.size(); i ++) {
			if(users.get(i).getUsername().equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public User selectOneUser(String id) {
		for(int i = 0; i < users.size(); i ++) {
			if(users.get(i).getUsername().equals(id)) {
				return users.get(i);
			}
		}
		return null;
	}
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static SerializableData readData() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		SerializableData data = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
			data = (SerializableData) ois.readObject();
		} catch (EOFException e) {
			// TODO: handle exception
			data = null;
		}
		ois.close();
		return data;
	}

	/**
	 * 
	 * @param data
	 * @throws IOException
	 */
	public static void writeData(SerializableData data) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(data);
		oos.close();
	}
}
