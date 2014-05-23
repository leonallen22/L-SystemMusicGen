/**
 * Generate a production with a context-free L-system,
 * convert the production into a score properly formatted for JFugue,
 * then play the generated score using JFugue.
 * @author		Harry Allen
 */

import org.jfugue.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;

public class MidiFile
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		scan.useDelimiter(System.getProperty("line.separator"));
		Player player = new Player();								//Create a player object to play a Pattern
		LSystem lsys = new LSystem();								//L-System for music generation
		ScoreGenerator scoreGen = new ScoreGenerator();				//Generates a JFugue score from an L-System production
		String alpha = "";
		int opt = 0;
		boolean exit = false;
		
		while(!exit)
		{
			StringBuffer bufferA = new StringBuffer();
			StringBuffer bufferR = new StringBuffer();
			ArrayList<String> lsysalpha = lsys.getAlphabet();
			ArrayList<String> lsysrules = lsys.getRules();
			scoreGen.resetTurtle();
			String option = "";
			opt = 0;
			
			for(String c : lsys.getAlphabet())
				bufferA.append(String.valueOf(c) + " ");
			
			for(int i=0 ; i < lsysrules.size() ; ++i)
				bufferR.append("\t\t" + lsysalpha.get(i) + ": " + lsysrules.get(i) + "\r\n");
			
			System.out.println("L-System:\r\n\tAlphabet: " + bufferA.toString() + "\r\n\tAxiom: " + lsys.getAxiom() + "\r\n\tRules:\r\n" + bufferR.toString() + "\r\n\r\nTempo: " + scoreGen.getTempo() + "\r\nKey Signature: " + scoreGen.getKey());
			System.out.println("\r\n***Each symbol should be separated with a space***\r\n1: Generate music\r\n2: Build new L-System\r\n3: Change alphabet\r\n4: Change axiom\r\n5: Change rules\r\n6: Change key\r\n7: Change tempo\r\n8: Exit");
			
			while(opt < 1 || opt > 8)
			{
				try
				{
					option = scan.next();
					opt = Integer.valueOf(option);
				}
				catch(NumberFormatException e)
				{
					opt = 0;
				}
			}
			
			switch(opt)
			{
				//Generate music using the current L-System
				case 1:
					if(lsys.getAlphabet().size() != lsys.getRules().size())
						System.out.println("Make sure each symbol in the alphabet has a corresponding rule.");
					
					else
					{
						System.out.println("1: Quick run\r\n2: Iterate step-by-step\r\n");
						String choice = "";
						
						while(!choice.equals("1") && !choice.equals("2"))
							choice = scan.next();
						
						//Iterate a set number of times
						if(choice.equals("1"))
						{
							System.out.println("Iterations: ");
							boolean replay = true;
							int iter = 0;
							
							while(iter <1)
							{
								try
								{
									String iterations = scan.next();
									iter = Integer.valueOf(iterations);
								}
								catch(NumberFormatException e)
								{
									iter = 0;
								}
							}
							
							lsys.iterate(iter);									//Expand the system the given number of times
							String production = lsys.getTree();					//Retrieve the production
							System.out.println(production);						//Print the raw production on-screen
							
							Pattern pattern = scoreGen.genScore(production);	//Convert the production into a suitable format; store in a Pattern
							System.out.println(pattern.toString());
							//Pattern pattern = new Pattern("I80 X[MOD_WHEEL_COARSE]=0 [83]iiiii [72]iiiii [76]iiii [68]iiii [89]iiiii [59]iiii [60]iiiii X[MOD_WHEEL_COARSE]=127 [80]iiii [68]iiii [95]iiiii [83]iiii [72]iii [75]iiii [80]iii [75]iii [86]iiiii");
							player.play(pattern);
							
							while(replay)
							{
								System.out.print("1 to replay, 2 to save as MIDI file, and anything else return to menu: ");
								
								String re = scan.next();
								
								if(re.equals("1"))
									player.play(pattern);
								
								else if(re.equals("2"))
								{
									System.out.print("\r\nEnter name for MIDI file: ");
									String file = scan.next();
									
									try
									{
										player.saveMidi(pattern, new File(file + ".mid"));
										System.out.println("File saved successfully.\r\n");
									}
									catch(IOException e)
									{
										System.out.println("Error saving Midi file: returning to menu.");
									}
								}
								
								else
									break;
							}
						}
						
						//Iterate step-by-step
						else if(choice.equals("2"))
						{
							String production = "";
							Pattern pattern = new Pattern();
							String input = "";
							int iterations = 0;
							
							while(true)
							{
								scoreGen.resetTurtle();
								System.out.println("Enter 1 to iterate, 2 to play again, 3 to save as a MIDI file and anything else to quit.");
								input = scan.next();
								
								//Continue iterating
								if(input.equals("1"))
								{
									lsys.iterate(++iterations);
									production = lsys.getTree();
									System.out.println("\r\nIteration: " + iterations);
									System.out.println(production);
									
									pattern = scoreGen.genScore(production);
									System.out.println(pattern.toString());
									player.play(pattern);
								}
								
								//Replay last iteration
								else if(input.equals("2"))
								{
									pattern = scoreGen.genScore(production);
									player.play(pattern);
								}
								
								//Save last iteration as MIDI file
								else if(input.equals("3"))
								{
									System.out.print("\r\nEnter name for MIDI file: ");
									String file = scan.next();
									
									try
									{
										player.saveMidi(pattern, new File(file + ".mid"));
										System.out.println("File saved successfully.\r\n");
									}
									catch(IOException e)
									{
										System.out.println("Error saving Midi file.");
									}
								}
								
								else
									break;
							}
						}
					}
					break;
				
				//Define a new L-System
				case 2:
					String input = "";
					System.out.println("1: Use a predefined L-System\r\n2: Build your own L-System\r\n");
					
					while(!input.equals("1") && !input.equals("2"))
						input = scan.next();
					
					if(input.equals("1"))
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
						String d = "";
						int def = 0;
						
						while(def < 1 || def > lsys.getDefaultAlphabets().size())
						{
							d = scan.next();
							
							try
							{
								def = Integer.valueOf(d);
							}
							catch(NumberFormatException e)
							{
								def = 0;
							}
						}
						
						lsys.setAlphabetDef(def-1);
						lsys.setAxiomDef(def-1);
						lsys.setRulesDef(def-1);
					}
					
					else if(input.equals("2"))
					{
						System.out.println("Define alphabet one symbol at a time, enter \"!\" to stop: ");
						ArrayList<String> newalphabet = new ArrayList<String>();
						alpha = "";
						
						while(true)
						{
							alpha = scan.next();
							
							if(!alpha.equals("!"))
								newalphabet.add(alpha);
							else
								break;
						}
						
						lsys.setAlphabet(newalphabet);
						
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
				
				//Define a new alphabet only
				case 3:
					System.out.println("Define alphabet one symbol at a time, enter \"!\" to stop: ");
					ArrayList<String> newalphabet = new ArrayList<String>();
					
					while(true)
					{
						alpha = scan.next();
						
						if(!alpha.equals("!"))
							newalphabet.add(alpha);
						else
							break;
					}
					
					lsys.setAlphabet(newalphabet);
					break;
				
				//Define a new axiom only
				case 4:
					System.out.println("Define axiom: ");
					String newaxiom = "!";
					
					while(newaxiom.equals("!"))
						newaxiom = scan.next();
					
					lsys.setAxiom(newaxiom);
					break;
				
				//Define new rules only
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
				
				//Change key signature
				case 6:
					System.out.println("1:  C\r\n2:  G\r\n3:  D\r\n4:  A\r\n5:  E\r\n6:  B\r\n7:  Gb/F#\r\n8:  Db\r\n9:  Ab\r\n10: Eb\r\n11: Bb\r\n12: F");
					String newkey = "";
					int key = 0;
					
					while(key < 1 || key > 12)
					{
						newkey = scan.next();
						
						try
						{
							key = Integer.valueOf(newkey);
						}
						catch(NumberFormatException e)
						{
							key = 0;
						}
					}
					
					scoreGen.setKey(key);
					break;
					
				case 7:
					System.out.print("Tempo (BPM 1 - 220): ");
					int t = 0;
					
					while(t < 1 || t > 220)
					{
						try
						{
							String newtempo = scan.next();
							t = Integer.valueOf(newtempo);
						}
						catch(NumberFormatException e)
						{
							t = 0;
						}
					}
					scoreGen.setTempo(t);
					break;
					
				//Exit program
				case 8:
					exit = true;
					break;
			}
		}
		
		scan.close();
		
		return;
	}
}