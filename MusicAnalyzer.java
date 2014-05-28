
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jfugue.*;
import com.esotericsoftware.wildcard.Paths;
import javax.sound.midi.InvalidMidiDataException;

public class MusicAnalyzer
{
	private ArrayList<ArrayList<Double>> prob;
	private String[] notes = {"C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};
	
	public MusicAnalyzer()
	{
		prob = new ArrayList<ArrayList<Double>>();
		
		for(int i=0 ; i < 12 ; ++i)
			prob.add(new ArrayList<Double>());
		
		for(ArrayList<Double> list : prob)
		{
			for(int i=0 ; i < 12 ; ++i)
				list.add(0.0);
		}
	}
	
	public ArrayList<Double> getProbability(int note)
	{
		return prob.get(note);
	}
	
	public void analyze()
	{
		Player player = new Player();
		ArrayList<Double> note;
		String musicstring = "";
		char[] music;
		double C = 0, Csharp = 0, Dflat = 0, D = 0, Dsharp = 0, Eflat = 0, E = 0, F = 0, Fsharp = 0, 
			Gflat = 0, G = 0, Gsharp = 0, Aflat = 0, A = 0, Asharp = 0, Bflat = 0, B = 0;
		
		try
		{
			Pattern pat = player.loadMidi(new File("bwv772.mid"));
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
		
		for(int i=0; i < music.length ; ++i)
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
					{
						
						break;
					}
					
					else if(music[i+1] == '#')
					{
						note.set(1, note.get(1)+1);
						++Csharp;
					}
					
					else
					{
						note.set(0, note.get(0)+1);
						++C;
					}
					break;
					
				case 'D':
					if(prevnote == -1)
						break;
					
					else if(music[i+1] == 'b')
					{
						note.set(1, note.get(1)+1);
						++Dflat;
					}
					
					else if(music[i+1] == '#')
					{
						note.set(3, note.get(3)+1);
						++Dsharp;
					}
					
					else
					{
						note.set(2, note.get(2)+1);
						++D;
					}
					break;
					
				case 'E':
					if(prevnote == -1)
						break;
					
					else if(music[i+1] == 'b')
					{
						note.set(3, note.get(3)+1);
						++Eflat;
					}
					
					else
					{
						note.set(4, note.get(4)+1);
						++E;
					}
					break;
					
				case 'F':
					if(prevnote == -1)
						break;
					
					else if(music[i+1] == '#')
					{
						note.set(6, note.get(6)+1);
						++Fsharp;
					}
					
					else
					{
						note.set(5, note.get(5)+1);
						++F;
					}
					break;
					
				case 'G':
					if(prevnote == -1)
						break;
					
					else if(music[i+1] == 'b')
					{
						note.set(6, note.get(6)+1);
						++Gflat;
					}
					
					else if(music[i+1] == '#')
					{
						note.set(8, note.get(8)+1);
						++Gsharp;
					}
					
					else
					{
						note.set(7, note.get(7)+1);
						++G;
					}
					break;
					
				case 'A':
					if(prevnote == -1)
						break;
					
					else if(music[i+1] == 'b')
					{
						note.set(8, note.get(8)+1);
						++Aflat;
					}
					
					else if(music[i+1] == '#')
					{
						note.set(10, note.get(10)+1);
						++Asharp;
					}
					
					else
					{
						note.set(9, note.get(9)+1);
						++A;
					}
					break;
					
				case 'B':
					if(prevnote == -1)
						break;
					
					else if(music[i+1] == 'b')
					{
						note.set(10, note.get(10)+1);
						++Bflat;
					}
					
					else
					{
						note.set(11, note.get(11)+1);
						++B;
					}
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
		
		System.out.println("\tC\tC#\tD\tD#\tE\tF\tF#\tG\tG#\tA\tA#\tB");
		int k = 0;
		for(ArrayList<Double> list : prob)
		{
			System.out.print(notes[k] + "\t");
			for(Double x : list)
			{
				System.out.print(x + "\t");
			}
			System.out.println();
			++k;
		}
		
		Paths midis = new Paths();
	}
}
