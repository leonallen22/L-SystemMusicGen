/**
 * Generate a production with a context-free L-system, 
 * convert the production into a score properly formatted for JFugue,
 * then play the generated score using JFugue.
 * @author		Harry Allen
 */

import org.jfugue.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MidiFile
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		scan.useDelimiter(System.getProperty("line.separator"));
		Player player = new Player();					//Create a player object to play a Pattern
		LSystem lsys = new LSystem();					//L-System for music generation
		ScoreGenerator scoreGen = new ScoreGenerator();	//Generates a JFugue score from an L-System production
		String alpha = "";
		int opt = 0;
		boolean exit = false;
		
		while(opt <= 1 && opt >= 7 || !exit)
		{
			StringBuffer bufferA = new StringBuffer();
			StringBuffer bufferR = new StringBuffer();
			ArrayList<String> lsysalpha = lsys.getAlphabet();
			ArrayList<String> lsysrules = lsys.getRules();
			
			for(String c : lsys.getAlphabet())
				bufferA.append(String.valueOf(c) + " ");
			
			for(int i=0 ; i < lsysrules.size() ; ++i)
				bufferR.append("\t\t" + lsysalpha.get(i) + ": " + lsysrules.get(i) + "\r\n");
			
			System.out.println("L-System:\r\n\tAlphabet: " + bufferA.toString() + "\r\n\tAxiom: " + lsys.getAxiom() + "\r\n\tRules:\r\n" + bufferR.toString() + "\r\n\r\nKey Signature: " + scoreGen.getKey());
			System.out.println("\r\n***Each symbol should be separated with a space***\r\n1: Generate music\r\n2: Build new L-System\r\n3: Change alphabet\r\n4: Change axiom\r\n5: Change rules\r\n6: Change key\r\n7: Exit");
			opt = scan.nextInt();
			
			switch(opt)
			{
				case 1:
					System.out.println("1: Quick run\r\n2: Iterate step-by-step\r\n");
					int choice = scan.nextInt();
					
					if(choice == 1)
					{
						System.out.println("Iterations: ");
						int iter = scan.nextInt();
						lsys.iterate(iter);									//Expand the system the given number of times
						String production = lsys.getTree();					//Retrieve the production
						
						System.out.println(production);						//Print the raw production on-screen
						Pattern pattern = scoreGen.genScore(production);	//Convert the production into a suitable format; store in a Pattern
						player.play(pattern);
					}
					
					else if(choice == 2)
					{
						int iterations = 0;
						
						while(true)
						{
							
							System.out.println("Enter 1 to iterate; anything else to quit.");
							String input = scan.next();
							
							if(input.equals("1"))
							{
								lsys.iterate(++iterations);
								String production = lsys.getTree();
								System.out.println("\r\nIteration: " + iterations);
								
								System.out.println(production);
								Pattern pattern = scoreGen.genScore(production);
								player.play(pattern);
							}
							
							else
								break;
						}
					}
					break;
				
				case 2:
					int input = 0;
					System.out.println("1: Use a predefined L-System\r\n2: Build your own L-System\r\n");
					
					while(input != 1 && input != 2)
						input = scan.nextInt();
					
					if(input == 1)
					{
						ArrayList<String[]> defaultalpha = lsys.getDefaultAlphabets();
						ArrayList<String> defaultaxiom = lsys.getDefaultAxioms();
						ArrayList<String[]> defaultrule = lsys.getDefaultRules();
						
						for(int i=0 ; i < defaultalpha.size() ; ++i)
						{
							String[] a = defaultalpha.get(i);
							String[] r = defaultrule.get(i);
							String ax = defaultaxiom.get(i);
							
							System.out.print("Default " + (i+1) + "\r\n\tAlphabet: ");
							
							for(String x : a)
								System.out.print(x + " ");
							
							System.out.println("\r\n\tAxiom: " + ax + "\r\n\tRules:");
							
							for(int j=0 ; j < a.length ; ++j)
								System.out.println("\t\t" + a[j] + ": " + r[j]);
							
						}
						
						System.out.println("Choose a default L-System: ");
						int def = 0;
						
						while(def < 1 || def > lsys.getDefaultAlphabets().size())
							def = scan.nextInt();
						
						lsys.setAlphabetDef(def-1);
						lsys.setAxiomDef(def-1);
						lsys.setRulesDef(def-1);
					}
					
					else
					{
						alpha = "";
						System.out.println("Define alphabet one symbol at a time, enter \"!\" to stop: ");
						
						while(!alpha.equals("!"));
							alpha = scan.next();
						
						System.out.println("Define axiom: ");
						String newaxiom = "!";
						
						while(newaxiom.equals("!"))
							newaxiom = scan.next();
						
						lsys.setAxiom(newaxiom);
						
						System.out.println("Define new rules (each char separated with a space): \r\n");
						ArrayList<String> newrules = new ArrayList<String>();
						ArrayList<String> alpha1 = lsys.getAlphabet();
						for(int i=0 ; i < alpha1.size() ; ++i)
						{
							System.out.println(alpha1.get(i) + ": ");
							newrules.add(scan.next());
						}
						lsys.setRules(newrules);
					}
					break;
				
				case 3:
					System.out.println("Define alphabet one symbol at a time, enter \"!\" to stop: ");
					ArrayList<String> newalphabet = new ArrayList<String>();
					
					while(!alpha.equals("!"));
					{
						alpha = scan.next();
						newalphabet.add(alpha);
					}
					
					ArrayList<String> alphabet = new ArrayList<String>();
					int j = 0;
					
					for(int i=0 ; i < newalphabet.size() ; ++i)
					{
						String symbol = newalphabet.get(i);
						
						if(!symbol.equals("!"))
						{
							alphabet.set(j, symbol);
							++j;
						}
						
						else
							break;
					}
					
					lsys.setAlphabet(alphabet);
					break;
				
				case 4:
					System.out.println("Define axiom: ");
					String newaxiom = "!";
					
					while(newaxiom.equals("!"))
						newaxiom = scan.next();
					
					lsys.setAxiom(newaxiom);
					break;
				
				case 5:
					System.out.println("Define new rules (each char separated with a space): \r\n");
					ArrayList<String> newrules = new ArrayList<String>();
					ArrayList<String> alpha1 = lsys.getAlphabet();
					for(int i=0 ; i < alpha1.size() ; ++i)
					{
						System.out.println(alpha1.get(i) + ": ");
						newrules.add(scan.next());
					}
					
					lsys.setRules(newrules);
					break;
				
				case 6:
					System.out.println("1:  C\r\n2:  G\r\n3:  D\r\n4:  A\r\n5:  E\r\n6:  B\r\n7:  Gb/F#\r\n8:  Db\r\n9:  Ab\r\n10: Eb\r\n11: Bb\r\n12: F");
					int newkey = 0;
					
					while(!(newkey >= 1 && newkey <= 12))
						newkey = scan.nextInt();
					
					scoreGen.setKey(newkey);
					break;
					
				case 7:
					exit = true;
					break;
			}
		}
		
		scan.close();
		
		return;
	}
}