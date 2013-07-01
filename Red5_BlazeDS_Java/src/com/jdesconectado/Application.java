package com.jdesconectado;

import java.util.ArrayList;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;

public class Application extends ApplicationAdapter{
	private ClientManager clientManager = new ClientManager("userList", false);
	private ArrayList<String> connectedClients = new ArrayList<String>();
	@Override
	public synchronized boolean connect(IConnection conn, IScope scope,
			Object[] params) {
		//Verify is username is in params
		if(params == null || params.length == 0){
			rejectClient("No username was provided");
		}
		if(!super.connect(conn, scope, params)){
			return false;
		}
		String username = params[0].toString();
		String uid = conn.getClient().getId();
		//add the username to the collection
		connectedClients.add(username);
		clientManager.addClient(scope, username, uid);
		return true;
	}
	
	@Override
	public synchronized void disconnect(IConnection conn, IScope scope) {
		String uid = conn.getClient().getId();
		String username = clientManager.removeClient(scope, uid);
		connectedClients.remove(username);
		super.disconnect(conn, scope);
	}
	
	public ArrayList<String> getConnectedClients(){
		return connectedClients;
	}
}
