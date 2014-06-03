import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jfugue.*;
import com.esotericsoftware.wildcard.*;
import javax.sound.midi.InvalidMidiDataException;

/**
 * Analyzes a series of MIDI files, producing a first-order Markov Chain of the probability of a note following a given note.
 * Currently must analyze again every time the key signature is changed.
 */
public class MusicAnalyzer
{
	private ArrayList<ArrayList<Double>> prob;			//Markov chain: stores the probability vector for each note
	private ArrayList<ArrayList<Double>> secondProb;	//Markov chain: stores the probability vector for each combination of the current note and immediately preceding note
	private int key;
	
	/**
	 * Default constructor.
	 */
	public MusicAnalyzer()
	{
		prob = new ArrayList<ArrayList<Double>>();
		secondProb = new ArrayList<ArrayList<Double>>();
		key = 1;
		
		for(int i=0 ; i < 12 ; ++i)
		{
			prob.add(new ArrayList<Double>());
			secondProb.add(new ArrayList<Double>());
		}
		
		for(ArrayList<Double> list : prob)
		{
			for(int i=0 ; i < 12 ; ++i)
				list.add(0.0);
		}
		
		for(ArrayList<Double> list : secondProb)
		{
			for(int i=0 ; i < 144 ; ++i)
				list.add(0.0);
		}
	}
	
	/**
	 * Returns the probability vector for the note passed as parameter.
	 * @param note note of the probability vector to return
	 * @return The probability vector for the note accepted as the parameter
	 */
	public ArrayList<Double> getProbability(int note)
	{
		return prob.get(note);
	}
	
	public ArrayList<Double> getSecondProb(int prevnote, int note)
	{
		return secondProb.get(note + 12*prevnote);
	}
	
	/**
	 * @return Integer representing key signature
	 */
	public int getKey()
	{
		return key;
	}
	
	/**
	 * Sets key signature to the integer passed in.
	 * @param newkey  new key signature
	 */
	public void setKey(int newkey)
	{
		key = newkey;
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
		Paths midis = new Paths();
		
		switch(key)
		{
			case 1:
				midis = new Paths("C:/EclipseWorkspace/L-SystemMusic/MIDIs/CMajor", "*.mid");
				break;
		}
		
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
				list.set(l, (x/sum));
			}
		}
	}
}
