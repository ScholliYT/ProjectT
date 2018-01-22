import greenfoot.*;

public class Plattform extends Scroller {
	private double x = -300;
	private double y = 0;
	private double velocityX; // in pixels per act() call
	private double velocityY; // in pixels per act() call
	private int state;


	public Plattform()
	{
		setImage(new GreenfootImage("images/grass_test.png"));
		getImage().scale(32, 32);
	}

	public Plattform(double newVelocityX, double newVelocityY)
	{
		velocityX = newVelocityX;
		velocityY = newVelocityY;

		setImage(new GreenfootImage("images/grass_test.png"));
		getImage().scale(32, 32);
	}

	public void act()
	{
		if(getX() < 20 && Greenfoot.isKeyDown("left"))
		{
			
		} else 
		{
			scrollingMethods();
		}		
	}
	public void move()
	{
		setLocation(x + velocityX, y + velocityY);
	}

	public void setLocation(double x, double y)
	{
		this.x = x;
		this.y = y;
		super.setLocation((int) x, (int) y);
	}

	public double getVelocityX()
	{
		return velocityX;
	}

	public double getVelocityY()
	{
		return velocityY;
	}

	public void delete(Class clss)
	{
		Actor actor = getOneIntersectingObject(clss);
		if(actor != null) {
			getWorld().removeObject(actor);
		}
	}

	public void setVelocityX(double newvelocityX)
	{
		velocityX = newvelocityX;
	}

	public void setVelocityY(double newVelocityY)
	{
		velocityY = newVelocityY;
	}

	public int getHeight()
	{
		return getImage().getHeight();
	}

	public int getWidth()
	{
		return getImage().getWidth();
	}

	public void setState(int newState)
	{
		state = newState;
	}

	public int getState()
	{
		return state;
	}

	public void removeFromWorld()
	{
		getWorld().removeObject(this);
	}

	public void setLocation(int x, int y)
	{
		setLocation((double) x, (double) y);
	}

	public boolean atWorldEdge()
	{
		if(getX() < 20 || getX() > getWorld().getWidth() - 20)
			return true;
		if(getY() < 20 || getY() > getWorld().getHeight() - 20)
			return true;
		else
			return false;
	}

	public boolean canSee(Class clss){
		Actor actor = getOneIntersectingObject(clss);
		return actor != null;     
	}

	public boolean onThis(Class clss){
		Actor actor = getOneObjectAtOffset(0, 32, clss);
		return actor != null;     
	}

	public boolean underThis(Class clss){
		Actor actor = getOneObjectAtOffset(0, -32, clss);
		return actor != null;     
	}

	public boolean byThis(Class clss){
		Actor actor = getOneObjectAtOffset(32, 0, clss);
		return actor != null;     
	}

	public boolean byThis2(Class clss){
		Actor actor = getOneObjectAtOffset(-32, 0, clss);
		return actor != null;     
	}
}
