package serverside;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chating")
public class ChatRoom {
	//static Set<Session> chater=Collections.synchronizedSet(new HashSet<Session>());
	static Map<String,Session> chater=Collections.synchronizedMap(new HashMap<String, Session>());
   
   @OnOpen
   public void handleOpen(Session usersession){
	//   chater.add(usersession);
	   System.out.println("Client connected...!");   
	   System.out.println(usersession.toString());
   }
   @OnClose
   public void handleClose(Session usersession){
	   System.out.println("Client disconnected...!");  
	   chater.remove(usersession);
   }
   
   @OnMessage
   public void handleMessage(String message,Session usersession) throws IOException{
	   String username=(String) usersession.getUserProperties().get("username");
	   System.out.println(username);
	   if(username==null){
		   usersession.getUserProperties().put("username",message);
		   chater.put(message, usersession);
		   usersession.getBasicRemote().sendText("You are as :"+message);
	   }else{
	   Set<String> set=chater.keySet();
	   Iterator<String> it=set.iterator();
	   while(it.hasNext()){
		   String key=it.next();
		   if(!key.equals(username)){
			   chater.get(key).getBasicRemote().sendText(username+" : "+message);
		   }
	   } 
	   }
   }
}
