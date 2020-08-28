package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class Bullet extends Entity {
    static float minDistanceBetweenBulletAndMonster = 0;

    /**Default constructor; creates a Bullet object with default values for attributes (0 or null)*/
    public Bullet() {
        super();
    }

    /**
     * Creates a Bullet object with the attributes specified by the parameters
     * @param bitmap Bitmap storing image of Bullet
     * @param xPos current x position of Bullet
     * @param yPos current y position of Bullet
     * @param xSpeed current x speed of Bullet
     * @param ySpeed current y speed of Bullet
     */
    public Bullet(Bitmap bitmap, float xPos, float yPos, float xSpeed, float ySpeed){
        super(bitmap, xPos, yPos, xSpeed, ySpeed);
    }

    /**
     * Check if this bullet has collided with any monster
     * @return the monster the bullet has collided with; null if the bullet has not collided with
     * any monsters
     */
    public Monster updateMonsterBulletCollision(ArrayList<Monster> monsters) {
        for(Monster m : monsters) {
            //Get actual distance (without square root) between the bullet and the monster being checked
            float distanceBetweenBulletAndMonster = (xPos - m.xPos) * (xPos - m.xPos) +
                    (yPos - m.yPos) *(yPos - m.yPos);

            //Check if the actual distance is lower than the allowed => collision
            if(minDistanceBetweenBulletAndMonster >= distanceBetweenBulletAndMonster) {
                return m;
            }
        }
        return null;
    }
}
