import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GameManager here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameManager extends World
{
    public GameManager()
    {    
        super(800, 600, 1); 
    }
    
    public void act()
    {
    	this.addObject(new Scroller(), 0, 0);
    	for(int i = 0; i < 400; i = i + 32)
    	{
    		this.addObject(new Plattform(), i, 200);
    	}
    }
}
