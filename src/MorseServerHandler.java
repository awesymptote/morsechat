import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

   public class MorseServerHandler extends Thread
   {
       private Socket sock;
       private int ID = -1;
       private ObjectOutputStream output;
       private ObjectInputStream input;
       private MorseServer server;

       public MorseServerHandler(Socket sock, MorseServer server, int ID)
       {
	   super();
	   this.sock = sock;
	   this.server = server;
	   this.ID = ID;
       }

       public void run()
       {
	   System.out.println("About to enter listening loop");
	   while(true)
	       {
		   try {
		       getStreams();
		       processConnection();
		   }
		   catch (IOException e)
		       {
			   e.printStackTrace();
		       }
	       }
       }

       public int getID()
       {
	   return ID;
       }

       private void getStreams() throws IOException
       {
	   output =  new ObjectOutputStream(sock.getOutputStream());
	   output.flush();
	   input = new ObjectInputStream(sock.getInputStream());
       }

       private void processConnection() throws IOException
       {
	   String message = "";
	   do {
	       try {
		   message = (String) input.readObject();
		   if (!message.equals(null) && !message.equals(""))
		       System.out.println("Current message is: " + message);
		       server.handleMessage(message,ID);
	       }
	       catch(ClassNotFoundException CNFE){
		   // display message about unknown object type
		   System.out.println("unknown object type");
		   CNFE.printStackTrace();
	       }

	   }
	   while(true);


       }

       public void send(String input)
       {
	   try {
	       output.writeObject(input);
	       output.flush();
	       // no server gui so don't need to display
	   }
	   catch(IOException e){
	       e.printStackTrace();
	   }
       }

   }

