package informatikforum.chatbox.entity;

import java.io.Serializable;

public class Smiley implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8147991424898527674L;
	private String name;
	private String bbCode;
	private int imageId;
	private String url;
	
	public static final char PLACEHOLDER = 'X';
	
	public Smiley(String name, String bbCode, String url, int imageId){
		this.name = name;
		this.bbCode = bbCode;
		this.imageId = imageId;
		this.setUrl(url);
	}
	
	public Smiley(){
		
	}
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public String getBbCode() {
		return bbCode;
	}


	public void setBbCode(String bbCode) {
		this.bbCode = bbCode;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
