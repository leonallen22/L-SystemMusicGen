
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jfugue.*;
import javax.sound.midi.InvalidMidiDataException;

public class MusicAnalyzer
{
	private ArrayList<ArrayList<Double>> prob;
	
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
		String musicstring = "";
		char[] music;
		double C = 0, Csharp = 0, Dflat = 0, D = 0, Dsharp = 0, Eflat = 0, E = 0, F = 0, Fsharp = 0, 
			Gflat = 0, G = 0, Gsharp = 0, Aflat = 0, A = 0, Asharp = 0, Bflat = 0, B = 0;
		
		try
		{
			Pattern pat = player.loadMidi(new File("test.mid"));
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
		
		for(int i=0; i < music.length ; ++i)
		{
			int prevnote = -1;
			
			switch(music[i])
			{
				case 'C':
					if(music[i+1] == '#')
					{
						++Csharp;
						i = i + 2;
					}
					
					else
					{
						++C;
						++i;
					}
					break;
					
				case 'D':
					if(music[i+1] == 'b')
					{
						++Dflat;
						i = i + 2;
					}
					
					else if(music[i+1] == '#')
					{
						++Dsharp;
						i = i + 2;
					}
					
					else
					{
						++D;
						++i;
					}
					break;
					
				case 'E':
					if(music[i+1] == 'b')
					{
						++Eflat;
						i = i + 2;
					}
					
					else
					{
						++E;
						++i;
					}
					break;
					
				case 'F':
					if(music[i+1] == '#')
					{
						++Fsharp;
						i = i + 2;
					}
					
					else
					{
						++F;
						++i;
					}
					break;
					
				case 'G':
					if(music[i+1] == 'b')
					{
						++Gflat;
						i = i + 2;
					}
					
					else if(music[i+1] == '#')
					{
						++Gsharp;
						i = i + 2;
					}
					
					else
					{
						++G;
						++i;
					}
					break;
					
				case 'A':
					if(music[i+1] == 'b')
					{
						++Aflat;
						i = i + 2;
					}
					
					else if(music[i+1] == '#')
					{
						++Asharp;
						i = i + 2;
					}
					
					else
					{
						++A;
						++i;
					}
					break;
					
				case 'B':
					if(music[i+1] == 'b')
					{
						++Bflat;
						i = i + 2;
					}
					
					else
					{
						++B;
						++i;
					}
					break;
			}
			
			switch(music[i])
			{
				case 'C':
					if(music[i+1] == '#')
						prevnote = 1;
					
					else
						prevnote = 0;
					break;
					
				case 'D':
					if(music[i+1] == 'b')
						prevnote = 1;
					
					else if(music[i+1] == '#')
						prevnote = 3;
					
					else
						prevnote = 2;
					break;
					
				case 'E':
					if(music[i+1] == 'b')
						prevnote = 3;
					
					else
						prevnote = 4;
					break;
					
				case 'F':
					if(music[i+1] == '#')
						prevnote = 6;
					
					else
						prevnote = 5;
					break;
					
				case 'G':
					if(music[i+1] == 'b')
						prevnote = 6;
					
					else if(music[i+1] == '#')
						prevnote = 8;
					
					else
						prevnote = 7;
					break;
					
				case 'A':
					if(music[i+1] == 'b')
						prevnote = 8;
					
					else if(music[i+1] == '#')
						prevnote = 10;
					
					else
						prevnote = 9;
					break;
					
				case 'B':
					if(music[i+1] == 'b')
						prevnote = 10;
					
					else
						prevnote = 11;
					break;
			}
				
		}
		
		prob.set(0, C);
		prob.set(1, Csharp+Dflat);
		prob.set(2, D);
		prob.set(3, Dsharp+Eflat);
		prob.set(4, E);
		prob.set(5, F);
		prob.set(6, Fsharp+Gflat);
		prob.set(7, G);
		prob.set(8, Gsharp+Aflat);
		prob.set(9, A);
		prob.set(10, Asharp+Bflat);
		prob.set(11, B);
		
	}
}
