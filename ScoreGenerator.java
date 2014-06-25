import org.jfugue.Pattern;
import java.util.ArrayList;

/**
 * Generates a properly formatted score for use in JFugue from an L-system production.<br>
 * Probabilities of transition from one chord to another in a chord progression.<br>
 * Only the most common chords for Major key progressions are included:
 * <table border="1">
 *  <thead>
 *      <td></td>
 *      <td><strong>I</strong></td>
 *      <td><strong>V</strong></td>
 *      <td><strong>IV</strong></td>
 *      <td><strong>vi</strong></td>
 *      <td><strong>iii</strong></td>
 *      <td><strong>ii</strong></td>
 *  </thead>
 *  <tbody>
 *      <tr>
 *          <td><strong>I</strong></td>
 *          <td>16.67</td>
 *          <td>16.67</td>
 *          <td>16.67</td>
 *          <td>16.67</td>
 *          <td>16.67</td>
 *          <td>16.67</td>
 *      </tr>
 *      <tr>
 *          <td><strong>V</strong></td>
 *          <td>33.33</td>
 *          <td>33.33</td>
 *          <td>00.00</td>
 *          <td>33.33</td>
 *          <td>00.00</td>
 *          <td>00.00</td>
 *      </tr>
 *      <tr>
 *          <td><strong>IV</strong></td>
 *          <td>20.00</td>
 *          <td>20.00</td>
 *          <td>20.00</td>
 *          <td>00.00</td>
 *          <td>20.00</td>
 *          <td>20.00</td>
 *      </tr>
 *      <tr>
 *          <td><strong>vi</strong></td>
 *          <td>00.00</td>
 *          <td>25.00</td>
 *          <td>25.00</td>
 *          <td>25.00</td>
 *          <td>00.00</td>
 *          <td>25.00</td>
 *      </tr>
 *      <tr>
 *          <td><strong>iii</strong></td>
 *          <td>00.00</td>
 *          <td>00.00</td>
 *          <td>25.00</td>
 *          <td>25.00</td>
 *          <td>25.00</td>
 *          <td>25.00</td>
 *      </tr>
 *      <tr>
 *          <td><strong>ii</strong></td>
 *          <td>00.00</td>
 *          <td>33.33</td>
 *          <td>00.00</td>
 *          <td>00.00</td>
 *          <td>33.33</td>
 *          <td>33.33</td>
 *      </tr>
 *  </tbody>
 * </table>
 * 
 * @author Harry Allen
 */
public class ScoreGenerator
{

    private Turtle                        turtle;       // Turtle to keep track of "drawing" actions
    private MusicAnalyzer                 analyzer;     // Analyzes MIDI files and generates a first-order Markov chain for all notes on the Western Scale
    private Score                         score;        // Stores the music score and related information
    private RhythmGenerator               rhythmGen;    // Generates rhythms to be implemented into the melody or accompaniment
    private int[]                         chords;       // Chord progression to be implemented into composition
    private ArrayList<ArrayList<Integer>> chordProb;    // Stores the chords and the likelihood of their transition to other chords
    private int                           beat;         // Keeps track of the beat to determine when to place measure marker and generate a new rhythm
    private int                           lowerBound;   // Lower bound of note pitch
    private int                           upperBound;   // Upper bound of note pitch
    private int                           chordInterval;// Interval at which chords are written into score

    /**
     * Default Constructor.
     */
    public ScoreGenerator()
    {
        analyzer = new MusicAnalyzer();
        turtle = new Turtle();
        score = new Score();
        rhythmGen = new RhythmGenerator();
        initChordProb();
        beat = 0;
        upperBound = 95;
        lowerBound = 45;
        chordInterval = 64;
    }

    /**
     * Constructor.
     * 
     * @param angle the angle at which the turtle should start
     */
    public ScoreGenerator(int angle)
    {
        analyzer = new MusicAnalyzer();
        turtle = new Turtle();
        score = new Score();
        rhythmGen = new RhythmGenerator();
        initChordProb();
        turtle.pushAngle(angle);
        beat = 0;
        upperBound = 95;
        lowerBound = 45;
        chordInterval = 64;
    }
    
    private void initChordProb()
    {
        chordProb = new ArrayList<ArrayList<Integer>>();
        
        for(int i=0 ; i < 7 ; ++i)
        {
            chordProb.add(new ArrayList<Integer>());
        }
        
        for(int i=0 ; i < chordProb.size() ; ++i)
        {
            ArrayList<Integer> list = chordProb.get(i);
            switch(i)
            {
                case 0:
                    for(int j=0 ; j < 6 ; ++j)
                        list.add(1667);
                    break;
                    
                case 1:
                    for(int j=0 ; j < 6 ; ++j)
                    {
                        if(j == 0 || j == 1 || j == 3)
                            list.add(3333);
                        
                        else
                            list.add(0);
                    }
                    break;
                    
                case 2:
                    for(int j=0 ; j < 6 ; ++j)
                    {
                        if(j == 3)
                            list.add(0);
                        
                        else
                            list.add(2000);
                    }
                    break;
                    
                case 3:
                    for(int j=0 ; j < 6 ; ++j)
                    {
                        if(j == 0 || j == 4)
                            list.add(0);
                        
                        else
                            list.add(2500);
                    }
                    break;
                    
                case 4:
                    for(int j=0 ; j < 6 ; ++j)
                    {
                        if(j == 0 || j == 1)
                            list.add(0);
                        
                        else
                        {
                            list.add(2500);
                        }
                    }
                    break;
                    
                case 5:
                    for(int j=0 ; j < 6 ; ++j)
                    {
                        if(j == 0 || j == 2 || j == 3)
                            list.add(0);
                        
                        else
                            list.add(3333);
                    }
                    break;
            }
        }
    }

    /**
     * @return Value of the key signature
     */
    public String getKey()
    {
        return score.getKey();
    }

    /**
     * @return Returns value of tempo
     */
    public int getTempo()
    {
        return score.getTempo();
    }

    /**
     * @return The generated score
     */
    public Pattern getScore()
    {
        return score.getScore();
    }
    
    public Score getScoreT()
    {
        return score;
    }
    
    /**
     * @return The current chord progression represented as an array of ints
     */
    public int[] getChordProgression()
    {
        return chords;
    }

    /**
     * Sets the value of keySig and the key of the analyzer to the integer accepted. This forces the analyzer to reset and analyze MIDI files in the new key.
     * 
     * @param key the new key signature
     */
    public void setKey(int key)
    {
        if (key >= 1 && key <= 12)
        {
            if (key != score.getKeyInt())
            {
                score.setKey(key);
                analyzer.setKey(key);
            }
        }
        else
        {
            score.setKey(1);
            analyzer.setKey(1);
        }
    }

    /**
     * Sets the value of tempo to the integer accepted.
     * 
     * @param tempo the new tempo
     */
    public void setTempo(int tempo)
    {
        score.setTempo(tempo);
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
     * 
     * @param production the L-System production to be parsed
     * @param markov indicates whether to use the Markov chain method
     * @param order indicates which order markov chain to utilize
     */
    public void genScore(String production, boolean markov, int order)
    {
        score.resetScore();
        String pat = "";
        beat = 0;

        switch (score.getKeyInt())
        {
            case 1:
                pat = generate(production, 48, markov, order);
                break;

            case 2:
                pat = generate(production, 55, markov, order);
                break;

            case 3:
                pat = generate(production, 50, markov, order);
                break;

            case 4:
                pat = generate(production, 57, markov, order);
                break;

            case 5:
                pat = generate(production, 52, markov, order);
                break;

            case 6:
                pat = generate(production, 59, markov, order);
                break;

            case 7:
                pat = generate(production, 54, markov, order);
                break;

            case 8:
                pat = generate(production, 49, markov, order);
                break;

            case 9:
                pat = generate(production, 56, markov, order);
                break;

            case 10:
                pat = generate(production, 51, markov, order);
                break;

            case 11:
                pat = generate(production, 58, markov, order);
                break;

            case 12:
                pat = generate(production, 53, markov, order);
                break;
        }

        Pattern pattern = new Pattern(pat);

        score.setScore(pattern);
    }

    /**
     * Accepts a production string and 4 integers which indicate where half steps should be made to keep music in key; generates music from the production.
     * 
     * @param production the L-System production to be parsed
     * @param tonic the tonic of the current key signature
     * @param markov indicates whether to use the Markov chain method
     * @return The music score as a string.
     */
    private String generate(String production, int tonic, boolean markov, int order)
    {
        StringBuffer buffer = new StringBuffer("T" + score.getTempo() + " V0 ");	// Stores the score string that will be returned
        char[] prod = production.toCharArray();										// Array of characters from the production
        char[] rhythm = rhythmGen.genRhythm();
        int color = turtle.getColor();
        genChordProgression();
        turtle.popY();
        turtle.pushY(tonic);
        int c = 0;
        int r = 0;
        
        for(int x: chords)
            System.out.print(x + " ");
        
       System.out.println();

        // Step through each symbol in production
        for (int i = 0; i < prod.length; ++i)
        {
            /*if(beat % 256 == 32)
            {
                writeChord(buffer, chords[c], rhythm[r]);
                ++r;
                ++c;
            }*/
            
            switch (prod[i])
            {
            // Increment turtle's yaw
                case '-':
                    turtle.pushYaw(turtle.popYaw() + turtle.getAngle());
                    break;

                // Decrement turtle's yaw
                case '+':
                    turtle.pushYaw(turtle.popYaw() - turtle.getAngle());
                    break;

                // Turtle draws a line
                case 'g':
                    if (!markov)
                    {
                        if (beat % 128 == 0 && buffer.toString().matches(".*\\[\\d*\\]s+"))
                            buffer.append(" |");

                        buffer = drawLine(buffer, true);
                    }

                    else
                    {
                        if (beat % 128 == 0 && r != 0)
                        {
                            buffer.append(" |");
                            rhythm = rhythmGen.genRhythm();
                            r = 0;
                        }

                        if(beat % chordInterval == 0 && rhythm.length <= 4)
                        {
                            c = c % chords.length;
                            writeChord(buffer, chords[c], rhythm[r]);
                            ++c;
                        }
                        
                        else if(beat % chordInterval == 0)
                        {
                            buffer.append(" V1 Rh V2 Rh V0");
                            buffer = drawMarkov(buffer, true, order, rhythm[r]);
                        }
                        
                        else
                        {
                            buffer = drawMarkov(buffer, true, order, rhythm[r]);
                        }
                        
                        ++r;
                    }
                    break;

                // Turtle moves without drawing
                case 'f':
                    if (!markov)
                    {
                        if (beat % 128 == 0 && buffer.toString().matches(".*\\[\\d*\\]s+"))
                            buffer.append(" |");

                        buffer = drawLine(buffer, false);
                    }

                    else
                    {
                        if (beat % 128 == 0 && r != 0)
                        {
                            buffer.append(" |");
                            rhythm = rhythmGen.genRhythm();
                            r = 0;
                        }
                        
                        if(beat % chordInterval == 0 && rhythm.length <= 4)
                        {
                            c = c % chords.length;
                            writeChord(buffer, chords[c], rhythm[r]);
                            ++c;
                        }
                        
                        else if(beat % 64 == 0)
                        {
                            buffer.append(" V1 Rh V2 Rh V0");
                            buffer = drawMarkov(buffer, true, order, rhythm[r]);
                        }
                        
                        else
                        {
                            buffer = drawMarkov(buffer, true, order, rhythm[r]);
                        }

                        ++r;
                    }
                    break;

                case 'r':
                    int direction = turtle.getDirection();

                    // If turtle is horizontal, record line as a rest
                    if (direction == 1 || direction == 3)
                    {
                        String str = buffer.toString();
                        String regex = ".*Rs+";

                        if (str.matches(regex))
                            buffer.append("s");

                        else
                            buffer.append(" Rs");
                    }
                    break;

                case '[':
                    int voices = score.getVoices();
                    int layers = score.getLayers();

                    if (voices < 16)
                    {
                        if (layers < 16)
                        {
                            turtle.saveState();
                            buffer.append(" L" + layers + " I80 ");
                            score.setLayers(layers + 1);
                        }

                        else
                        {
                            turtle.saveState();
                            buffer.append(" V" + voices + " I80 ");
                            score.setVoices(voices + 1);
                            score.setLayers(1);
                        }
                    }
                    break;

                case ']':
                    voices = score.getVoices();
                    layers = score.getLayers();

                    if (voices >= 1)
                    {
                        if (layers > 1)
                        {
                            turtle.restoreState();
                            score.setLayers(--layers);
                            buffer.append(" L" + (layers - 1) + " ");
                        }

                        else if (voices > 1)
                        {
                            turtle.restoreState();
                            score.setVoices(--voices);
                            score.setLayers(16);
                            layers = 16;
                            buffer.append(" V" + (voices - 1) + " " + " L" + (layers - 1) + " ");
                        }
                    }
                    break;

                case '#':
                    color = turtle.popColor();
                    int hueChange = turtle.getHueChange();
                    turtle.pushColor(color + hueChange);

                    if (prod[i + 2] != '#' && prod[i + 2] != '@')
                        buffer.append(" X1=" + (750 - turtle.getColor()) / 3);
                    break;

                case '@':
                    color = turtle.popColor();
                    int hueC = turtle.getHueChange();
                    turtle.pushColor(color - hueC);

                    if (prod[i + 2] != '#' && prod[i + 2] != '@')
                        buffer.append(" X1=" + (750 - turtle.getColor()) / 3);
                    break;
            }
        }
        return buffer.toString();
    }

    /**
     * Simulates drawing a line with the turtle and maps the action directly onto the music score.
     * 
     * @param buffer stores the music score as it is being built
     * @param draw is the note to be treated as a tie
     * @return The StringBuffer with the necessary modifications made.
     */
    private StringBuffer drawLine(StringBuffer buffer, boolean draw)
    {
        int pitch = turtle.getY();
        int direction = turtle.getDirection();

        // If turtle is horizontal, record line as a note
        if ((direction == 1 || direction == 3) && draw)
        {
            String str = buffer.toString();
            String regex = ".*\\[" + pitch + "\\]s+";

            if (str.matches(regex))
                buffer.append("s");

            else
                buffer.append(" [" + pitch + "]s");

            beat += 8;
        }

        else if (direction == 1 || direction == 3)
            buffer.append(" [" + pitch + "]s");

        // If turtle facing upward, record line as a change up in pitch
        else if (direction == 2)
        {
            double degree = score.getDegree();
            int newnote = -1;

            if (degree == 3 || degree == 7 || degree % 1.0 == 0.5)
            {
                newnote = score.upHalfStep(pitch);
                score.setNotePitch(newnote);
                turtle.popY();
                turtle.pushY(newnote);
            }

            else
            {
                newnote = score.upWholeStep(pitch);
                score.setNotePitch(newnote);
                turtle.popY();
                turtle.pushY(newnote);
            }

            if (newnote > upperBound)
            {
                turtle.popY();
                turtle.pushY(newnote - 24);
                score.setNotePitch(newnote - 24);
            }
        }

        // If turtle facing downward, record line as a change down in pitch
        else if (direction == 4)
        {
            double degree = score.getDegree();
            int newnote = -1;

            if (degree == 1 || degree == 4 || degree % 1.0 == 0.5)
            {
                newnote = score.downHalfStep(pitch);
                score.setNotePitch(newnote);
                turtle.popY();
                turtle.pushY(newnote);
            }

            else
            {
                newnote = score.downWholeStep(pitch);
                score.setNotePitch(newnote);
                turtle.popY();
                turtle.pushY(newnote);
            }

            if (newnote < lowerBound)
            {
                turtle.popY();
                turtle.pushY(newnote + 24);
                score.setNotePitch(newnote + 24);
            }
        }

        return buffer;
    }

    /**
     * Uses L-System as a guide instead of mapping the system directly onto the score. Uses a first-order Markov Chain to choose notes as the system progresses.
     * 
     * @param buffer stores the music score as it is being built
     * @param draw whether the note is to be treated as a tie
     * @return The StringBuffer with the necessary modifications made.
     */
    private StringBuffer drawMarkov(StringBuffer buffer, boolean draw, int order, char duration)
    {
        int pitch = turtle.getY();
        int direction = turtle.getDirection();
        int originalnote = score.getNote();
        int note = originalnote;
        int nextnote = -1;
        
        switch (duration)
        {
            case '0':
                buffer.append(" Ro");
                beat += 1;
                return buffer;

            case '1':
                buffer.append(" Rx");
                beat += 2;
                return buffer;

            case '2':
                buffer.append(" Rt");
                beat += 4;
                return buffer;

            case '3':
                buffer.append(" Rs");
                beat += 8;
                return buffer;

            case '4':
                buffer.append(" Ri");
                beat += 16;
                return buffer;

            case '5':
                buffer.append(" Rq");
                beat += 32;
                return buffer;

            case '6':
                buffer.append(" Rh");
                beat += 64;
                return buffer;

            case '7':
                buffer.append(" Rw");
                beat += 128;
                return buffer;
        }

        // If turtle is horizontal, go in direction that minimizes the distance between the previous note and new note.
        if ((direction == 1 || direction == 3) && draw)
        {
            if (note == -1)
            {
                score.setNotePitch(pitch);
                originalnote = score.getNote();
                note = originalnote;
            }

            if (order == 1)
                nextnote = getFirstOrderNote(note);

            else
                nextnote = getSecondOrderNote(score.getPrevNote(), note);

            if (nextnote != -1)
            {
                pitch = score.findClosestPitch(nextnote, pitch, lowerBound, upperBound);

                turtle.popY();
                turtle.pushY(pitch);
            }

            buffer.append(" [" + pitch + "]" + duration);
            score.setNote(note);
        }

        else if (direction == 1 || direction == 3)
        {
            if (note == -1)
            {
                score.setNotePitch(pitch);
                note = score.getNote();
            }
            
            if (order == 1)
                nextnote = getFirstOrderNote(note);

            else
                nextnote = getSecondOrderNote(score.getPrevNote(), note);

            if (nextnote != -1)
            {
                pitch = score.findClosestPitch(nextnote, pitch, lowerBound, upperBound);

                turtle.popY();
                turtle.pushY(pitch);
            }

            buffer.append(" [" + pitch + "]" + duration);
            score.setNote(note);
        }

        // If turtle facing upward, record line as a change up in pitch
        else if (direction == 2)
        {
            if (note == -1)
            {
                score.setNotePitch(pitch);
                note = score.getNote();
            }

            if (order == 1)
                nextnote = getFirstOrderNote(note);

            else
                nextnote = getSecondOrderNote(score.getPrevNote(), note);

            if (nextnote != -1)
            {
                while (note != nextnote)
                {
                    note = score.upHalfStep(note);

                    if (note > 11)
                        note = 0;

                    pitch = score.upHalfStep(pitch);

                    if (pitch > upperBound)
                        pitch -= 24;
                }

                score.setNote(note);
                turtle.popY();
                turtle.pushY(pitch);
            }

            buffer.append(" [" + pitch + "]" + duration);
        }

        // If turtle facing downward, record line as a change down in pitch
        else if (direction == 4)
        {
            if (note == -1)
            {
                score.setNotePitch(pitch);
                note = score.getNote();
            }

            if (order == 1)
                nextnote = getFirstOrderNote(note);

            else
                nextnote = getSecondOrderNote(score.getPrevNote(), note);

            if (nextnote != -1)
            {
                while (note != nextnote)
                {
                    note = score.downHalfStep(note);

                    if (note < 0)
                        note = 11;

                    pitch = score.downHalfStep(pitch);

                    if (pitch < lowerBound)
                        pitch += 24;
                }

                turtle.popY();
                turtle.pushY(pitch);
            }

            buffer.append(" [" + pitch + "]" + duration);
        }

        switch (duration)
        {
            case 'o':
                beat += 1;
                break;

            case 'x':
                beat += 2;
                break;

            case 't':
                beat += 4;
                break;

            case 's':
                beat += 8;
                break;

            case 'i':
                beat += 16;
                break;

            case 'q':
                beat += 32;
                break;

            case 'h':
                beat += 64;
                break;

            case 'w':
                beat += 128;
                break;
        }

        return buffer;
    }
    
    /**
     * Writes desired chord to the StringBuffer passed.
     * @param buffer  buffer to be written to
     * @param chordDegree  chord to write to buffer
     * @param duration  duration of the chord
     * @return  Modified buffer.
     */
    public StringBuffer writeChord(StringBuffer buffer, int chordDegree, char duration)
    {
        int[] chord = score.getChord(chordDegree+1);
        int note1 = chord[0];
        int pitch1 = turtle.getY();
        int pitch2 = pitch1;
        int pitch3 = pitch2;
        double restingtime = 64;
        String rests = "";
        
        switch (duration)
        {
            case 'o':
                beat += 1;
                restingtime -= 1;
                break;

            case 'x':
                beat += 2;
                restingtime -= 2;
                break;

            case 't':
                beat += 4;
                restingtime -= 4;
                break;

            case 's':
                beat += 8;
                restingtime -= 8;
                break;

            case 'i':
                beat += 16;
                restingtime -= 16;
                break;

            case 'q':
                beat += 32;
                restingtime -= 32;
                break;

            case 'h':
                beat += 64;
                restingtime -= 64;
                break;

            case 'w':
                beat += 128;
                restingtime -= 128;
                break;
                
            case '0':
                duration = 'o';
                beat += 1;
                restingtime -= 1;
                break;

            case '1':
                duration = 'x';
                beat += 2;
                restingtime -= 2;
                break;

            case '2':
                duration = 't';
                beat += 4;
                restingtime -= 4;
                break;

            case '3':
                duration = 's';
                beat += 8;
                restingtime -= 8;
                break;

            case '4':
                duration = 'i';
                beat += 16;
                restingtime -= 16;
                break;

            case '5':
                duration = 'q';
                beat += 32;
                restingtime -= 32;
                break;

            case '6':
                duration = 'h';
                beat += 64;
                restingtime -= 64;
                break;

            case '7':
                duration = 'w';
                beat += 128;
                restingtime -= 128;
                break;
        }
        
        while(restingtime != 0)
        {
            if(restingtime >= 128)
            {
                rests += "Rw";
                restingtime -= 128;
            }
            
            else if(restingtime >= 64)
            {
                while(restingtime >= 64)
                {
                    rests += "Rh";
                    restingtime -= 64;
                }
            }
            else if(restingtime >= 32)
            {
                while(restingtime >= 32)
                {
                    rests += " Rq";
                    restingtime -= 32;
                }
            }
            
            else if(restingtime >= 16)
            {
                while(restingtime >= 16)
                {
                    rests += " Ri";
                    restingtime -= 16;
                }
            }
            
            else if(restingtime >= 8)
            {
                while(restingtime >= 8)
                {
                    rests += " Rs";
                    restingtime -= 8;
                }
            }
            
            else if(restingtime >= 4)
            {
                while(restingtime >= 4)
                {
                    rests += " Rt";
                    restingtime -= 4;
                }
            }
            
            else if(restingtime >= 2)
            {
                while(restingtime >= 2)
                {
                    rests += " Rx";
                    restingtime -= 2;
                }
            }
            
            else if(restingtime >= 1)
            {
                while(restingtime >= 1)
                {
                    rests += " Ro";
                    restingtime -= 1;
                }
            }
        }

        pitch1 = score.findClosestPitch(note1, pitch1, lowerBound, upperBound);
        
        if(pitch1 + 7 <= upperBound)
        {
            pitch2 = pitch1 + 4;
            pitch3 = pitch2 + 3;
        }
        
        else
        {
            pitch1 -= 12;
            pitch2 = pitch1 + 4;
            pitch3 = pitch2 + 3;
        }
        
        if(pitch1 > pitch2 && pitch1 > pitch3)
        {
            buffer.append(" [" + pitch1 + "]" + duration);
            
            if(pitch2 > pitch3)
            {
                buffer.append(" V1 [" + pitch2 + "] " + duration + rests);
                buffer.append(" V2 [" + pitch3 + "] " + duration + rests);
            }
            
            else
            {
                buffer.append(" V1 [" + pitch3 + "]" + duration + rests);
                buffer.append(" V2 [" + pitch2 + "]" + duration + rests);
            }
        }
        
        else if(pitch2 > pitch1 && pitch2 > pitch3)
        {
            buffer.append(" [" + pitch2 + "]" + duration);
            
            if(pitch1 > pitch3)
            {
                buffer.append(" V1 [" + pitch1 + "]" + duration + rests);
                buffer.append(" V2 [" + pitch3 + "]" + duration + rests);
            }
            
            else
            {
                buffer.append(" V1 [" + pitch3 + "]" + duration + rests);
                buffer.append(" V2 [" + pitch1 + "]" + duration + rests);
            }
        }
        
        else
        {
            buffer.append(" [" + pitch3 + "]" + duration);
            
            if(pitch1 > pitch2)
            {
                buffer.append(" V1 [" + pitch1 + "]" + duration + rests);
                buffer.append(" V2 [" + pitch2 + "]" + duration + rests);
            }
            
            else
            {
                buffer.append(" V1 [" + pitch2 + "]" + duration + rests);
                buffer.append(" V2 [" + pitch1 + "]" + duration + rests);
            }
        }
        
        buffer.append(" V0");
        
        return buffer;
    }

    /**
     * Generates chord progressions for Major scales for use in the underlying harmonic structure of the composition.
     */
    public void genChordProgression()
    {
        ArrayList<Integer> chordProg = new ArrayList<Integer>();
        chordProg.add(0);
        boolean complete = false;
        int chord = 0;
        int rand = 0;
        
        while(!complete)
        {
            int count = 0;
            rand = (int)Math.floor(Math.random() * 10000);
            
            switch(chord)
            {
                // I Chord
                case 0:
                    for(int i=1667 ; i <= 10002 ; i += 1667)
                    {
                        if(rand <= i)
                        {
                            chordProg.add(count);
                            break;
                        }
                        
                        ++count;
                    }
                    break;
                    
                // V Chord
                case 1:
                    for(int i=3333 ; i <= 9999 ; i += 3333)
                    {
                        if(rand <= i)
                        {
                            chordProg.add(count);
                            break;
                        }
                        
                        if(count == 1)
                            count += 2;
                        
                        else
                            ++count;
                    }
                    
                    if(rand > 9999)
                    {
                        int last = chordProg.size()-1;
                        chordProg.add(last);
                    }
                    
                    break;

                // IV Chord
                case 2:
                    for(int i=2000 ; i <= 10000 ; i += 2000)
                    {
                        if(rand <= i)
                        {
                            chordProg.add(count);
                            break;
                        }
                        
                        if(count == 2)
                            count += 2;
                    }
                    break;
                
                // vi Chord
                case 3:
                    count = 1;
                    
                    for(int i=2500 ; i <= 10000 ; i += 2500)
                    {
                        if(rand <= i)
                        {
                            chordProg.add(count);
                            break;
                        }
                        
                        if(count == 3)
                            count += 2;
                    }
                    break;
                    
                // iii Chord
                case 4:
                    count = 2;
                    
                    for(int i=2500 ; i <= 10000 ; i += 2500)
                    {
                        if(rand <= i)
                        {
                            chordProg.add(count);
                            break;
                        }
                        
                        ++count;
                    }
                    break;
                    
                // ii Chord
                case 5:
                    count = 1;
                    
                    for(int i=3333 ; i <= 9999 ; i += 3333)
                    {
                        if(rand <= i)
                        {
                            chordProg.add(count);
                            break;
                        }
                        
                        if(count == 1)
                            count += 3;
                    }
                    
                    if(rand > 9999)
                    {
                        int last = chordProg.size()-1;
                        chordProg.add(last);
                    }
                    
                    break;
            }
            
            // If Last chord was the I chord or size of the chord progression is >= 10, progression may be finished
            if(chordProg.lastIndexOf(0) == chordProg.size()-1 || chordProg.size() >= 10)
            {
                int last = chordProg.size()-1;
                
                if(chordProg.get(last) != 0)
                {
                    chordProg.add(0);
                    complete = true;
                }
                
                else
                {
                    for(int i=1 ; i < 6 ; ++i)
                    {
                        if(chordProg.contains(i))
                            complete = true;
                    }
                }
            }
        }
        
        chords = new int[chordProg.size()];
        
        for(int i=0 ; i < chordProg.size(); ++i)
            chords[i] = chordProg.get(i);
    }

    /**
     * Randomly chooses a note based on the first-order probabilities provided by MusicAnalyzer.
     * 
     * @param note the last note in the score
     * @return The next note to be recorded represented as an integer.
     */
    private int getFirstOrderNote(int note)
    {
        ArrayList<Double> list = analyzer.getProbability(note);
        double range = 0.0;
        double rand = Math.random();

        for (int l = 0; l < list.size(); ++l)
        {
            range += list.get(l);

            if (rand <= range)
                return l;
        }

        return -1;
    }

    /**
     * Randomly chooses a note based on the second-order probabilities provided by MusicAnalyzer.
     * 
     * @param prevnote the second to last note in the score
     * @param note the last note in the score
     * @return The next note to be recorded represented as an integer.
     */
    private int getSecondOrderNote(int prevnote, int note)
    {
        ArrayList<Double> list = analyzer.getSecondProb(prevnote, note);
        double range = 0.0;
        double rand = Math.random();

        for (int l = 0; l < list.size(); ++l)
        {
            range += list.get(l);

            if (rand <= range)
                return l;
        }

        return -1;
    }

    /**
     * Adds the string passed as a separate part for the passed instrument in the composition.
     * 
     * @param voice Part to add
     * @param instrument Instrument that will play the part
     */
    public void addPart(String voice, int instrument)
    {
        score.appendPart(voice, instrument);
    }

}
