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
    	this.addObject(new Scroller(), 0, 0);
    	int i = 0;
    	do
    	{
    		this.addObject(new Plattform(), i, 300);
    		i += 32;
    	} while(i < 832);
    	this.addObject(new Sprite(), 0, 0);
    	this.addObject(new Player(), 20, 0);
    	
    }
    public void act()
    {
    }
}
