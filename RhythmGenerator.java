import java.util.ArrayList;

/**
 * Generates rhythms for use in melody and accompaniment.<br>
 * Rhythms are represented as a sequence characters which represent durations:
 * <table border="1">
 *  <tr>
 *      <td>Whole note</td>
 *      <td>w</td>
 *      <td>Whole rest</td>
 *      <td>7</td>
 *  </tr>
 *  <tr>
 *      <td>Half note</td>
 *      <td>h</td>
 *      <td>Half rest</td>
 *      <td>6</td>
 *  </tr>
 *  <tr>
 *      <td>Quarter note</td>
 *      <td>q</td>
 *      <td>Quarter rest</td>
 *      <td>5</td>
 *  </tr>
 *  <tr>
 *      <td>Eighth note</td>
 *      <td>i</td>
 *      <td>Eighth rest</td>
 *      <td>4</td>
 *  </tr>
 *  <tr>
 *      <td>Sixteenth note</td>
 *      <td>s</td>
 *      <td>Sixteenth rest</td>
 *      <td>3</td>
 *  </tr>
 *  <tr>
 *      <td>Thirty-second note</td>
 *      <td>t</td>
 *      <td>Thirty-second rest</td>
 *      <td>2</td>
 *  </tr>
 *  <tr>
 *      <td>Sixty-fourth note</td>
 *      <td>x</td>
 *      <td>Sixty-fourth rest</td>
 *      <td>1</td>
 *  </tr>
 *  <tr>
 *      <td>One-twenty-eighth note</td>
 *      <td>o</td>
 *      <td>One-twenty-eighth rest</td>
 *      <td>0</td>
 *  </tr>
 * </table>
 * 
 * @author Harry Allen
 */
public class RhythmGenerator
{

    private int           density;
    private int           minDuration;
    private static char[] durations = { 'o', 'x', 't', 's', 'i', 'q', 'h', 'w' };
    private char[]        rhythm;

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
     * @return rhythm as a char array.
     */
    public char[] getRhythm()
    {
        return rhythm;
    }

    /**
     * @param newdensity the new probability that subdivision will occur
     */
    public void setDensity(int newdensity)
    {
        density = newdensity;
    }

    /**
     * @param newMinDuration the new lowest level of subdivision that can occur
     */
    public void setMinDuration(int newMinDuration)
    {
        minDuration = newMinDuration;
    }

    /**
     * Uses Bjorklund to generate rhythms one 4/4 measure at a time to be implemented into the music.
     * 
     * @return char array with the durations and rests to be used.
     */
    public char[] genRhythm()
    {
        // ArrayList<Integer> durations = new ArrayList<Integer>();
        // char[] d = {'s', 'i', 'q', 'h', 'w'};
        double pulse = Math.random() * 1000;
        double step = Math.random() * 1000;
        String rhythm = "";
        int duration = 3;
        int pulses = 0;
        int steps = 0;
        double beats = 0.0;

        if (step <= 333)
        {
            steps = 4;
            duration = 5;

            if (pulse <= 250)
                pulses = 1;

            else if (pulse <= 500)
                pulses = 2;

            else if (pulse <= 750)
                pulses = 3;

            else
                pulses = 3;
        }

        else if (step <= 666)
        {
            int count = 3;
            steps = 8;
            duration = 4;

            for (int i = 200; i <= 1000; i = i += 200)
            {
                if (pulse <= i)
                {
                    pulses = count;
                    break;
                }

                ++count;
            }

            if (pulses == 0)
                pulses = count - 2;
        }

        else
        {
            int count = 6;
            steps = 16;

            for (int i = 100; i <= 1000; i = i += 100)
            {
                if (pulse <= i)
                {
                    pulses = count;
                    break;
                }

                ++count;
            }

            if (pulses == 0)
                pulses = count - 2;
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

        char dur = durations[duration];

        for (int i = 0; i < r.size(); ++i)
        {
            if (beats >= 4.0)
                break;

            if (r.get(i) == true)
                rhythm += dur;

            else
            {
                switch (duration)
                {
                    case 0:
                        rhythm += '0';
                        break;

                    case 1:
                        rhythm += '1';
                        break;

                    case 2:
                        rhythm += '2';
                        break;

                    case 3:
                        rhythm += '3';
                        break;

                    case 4:
                        rhythm += '4';
                        break;

                    case 5:
                        rhythm += '5';
                        break;

                    case 6:
                        rhythm += '6';
                        break;

                    case 7:
                        rhythm += '7';
                        break;
                }
            }

            switch (duration)
            {
                case 0:
                    beats += 0.03125;
                    break;

                case 1:
                    beats += 0.0625;
                    break;

                case 2:
                    beats += 0.125;
                    break;

                case 3:
                    beats += 0.25;
                    break;

                case 4:
                    beats += 0.5;
                    break;

                case 5:
                    beats += 1.0;
                    break;

                case 6:
                    beats += 2.0;
                    break;

                case 7:
                    beats += 4.0;
                    break;
            }
        }

        /*for(Integer dur : durations)
        	rhythm += d[dur];*/

        System.out.println(rhythm);
        this.rhythm = rhythm.toCharArray();
        return this.rhythm;
    }
}
