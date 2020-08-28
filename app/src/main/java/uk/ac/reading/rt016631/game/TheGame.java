package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Iterator;
import static uk.ac.reading.rt016631.game.PlatformType.*;

public class TheGame extends GameThread{

    //Level
    private Level mLevel;

    //Player images
    private Bitmap mPlayerFront;
    private Bitmap mPlayerLeft;
    private Bitmap mPlayerRight;

    //PlayerCharacter object
    private PlayerCharacter mPlayerChar = new PlayerCharacter(mPlayerFront, mPlayerLeft, mPlayerRight, 0,0,0,0);

    //Maximum player speed in the x direction
    private int mMaxXSpeed = 1000;

    //Platform image
    private Bitmap mPlatform;

    //Monster image
    private Bitmap mMonster;

    //Bullet image
    private Bitmap mBullet;

    //Bullet object array
    private ArrayList<Bullet> mBullets;

    //Variables for bouncing
    private int mMaxBounceHeight;
    private float mFullBounceHeight;

    //Variables for holding the total height of the world
    float lastCollisionHeight = 0;
    //holds the distance that the world has been scrolled down by; i.e. the distance from
    //the bottom of the screen at the start of the game to the botto mof the screen after
    //the player has moved up in the world; lastCollisionHeight should always be larger
    float totalWorldHeight = 0;

    float amountToScrollDownBy = 0;

    /**This is run before anything else, so we can prepare things here
     * @param gameView the game surface
     * @param level the level that is going to be played
     */
    public TheGame(GameView gameView, Level level) {
        //House keeping
        super(gameView);
        this.mLevel = level;

        //Prepare the image of the mPlayerFront
        Bitmap playerFront =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.brown_front);
        //Scale the player character image down by half
        mPlayerFront = Bitmap.createScaledBitmap(playerFront, (int)(playerFront.getWidth()*0.5),
                (int)(playerFront.getHeight()*0.5) , true);

        //Prepare the image of the mPlayerLeft
        Bitmap playerLeft =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.brown_left);
        mPlayerLeft = Bitmap.createScaledBitmap(playerLeft, (int)(playerLeft.getWidth()*0.5),
                (int)(playerLeft.getHeight()*0.5), true);

        //Prepare the image of the mPlayerRight
        Bitmap playerRight =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.brown_right);
        mPlayerRight = Bitmap.createScaledBitmap(playerRight, (int)(playerRight.getWidth()*0.5),
                (int)(playerRight.getHeight()*0.5) , true);

        //Prepare the image of the bullets
        mBullet =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.circle_bullet);

        //Prepare the image of the monsters
        Bitmap monster =  BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.monster);
        mMonster = Bitmap.createScaledBitmap(monster, (int)(monster.getWidth()*0.5),
                (int)(monster.getHeight()*0.5) , true);

        //Prepare the image of the platforms
        if(mLevel.platformType == PLATFORM_CANDY) {
            mPlatform = BitmapFactory.decodeResource
                    (gameView.getContext().getResources(),
                            R.drawable.zigzagcandy_half_round);
        } else if(mLevel.platformType == PLATFORM_SNOW) {
            mPlatform = BitmapFactory.decodeResource
                    (gameView.getContext().getResources(),
                            R.drawable.wavesnow_half_round);
        } else if(mLevel.platformType == PLATFORM_GRASS) {
            mPlatform = BitmapFactory.decodeResource
                    (gameView.getContext().getResources(),
                            R.drawable.zigzaggrass_half_round);
        } else if(mLevel.platformType == PLATFORM_DESERT) {
            mPlatform = BitmapFactory.decodeResource
                    (gameView.getContext().getResources(),
                            R.drawable.wavedesert_half_round);
        } else if(mLevel.platformType == PLATFORM_YELLOW) {
            mPlatform = BitmapFactory.decodeResource
                    (gameView.getContext().getResources(),
                            R.drawable.zigzagyellow_half_round);
        } else if(mLevel.platformType == PLATFORM_FOREST) {
            mPlatform = BitmapFactory.decodeResource
                    (gameView.getContext().getResources(),
                            R.drawable.waveforest_half_round);
        }
        //Tell level which bitmap is being used for platforms
        mLevel.platformBitmap = mPlatform;
    }

    /**This is run before a new game (also after an old game)*/
    @Override
    public void setupBeginning() {
        //Set/reset bounce variables
        mFullBounceHeight = 0;
        mMaxBounceHeight = mCanvasHeight / 4;
        lastCollisionHeight = 0;
        totalWorldHeight = 0;
        amountToScrollDownBy = 0;

        //Set up level
        mLevel.canvasHeight = mCanvasHeight;
        mLevel.canvasWidth = mCanvasWidth;
        mLevel.maxJumpHeight = mMaxBounceHeight;
        mLevel.offScreenPlatformsBottomY = 0;

        //Initialise or clear platforms and monsters ArrayLists
        mLevel.platforms = new ArrayList<>();
        mLevel.monsters = new ArrayList<>();

        //Add new platforms
        mLevel.loadLevel();
        mLevel.generateNextPlatforms();

        //If a platform has a monster on it, create it
        for(Platform p : mLevel.platforms) {
            if(p.hasMonster) {
                mLevel.monsters.add(new Monster(mMonster, p.xPos,
                        p.yPos - mPlatform.getHeight() / 2 - mMonster.getHeight() / 2, 0, 0,
                        p.monsterMoves ? Monster.doSetMoving() : false));
            }
        }

        //Initialise or clear mBullets ArrayList
        mBullets = new ArrayList<>();

        //Set/reset backgrounds' positions
        mBackgroundAYPos = 0;
        mBackgroundBYPos = -mCanvasHeight;

        //Reset scores
        setHeightScore(0);
        setMonsterScore(0);
        setTotalScore(0);

        //Initialise player starting position and speeds
        mPlayerChar.xPos = mCanvasWidth / 2;
        mPlayerChar.yPos =  2 * mCanvasHeight / 3;
        mPlayerChar.xSpeed = 0;
        mPlayerChar.ySpeed = 500;

        //Set min distances between entities without square root
        Monster.minDistanceBetweenPlayerAndMonster = (mPlayerFront.getWidth() / 2 + mMonster.getWidth() / 2)
                * (mPlayerFront.getWidth() / 2 + mMonster.getWidth() / 2);
        Bullet.minDistanceBetweenBulletAndMonster = (mMonster.getWidth() / 2 + mBullet.getWidth() / 2)
                * (mMonster.getWidth() / 2 + mBullet.getWidth() / 2);
    }

    /**
     * Draw entities onto the canvas
     * @param canvas canvas onto which to draw the game
     */
    @Override
    protected void doDraw(Canvas canvas) {
        //If there isn't a canvas to do nothing
        if(canvas == null) return;

        //House keeping
        super.doDraw(canvas);

        //canvas.drawBitmap(bitmap, x, y, paint) uses top/left corner of bitmap as 0,0
        //we use 0,0 in the offScreenPlatformsBottomY of the bitmap, so negate half of the width and height of the ball to draw the ball as expected
        //A paint of null means that we will use the image without any extra features (called Paint)

        //Draw the player character depending on which direction they are moving in
        if(mPlayerChar.xSpeed > 0) { //if moving left
            //draw left-facing image
            mPlayerChar.bitmap = mPlayerRight;
            mPlayerChar.drawEntity(canvas);
        } else if(mPlayerChar.xSpeed < 0){ //if moving right
            //draw right-facing image
            mPlayerChar.bitmap = mPlayerLeft;
            mPlayerChar.drawEntity(canvas);
        } else { //if not moving left or right
            //draw front-facing image
            mPlayerChar.bitmap = mPlayerFront;
            mPlayerChar.drawEntity(canvas);
        }

        //Loop through all of platforms ArrayList
        for(Platform p : mLevel.platforms) {
            p.drawEntity(canvas);
        }

        if(mLevel.monsters != null) {
            //Loop through mMonsters
            for (Monster m : mLevel.monsters) {
                m.drawEntity(canvas);
            }
        }

        if(mBullets != null) {
            //Loop through mBullets
            for (Bullet b : mBullets) {
                b.drawEntity(canvas);
            }
        }
    }


    /**This is run whenever the phone is touched by the user*/
    @Override
    protected void actionOnTouch(float x, float y) {
        float speedX = (x - mPlayerChar.xPos) / 0.5f;
        float speedY = (y - mPlayerChar.yPos) / 0.5f;
        mBullets.add(new Bullet(mBullet, mPlayerChar.xPos, mPlayerChar.yPos, speedX, speedY));
    }

    /**This is run whenever the phone moves around its axises*/
    @Override
    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
        //Change the player's speed in x direction
        float speed = mPlayerChar.xSpeed + 150f * xDirection;
        //If player's speed in positive or negative x direction is greater than maxSpeed,
        //set player's speed to maxSpeed
        if (speed > mMaxXSpeed) {
            mPlayerChar.xSpeed = mMaxXSpeed;
        } else if (speed < -mMaxXSpeed) {
            mPlayerChar.xSpeed = -mMaxXSpeed;
        } else {
            mPlayerChar.xSpeed = speed;
        }
    }

    /**
     * This is run just before the game "scenario" is printed on the screen
     * @param secondsElapsed the time that has elapsed since the game was last drawn on the screen
     */
    @Override
    protected void updateGame(float secondsElapsed) {
        //Code to gradually scroll world down when player jumps to a higher platform
        //==> suspend usual game updates
        if (amountToScrollDownBy > 0) {
            if (amountToScrollDownBy - 25 > 0) {
                amountToScrollDownBy -= 25;
                //Move platforms, monsters, player and backgrounds down
                moveWorldDown(25);
            } else {
                //else use the remaining amount of amountToScrollDownBy;
                moveWorldDown(amountToScrollDownBy);
                amountToScrollDownBy = 0;
            }

            //if world has finished scrolling down
            if (amountToScrollDownBy <= 0) {
                //start player bounce
                mPlayerChar.ySpeed = -mPlayerChar.ySpeed;
                mFullBounceHeight = mPlayerChar.yPos - mMaxBounceHeight;
            }
        }
        //else handle the game updates normally
        else {
            //If player is moving up the screen (set by updateCollisions), handle bouncing
            if (mPlayerChar.ySpeed < 0) {
                //if player is above or at full height of bounce
                if (mPlayerChar.yPos <= mFullBounceHeight) {
                    //start falling
                    mPlayerChar.ySpeed = -mPlayerChar.ySpeed;
                    mFullBounceHeight = 0;
                }
            }

            //If the player is outside the screen and moving further away
            //Move them to the opposite side of the screen
            if (mPlayerChar.xPos <= 0 && mPlayerChar.xSpeed < 0) {
                mPlayerChar.xPos = mCanvasWidth;
            }
            if (mPlayerChar.xPos >= mCanvasWidth && mPlayerChar.xSpeed > 0) {
                mPlayerChar.xPos = 0;
            }

            //Move the player's X and Y using the speed (pixel/sec)
            mPlayerChar.xPos = mPlayerChar.xPos + secondsElapsed * mPlayerChar.xSpeed;
            mPlayerChar.yPos = mPlayerChar.yPos + secondsElapsed * mPlayerChar.ySpeed;

            //Move bullets
            for (Bullet b : mBullets) {
                b.xPos = b.xPos + secondsElapsed * b.xSpeed;
                b.yPos = b.yPos + secondsElapsed * b.ySpeed;
            }

            //Delete offscreen bullets
            //read as: for(Bullet b : mBullets) if(b is offscreen) mBullets.remove(b);
            Iterator<Bullet> iterator = mBullets.iterator();
            while (iterator.hasNext()) {
                Bullet b = iterator.next();
                if (b.xPos > mCanvasWidth || b.xPos < 0 || b.yPos > mCanvasHeight || b.yPos < 0)
                    iterator.remove();
            }

            //Move the platforms that move
            for (Platform p : mLevel.platforms) {
                if (p.isAMovingPlatform) {
                    p.xPos = p.xPos + secondsElapsed * p.xSpeed;
                    p.yPos = p.yPos + secondsElapsed * p.ySpeed;
                    //if a monster hits edge of screen, reverse its speed in the x direction
                    if (p.xPos - p.bitmap.getWidth() / 2 <= 0 ||
                            p.xPos + p.bitmap.getWidth() / 2 >= mCanvasWidth) {
                        p.xSpeed = -p.xSpeed;
                    }
                }
            }

            //Move the monsters that move
            if (mLevel.monsters != null) {
                for (Monster m : mLevel.monsters) {
                    if (m.isAMovingMonster) {
                        m.xPos = m.xPos + secondsElapsed * m.xSpeed;
                        m.yPos = m.yPos + secondsElapsed * m.ySpeed;
                        //if a monster hits edge of screen, reverse its speed in the x direction
                        if (m.xPos - m.bitmap.getWidth() / 2 <= 0 ||
                                m.xPos + m.bitmap.getWidth() / 2 >= mCanvasWidth) {
                            m.xSpeed = -m.xSpeed;
                        }
                    }
                }
            }

            //Update score and remove monster if a bullet hits a monster
            //return iterator to start of mBullets ArrayList
            iterator = mBullets.iterator();
            while (iterator.hasNext()) {
                Bullet b = iterator.next();
                //get the monster that this bullet has collided with
                Monster m = b.updateMonsterBulletCollision(mLevel.monsters);
                //if(collision has occurred)
                if (m != null) {
                    //delete both bullet and monster
                    iterator.remove();
                    mLevel.monsters.remove(m);
                    updateMonsterScore(5);
                }
            }

            //Check for platform collisions if player is travelling down the screen
            if (mPlayerChar.ySpeed > 0) {
                //if player has collided with a platform
                if (updatePlatformCollision(mLevel.platforms)) {
                    //if(first collision in game)
                    if (lastCollisionHeight == 0) {
                        //update lastCollisionHeight
                        lastCollisionHeight = totalWorldHeight + (mCanvasHeight - mPlayerChar.yPos);
                        //do not move world down
                        //do not update totalWorldHeight
                        //do not pass go
                        //do not collect £200
                        //start bounce
                        mPlayerChar.ySpeed = -mPlayerChar.ySpeed;
                        mFullBounceHeight = mPlayerChar.yPos - mMaxBounceHeight;
                    }
                    //else if(player lands on a platform higher up that the one they took off from)
                    else {
                        //convert player y pos to world height pos
                        float currentPlayerHeight = totalWorldHeight + (mCanvasHeight - mPlayerChar.yPos);
                        if (currentPlayerHeight > lastCollisionHeight) {
                            amountToScrollDownBy = currentPlayerHeight - lastCollisionHeight;

                            //update totalWorldHeight
                            totalWorldHeight += amountToScrollDownBy;
                            //update lastCollisionHeight
                            lastCollisionHeight = currentPlayerHeight;

                            //update height score by the distance between the two platforms
                            updateHeightScore(Math.round(amountToScrollDownBy / 100));

                        }
                        //else just bounce on this platform
                        else {
                            //start bounce
                            mPlayerChar.ySpeed = -mPlayerChar.ySpeed;
                            mFullBounceHeight = mPlayerChar.yPos - mMaxBounceHeight;
                        }
                    }
                }
            }

            //Check if player collides with a monster => lose the game
            if (mLevel.monsters != null) {
                for (Monster m : mLevel.monsters) {
                    if (m.updateMonsterPlayerCollision(mPlayerChar.xPos, mPlayerChar.yPos)) {
                        setState(GameThread.STATE_LOSE);
                    }
                }
            }

            //If the player goes out of the bottom or top of the screen, lose the game
            if (mPlayerChar.yPos >= mCanvasHeight || mPlayerChar.yPos <= 0) {
                //stop game and trigger end screen
                setState(GameThread.STATE_LOSE);
            }
        }
    }

    //scroll smooth algorithms
    //call do draw many tmes
    //edit updategame()
    //if(counter > 0)
    //counter--;
    //suspend user functions
    //move objects down
    //else {
    //current update game
    //}

    /**
     * Check to see if the player has landed on a platform
     * @param platforms the platforms that the player could land on
     * @return true if player has landed on a platform; false otherwise
     */
    private boolean updatePlatformCollision(ArrayList<Platform> platforms){
        //The offScreenPlatformsBottomY bottom of the image
        float playerBottomY = mPlayerChar.yPos + getRadius(mPlayerFront.getHeight());

        //For all platforms, check if the user has collided with one
        for(Platform p : platforms) {
            float minPlatY = p.yPos - getRadius(mPlatform.getHeight());
            float maxPlatY = p.yPos - (1 / 3) * getRadius(mPlatform.getHeight());

            float minPlatX = p.xPos - getRadius(mPlatform.getWidth());
            float maxPlatX = p.xPos + getRadius(mPlatform.getWidth());

            float playerBottomMinX = mPlayerChar.xPos - getRadius(mPlayerFront.getWidth());
            float playerBottomMaxX = mPlayerChar.xPos + getRadius(mPlayerFront.getWidth());

            //If the bottom of the player's image has reached the top of the platform's image
            //Actually has some leeway; checks between top of image and 1/6th of the way down
            if (playerBottomY >= minPlatY && playerBottomY <= maxPlatY) {
                //If the player is on the same X
                //(first condition is for images larger than the platform)
                //note: collisions will be inconsistent if the playerImage's width is greater than
                //twice the platforms' width, which it is not currently
                if ((mPlayerChar.xPos >= minPlatX && mPlayerChar.xPos <= maxPlatX)
                        ||(playerBottomMinX >= minPlatX && playerBottomMinX <= maxPlatX)
                        || (playerBottomMaxX >= minPlatX && playerBottomMaxX <= maxPlatX)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Move background, player and level down the screen by the distance specified by {@code amountToScrollDownBy}
     * @param amountToScrollDownByAsFloat the y distance to scroll down by
     */
    private void moveWorldDown(float amountToScrollDownByAsFloat) {
        //cast to int with appropriate rounding
        int amountToScrollDownBy = Math.round(amountToScrollDownByAsFloat);
        mBackgroundAYPos += amountToScrollDownBy;
        mBackgroundBYPos += amountToScrollDownBy;
        //if a background goes off the bottom of the screen, place it above the other background
        if (mBackgroundBYPos > mCanvasHeight) {
            mBackgroundBYPos = mBackgroundAYPos - mCanvasHeight;
        } else if (mBackgroundAYPos > mCanvasHeight) {
            mBackgroundAYPos = mBackgroundBYPos - mCanvasHeight;
        }
        mLevel.scrollLevelDown(amountToScrollDownBy);
        //move player down
        mPlayerChar.yPos += amountToScrollDownBy;
    }

    /**
     * Halves the input value; to improve code readability
     * @param val Value to be halved
     * @return Half the input value
     */
    private float getRadius(float val) {
        return val / 2;
    }
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.
