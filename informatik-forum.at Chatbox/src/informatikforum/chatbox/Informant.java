package informatikforum.chatbox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Informant implements ClientCallback, ServerCallback{
	
	private static Informant instance = null;
	
	private List<ServerCallback> scbs;
	private List<ClientCallback> ccbs;
	private List<Thread> threads;

	private Informant(){
		scbs = Collections.synchronizedList(new ArrayList<ServerCallback>());
		ccbs = Collections.synchronizedList(new ArrayList<ClientCallback>());
		threads = Collections.synchronizedList(new ArrayList<Thread>());
	}
	
	public static Informant getInstance(){
		if(instance == null){
			instance = new Informant();
		}
		
		return instance;
	}
	
	public synchronized void removeThread(Thread t) {
		this.threads.remove(t);
	}
	
	public synchronized void interruptThreadByClass(Class<?> cl){
		Iterator<Thread> it = threads.iterator();
		while(it.hasNext()){
			Thread t = it.next();
			
			if(cl.isInstance(t)){
				t.interrupt();
				it.remove();
			}
		}
	}

	public synchronized boolean isThreadTypeRunning(Class<?> cl){
		Iterator<Thread> it = threads.iterator();
		while(it.hasNext()){
			Thread t = it.next();
			if(cl.isInstance(t)){
				return true;
			}
		}
		
		return false;
	}

	public synchronized void addThread(Thread t){
		this.threads.add(t);
	}
	
	// TODO Use iterator for methods below.

	@Override
	public void serverStopService() {
		for(ServerCallback scb : this.scbs){
			if(scb.serverIsRunning()){
				scb.serverStopService();
			}
		}
	}

	@Override
	public void clientNotifyMessageUpdate() {
		for(ClientCallback ccb : this.ccbs){
			ccb.clientNotifyMessageUpdate();
		}
	}

	@Override
	public void clientRaiseNormalError(Exception e) {
		for(ClientCallback ccb : this.ccbs){
			if(ccb.clientIsRunning()){
				ccb.clientRaiseNormalError(e);
			}
		}
	}
	
	
	@Override
	public void serverRaiseNormalError(Exception e) {
		for(ServerCallback scb : this.scbs){
			if(scb.serverIsRunning()){
				scb.serverRaiseNormalError(e);
			}
		}
	}
	

	@Override
	public boolean clientIsRunning() {
		boolean isRunning = false;
		
		for(ClientCallback ccb : this.ccbs){
			isRunning |= ccb.clientIsRunning();
		}
		
		return isRunning;
	}
	
	public void registerServerCallback(ServerCallback scb){
		this.scbs.add(scb);
	}
	
	public void registerClientCallback(ClientCallback ccb){
		this.ccbs.add(ccb);
	}
	
	public void unRegisterServerCallback(ServerCallback scb){
		this.scbs.remove(scb);
	}
	
	public void unRegisterClientCallback(ClientCallback ccb){
		this.ccbs.remove(ccb);
	}

	@Override
	public boolean serverIsRunning() {
		boolean isRunning = false;
		
		for(ServerCallback scb : this.scbs){
			isRunning |= scb.serverIsRunning();
		}
		
		return isRunning;
	}


}
