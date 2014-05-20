/**
 * Generate a properly formatted score for use in JFugue from an L-system production.
 * @author		Harry Allen
 */

import org.jfugue.Pattern;

/**
 * To do:
 * Allow symbols of arbitrary length to be used in alphabet
 * Implement all key signatures
 * implement tempo
 */

public class ScoreGenerator
{
	private Turtle turtle;
	private int keySig;
	private int tempo;
	private String[] keySigs = {"C", "G", "D", "A", "E", "B", "Gb/F#", "Db", "Ab", "Eb", "Bb", "F"};
	
	/** Default Constructor */
	public ScoreGenerator()
	{
		turtle = new Turtle();
		keySig = 1;
		tempo = 120;
	}
	
	/** Constructor. Accepts an integer for angle */
	public ScoreGenerator(int angle)
	{
		turtle = new Turtle();
		turtle.pushAngle(angle);
		keySig = 1;
		tempo = 120;
	}
	
	/** Constructor. Accepts 3 integers for angle, keySig, and tempo, respectively */
	public ScoreGenerator(int angle, int key, int tempo)
	{
		turtle = new Turtle();
		turtle.pushAngle(angle);
		
		if(key >= 1 && key <= 15)
			this.keySig = key;
		else
			this.keySig = 1;
		
		this.tempo = tempo;
	}
	
	/** Returns value of keySig */
	public String getKey()
	{
		return this.keySigs[this.keySig-1];
	}
	
	/** Returns value of tempo */
	public int getTempo()
	{
		return this.tempo;
	}
	
	/** Sets the value of keySig to the integer accepted */
	public void setKey(int key)
	{
		if(key >= 1 && key <=12)
			this.keySig = key;
		else
			this.keySig = 1;
	}
	
	/** Sets the value of tempo to the integer accepted */
	public void setTempo(int tempo)
	{
		this.tempo = tempo;
	}
	
	/** Accepts an integer representing a note value, and increments it a half step */
	public int upHalfStep(int note)
	{
		return note+1;
	}
	
	/** Accepts an integer representing a note value, and decrements it a half step */
	public int downHalfStep(int note)
	{
		return note-1;
	}
	
	/** Accepts an integer representing a note value, and increments it a whole step */
	public int upWholeStep(int note)
	{
		return note+2;
	}
	
	/** Accepts an integer representing a note value, and decrements it a whole step */
	public int downWholeStep(int note)
	{
		return note-2;
	}
	
	/** Accepts an integer representing a note value, and increments it an octave */
	public int upOctave(int note)
	{
		return note+12;
	}
	
	/** Accepts an integer representing a note value, and decrements it an octave */
	public int downOctave(int note)
	{
		return note-12;
	}
	
	/** Accepts a string and parses through it to generate a pattern properly formatted for JFugue */
	public Pattern genScore(String production)
	{
		String pat = "";
		
		switch(this.keySig)
		{
			case 1:
				pat = generate(production, 48);
				break;
			
			case 2:
				pat = generate(production, 55);
				break;
			
			case 3:
				pat = generate(production, 50);
				break;
			
			case 4:
				pat = generate(production, 57);
				break;
			
			case 5:
				pat = generate(production, 52);
				break;
			
			case 6:
				pat = generate(production, 59);
				break;
			
			case 7:
				pat = generate(production, 54);
				break;
			
			case 8:
				pat = generate(production, 49);
				break;
			
			case 9:
				pat = generate(production, 56);
				break;
			
			case 10:
				pat = generate(production, 51);
				break;
			
			case 11:
				pat = generate(production, 58);
				break;
			
			case 12:
				pat = generate(production, 53);
				break;
		}
		
		Pattern pattern = new Pattern(pat);
		
		return pattern;
	}
	
	/** Accepts a production string and 4 integers which indicate where half steps should be made to keep music in key; generates music from the production */
	public String generate(String production, int tonic)
	{
		StringBuffer buffer = new StringBuffer();	//Stores the score string that will be returned
		int degree = 1;								//Represents the degree of the current pitch in the given key signature
		char[] prod = production.toCharArray();		//Array of characters from the production
		String str = "";
		turtle.popY();
		turtle.pushY(tonic);							//Represents the pitch of the note and the relative height of the turtle
		
		//Step through each symbol in production
		for(int i=0 ; i < prod.length ; ++i)
		{
			if(prod[i] == '-')
				turtle.pushYaw(turtle.getYaw() + turtle.getAngle());
			
			else if(prod[i] == '+')
				turtle.pushYaw(turtle.getYaw() - turtle.getAngle());
			
			//Turtle draws a line
			else if(prod[i] == 'g')
			{
				int pitch = turtle.getY();
				str = buffer.toString();
				
				//If turtle is horizontal, record line as a note
				if(turtle.getDirection() == 1 || turtle.getDirection() == 3)
				{
					if(str.endsWith("[" + pitch + "]i") || str.endsWith("[" + pitch + "]ii") || str.endsWith("[" + pitch + "]iii") || str.endsWith("[" + pitch + "]iiii"))
						buffer.append("i");
					
					else
						buffer.append(" [" + pitch + "]i");
				}
				
				//If turtle facing upward, record line as a change up in pitch
				else if(turtle.getDirection() == 2)
				{
					if(degree == 3 || degree == 7)
					{
						turtle.popY();
						turtle.pushY(upHalfStep(pitch));
					}
					
					else
					{
						turtle.popY();
						turtle.pushY(upWholeStep(pitch));
					}
					
					++degree;
					
					if(turtle.getY() > 127)
					{
						pitch = turtle.popY();
						turtle.pushY(pitch - 72);
					}
					
					if(degree == 8)
						degree = 1;
				}
				
				//If turtle facing downward, record line as a change down in pitch
				else if(turtle.getDirection() == 4)
				{
					if(degree == 1 || degree == 4)
					{
						turtle.popY();
						turtle.pushY(downHalfStep(pitch));
					}
					
					else
					{
						turtle.popY();
						turtle.pushY(downWholeStep(pitch));
					}
					
					--degree;
					
					if(turtle.getY() < 0)
					{
						pitch = turtle.popY();
						turtle.pushY(pitch + 60);
					}
					
					if(degree == 0)
						degree = 7;
				}
			}
		}
		
		return buffer.toString();
	}
}
