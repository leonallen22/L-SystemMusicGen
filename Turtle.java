/** 
 * Turtle to simulate drawing to be interpreted, producing a musical score to be played
 * @author Harry Allen
 *
 */
import java.util.Stack;

public class Turtle
{
	private Stack<Integer> X;
	private Stack<Integer> Y;
	private Stack<Integer> Z;
	private Stack<Integer> yaw;
	private Stack<Integer> angle;
	private Stack<Integer> color;
	private Stack<Integer> thickness;
	
	/** Default Constructor. */
	public Turtle()
	{
		X = new Stack<Integer>();
		Y = new Stack<Integer>();
		Z = new Stack<Integer>();
		yaw = new Stack<Integer>();
		angle = new Stack<Integer>();
		color = new Stack<Integer>();
		thickness = new Stack<Integer>();
		
		X.push(0);
		Y.push(0);
		Z.push(0);
		yaw.push(0);
		angle.push(90);
		color.push(0);
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
	
	/** Returns turtle's current angle increment value. */
	public int getAngle()
	{
		return angle.peek().intValue();
	}
	
	/** Returns turtle's current draw color. */
	public int getColor()
	{
		return color.peek().intValue();
	}
	
	/** Returns turtle's current draw thickness. */
	public int getThickness()
	{
		return thickness.peek().intValue();
	}
	
	/** Returns turtle's current direction represented as an integer. */
	public int getDirection()
	{
		if(Math.abs(yaw.peek() % 360) == 0)
			return 1;
		
		else if(Math.abs(yaw.peek() % 360) == 180)
			return 3;
		
		else if(yaw.peek() % 360 == 90 || yaw.peek() % 360 == -270)
			return 2;
		
		else if(yaw.peek() % 360 == 270 || yaw.peek() % 360 == -90)
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
	
	/** Returns & pops turtle's current angle increment value. */
	public int popAngle()
	{
		return angle.pop().intValue();
	}
	
	/** Returns & pops turtle's current draw color. */
	public int popColor()
	{
		return color.pop().intValue();
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
		yaw.push(new Integer(yw));
	}
	
	/** Pushes the given integer to turtle's current angle increment value. */
	public void pushAngle(int a)
	{
		angle.push(new Integer(a));
	}
	
	/** Pushes the given integer to turtle's current draw color. */
	public void pushColor(int c)
	{
		color.push(new Integer(c));
	}
	
	/** Pushes the given integer to turtle's current draw thickness. */
	public void pushThickness(int t)
	{
		thickness.push(new Integer(t));
	}
}
