package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Photo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private List<Tag> tag;
	private Calendar calendar;
	private String caption;
	private File file;
	
	public Photo(File file) {
		this.id = UUID.randomUUID().toString();
		this.caption = "";
		this.tag = new ArrayList<Tag>();
		this.calendar = Calendar.getInstance();
		this.calendar.set(Calendar.MILLISECOND, 0);
		this.file = file;
	}
	
	public String getId() {
		return id;
	}

	public List<Tag> getTag() {
		return tag;
	}

	public Calendar getCalendar() {
		return calendar;
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getDate() {
		String[] temp = calendar.getTime().toString().split("\\s+");
		String date = temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[5]; 
		return date;
	}
	
	public void addTag(String type, String data) {
		// TODO Auto-generated method stub
		tag.add(new Tag(type, data));
	}

	public Tag getOneTag(int index) {
		// TODO Auto-generated method stub
		return tag.get(index);
	}

	public void deleteTag(int i) {
		// TODO Auto-generated method stub
		tag.remove(i);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public boolean matchTag(List<Tag> searchTagList) {
		// TODO Auto-generated method stub
		boolean result = false;
		System.out.println("searchTag : " + searchTagList);
		
		for(int i = 0; i < tag.size(); i++) {
			System.out.println("-------------------------");
			System.out.println(tag.get(i).getType());
			System.out.println(searchTagList.get(0).getType());
			System.out.println(tag.get(i).getData());
			System.out.println(searchTagList.get(0).getData());
			if(tag.get(i).getType().equals(searchTagList.get(0).getType()) && tag.get(i).getData().equals(searchTagList.get(0).getData())) {
				result = true;
				System.out.println(result);
			}
		}
		
		if(searchTagList.size() > 1 && result == true) {
			for(int i = 0; i < tag.size(); i++) {
				System.out.println("-------------------------");
				System.out.println(tag.get(i).getType());
				System.out.println(searchTagList.get(1).getType());
				System.out.println(tag.get(i).getData());
				System.out.println(searchTagList.get(1).getData());
				if(tag.get(i).getType().equals(searchTagList.get(1).getType()) && tag.get(i).getData().equals(searchTagList.get(1).getData())) {
					result = true;
					System.out.println(result);
				} else {
					result = false;
					System.out.println(result);
				}
			}
		}
		System.out.println(result);
		return result;
	}
}
