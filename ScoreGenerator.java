import org.jfugue.Pattern;
import java.util.ArrayList;

/**
 * Generates a properly formatted score for use in JFugue from an L-system production.
 * 
 * @author Harry Allen
 */
public class ScoreGenerator
{

    private Turtle          turtle;           // Turtle to keep track of "drawing" actions
    private MusicAnalyzer   analyzer;         // Analyzes MIDI files and generates a first-order Markov chain for all notes on the Western Scale
    private Score           score;            // Stores the music score and related information
    private RhythmGenerator rhythmGen;        // Generates rhythms to be implemented into the melody or accompaniment
    private double          beat;             // Keeps track of the beat to determine when to place measure marker and generate a new rhythm
    private int             lowerBound;       // Lower bound of note pitch
    private int             upperBound;       // Upper bound of note pitch

    /**
     * Default Constructor.
     */
    public ScoreGenerator()
    {
        analyzer = new MusicAnalyzer();
        turtle = new Turtle();
        score = new Score();
        rhythmGen = new RhythmGenerator();
        beat = 1.0;
        upperBound = 95;
        lowerBound = 36;
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
        turtle.pushAngle(angle);
        beat = 1.0;
        upperBound = 95;
        lowerBound = 36;
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
        beat = 1;

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
        StringBuffer buffer = new StringBuffer("T" + score.getTempo() + " V0 I80 ");	// Stores the score string that will be returned
        int color = turtle.getColor();
        char[] prod = production.toCharArray();											// Array of characters from the production
        turtle.popY();
        turtle.pushY(tonic);
        char[] rhythm = rhythmGen.genRhythm();
        int r = 0;

        // Step through each symbol in production
        for (int i = 0; i < prod.length; ++i)
        {
            if (beat % 4.0 == 1.0 && r != 0)
            {
                buffer.append(" |");
                System.out.println("---New Rhythm---\r\n");
                rhythm = rhythmGen.genRhythm();
                r = 0;
            }

            // r = r % rhythm.length;

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
                        buffer = drawLine(buffer, true);

                    else
                        buffer = drawMarkov(buffer, true, order, rhythm[r]);

                    ++r;
                    break;

                // Turtle moves without drawing
                case 'f':
                    if (!markov)
                        buffer = drawLine(buffer, false);

                    else
                        buffer = drawMarkov(buffer, false, order, rhythm[r]);

                    ++r;
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
                beat += 0.03125;
                return buffer;

            case '1':
                buffer.append(" Rx");
                beat += 0.0625;
                return buffer;

            case '2':
                buffer.append(" Rt");
                beat += 0.125;
                return buffer;

            case '3':
                buffer.append(" Rs");
                beat += 0.25;
                return buffer;

            case '4':
                buffer.append(" Ri");
                beat += 0.5;
                return buffer;

            case '5':
                buffer.append(" Rq");
                beat += 1.0;
                return buffer;

            case '6':
                buffer.append(" Rh");
                beat += 2.0;
                return buffer;

            case '7':
                buffer.append(" Rw");
                beat += 4.0;
                return buffer;
        }

        // If turtle is horizontal, no note change occurs
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
                int distance = 0;
                int distance2 = 0;

                while (note != nextnote)
                {
                    note = score.downHalfStep(note);

                    if (note < 0)
                        note = 11;

                    ++distance;
                }

                note = originalnote;

                while (note != nextnote)
                {
                    note = score.upHalfStep(note);

                    if (note > 11)
                        note = 0;

                    ++distance2;
                }

                note = originalnote;

                if (distance < distance2)
                {
                    while (distance != 0)
                    {
                        note = score.downHalfStep(note);

                        if (note < 0)
                            note = 11;

                        pitch = score.downHalfStep(pitch);

                        if (pitch < lowerBound)
                            pitch += 24;

                        --distance;
                    }
                }

                else
                {
                    while (distance2 != 0)
                    {
                        note = score.upHalfStep(note);

                        if (note > 11)
                            note = 0;

                        pitch = score.upHalfStep(pitch);

                        if (pitch > upperBound)
                            pitch -= 24;

                        --distance2;
                    }
                }

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
                beat += 0.03125;
                break;

            case 'x':
                beat += 0.0625;
                break;

            case 't':
                beat += 0.125;
                break;

            case 's':
                beat += 0.25;
                break;

            case 'i':
                beat += 0.5;
                break;

            case 'q':
                beat += 1.0;
                break;

            case 'h':
                beat += 2.0;
                break;

            case 'w':
                beat += 4.0;
                break;
        }

        return buffer;
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

    public void addPart(String voice, int instrument)
    {
        score.appendPart(voice, instrument);
    }
}
