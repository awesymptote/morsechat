import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class MorseServer {
    
    private ServerSocket server;
    private Socket clientconn;
    public static boolean listening = true;
    private MorseServerHandler clients[] = new MorseServerHandler[10];
    private int clientCount = 0;


    public MorseServer () {
	//nothing needed right now if we don't have event driven message sending
	//or gui
    }
    
    public void runServer()
    {
	try{
	    server = new ServerSocket(8080);
	    
	    while(listening)
		{
		    waitForConnection();
		}//end of listening loop

	}

	catch(IOException e){
	    e.printStackTrace();
	}

    }

    private void waitForConnection() 
    {
	if (clientCount < clients.length){

		System.out.println("Waiting for connection");
		try {
		    clientconn = server.accept();
		    System.out.println("Accepted connection");
		    clients[clientCount] = new MorseServerHandler(clientconn, this,clientCount);
		    clients[clientCount].start();
		    clientCount++;
		    System.out.println("Connection received from " + clientconn.getInetAddress().getHostName());
		}
		catch(IOException e){
		    e.printStackTrace();
		}

	}

	else
	    System.out.println("Client refused due to maximum conns");
    }

    public synchronized void handleMessage(String input,int ID)
    {
	for (int i = 0; i < clientCount; i++)
	    {
		clients[i].send("ID:" + ID + "\n" + input); //TODO: declare send function
	    }
    }

    private int lookupClient(int ID)
    {
	for (int i = 0; i < clients.length; i++)
	    {
		if(clients[i].getID() == ID)
		    return i;
	    }
	return -1;
    }

    
}