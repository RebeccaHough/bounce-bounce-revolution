package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;
import java.util.Random;

public class Platform extends Entity {
    boolean hasMonster;
    boolean monsterMoves;
    boolean isAMovingPlatform;
    private static Random randomGen = new Random();

    /**Default constructor; creates a Platform object with default values for
     *  attributes (0, null or false)*/
    public Platform(){
        super();
        hasMonster = false;
        monsterMoves = false;
        isAMovingPlatform = false;
    }

    /**
     * Create a Platform object with the attributes specified by the parameters
     * @param bitmap Bitmap storing image of platform
     * @param xPos current x position of platform
     * @param yPos current y position of platform
     * @param xSpeed current x speed of platform
     * @param ySpeed current y speed of platform
     * @param hasMonster boolean storing whether the platform has a monster on it
     * @param monsterMoves boolean storing whether the monster on the platform moves
     * @param isAMovingPlatform boolean storing whether the platform moves
     */
    public Platform(Bitmap bitmap, float xPos, float yPos, float xSpeed, float ySpeed,
                    boolean hasMonster, boolean monsterMoves, boolean isAMovingPlatform){
        super(bitmap, xPos, yPos,xSpeed, ySpeed);
        this.hasMonster = hasMonster;
        this.monsterMoves = monsterMoves;
        this.isAMovingPlatform = isAMovingPlatform;
        if(this.isAMovingPlatform) {
            this.xSpeed = 50;
            this.ySpeed = 0;
        }
    }

    /**
     * Tell the caller whether to let the platform move (25% chance)
     * @return true if the platform should move; false otherwise
     */
    public static boolean doSetMoving() {
        return randomGen.nextInt(4) == 0;
    }
}
