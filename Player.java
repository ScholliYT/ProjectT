import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The main character. Responds to certain keys.
 * Jumps around killing stuff and avoids being killed
 *
 * @author Ed Parrish
 * @author Nigel Cunliffe
 * @version 1.3 12/5/11
 */
public class Player extends Sprite
{
    private static double runSpeed = .75;
    private int jumpForce = 14;
    public static final double GRAVITY = 0.7;
    private static final double MAX_VEL = 14;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private int direction = RIGHT;
    private boolean canJump;
    private int imageCount = 0;
    private int counter;
    private int reloadCount = 30;
    private int slideCount;

    private Actor previous; // to control collision sounds
    private Scroller scroll;

    public Player() {

    }

    /**
     * Update and then move this sprite.
     */
    public void act()
    {
        checkKeys();
        checkCollisionHorizontal();
        manageJumping();
        checkBounce();
        removeGate();
        checkKillers();
        checkCoins();
        addLife();
        boardMove();
        platformMove();
        //slide();
        move();
        win();
    }

    /**
     * Check for and respond to key presses.
     */
    public void checkKeys()
    {
        if (Greenfoot.isKeyDown("right") &! (Greenfoot.isKeyDown("left")))
        {
            direction = RIGHT;
            runRight();
        }
        else if (Greenfoot.isKeyDown("left") &! (Greenfoot.isKeyDown("right")))
        {
            direction = LEFT;
            runLeft();
        }
        else
        {
            stopMoving();
            if(direction == RIGHT) {
                setImage("standing.gif");
            }
            if(direction == LEFT) {
                setImage("standingleft.gif");
            }
        }
        if (Greenfoot.isKeyDown("up"))
        {
            jump();
        }

        if (Greenfoot.isKeyDown("space") && counter > reloadCount)
        {
            fire();
            counter = 0;
        }
        counter++;
    }

    //     public void slide() {
    //         if(onThis(Platform.class) && Greenfoot.isKeyDown("down")) {
    //             slideCount++;
    //             setRotation(270);
    //             setLocation(getX() + 5, getY());
    //         }
    //         if(slideCount >= 10) {
    //             setRotation(90);
    //             slideCount = 0;
    //         }
    //     }

    /**
     * Run to the right.
     */
    public void runRight()
    {
        setVelocityX(runSpeed);
        imageCount++;
        if(imageCount <= 1){
            setImage("run3.gif");
        }
        if(imageCount == 11){
            setImage("run1.gif");
        }
        if(imageCount == 21){
            setImage("run3.gif");
            imageCount = 0;
        }
    }

    /**
     * Run to the left.
     */
    public void runLeft()
    {
        setVelocityX(-runSpeed);
        imageCount++;
        if(imageCount <= 1){
            setImage("run3left.gif");
        }
        if(imageCount == 11){
            setImage("run1left.gif");
        }
        if(imageCount == 21){
            setImage("run3left.gif");
            imageCount = 0;
        }
    }

    /**
     * Stop moving.
     */
    public void stopMoving()
    {
        setVelocityX(0.0);
    }

    /**
     * Create a bullet and move it in the direction that the actor is facing
     */
    public void fire(){
        if(direction == RIGHT){
            ((GameManager)getWorld()).addBulletRight(getX()+30, getY());
        }
        if(direction == LEFT){
            ((GameManager)getWorld()).addBulletLeft(getX()-30, getY());
        }
    }

    public void platformMove()
    {
        if(onThis(HoverBoard.class))
        {
            if(Greenfoot.isKeyDown("d"))
            {
                move(2);
            }
            if(Greenfoot.isKeyDown("a"))
            {
                move(-2);
            }
            if(Greenfoot.isKeyDown("w"))
            {
                stopMoving();
            }
            if(Greenfoot.isKeyDown("s"))
            {
                stopMoving();
            }
        }
    }

    /**
     * Perform a jump.
     */
    public void jump()
    {
        if (canJump)
        {
            setVelocityY(-jumpForce);
            canJump = false;
            //Greenfoot.playSound("jump.wav");
        }
    }

    /**
     * Check for vertical collisions and adjust jumping or falling.
     */
    private void manageJumping()
    {
        // Calculate distance sprite will move vertically
        double velocityY = getVelocityY();
        int lookY = 0;
        if (velocityY > 0)
        {
            lookY = (int) (velocityY + GRAVITY + getHeight() / 2);
        }
        else
        {
            lookY = (int) (velocityY + GRAVITY - getHeight() / 2);
        }
        // Check for vertical collision this cycle
        Actor tile = getOneObjectAtOffset(0, lookY, Platform.class);
        if (tile == null) {
            // No collision this cycle
            applyGravity();
            canJump = false; // in case of falling off an edge
        }
        else
        {
            // Collision detected
            moveToContactVertical(tile);
            if (velocityY > 0)
            {
                // Player has landed
                canJump = true;
            }
            setVelocityY(0.0);
        }
    }

    /**
     * Apply gravity when the sprite is jumping or falling.
     */
    public void applyGravity()
    {
        double velocityY = getVelocityY() + GRAVITY; // add gravity
        if (velocityY > MAX_VEL) velocityY = MAX_VEL; // limit velocity
        setVelocityY(velocityY);  // save current velocity
    }

    /**
     * Move this sprite into contact with the specified Actor vertically.
     *
     * @param target The target this sprite is approaching.
     */
    public void moveToContactVertical(Actor target)
    {
        int h2 = (target.getImage().getHeight() + getImage().getHeight()) / 2;
        int newY = 0;

        if (target.getY() > getY())
        {
            newY = target.getY() - h2;
        }
        else
        {
            newY = target.getY() + h2;
        }

        setLocation(getX(), newY);

    }

    /**
     * Check for a horizontal collision with a platform.
     */
    public void checkCollisionHorizontal() {
        double velocityX = getVelocityX();
        if (velocityX == 0) return;
        int lookX = 0;
        if (velocityX < 0)
        {
            lookX = (int) velocityX - getWidth() / 2;
        }
        else
        {
            lookX = (int) velocityX + getWidth() / 2;
        }
        Actor a = getOneObjectAtOffset(lookX, 0, Platform.class);
        if (a != null) {
            moveToContactHorizontal(a);
            stopMoving();
        }
        previous = a;
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

    /**
     * jumpForce = 28 if on Spring.class
     */
    public void checkBounce(){       

        if(onThis(Spring.class)){
            jumpForce = 28;
        }else{
            jumpForce = 14;
        }
    }

    /**
     * Remove ending gates when button is pressed
     */
    public void removeGate()
    {
        if(onThis(Button.class)){
            ((GameManager)getWorld()).removeGate1();
        }
    }

    public void changeSpeed(double amount)
    {
        runSpeed = amount;
    }

    /**
     * check for things that kill the player
     */
    public void checkKillers()
    {
        GameManager world = (GameManager) getWorld();

        if(canSee(ENEMIES.class))
        {
            setLocation(75, -300);
            world.subtractLife(1);
            //Greenfoot.playSound("scream.wav");
        }

        if (getY() + getHeight() / 2 > getWorld().getHeight())
        {
            setLocation(75, -300);
            world.subtractLife(1);
            //Greenfoot.playSound("scream.wav");
        }
    }

    public void checkCoins()
    {
        GameManager world = (GameManager) getWorld();
        if(canSee(Coin.class))
        {
            //Greenfoot.playSound("ding.wav");
            delete(Coin.class);
            world.addToScore(1);
        }
    }

    /**
     * Add a new level when PLayer reaches Sign
     */
    public void win(){
        Actor finish = getOneIntersectingObject(Sign.class);
        if(canSee(Sign.class)||canSee(SignNS.class)){
            //add sound
            ((GameManager)getWorld()).addMap();
        }
    }

    /**
     * Add life when player reaches lifeIcon
     */
    public void addLife() {
        Actor actor = this.getOneIntersectingObject(Actor.class);
        Actor lifeIcon = getOneIntersectingObject(LivesIcon2.class);
        if(actor instanceof LivesIcon2){             
            GameManager world = (GameManager) getWorld();

            world.addLife(1);
            //Greenfoot.playSound("1up.wav");
        }
        getWorld().removeObject(lifeIcon);

    }

    public void boardMove()
    {
        if(onThis(Board.class)&!byThis(Platform.class))
        {
            move(3);
        }
        if(onThis(Board2.class)&!byThis2(Platform.class))
        {
            move(-3);
        }
    }

    /**
     * Move this Actor into contact with the specified Actor in the
     * horizontal (x) direction.
     *
     * @param target The target this sprite is approaching.
     */
    public void moveToContactHorizontal(Actor target)
    {
        int w2 = (getWidth() + target.getImage().getWidth()) / 2;
        int newX = 0;
        if (target.getX() > getX())
        {
            newX = target.getX() - w2;
        }
        else
        {
            newX = target.getX() + w2;
        }
        setLocation(newX, getY());
    }
}
