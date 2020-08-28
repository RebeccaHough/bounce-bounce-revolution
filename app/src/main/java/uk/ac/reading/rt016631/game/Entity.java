package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Entity {
    public Bitmap bitmap;
    public float xPos;
    public float yPos;
    public float xSpeed;
    public float ySpeed;

    /**Default constructor; creates an Entity object with default values for attributes (0 or null)*/
    public Entity(){
        bitmap = null;
        xPos = 0;
        yPos = 0;
        xSpeed = 0;
        ySpeed = 0;
    }

    /**
     * Creates an Entity object with the attributes specified by the parameters
     * @param bitmap Bitmap storing image of Entity
     * @param xPos current x position of Entity
     * @param yPos current y position of Entity
     * @param xSpeed current x speed of Entity
     * @param ySpeed current y speed of Entity
     */
    public Entity(Bitmap bitmap, float xPos, float yPos, float xSpeed, float ySpeed){
        this.bitmap = bitmap;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    /**
     * Draw the Entity object onto the canvas specified
     * @param canvas the canvas onto which the Entity will be drawn
     */
    public void drawEntity(Canvas canvas) {
        canvas.drawBitmap(bitmap, xPos - bitmap.getWidth() / 2, yPos - bitmap.getHeight() / 2, null);
    }
}
