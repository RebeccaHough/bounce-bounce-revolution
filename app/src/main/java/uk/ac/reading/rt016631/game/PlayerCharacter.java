package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;

public class PlayerCharacter extends Entity {
    public float xSpeed;
    public float ySpeed;
    public Bitmap leftSide;
    public Bitmap rightSide;

    /**Default constructor; creates a playerCharacter with default values for
     * attributes (0,null or false)*/
    public PlayerCharacter(){
        super();
        leftSide = null;
        rightSide = null;
    }

    /**
     * Creates a PlayerCharacter object with the attributes specified by the parameters
     * @param bitmap Bitmap storing image of PlayerCharacter
     * @param xPos current x position of PlayerCharacter
     * @param yPos current y position of PlayerCharacter
     * @param xSpeed current x speed of PlayerCharacter
     * @param ySpeed current y speed of PlayerCharacter
     */
    public PlayerCharacter(Bitmap bitmap, Bitmap leftSide, Bitmap rightSide, float xPos, float yPos, float xSpeed, float ySpeed){
        super(bitmap, xPos, yPos, xSpeed, ySpeed);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }
}
