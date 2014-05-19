/**
 * Generate a properly formatter score for use in JFugue from an L-system production.
 * @author		Harry Allen
 */

import org.jfugue.Pattern;

/**
 * To do:
 * Use String Buffer for score building 
 * Allow symbols of arbitrary length to be used in alphabet
 * Implement all key signatures
 * Allow notes of arbitrary length to be played
 * implement tempo
 */

public class ScoreGenerator
{
	private int angle;
	private int keySig;
	private int tempo;
	private String[] keySigs = {"C", "G", "D", "A", "E", "B", "Gb/F#", "Db", "Ab", "Eb", "Bb", "F"};
	
	/** Default Constructor */
	public ScoreGenerator()
	{
		angle = 90;
		keySig = 1;
		tempo = 120;
	}
	
	/** Constructor. Accepts an integer for angle */
	public ScoreGenerator(int angle)
	{
		this.angle = angle;
		keySig = 1;
		tempo = 120;
	}
	
	/** Constructor. Accepts 3 integers for angle, keySig, and tempo, respectively */
	public ScoreGenerator(int angle, int key, int tempo)
	{
		this.angle = angle;
		
		if(key >= 1 && key <= 15)
			this.keySig = key;
		else
			this.keySig = 1;
		
		this.tempo = tempo;
	}
	
	/** Returns value of angle */
	public int getAngle()
	{
		return this.angle;
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
	
	/** Sets the value of angle to the integer accepted */
	public void setAngle(int angle)
	{
		this.angle = angle;
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
		int pitch = tonic;							//Represents the pitch of the note and the relative height of the turtle
		int yaw = 0;								//Represents the angle the turtle is facing
		char[] prod = production.toCharArray();		//Array of characters from the production
		String str = "";
		
		//Step through each symbol in production
		for(int i=0 ; i < prod.length ; ++i)
		{
			if(prod[i] == '-')
				yaw += angle;
			
			else if(prod[i] == '+')
				yaw -= angle;
			
			//Turtle draws a line
			else if(prod[i] == 'g')
			{
				str = buffer.toString();
				
				//If turtle is horizontal, record line as a note
				if(Math.abs(yaw % 360) == 0 || Math.abs(yaw % 360) == 180)
				{
					if(str.endsWith("[" + pitch + "]i") || str.endsWith("[" + pitch + "]ii") || str.endsWith("[" + pitch + "]iii") || str.endsWith("[" + pitch + "]iiii"))
						buffer.append("i");
					
					else
						buffer.append(" [" + pitch + "]i");
				}
				
				//If turtle facing upward, record line as a change up in pitch
				else if(yaw % 360 == 90 || yaw % 360 == -270)
				{
					if(degree == 3 || degree == 7)
						pitch = upHalfStep(pitch);
					
					else
						pitch =  upWholeStep(pitch);
					
					++degree;
					
					if(pitch > 127)
						pitch = pitch - 72;
					
					if(degree == 8)
						degree = 1;
				}
				
				//If turtle facing downward, record line as a change down in pitch
				else if(yaw % 360 == 270 || yaw % 360 == -90)
				{
					if(degree == 1 || degree == 4)
						pitch = downHalfStep(pitch);
					
					else
						pitch = downWholeStep(pitch);
					
					--degree;
					
					if(pitch < 0)
						pitch = pitch + 60;
					
					if(degree == 0)
						degree = 7;
				}
			}
		}
		
		return buffer.toString();
	}
}
