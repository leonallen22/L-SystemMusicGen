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
	
	public int getX()
	{
		return X.peek().intValue();
	}
	
	public int getY()
	{
		return Y.peek().intValue();
	}
	
	public int getZ()
	{
		return Z.peek().intValue();
	}
	
	public int getYaw()
	{
		return yaw.peek().intValue();
	}
	
	public int getAngle()
	{
		return angle.peek().intValue();
	}
	
	public int getColor()
	{
		return color.peek().intValue();
	}
	
	public int getThickness()
	{
		return thickness.peek().intValue();
	}
	
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
	
	public int popX()
	{
		return X.pop().intValue();
	}
	
	public int popY()
	{
		return Y.pop().intValue();
	}
	
	public int popZ()
	{
		return Z.pop().intValue();
	}
	
	public int popYaw()
	{
		return yaw.pop().intValue();
	}
	
	public int popAngle()
	{
		return angle.pop().intValue();
	}
	
	public int popColor()
	{
		return color.pop().intValue();
	}
	
	public int popThickness()
	{
		return thickness.pop().intValue();
	}
	
	public void pushX(int x)
	{
		X.push(new Integer(x));
	}
	
	public void pushY(int y)
	{
		Y.push(new Integer(y));
	}
	
	public void pushZ(int z)
	{
		Z.push(new Integer(z));
	}
	
	public void pushYaw(int yw)
	{
		yaw.push(new Integer(yw));
	}
	
	public void pushAngle(int a)
	{
		angle.push(new Integer(a));
	}
	
	public void pushColor(int c)
	{
		color.push(new Integer(c));
	}
	
	public void pushThickness(int t)
	{
		thickness.push(new Integer(t));
	}
}
