package informatikforum.chatbox;

public interface ClientCallback {
	
	public void clientNotifyMessageUpdate();
	public void clientRaiseNormalError(Exception e);
	public boolean clientIsRunning();
}
