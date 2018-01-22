import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass of all scrolling actors (all except Player)
 * 
 * @author (Felix, Simon, Nico)
 * @version 1.0
 */
public class Scroller extends Actor
{
    public static int sX; 

    public Scroller()
    {
        sX = 0;
    }

    /**
     * Set the actor's location.
     */
    public void setLocation()
    {
        setLocation(getX() + sX, getY());
    }

    /**
     * Move the world a certain amount when arrow keys are pressed.
     */
    public void checkKeyPress(int amount)
    {
        GameManager world = (GameManager) getWorld();
        
        if(Greenfoot.isKeyDown("left") &! Greenfoot.isKeyDown("right"))
        {
            sX = amount;

            if(Greenfoot.isKeyDown("z"))
            {
                sX = amount + 2; // world will scroll faster if z is pressed while moving
            }
        }
        else if(Greenfoot.isKeyDown("right") &! Greenfoot.isKeyDown("left"))
        {
            sX = -amount;

            if(Greenfoot.isKeyDown("z"))
            {
                sX = -amount - 2; // world will scroll faster if z is pressed while moving
            }
        }
        else
        {
            stopScroll();
        }
    }

    /**
     * add scrollingMethods to all actors that should scroll
     */
    public void scrollingMethods()
    {
        checkKeyPress(2);
        setLocation();
    }

    public static void stopScroll()
    {
        sX = 0;
    }
}
