import java.util.*;

/**
 *  @author         Kristopher W. Reese <http://kreese.net>
 *  @contrib        Douglas Ferguson <>
 *
 *  This is a class used for maximizing the distance between pulses
 *  using Boolean values.  In this class, a truth is a pulse, a false
 *  is a pause.  This idea is based on the Bjorklund algorithm, but uses
 *  modular arithmetic to acheive the same effects.  
 *
 *  Special Thanks to Douglas Ferguson for helping to recognize and fix
 *  several errors in the original code.
 */
public class Bjorklund {
	private ArrayList<Boolean> rhythm = new ArrayList<Boolean>();
	int pauses, per_pulse, remainder, steps, pulses, noskip, skipXTime;
	boolean switcher;
    
    /**
     *  This is the primary constructor for the Bjorklund Class.
     *  this initializes the Bjorklund rhythm.  This takes care
     *  of determining possible issues which can cause some issues
     *  when building the rhythm on-the-fly.  
     *
     *  @param pulses   The total number of pulses in the system we are smoothing
     *  @param steps    The total number of steps in the system
     */
	public Bjorklund(int pulses, int steps) {
		this.steps = steps;
		this.pulses = pulses;
		this.pauses = steps - pulses;
		this.switcher = false;
		if (this.pulses > this.pauses) {
			this.switcher = true;
			// XOR swap pauses and pulses
			this.pauses ^= this.pulses;
			this.pulses ^= this.pauses;
			this.pauses ^= this.pulses;
		}
		this.per_pulse = (int) Math.floor(this.pauses / this.pulses);
		this.remainder = this.pauses % this.pulses;
		this.noskip = (this.remainder == 0) ? 0 : (int) Math.floor(this.pulses / this.remainder);
        this.skipXTime = (this.noskip == 0) ? 0 : (int)Math.floor((this.pulses - this.remainder)/this.noskip);
        
		this.buildRhythm();
        
        if(this.switcher) {
            // XOR swap pauses and pulses
            this.pauses ^= this.pulses;
            this.pulses ^= this.pauses;
            this.pauses ^= this.pulses;
        }
	}
    
    /**
     *  This is the secondary constructor for people who generate a rhythm
     *  by hand and want the Bjorklund to autorotate its generated rhythm 
     *  to the rhythm expected.
     *
     *  Expected Rhythm should follow Douglas Ferguson's rhythm string which
     *  was created based on the strings in Gottfried Toussaint's paper. A x (lowercase x) 
     *  represents a pulse and a . represents a pause.  There should be spaces 
     *  between each step.  Therefore if we want a specific rhythm from Bjorklund 
     *  E(5,13), we can write:
     *
     *      x . . x . x . . x . x . .
     *
     *  and the algorithm with autorotate to this rhythm.
     *
     *  @param pulses   The total number of pulses in the system we are smoothing
     *  @param steps    The total number of steps in the system
     *  @param expected The expected rhythm that we will autorotate to
     */
    public Bjorklund(int pulses, int steps, String expected) {
        this(pulses, steps);
        autorotate(expected);
    }
    
    /**
     *  This is a private function which is called from the constructor.
     *  It is used to build the rhythm based on the Bjorklund smoothing
     *  algorithm.
     */
	private void buildRhythm() {
        int count = 0;
        int skipper = 0;
		for (int i = 1; i <= this.steps; i++) {
			if (count == 0) {
                this.rhythm.add(!this.switcher);
                count = this.per_pulse;
                
                if (this.remainder > 0 && skipper == 0) {
					count++;
					this.remainder--;
                    skipper = (this.skipXTime > 0) ? this.noskip : 0;
                    this.skipXTime--;
                } else {
                    skipper--;
                }
			} else {
				this.rhythm.add(this.switcher);
				count--;
			}
		}
	}
    
    /**
     *  Returns the generated rhythm.
     *
     *  @return ArrayList<Boolean> Rhythm   This returns the generated Rhythm
     */
	public ArrayList<Boolean> getRhythm() {
		return this.rhythm;
	}
    
    /**
     *  Returns the total number of steps in the rhythm.
     *
     *  @return size of the rhythm
     */
    public int getRhythmSize() {
        return this.rhythm.size();
    }
    
    /**
     *  This function will rotate a rhythm to a specific string:
     *
     *  Expected Rhythm should follow Douglas Ferguson's rhythm string which
     *  was created based on the strings in Gottfried Toussaint's paper. A x (lowercase x) 
     *  represents a pulse and a . represents a pause.  There should be spaces 
     *  between each step.  Therefore if we want a specific rhythm from Bjorklund 
     *  E(5,13), we can write:
     *
     *      x . . x . x . . x . x . .
     *
     *  and the algorithm with autorotate to this rhythm.
     *
     *  @param expected The expected rhythm that we will autorotate to
     */
    public void autorotate(String expected) {
        boolean verified = false;
        int size = this.rhythm.size();
        int rotate = 1;
        this.rotateRightByPulses(0);
        String found = this.getRhythmString();
        while(!found.equals(expected) || rotate < this.pulses) {
            this.rotateRightByPulses(1);
            found = this.getRhythmString();
            if(found.equals(expected)){
                verified = true;
                break;
            }
        }
        
        if(!verified) {
            System.err.println("Rhythmic string passed cannot be generated from E("+this.pulses+","+this.steps+")");
        }
        
    }
    
    /**
     *  Rotates the rhythm by a specified number of bits, whether 
     *  it is a pulse or a pause.
     *
     *  @param numBits  The total number of bits to rotate by
     */
	public void rotateRightByBits(int numBits) {
		Collections.rotate(this.rhythm, numBits);
	}
    
    /**
     *  Rotates the rhythm by a specific number of pulses.  It will
     *  rotate groups of pulses.
     *
     *  @param numPulses    Rotates groups of pulses this many times.
     */
	public void rotateRightByPulses(int numPulses) {
		for (int i = 0; i < numPulses; i++) {
			int rotater = this.rhythm.size() - 1;
			int count = 1;
			while (this.rhythm.get(rotater) == false) {
				rotater--;
				count++;
			}
			this.rotateRightByBits(count);
		}
	}
    
    /**
     *  @author Douglas Ferguson
     *
     *  returns a string created based on the strings in Gottfried Toussaint's 
     *  paper. an X represents a pulse and a . represents a pause.  There should 
     *  be spaces between each step.  Therefore the string:
     *
     *      X . . X . X . . X . X . .
     *
     *  represents a specific rotation of the E(5,13) rhythm.
     *
     *  @return rhythmic string
     */
    private String getRhythmString(){
    	Iterator<Boolean> iterator = this.rhythm.iterator();
    	StringBuffer buffer = new StringBuffer();
    	while(iterator.hasNext()){
    		buffer.append(iterator.next() ? "x" : ".");
    		if(iterator.hasNext()){
    			buffer.append(" ");
    		}
    	}
    	return buffer.toString();
    }
    
    /**
     *  @author Douglas Ferguson
     *
     *  Prints information to the screen about the Bjorklund 
     *  rhythmic generation.  Includes the number of pulses,
     *  number of steps in the system, and the rhythm generated.
     */
	public void print() {
    	System.out.println(this.pulses + ":" + this.steps +" -> ");
    	System.out.print(this.getRhythmString());
    	System.out.println();
	}
    
    /**
     *  Based on some work from @author Douglas Ferguson where verfication 
     *  functions were originally created by Douglas Ferguson.  This function
     *  can be used to test whether the class is generating acceptable rhythms 
     *  or not.
     *
     *  Expected Rhythm should follow Douglas Ferguson's rhythm string which
     *  was created based on the strings in Gottfried Toussaint's paper. A x (lowercase x) 
     *  represents a pulse and a . represents a pause.  There should be spaces 
     *  between each step.  Therefore if we want a specific rhythm from Bjorklund 
     *  E(5,13), we can write:
     *
     *      x . . x . x . . x . x . .
     *
     *  and the algorithm with autorotate to this rhythm if possible, otherwise
     *  it will print out an error stating that the rhythm is not acceptable.  
     *  Before sending bug reports, be sure the rhythm you type in contains the same
     *  number of pulses and steps as the generated rhythm requested.
     *
     *  @param expected The expected rhythm that we will autorotate to
     */
    public void autoverify(String expected) {
        boolean verified = false;
        int size = this.rhythm.size();
        int rotate = 1;
        this.rotateRightByBits(0);
        String found = this.getRhythmString();
        while(!found.equals(expected) || rotate < size) {
            this.rotateRightByBits(1);
            found = this.getRhythmString();
            if(found.equals(expected)){
                System.out.println("E("+this.pulses+","+this.steps+") verified for <<" + found + ">> by rotating bits right "+rotate+" times");
                verified = true;
                break;
            }
            rotate++;
        }
        
        if(verified == false)
        {
            System.err.println("missed E("+this.pulses+","+this.steps+") expected: <<"+ expected + ">> but found: <<"+found+">>");
        }
    }
}