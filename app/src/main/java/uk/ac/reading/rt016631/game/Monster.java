package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;

import java.util.Random;

public class Monster extends Entity {
    static float minDistanceBetweenPlayerAndMonster = 0;
    /**Chance to spawn monster; higher numbers mean lower chance to spawn*/
    private final static int chanceToSpawnMonster = 5;
    private static Random randomGen = new Random();

    boolean isAMovingMonster;

    /**
     * Default constructor; creates a Monster object with default values for
     * attributes (0, null or false)
     */
    public Monster(){
        super();
        isAMovingMonster = false;
    }

    /**
     * Create a Monster object with the attributes specified by the parameters
     * @param bitmap Bitmap storing image of monster
     * @param xPos current x position of monster
     * @param yPos current y position of monster
     * @param xSpeed current x speed of monster
     * @param ySpeed current y speed of monster
     * @param isAMovingMonster boolean storing whether the monster moves or not
     */
    public Monster(Bitmap bitmap, float xPos, float yPos, float xSpeed, float ySpeed,
                   boolean isAMovingMonster){
        super(bitmap, xPos, yPos, xSpeed, ySpeed);
        this.isAMovingMonster = isAMovingMonster;
        if(this.isAMovingMonster) {
            this.xSpeed = 50;
            this.ySpeed = 0;
        }
    }

    /**
     * Check if the player has collided with this monster
     * @return true if player has collided with monster; false otherwise
     */
    public boolean updateMonsterPlayerCollision(float playerX, float playerY) {
        //Get actual distance (without square root) between player and the monster being checked
        float distanceBetweenPlayerAndMonster = (xPos - playerX) * (xPos - playerX) +
                (yPos - playerY) *(yPos - playerY);

        //Check if the actual distance is lower than the allowed => collision
        return minDistanceBetweenPlayerAndMonster >= distanceBetweenPlayerAndMonster;
    }

    /**
     * Tell the caller whether to create a monster, based on the
     * {@code chanceToSpawnMonster} variable
     * @return true if a monster should be spawned; false otherwise
     */
    public static boolean doGenerateMonster(){
        return randomGen.nextInt(chanceToSpawnMonster) == 0;
    }

    /**
     * Tell the caller whether to let the monster move (50% chance)
     * @return true if the monster should move; false otherwise
     */
    public static boolean doSetMoving() {
        return randomGen.nextInt(2) == 0;
    }
}

