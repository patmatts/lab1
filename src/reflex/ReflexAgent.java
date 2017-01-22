// CS 455: AI, Group 8: Patrick Matts, Mark Huff, Brandon Von Vilet
//ReflexAgent.java contains the reflexagent and the protocols to connect to the Maeden Simulator Environment

//note to other members: will try to clean up program, eventually I want ReflexAgent to just deal with 
//controlling the agent and will set up a class called MaedenClient.java to handle initial connection
//and contain the main function, many things will be static for now

//test changes

package reflex;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReflexAgent {
	
	private DataOutputStream dOut;
	private BufferedReader in;
	
	private int agentNum;
	
	public ReflexAgent()
	{
		
	}
	
	public ReflexAgent(BufferedReader in, DataOutputStream dOut, int agentNum)
	{
		this.in = in;
		this.dOut = dOut;
		
		this.agentNum = agentNum;
	}
	
	public void reflexMain() throws IOException, UnknownHostException
	{
		
		char direction = 'f';
		String success = "ok";
		String line;
		
		do
		   {
		    
			   for(int i = 0; i < 9; i++)
			   {
				   
				   line = in.readLine();
				   
			    	System.out.println("Line " + i + ":" + line);
			    	if(i == 1)
			    	{
			    		direction = line.charAt(0);
			    	}
			    	
			    	if(i == 7)
			    	{
			    		success = line;
			    	}
			   }
			   
			   if(success.equals("ok\n"))
			   {
				   dOut.writeBytes("b\n");
				   dOut.flush();
			   }
			   
			   else if(direction == 'r')
			   {
				   dOut.writeBytes("r\n");
				   dOut.flush();
			   }
			   
			   else if(direction == 'b')
			   {
				   dOut.writeBytes("r\n");
				   dOut.flush();
			   }
			   
			   else if(direction == 'l')
			   {
				   dOut.writeBytes("l\n");
				   dOut.flush();
			   }
			   
			   else if(direction == 'f')
			   {
				   dOut.writeBytes("f\n");
				   dOut.flush(); 
			   }
			   
			   else if(direction == 'h')
			   {
				   dOut.writeBytes("g\n");
				   dOut.flush();   
				   dOut.writeBytes("u\n");
				   dOut.flush(); 
				   
				   break;
			   }
			   
		   } while(true);
	}
	
}