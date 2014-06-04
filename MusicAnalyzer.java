import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jfugue.*;
import com.esotericsoftware.wildcard.*;
import javax.sound.midi.InvalidMidiDataException;

/**
 * Analyzes a series of MIDI files, producing first-order and second-order Markov Chains which represent the probability of one note following another and one note following a sequence of two notes, respectively.
 */
public class MusicAnalyzer
{
	private ArrayList<ArrayList<Double>> prob;					//Markov chain: stores the probability vector for each note
	private ArrayList<ArrayList<ArrayList<Double>>> secondProb;	//Markov chain: stores the probability vector for each combination of the current note and immediately preceding note
	private int key;
	
	/**
	 * Default constructor.
	 */
	public MusicAnalyzer()
	{
		prob = new ArrayList<ArrayList<Double>>();
		secondProb = new ArrayList<ArrayList<ArrayList<Double>>>();
		key = 1;
		
		resetProbabilities();
		analyze();
		analyzeSecondOrder();
	}
	
	/**
	 * @param note note of the probability vector to return
	 * @return The probability vector for the note accepted as the parameter
	 */
	public ArrayList<Double> getProbability(int note)
	{
		return prob.get(note);
	}
	
	/**
	 * @param prevnote  second to last note recorded in the score
	 * @param note  last note recorded in the score
	 * @return the probability vector based on the previous two notes recorded
	 */
	public ArrayList<Double> getSecondProb(int prevnote, int note)
	{
		return secondProb.get(note).get(prevnote);
	}
	
	/**
	 * @return Integer representing key signature
	 */
	public int getKey()
	{
		return key;
	}
	
	/**
	 * Sets key signature to the integer passed in, resets the probabilities, and analyzes MIDI files in the new key.
	 * @param newkey  new key signature
	 */
	public void setKey(int newkey)
	{
		if(newkey != key)
		{
			if(newkey >= 1 && newkey <= 12)
				key = newkey;
			
			else
				key = 1;
			
			resetProbabilities();
			analyze();
			analyzeSecondOrder();
		}
	}
	
	/**
	 * Resets all probability vectors to 0.0 in preparation for another call to analyze() and analyzeSecondOrder().
	 */
	public void resetProbabilities()
	{
		prob.clear();
		secondProb.clear();
		
		for(int i=0 ; i < 12 ; ++i)
		{
			prob.add(new ArrayList<Double>());
			secondProb.add(new ArrayList<ArrayList<Double>>());
		}
		
		for(ArrayList<Double> list : prob)
		{
			for(int i=0 ; i < 12 ; ++i)
				list.add(0.0);
		}
		
		for(ArrayList<ArrayList<Double>> list : secondProb)
		{
			for(int i=0 ; i < 12 ; ++i)
				list.add(new ArrayList<Double>());
		}
		
		for(ArrayList<ArrayList<Double>> list : secondProb)
		{
			for(ArrayList<Double> x : list)
			{
				for(int i=0 ; i < 12 ; ++i)
					x.add(0.0);
			}
		}
	}
	
	/**
	 * Performs a basic analysis of the MIDI files in the MIDIs folder. Based on the given MIDI files
	 * the method will generate a first-order Markov Chain which will give the likelihood of a note being
	 * chosen based on the previous note.
	 */
	public void analyze()
	{
		Player player = new Player();
		ArrayList<Double> note;
		String musicstring = "";
		char[] music;
		Paths midis = getPath();
		
		//Run analysis on each MIDI file
		for(File midi : midis.getFiles())
		{
			try
			{
				Pattern pat = player.loadMidi(midi);
				musicstring = pat.getMusicString();
			}
			catch(InvalidMidiDataException e)
			{
				System.out.println(e.toString());
			}
			catch(IOException e)
			{
				System.out.println(e.toString());
			}
			
			music = musicstring.toCharArray();
			int prevnote = -1;
			
			for(int i=0 ; i < music.length ; ++i)
			{
				if(prevnote != -1)
					note = prob.get(prevnote);
				
				else
				{
					note = new ArrayList<Double>();
					
					for(int j=0 ; j < 12 ; ++j)
						note.add(0.0);
				}
				
				switch(music[i])
				{
					case 'C':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == '#')
							note.set(1, note.get(1)+1);
						
						else
							note.set(0, note.get(0)+1);
						break;
						
					case 'D':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(1, note.get(1)+1);

						
						else if(music[i+1] == '#')
							note.set(3, note.get(3)+1);
						
						else
							note.set(2, note.get(2)+1);
						break;
						
					case 'E':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(3, note.get(3)+1);
						
						else
							note.set(4, note.get(4)+1);

						break;
						
					case 'F':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == '#')
							note.set(6, note.get(6)+1);

						
						else
							note.set(5, note.get(5)+1);

						break;
						
					case 'G':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(6, note.get(6)+1);
						
						else if(music[i+1] == '#')
							note.set(8, note.get(8)+1);
						
						else
							note.set(7, note.get(7)+1);
						break;
						
					case 'A':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(8, note.get(8)+1);
						
						else if(music[i+1] == '#')
							note.set(10, note.get(10)+1);
						
						else
							note.set(9, note.get(9)+1);
						break;
						
					case 'B':
						if(prevnote == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(10, note.get(10)+1);
						
						else
							note.set(11, note.get(11)+1);
						break;
				}
				
				switch(music[i])
				{
					case 'C':
						if(music[i+1] == '#')
						{
							prevnote = 1;
							i = i + 2;
						}
						
						else
						{
							prevnote = 0;
							++i;
						}
						break;
						
					case 'D':
						if(music[i+1] == 'b')
						{
							prevnote = 1;
							i = i + 2;
						}
						
						else if(music[i+1] == '#')
						{
							prevnote = 3;
							i = i + 2;
						}
						
						else
						{
							prevnote = 2;
							++i;
						}
						break;
						
					case 'E':
						if(music[i+1] == 'b')
						{
							prevnote = 3;
							i = i + 2;
						}
						
						else
						{
							prevnote = 4;
							++i;
						}
						break;
						
					case 'F':
						if(music[i+1] == '#')
						{
							prevnote = 6;
							i = i + 2;
						}
						
						else
						{
							prevnote = 5;
							++i;
						}
						break;
						
					case 'G':
						if(music[i+1] == 'b')
						{
							prevnote = 6;
							i = i + 2;
						}
						
						else if(music[i+1] == '#')
						{
							prevnote = 8;
							i = i + 2;
						}
						
						else
						{
							prevnote = 7;
							++i;
						}
						break;
						
					case 'A':
						if(music[i+1] == 'b')
						{
							prevnote = 8;
							i = i + 2;
						}
						
						else if(music[i+1] == '#')
						{
							prevnote = 10;
							i = i + 2;
						}
						
						else
						{
							prevnote = 9;
							++i;
						}
						break;
						
					case 'B':
						if(music[i+1] == 'b')
						{
							prevnote = 10;
							i = i + 2;
						}
						
						else
						{
							prevnote = 11;
							++i;
						}
						break;
				}
					
			}
		}
		
		//Iterate through each note's probability vector and calculate the probability of a given note being chosen
		for(int k=0 ; k < prob.size() ; ++k)
		{
			ArrayList<Double> list = prob.get(k);
			double x = 0.0;
			double sum = 0.0;
						
			for(Double y : list)
				sum += y;
			
			for(int l=0 ; l < list.size() ; ++l)
			{
				x = list.get(l);
				
				if(sum != 0)
					list.set(l, (x/sum));
			}
		}
	}
	
	/**
	 * Performs an analysis of the MIDI files in the MIDIs folder. Based on the given MIDI files
	 * the method will generate a second-order Markov Chain which will give the likelihood of a note being
	 * chosen based on the previous two notes.
	 */
	public void analyzeSecondOrder()
	{
		Player player = new Player();
		ArrayList<Double> note;				//Temporarily stores musical data to calculate probabilities
		int[] prevnotes = {-1, -1};
		String musicstring = "";
		char[] music;						//Stores music from MIDI files
		Paths midis = getPath();
		
		//Run analysis on each MIDI file
		for(File midi : midis.getFiles())
		{
			try
			{
				Pattern pat = player.loadMidi(midi);
				musicstring = pat.getMusicString();
			}
			catch(InvalidMidiDataException e)
			{
				System.out.println(e.toString());
			}
			catch(IOException e)
			{
				System.out.println(e.toString());
			}
			
			music = musicstring.toCharArray();
			
			for(int i=0 ; i < music.length ; ++i)
			{
				if(prevnotes[0] != -1 && prevnotes[1] != -1)
					note = secondProb.get(prevnotes[1]).get(prevnotes[0]);
				
				else
				{
					note = new ArrayList<Double>();
					
					for(int j=0 ; j < 12 ; ++j)
						note.add(0.0);
				}
				
				switch(music[i])
				{
					case 'C':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == '#')
							note.set(1, note.get(1)+1);
						
						else
							note.set(0, note.get(0)+1);
						break;
						
					case 'D':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(1, note.get(1)+1);

						
						else if(music[i+1] == '#')
							note.set(3, note.get(3)+1);
						
						else
							note.set(2, note.get(2)+1);
						break;
						
					case 'E':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(3, note.get(3)+1);
						
						else
							note.set(4, note.get(4)+1);

						break;
						
					case 'F':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == '#')
							note.set(6, note.get(6)+1);

						
						else
							note.set(5, note.get(5)+1);

						break;
						
					case 'G':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(6, note.get(6)+1);
						
						else if(music[i+1] == '#')
							note.set(8, note.get(8)+1);
						
						else
							note.set(7, note.get(7)+1);
						break;
						
					case 'A':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(8, note.get(8)+1);
						
						else if(music[i+1] == '#')
							note.set(10, note.get(10)+1);
						
						else
							note.set(9, note.get(9)+1);
						break;
						
					case 'B':
						if(prevnotes[0] == -1 || prevnotes[1] == -1)
							break;
						
						else if(music[i+1] == 'b')
							note.set(10, note.get(10)+1);
						
						else
							note.set(11, note.get(11)+1);
						break;
				}
				
				switch(music[i])
				{
					case 'C':
						if(music[i+1] == '#')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 1;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 0;
							++i;
						}
						break;
						
					case 'D':
						if(music[i+1] == 'b')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 1;
							i = i + 2;
						}
						
						else if(music[i+1] == '#')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 3;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 2;
							++i;
						}
						break;
						
					case 'E':
						if(music[i+1] == 'b')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 3;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 4;
							++i;
						}
						break;
						
					case 'F':
						if(music[i+1] == '#')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 6;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 5;
							++i;
						}
						break;
						
					case 'G':
						if(music[i+1] == 'b')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 6;
							i = i + 2;
						}
						
						else if(music[i+1] == '#')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 8;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 7;
							++i;
						}
						break;
						
					case 'A':
						if(music[i+1] == 'b')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 8;
							i = i + 2;
						}
						
						else if(music[i+1] == '#')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 10;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 9;
							++i;
						}
						break;
						
					case 'B':
						if(music[i+1] == 'b')
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 10;
							i = i + 2;
						}
						
						else
						{
							prevnotes[1] = prevnotes[0];
							prevnotes[0] = 11;
							++i;
						}
						break;
				}
					
			}
		}
		
		//Iterate through each note's probability vector and calculate the probability of a given note being chosen
		for(int k=0 ; k < 12 ; ++k)
		{
			for(int l=0 ; l < 12 ; ++l)
			{
				ArrayList<Double> list = secondProb.get(k).get(l);
				double x = 0.0;
				double sum = 0.0;
							
				for(Double y : list)
					sum += y;
				
				for(int m=0 ; m < list.size() ; ++m)
				{
					x = list.get(m);
					
					if(sum != 0)
						list.set(m, (x/sum));
				}
			}
		}
	}
	
	public Paths getPath()
	{
		switch(key)
		{
			case 1:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/CMajor", "*.mid");
				
			case 2:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/GMajor", "*.mid");
				
			case 3:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/DMajor", "*.mid");
				
			case 4:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/AMajor", "*.mid");
				
			case 5:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/EMajor", "*.mid");
				
			case 6:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/BMajor", "*.mid");
				
			case 7:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/F#Major", "*.mid");
				
			case 8:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/DbMajor", "*.mid");

			case 9:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/AbMajor", "*.mid");
				
			case 10:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/EbMajor", "*.mid");
				
			case 11:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/BbMajor", "*.mid");
				
			case 12:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/FMajor", "*.mid");	
				
			default:
				return new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/CMajor", "*.mid");
		}
	}
}
