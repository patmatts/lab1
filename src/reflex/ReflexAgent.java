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
	
	private char smell;
	private char[] inventory;
	private String[][] sight;
	private char[] ground;
	private String[] messages;
	private int energy;
	private String status;
	private int time;
	
	
	public ReflexAgent()
	{
		inventory = new char[10];
		ground = new char[10];
		messages = new String[10];
	}
	
	public ReflexAgent(BufferedReader in, DataOutputStream dOut, int agentNum)
	{
		this();
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
		    
			   /*for(int i = 0; i < 9; i++)
			   {
				   
				   line = in.readLine();
				   
			    	System.out.println("Line " + i + ":" + line);
			    	if(i == 1)
			    	{
			    		direction = line.charAt(0);
			    	}
			    	
			    	if(i == 2)
			    		if(line.equals("(\"+\")"))
			    		{
			    			use();
			    		}	
			    	
			    	if(i == 7)
			    	{
			    		success = line;
			    	}
			   }*/
			   
			   readSensors();
			   printSensors();
				
			   //System.out.println("F: " + sight[2][2] + "L: " + sight[1][1] + " R: " + sight[1][3]);
			   
			   if(haveCheese())
				  use();

			   
			   else if(sight[2][2].equals("*"))
				  if(1 + (int)(Math.random() * 2) == 1)
					  turnRight();
				  else 
					  turnLeft();
			   
			   else if(sight[1][1].equals("*") && smell == 'l')
				   forward();  
			   
			   else if(sight[1][3].equals("*") && smell == 'r')
				   forward();
			   
			   else if(smell == 'b' && sight[0][2].equals("*") && sight[1][1].equals("*") && sight[1][3].equals(""))
				   turnRight();
			   
			   else if(smell == 'b' && sight[0][2].equals("*") && sight[1][1].equals("") && sight[1][3].equals("*"))
				   turnLeft();
			   
			   /*if(!status.equals("ok"))
			   {
				   back();
			   }*/
			   
			   else if(smell == 'r')
			   {
				   turnRight();
			   }
			   
			   else if(smell == 'b')
			   {
				   turnLeft();
			   }
			   
			   else if(smell == 'l')
			   {
				   turnLeft();
			   }
			   
			   else if(smell == 'f')
			   {
				   forward();
			   }
			   
			   else if(smell == 'h')
			   {
				   grab();
			   }
			   
		   } while(true);
	}
	
	//reads in sensory data for the agent from the host
	private int readSensors() throws IOException
	{
		String line;
		String[] tokens = new String[10];
		
		//read in Maeden Directive and return error if it is not 8(0)
		line = in.readLine();
		if(!line.equals("8"))
			return 2;
		
		//read in smell direction to line(1)
		line = in.readLine();
		smell = line.charAt(0);
		
		//read in the inventory for the agent to line and parse data(2)
		line = in.readLine();
		
		tokens = parseRegular(line);
		for(int i = 0; i < 10; i++)
			inventory[i] = tokens[i].charAt(0);
		
		//read in sight information and call parseSight to parse the information(3)
		line  = in.readLine();
		
		sight = parseSight(line);
		
		//read in items on the "ground" and store into the class array(4)
		line = in.readLine();
		
		tokens = parseRegular(line);
		for(int i = 0; i < 10; i++)
			ground[i] = tokens[i].charAt(0);
		
		//read in messages from other agents and store into the class array(5)
		line = in.readLine();
		messages = parseRegular(line);
		
		//read in energy and store into class variable(6)
		line = in.readLine();
		energy = Integer.parseInt(line);
		
		//read in status of the agent(7)
		line = in.readLine();
		status = line;
		
		//read in world time of the environment(8)
		line = in.readLine();
		time = Integer.parseInt(line);
		
		return 0;
	}
	
	//print all sensor variables of agent except for sight
	private void printSensors()
	{
		System.out.println("\n**NEW SET OF SENSES**\n");
		System.out.println("Smell: " + smell);
		
		System.out.println("Inventory:");
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == 'e')
				break;
			System.out.print(" \"" + inventory[i] + "\"");
		}
		System.out.println();
		
		System.out.println("Ground:");
		for(int i = 0; i < ground.length; i++)
		{
			if(ground[i] == 'e')
				break;
			System.out.print(" \"" + ground[i] + "\"");
		}
		System.out.println();
		
		/*for(int i = 0; i < messages.length; i++)
		{
			if(messages[i].charAt(0) == 'e')
				break;
			System.out.print(" \"" + messages[i] + "\"");
		}*/
		//System.out.println();
		
		System.out.println("Status: " + status);
		
		System.out.println("Energy: " + energy);
		
		System.out.println("World Time: " + time);
	}
	
	//parsing functions to return items from all lists that the hosts sends except for sight
	private String[] parseRegular(String line)
	{
		
		String[] tokens = new String[10];
		line = line.replaceAll("\\s+\"", "");
		line = line.replaceAll("\\(\\)", "");
		
		//fills up rest of array with e's(empty) entries
		for(int i = 0; i < 10; i++)
			tokens[i] = "e";
				
		for(int i = 0; i < line.length(); i++)
			tokens[i] = Character.toString(line.charAt(i));
		
		return tokens;
	}
	
	private String[][] parseSight(String line)
	{
		line = line.replaceAll("\\s+","");
		String[][] parsed = new String[7][5];
		
		
		int count = 0;
		String space;
		
		if(line.charAt(count) != '(')
			return null;
		count++;
		
		for(int row = 0; row < 7; row++)
		{
			if(line.charAt(count) != '(')
				return null;
			count++;
			
			for(int col = 0; col < 5; col++)
			{
				space = "";
				
				if(line.charAt(count) != '(')
					return null;
				count++;
				
				while(line.charAt(count) != ')')
				{
					space = space + line.charAt(count);
					count++;
				}
				count++;
				
				space = space.replaceAll("\"", "");
				parsed[row][col] = space;
				 
			}
			
			
			if(line.charAt(count) != ')')
				return null;
			count++;
		}
		
		return parsed;
		
	}
	
	private boolean haveCheese()
	{
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == '+')
				return true;
		}
		
		return false;
	}
	
	private void forward() throws IOException
	{
		dOut.writeBytes("f\n");
		dOut.flush(); 
	}
	
	private void back() throws IOException
	{
		dOut.writeBytes("b\n");
		dOut.flush(); 
	}
	
	private void turnRight() throws IOException
	{
		dOut.writeBytes("r\n");
		dOut.flush(); 
	}
	
	private void turnLeft() throws IOException
	{
		dOut.writeBytes("l\n");
		dOut.flush(); 
	}
	
	private void grab() throws IOException
	{
		dOut.writeBytes("g\n");
		dOut.flush();
	}
	
	private void use() throws IOException
	{
		dOut.writeBytes("u\n");
		dOut.flush();
	}
	
	private void finishLevel() throws IOException
	{
		dOut.writeBytes("g\n");
		dOut.flush();   
		dOut.writeBytes("u\n");
		dOut.flush(); 
	}
	
}