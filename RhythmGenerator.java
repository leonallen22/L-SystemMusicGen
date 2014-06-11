

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
	
	/*public char[] generateRhythm()
	{
		
	}*/
}
