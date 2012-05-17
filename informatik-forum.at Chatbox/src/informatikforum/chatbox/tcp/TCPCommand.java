package informatikforum.chatbox.tcp;

public enum TCPCommand {

	REGISTER("register"),
	REGISTEROK("registerok"),
	NOTIFY("notify"),
	NOTIFYOK("notifyok"),
	BEAT("beat");
	
	private final String name;
	
	private TCPCommand(String name){
		this.name = name;
	}
	
	public String toString(){
		return this.name;
	}
}
