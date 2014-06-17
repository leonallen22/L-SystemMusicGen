import java.util.ArrayList;


/**
 * Generates rhythms for use in melody and accompaniment.
 * 
 * @author Harry Allen
 */
public class RhythmGenerator
{
	private int density;
	private int	minDuration;
	private static char[] durations =
		{ 'o', 'x', 't', 's', 'i', 'q', 'h', 'w' };
	private char[] rhythm;
	
	/**
	 * Default Constructor.
	 */
	RhythmGenerator()
	{
		density = 50;
		minDuration = 3;
	}
	
	/**
	 * @return The probability of subdivision to the next level represented as a percentage
	 */
	public int getDensity()
	{
		return density;
	}
	
	/**
	 * @return The lowest level of subdivision that can occur in the rhythm
	 */
	public int getMinDuration()
	{
		return minDuration;
	}
	
	/**
	 * @param newdensity  the new probability that subdivision will occur
	 */
	public void setDensity(int newdensity)
	{
		density = newdensity;
	}
	
	/**
	 * @param newMinDuration  the new lowest level of subdivision that can occur
	 */
	public void setMinDuration(int newMinDuration)
	{
		minDuration = newMinDuration;
	}
	
	/**
	 * Uses Bjorklund to generate rhythms to be implemented into the music.
	 * @return char array with the durations and rests to be used.
	 */
	public char[] genRhythm()
	{
		//ArrayList<Integer> durations = new ArrayList<Integer>();
		//char[] d = {'s', 'i', 'q', 'h', 'w'};
		double pulse = Math.random()*1000;
		double step = Math.random()*1000;
		String rhythm = "";
		int duration = 3;
		int pulses = 0;
		int steps = 0;
		
		if(step <= 333)
		{
			steps = 4;
			duration = 5;
			
			if(pulse <= 250)
				pulses = 1;
			
			else if(pulse <= 500)
				pulses = 2;
			
			else if(pulse <= 750)
				pulses = 3;
			
			else
				pulses = 3;
		}
		
		else if(step <= 666)
		{
			int count = 4;
			steps = 16;
			duration = 4;
			
			for(int i=125 ; i <= 1000 ; i = i += 125)
			{
				if(pulse <= i)
				{
					pulses = count;
					break;
				}
				
				++count;
			}
			
			if(pulses == 0)
				pulses = count-2;
		}
		
		else
		{
			int count = 20;
			steps = 64;
			
			for(int i=25 ; i <= 1000 ; i = i += 25)
			{
				if(pulse <= i)
				{
					pulses = count;
					break;
				}
				
				++count;
			}
			
			if(pulses == 0)
				pulses = count-2;
		}
		Bjorklund gen = new Bjorklund(pulses, steps);
		ArrayList<Boolean> r = gen.getRhythm();
		
		/*for(int i=0 ; i < r.size() ; ++i)
		{
			duration = duration % d.length;
			
			if(i != 0 && r.get(i-1) == true && r.get(i) == true)
				++duration;
			
			else if(r.get(i) == true)
				duration = 0;
			
			else
			{
				durations.add(duration);
				duration = 0;
			}
		}*/
		
		for(int i=0 ; i < r.size() ; ++i)
		{			
			if(r.get(i) == true)
				rhythm += durations[duration];
			
			else
			{
				switch(duration)
				{
					case 3:
						rhythm += '3';
						break;
						
					case 4:
						rhythm += '4';
						break;
						
					case 5:
						rhythm += '5';
						
					case 6:
						rhythm += '6';
						
					case 7:
						rhythm += '7';
				}
			}
		}
		
		/*for(Integer dur : durations)
			rhythm += d[dur];*/
		
		System.out.println(rhythm);
		this.rhythm = rhythm.toCharArray();
		return this.rhythm;
	}
}
