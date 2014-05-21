/** 
 * Turtle to simulate drawing to be interpreted, producing a musical score to be played
 * @author Harry Allen
 */
import java.util.Stack;

public class Turtle
{
	private Stack<Integer> X;				//Turtle's position on the X-axis
	private Stack<Integer> Y;				//Turtle's position on the Y-axis
	private Stack<Integer> Z;				//Turtle's position on the Z-axis
	private Stack<Integer> yaw;				//Turtle's heading
	private Stack<Integer> angle;			//Turtle's angle increment/decrement value
	private Stack<Integer> color;			//Turtle's draw color. Based on visible light spectrum wavelengths: 380-750 nm
	private Stack<Integer> hueChange;		//Turtle's color increment/decrement value
	private Stack<Integer> thickness;		//Turtle's draw thickness
	
	/** Default Constructor. */
	public Turtle()
	{
		X = new Stack<Integer>();
		Y = new Stack<Integer>();
		Z = new Stack<Integer>();
		yaw = new Stack<Integer>();
		angle = new Stack<Integer>();
		color = new Stack<Integer>();
		hueChange = new Stack<Integer>();
		thickness = new Stack<Integer>();
		
		X.push(0);
		Y.push(0);
		Z.push(0);
		yaw.push(0);
		angle.push(90);
		color.push(565);
		hueChange.push(10);
		thickness.push(50);
	}
	
	/** Returns turtle's current X-axis position. */
	public int getX()
	{
		return X.peek().intValue();
	}
	
	/** Returns turtle's current Y-axis position. */
	public int getY()
	{
		return Y.peek().intValue();
	}
	
	/** Returns turtle's current Z-axis position. */
	public int getZ()
	{
		return Z.peek().intValue();
	}
	
	/** Returns turtle's current yaw or heading. */
	public int getYaw()
	{
		return yaw.peek().intValue();
	}
	
	/** Returns turtle's current angle increment/decrement value. */
	public int getAngle()
	{
		return angle.peek().intValue();
	}
	
	/** Returns turtle's current draw color. */
	public int getColor()
	{
		return color.peek().intValue();
	}
	
	/** Returns turtle's current color increment/decrement value */
	public int getHueChange()
	{
		return hueChange.peek().intValue();
	}
	
	/** Returns turtle's current draw thickness. */
	public int getThickness()
	{
		return thickness.peek().intValue();
	}
	
	/** Returns turtle's current direction represented as an integer. */
	public int getDirection()
	{
		if(yaw.peek() == 0)
			return 1;
		
		else if(yaw.peek()== 180)
			return 3;
		
		else if(yaw.peek() == 90)
			return 2;
		
		else if(yaw.peek() == 270)
			return 4;
		
		else
			return 0;
	}
	
	/** Returns & pops turtle's current X-axis position. */
	public int popX()
	{
		return X.pop().intValue();
	}
	
	/** Returns & pops turtle's current Y-axis position. */
	public int popY()
	{
		return Y.pop().intValue();
	}
	
	/** Returns & pops turtle's current Z-axis position. */
	public int popZ()
	{
		return Z.pop().intValue();
	}
	
	/** Returns & pops turtle's current yaw or heading. */
	public int popYaw()
	{
		return yaw.pop().intValue();
	}
	
	/** Returns & pops turtle's current angle increment/decrement value. */
	public int popAngle()
	{
		return angle.pop().intValue();
	}
	
	/** Returns & pops turtle's current draw color. */
	public int popColor()
	{
		return color.pop().intValue();
	}
	
	/** Returns & pops turtle's current color increment/decrement value. */
	public int popHueChange()
	{
		return hueChange.pop().intValue();
	}
	
	/** Returns & pops turtle's current draw thickness. */
	public int popThickness()
	{
		return thickness.pop().intValue();
	}
	
	/** Pushes the given integer to turtle's current X-axis position. */
	public void pushX(int x)
	{
		X.push(new Integer(x));
	}
	
	/** Pushes the given integer to turtle's current Y-axis position. */
	public void pushY(int y)
	{
		Y.push(new Integer(y));
	}
	
	/** Pushes the given integer to turtle's current Z-axis position. */
	public void pushZ(int z)
	{
		Z.push(new Integer(z));
	}
	
	/** Pushes the given integer to turtle's current yaw or heading. */
	public void pushYaw(int yw)
	{
		if(yw >= 0 && yw < 360)
			yaw.push(new Integer(yw));
		
		else if(yw == 360)
			yaw.push(new Integer(0));
		
		else if(yw < 0)
			yaw.push(new Integer(yw+360));
		
		else
			yaw.push(new Integer(yw-360));
	}
	
	/** Pushes the given integer to turtle's current angle increment/decrement value. */
	public void pushAngle(int a)
	{
		angle.push(new Integer(a));
	}
	
	/** Pushes the given integer to turtle's current draw color. */
	public void pushColor(int c)
	{
		if(c >= 380 && c <= 750)
			color.push(new Integer(c));
		
		else if(c < 380)
		{
			c = 380 - c;
			color.push(new Integer(750-c));
		}
		
		else
		{
			c = c - 750;
			color.push(new Integer(380+c));
		}
	}
	
	/** Pushes the given integer to turtle's current angle increment/decrement value. */
	public void pushHueChange(int h)
	{
		hueChange.push(new Integer(h));
	}
	
	/** Pushes the given integer to turtle's current draw thickness. */
	public void pushThickness(int t)
	{
		thickness.push(new Integer(t));
	}
	
	public void saveState()
	{
		X.push(X.peek());
		Y.push(Y.peek());
		Z.push(Z.peek());
		yaw.push(yaw.peek());
		angle.push(angle.peek());
		color.push(color.peek());
		hueChange.push(hueChange.peek());
		thickness.push(thickness.peek());
	}
	
	public void restoreState()
	{
		X.pop();
		Y.pop();
		Z.pop();
		yaw.pop();
		angle.pop();
		color.pop();
		hueChange.pop();
		thickness.pop();
	}
}
