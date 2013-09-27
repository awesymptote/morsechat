import javax.swing.JFrame;

public class MorseClientTest 
{
    public static void main(String[] args)
    {
	MorseClient client;

	client = new MorseClient("127.0.0.1");

	client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	client.runClient();
    }

}