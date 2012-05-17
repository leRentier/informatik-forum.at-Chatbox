package informatikforum.chatbox;

public interface ServerCallback {

	public void serverStopService();
	public boolean serverIsRunning();
	public void serverRaiseNormalError(Exception e);
}
