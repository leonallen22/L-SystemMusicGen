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
	
	public void setX(int x)
	{
		X.push(new Integer(x));
	}
	
	public void setY(int y)
	{
		Y.push(new Integer(y));
	}
	
	public void setZ(int z)
	{
		Z.push(new Integer(z));
	}
	
	public void setYaw(int yw)
	{
		yaw.push(new Integer(yw));
	}
	
	public void setAngle(int a)
	{
		angle.push(new Integer(a));
	}
	
	public void setColor(int c)
	{
		color.push(new Integer(c));
	}
	
	public void setThickness(int t)
	{
		thickness.push(new Integer(t));
	}
}
