import org.jfugue.Pattern;
import java.util.ArrayList;

/**
 * Generates a properly formatted score for use in JFugue from an L-system production.
 * @author		Harry Allen
 */
public class ScoreGenerator
{
	private Turtle turtle;																						//Turtle to keep track of "drawing" actions
	private MusicAnalyzer analyzer;																				//Analyzes MIDI files and generates a first-order Markov chain for all notes on the Western Scale
	private int keySig;																							//Stores current key signature
	private int tempo;																							//Stores tempo for music to be played
	private int degree;																							//Represents the degree of the current pitch in the given key signature
	private int prevDegree;
	private int upperNoteB;
	private int lowerNoteB;
	private String[] keySigs = {"C", "G", "D", "A", "E", "B", "Gb/F#", "Db", "Ab", "Eb", "Bb", "F"};			//Stores key signatures
	private String[] notes = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};	//Stores all possible notes

	/**
	 * Default Constructor.
	 */
	public ScoreGenerator()
	{
		analyzer = new MusicAnalyzer();
		turtle = new Turtle();
		keySig = 1;
		tempo = 120;
		degree = 1;
		prevDegree = -1;
		upperNoteB = 95;
		lowerNoteB = 36;
	}

	/**
	 * Constructor.
	 * @param angle the angle at which the turtle should start
	 */
	public ScoreGenerator(int angle)
	{
		analyzer = new MusicAnalyzer();
		turtle = new Turtle();
		turtle.pushAngle(angle);
		keySig = 1;
		tempo = 120;
		degree = 1;
		prevDegree = -1;
		upperNoteB = 95;
		lowerNoteB = 36;
	}

	/**
	 * Constructor.
	 * @param angle the angle at which the turtle should start
	 * @param key the initial key signature
	 * @param tempo the initial tempo
	 */
	public ScoreGenerator(int angle, int key, int tempo)
	{
		analyzer = new MusicAnalyzer();
		turtle = new Turtle();
		turtle.pushAngle(angle);

		if(key >= 1 && key <= 15)
			this.keySig = key;
		else
			this.keySig = 1;

		this.tempo = tempo;

		degree = 1;
		upperNoteB = 95;
		lowerNoteB = 36;
	}

	/**
	 * @return Value of the key signature
	 */
	public String getKey()
	{
		return this.keySigs[this.keySig-1];
	}

	/**
	 * @return Returns value of tempo
	 */
	public int getTempo()
	{
		return this.tempo;
	}

	/**
	 * Sets the value of keySig and the key of the analyzer to the integer accepted. This forces the analyzer to reset and analyze MIDI files in the new key.
	 * @param key the new key signature
	 */
	public void setKey(int key)
	{
		if(key >= 1 && key <=12)
		{
			if(key != this.keySig)
			{
				this.keySig = key;
				analyzer.setKey(key);
			}
		}
		else
		{
			this.keySig = 1;
		}
	}

	/**
	 * Sets the value of tempo to the integer accepted.
	 * @param tempo the new tempo
	 */
	public void setTempo(int tempo)
	{
		this.tempo = tempo;
	}

	/**
	 * Accepts an integer representing a note value, and increments it a half step.
	 * @param note the note to be modified
	 * @return The modified note.
	 */
	private int upHalfStep(int note)
	{
		return note+1;
	}

	/**
	 * Accepts an integer representing a note value, and decrements it a half step.
	 * @param note the note to be modified
	 * @return The modified note.
	 */
	private int downHalfStep(int note)
	{
		return note-1;
	}

	/**
	 * Accepts an integer representing a note value, and increments it a whole step.
	 * @param note the note to be modified
	 * @return The modified note.
	 */
	private int upWholeStep(int note)
	{
		return note+2;
	}

	/**
	 * Accepts an integer representing a note value, and decrements it a whole step.
	 * @param note the note to be modified
	 * @return The modified note.
	 */
	private int downWholeStep(int note)
	{
		return note-2;
	}

	/**
	 * Accepts an integer representing a note value, and takes it up an octave.
	 * @param note the note to be modified
	 * @return The modified note
	 */
	private int upOctave(int note)
	{
		return note+12;
	}

	/**
	 * Accepts an integer representing a note value, and decrements it an octave.
	 * @param note the note to be modified
	 * @return The modified note.
	 */
	private int downOctave(int note)
	{
		return note-12;
	}

	/**
	 * Resets turtle to defaults between consecutive iterations.
	 */
	public void resetTurtle()
	{
		turtle.reset();
	}

	/**
	 * Accepts a string and parses through it to generate a pattern properly formatted for JFugue.
	 * @param production the L-System production to be parsed
	 * @param markov indicates whether to use the Markov chain method
	 * @return The music score as a Pattern
	 */
	public Pattern genScore(String production, boolean markov)
	{
		String pat = "";

		switch(this.keySig)
		{
			case 1:
				pat = generate(production, 48, markov);
				break;

			case 2:
				pat = generate(production, 55, markov);
				break;

			case 3:
				pat = generate(production, 50, markov);
				break;

			case 4:
				pat = generate(production, 57, markov);
				break;

			case 5:
				pat = generate(production, 52, markov);
				break;

			case 6:
				pat = generate(production, 59, markov);
				break;

			case 7:
				pat = generate(production, 54, markov);
				break;

			case 8:
				pat = generate(production, 49, markov);
				break;

			case 9:
				pat = generate(production, 56, markov);
				break;

			case 10:
				pat = generate(production, 51, markov);
				break;

			case 11:
				pat = generate(production, 58, markov);
				break;

			case 12:
				pat = generate(production, 53, markov);
				break;
		}

		Pattern pattern = new Pattern(pat);

		return pattern;
	}

	/**
	 * Accepts a production string and 4 integers which indicate where half steps should be made to keep music in key; generates music from the production.
	 * @param production the L-System production to be parsed
	 * @param tonic the tonic of the current key signature
	 * @param markov indicates whether to use the Markov chain method
	 * @return The music score as a string.
	 */
	private String generate(String production, int tonic, boolean markov)
	{
		StringBuffer buffer = new StringBuffer("T" + tempo + " V0 I80 ");	//Stores the score string that will be returned
		int voices = 1;														//Represents the number of voices currently active in the score
		degree = 1;
		int layers = 1;														//Represents the number of layers currently active in a voice
		int color = turtle.getColor();
		char[] prod = production.toCharArray();								//Array of characters from the production
		turtle.popY();
		turtle.pushY(tonic);

		//Step through each symbol in production
		for(int i=0 ; i < prod.length ; ++i)
		{
			switch(prod[i])
			{
				//Increment turtle's yaw
				case '-':
					turtle.pushYaw(turtle.popYaw() + turtle.getAngle());
					break;

				//Decrement turtle's yaw
				case '+':
					turtle.pushYaw(turtle.popYaw() - turtle.getAngle());
					break;

				//Turtle draws a line
				case 'g':
					if(!markov)
						buffer = drawLine(buffer, true);

					else
						buffer = drawMarkov(buffer, true);
					break;

				//Turtle moves without drawing
				case 'f':
					if(!markov)
						buffer = drawLine(buffer, false);

					else
						buffer = drawMarkov(buffer, false);
					break;

				case 'r':
					int direction = turtle.getDirection();

					//If turtle is horizontal, record line as a rest
					if(direction == 1 || direction == 3)
					{
						String str = buffer.toString();
						String regex = ".*Rs+";

						if(str.matches(regex))
							buffer.append("s");

						else
							buffer.append(" Rs");
					}
					break;

				case '[':
					if(voices < 16)
					{
						if(layers < 16)
						{
							turtle.saveState();
							buffer.append(" L" + layers + " I80 ");
							++layers;
						}

						else
						{
							turtle.saveState();
							buffer.append(" V" + voices + " I80 ");
							++voices;
							layers = 1;
						}
					}
					break;

				case ']':
					if(voices >= 1)
					{
						if(layers > 1)
						{
							turtle.restoreState();
							--layers;
							buffer.append(" L" + (layers-1) + " ");
						}

						else if(voices > 1)
						{
							turtle.restoreState();
							--voices;
							layers = 16;
							buffer.append(" V" + (voices-1) + " " + " L" + (layers-1) + " ");
						}
					}
					break;

				case '#':
					color = turtle.popColor();
					int hueChange = turtle.getHueChange();
					turtle.pushColor(color + hueChange);

					if(prod[i+2] != '#' && prod[i+2] != '@')
						buffer.append(" X1=" + (750-turtle.getColor())/3);
					break;

				case '@':
					color = turtle.popColor();
					int hueC = turtle.getHueChange();
					turtle.pushColor(color - hueC);

					if(prod[i+2] != '#' && prod[i+2] != '@')
						buffer.append(" X1=" + (750-turtle.getColor())/3);
					break;
			}
		}
		return buffer.toString();
	}

	/**
	 * Simulates drawing a line with the turtle and maps the action directly onto the music score.
	 * @param buffer stores the music score as it is being built
	 * @param draw is the note to be treated as a tie
	 * @return The StringBuffer with the necessary modifications made.
	 */
	private StringBuffer drawLine(StringBuffer buffer, boolean draw)
	{
		int pitch = turtle.getY();
		int direction = turtle.getDirection();

		//If turtle is horizontal, record line as a note
		if((direction == 1 || direction == 3) && draw)
		{
			String str = buffer.toString();
			String regex = ".*\\[" + pitch + "\\]s+";

			if(str.matches(regex))
				buffer.append("s");

			else
				buffer.append(" [" + pitch + "]s");
		}


		else if(direction == 1 || direction == 3)
			buffer.append(" [" + pitch + "]s");

		//If turtle facing upward, record line as a change up in pitch
		else if(direction == 2)
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

			if(turtle.getY() > upperNoteB)
			{
				pitch = turtle.popY();
				turtle.pushY(pitch - 24);
			}

			if(degree == 8)
				degree = 1;
		}

		//If turtle facing downward, record line as a change down in pitch
		else if(direction == 4)
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

			if(turtle.getY() < lowerNoteB)
			{
				pitch = turtle.popY();
				turtle.pushY(pitch + 24);
			}

			if(degree == 0)
				degree = 7;
		}

		return buffer;
	}

	/**
	 * Uses L-System as a guide instead of mapping the system directly onto the score. Uses a first-order Markov Chain to choose notes as the system progresses.
	 * @param buffer stores the music score as it is being built
	 * @param draw is the note to be treated as a tie
	 * @return The StringBuffer with the necessary modifications made.
	 */
	private StringBuffer drawMarkov(StringBuffer buffer, boolean draw)
	{
		int pitch = turtle.getY();
		int direction = turtle.getDirection();
		String key = this.getKey();
		int note = -1;
		int nextnote = -1;
		int noteDegree = 0;

		//Find the tonic of the current key in list of notes and store its integer representation
		for(int l=0 ; l < notes.length ; ++l)
		{
			if(key.equals(notes[l]))
			{
				note = l;
				noteDegree = 1;
				break;
			}
		}

		//Find the note corresponding to the current degree
		while(noteDegree != degree)
		{
			if(noteDegree == 3 || noteDegree == 7)
				note = upHalfStep(note);

			else
				note = upWholeStep(note);

			++noteDegree;
		}

		//If turtle is horizontal, no note change occurs
		if((direction == 1 || direction == 3) && draw)
		{
			String str = buffer.toString();
			String regex = ".*\\[" + pitch + "\\]s+";

			if(str.matches(regex))
				buffer.append("s");

			else
				buffer.append(" [" + pitch + "]s");

			return buffer;
		}

		else if(direction == 1 || direction == 3)
			buffer.append(" [" + pitch + "]s");

		//If turtle facing upward, record line as a change up in pitch
		else if(direction == 2)
		{
			nextnote = getFirstOrderNote(note);
			
			if(nextnote != -1)
			{
				prevDegree = degree;
					
				while(note != nextnote)
				{
					note = upHalfStep(note);
	
					if(note > 11)
						note = 0;
	
					pitch = upHalfStep(pitch);
					turtle.popY();
					turtle.pushY(pitch);
	
					++degree;
	
					if(turtle.getY() > upperNoteB)
					{
						pitch = turtle.popY();
						turtle.pushY(pitch - 24);
					}
						
					if(degree == 8)
						degree = 1;
				}
			}

			buffer.append(" [" + pitch + "]s");
		}

		//If turtle facing downward, record line as a change down in pitch
		else if(direction == 4)
		{
			nextnote = getFirstOrderNote(note);
			
			if(nextnote != -1)
			{
				prevDegree = degree;
					
				while(note != nextnote)
				{
					note = downHalfStep(note);
	
					if(note < 0)
						note = 11;
	
					pitch = downHalfStep(pitch);
					turtle.popY();
					turtle.pushY(pitch);
	
					--degree;
					
					if(turtle.getY() < lowerNoteB)
					{
						pitch = turtle.popY();
						turtle.pushY(pitch + 24);
					}
	
					if(degree == 0)
						degree = 7;
				}
			}
				
			buffer.append(" [" + pitch + "]s");
		}

		return buffer;
	}
	
	/**
	 * Randomly chooses a note based on the probabilities provided by MusicAnalyzer
	 * @param note  the last note in the score
	 * @return The next note to be recorded represented as an integer
	 */
	private int getFirstOrderNote(int note)
	{
		ArrayList<Double> list = analyzer.getProbability(note);
		double range = 0.0;
		double rand = Math.random();

		for(int l=0 ; l < list.size() ; ++l)
		{
			range += list.get(l);

			if(rand <= range)
				return l;
		}
		
		return -1;
	}
	
	/*private int getSecondOrderNote(int prevnote, int note)
	{
		
	}*/
}