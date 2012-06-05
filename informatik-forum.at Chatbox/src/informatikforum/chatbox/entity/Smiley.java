package informatikforum.chatbox.entity;

import java.io.Serializable;

public class Smiley implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8147991424898527674L;
	private String name;
	private String bbCode;
	private String url;
	
	public static final char PLACEHOLDER = 'X';
	
	public Smiley(String name, String bbCode, String url){
		this.name = name;
		this.bbCode = bbCode;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
