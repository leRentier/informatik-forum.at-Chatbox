package informatikforum.chatbox.tcp;

import informatikforum.chatbox.StubbornTimedBinarySemaphore;
import informatikforum.chatbox.TimedBinarySemaphore;

import java.net.Socket;

class CommonData {
	
	private static CommonData instance = null;
	private String serverHost;
	private int serverPort;
	private long lastTimeReceiving;
	private StubbornTimedBinarySemaphore stbs;
	private boolean registered;
	private Socket socket;
	private long lastId;


	
	private CommonData(){
		setServerHost("emptyvi.net");
		setServerPort(45654);
		lastTimeReceiving = System.currentTimeMillis();
		stbs = new StubbornTimedBinarySemaphore(10000,0);
		setRegistered(false);
		setLastId(0l);
	}
	
	
	
	static CommonData getInstance(){
		if(instance == null){
			instance = new CommonData();
		}
		
		return instance;
	}


	String getServerHost() {
		return serverHost;
	}



	void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}



	int getServerPort() {
		return serverPort;
	}



	void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	
	long getLastTimeReceiving() {
		return lastTimeReceiving;
	}


	void setLastTimeReceiving(long lastTimeReceiving) {
		this.lastTimeReceiving = lastTimeReceiving;
	}


	boolean isRegistered() {
		return registered;
	}



	void setRegistered(boolean registered) {
		this.registered = registered;
	}



	StubbornTimedBinarySemaphore getStbs() {
		return stbs;
	}

	Socket getSocket() {
		return socket;
	}



	void setSocket(Socket socket) {
		this.socket = socket;
	}



	public long getLastId() {
		return lastId;
	}



	public void setLastId(long lastId) {
		this.lastId = lastId;
	}

}
