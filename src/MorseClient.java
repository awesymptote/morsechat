import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;

public class MorseClient extends JFrame
{
    private Socket sock;
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;

    public MorseClient(String host)
    {
	super("Morse Code Client");
	chatServer = host;
	enterField = new JTextField();
	enterField.setEditable(false);
	enterField.addActionListener(new ActionListener() {
	    //send msg to server
	    public void actionPerformed(ActionEvent event)
	    {
		sendData(event.getActionCommand());
		enterField.setText("");
	    }
	    });
	add(enterField, BorderLayout.NORTH);
	displayArea = new JTextArea();
	add(new JScrollPane(displayArea),BorderLayout.CENTER);
	setSize(300,150);
	setVisible(true);
    }


    public void runClient()
    {
	try {
	    connectToServer();
	    getStreams();
	    processConnection();
	}catch(EOFException eofException){
	    displayMessage("\nClient terminated connection");
	}catch(IOException e){
	    e.printStackTrace();
	}
	finally {
	    closeConnection();
	}
    }   

    private void connectToServer() throws IOException
    {
	displayMessage("\nTrying to connect.");
	sock = new Socket(InetAddress.getByName(chatServer),8080);
	displayMessage("\nConnected to " + sock.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException
    {

	output = new ObjectOutputStream(sock.getOutputStream());
	output.flush();

	input = new ObjectInputStream(sock.getInputStream());
	displayMessage("\nhave IO streams");
    }

    private void processConnection() throws IOException
    {
	setTextFieldEditable(true);
	do {
	    try{
		message = (String) input.readObject();
		displayMessage("\n" + message);
	    }catch (ClassNotFoundException c){
		displayMessage("\nUnknown object rec'd");
	    }
	}while(!message.equals("SERVER>>>QUIT"));

    }

    private void closeConnection(){
	displayMessage("\nclosing conn");
	setTextFieldEditable(false);

	try {
	    output.close();
	    input.close();
	    sock.close();
	} catch(IOException e) {
	    e.printStackTrace();
	}
    }

    private void sendData(String message)
    {
	try{
	    output.writeObject(message);
	    output.flush();
	}catch(IOException e){
	    displayArea.append("\nerror writing obj");
	}
    }

    private void displayMessage(final String messageToDisplay)
    {
	SwingUtilities.invokeLater(new Runnable() {
		public void run()
		{
		    displayArea.append(messageToDisplay);
		}	
	    });

    }

    private void setTextFieldEditable(final boolean editable)
    {

	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    enterField.setEditable(editable);
		}
	    });
    }
}