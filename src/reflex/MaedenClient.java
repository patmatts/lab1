package reflex;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MaedenClient {
	
	private final static int MAEDENPORT = 7237;
	private final static String HOST = "localhost";
	
	private static DataOutputStream dOut = null;
	private static DataInputStream dIn = null;
	private static BufferedReader in = null;
    private static int[] agentNums = new int[10];
    
    private static Socket gridSocket;
	
	public static void main(String[] args)
	{
		
		
		
		try
		{
			if(socketConnect(HOST, MAEDENPORT, agentNums) != 0)
			{
				System.out.println("Unable to acquire agent number from host");
				return;
			}
			
			ReflexAgent refAgent = new ReflexAgent(in, dOut, agentNums[0]);
			refAgent.reflexMain();
			
			dIn.close();
			dOut.close();
			gridSocket.close();
		}
		catch(UnknownHostException e) {
			System.out.println("Unknown Host Exception.");
			return;
		} 
		catch(IOException e) {
			System.out.println("IOException ");
			return;
		}
		
	}
	
	public static int socketConnect(String host, int port, int agentNums[]) throws IOException, UnknownHostException
	{
		System.out.print("Connecting to: " + host + " Port number: " + port + "\n");
	    gridSocket = new Socket(host, port);
	    System.out.println("Socket creation successful");
	    
	    dOut = new DataOutputStream(gridSocket.getOutputStream());
	    
	    dIn = new DataInputStream(gridSocket.getInputStream());
	    
	    in = new BufferedReader(new InputStreamReader(dIn));
		
	    dOut.writeBytes("base\n");
	    dOut.flush();
	    
	    agentNums[0] = Integer.parseInt(in.readLine());
	    
	    System.out.println("Agent: " + agentNums[0]);
	    
		return 0;
	}

}
