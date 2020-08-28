package uk.ac.reading.rt016631.game;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Random;


@SuppressWarnings("SimplifiableConditionalExpression")
//conditional operator '?' is used for readability, instead of '&&'
public class Level {

    ArrayList<Platform> platforms;
    ArrayList<Monster> monsters;
    PlatformType platformType;
    Bitmap platformBitmap;
    int canvasHeight;
    int canvasWidth;
    int maxJumpHeight;

    /**Used to judge when to generate extra offscreen platforms*/
    int offScreenPlatformsBottomY;

    private boolean levelHasMonsters;
    private boolean monstersMove;
    private boolean platformsMove;

    /**
     * Creates a Level object with the platformType as specified by the parameter; all
     * other attributes are 0 or null
     * @param platformType the type of userPlatform the level uses
     */
    public Level(PlatformType platformType) {
        this.platforms = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.platformType = platformType;
        this.platformBitmap = null;
        this.canvasHeight = 0;
        this.canvasWidth = 0;
        this.maxJumpHeight = 0;
        this.offScreenPlatformsBottomY = 0;
    }

    /**Load the correct level according to the platformType*/
    public void loadLevel(){
        switch(platformType) {
            case PLATFORM_CANDY:
                loadLevelOne();
                break;
            case PLATFORM_SNOW:
                loadLevelTwo();
                break;
            case PLATFORM_GRASS:
                loadLevelThree();
                break;
            case PLATFORM_DESERT:
                loadLevelFour();
                break;
            case PLATFORM_YELLOW:
                loadLevelFive();
                break;
            case PLATFORM_FOREST:
                loadLevelSix();
                break;
        }
    }

    /**
     * Set level parameters for user-created level
     * @param userPlatformType the colour/type of platform the users wishes their level to have
     * @param userLevelHasMonsters whether the user wishes their level to have monsters or not
     * @param userMonstersMove whether the user wishes their level to have monsters move or not
     * @param userPlatformsMove whether the user wishes their level to have moving platforms or not
     */
    public void setUserParams(PlatformType userPlatformType, boolean userLevelHasMonsters,
                              boolean userMonstersMove, boolean userPlatformsMove){
        platformType = userPlatformType;
        levelHasMonsters = userLevelHasMonsters;
        monstersMove = userMonstersMove;
        platformsMove = userPlatformsMove;
    }

    /*Level key*/
    //1. no monster mode
    //2. monsters
    //3. moving monsters
    //4. moving platforms but static monsters
    //5. moving platforms and moving monsters
    //6. user created level

    /**Load the easiest level, 'Candy'*/
    private void loadLevelOne() {
        //Set up params for further userPlatform generation
        levelHasMonsters = false;
        monstersMove = false;
        platformsMove = false;
        //Set up userPlatform positions
        //Three top platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  canvasHeight / 5, 0 , 0,
                false, false, false));
        //Two centre platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, 4 * canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                false, false, false));
        //Three bottom platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4, 2 * (canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  2 *(canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  7 * (canvasHeight / 8),  0 , 0,
                false, false, false));

    }

    /**Load the second easiest level, 'Snow'*/
    private void loadLevelTwo() {
        //Set up params for further userPlatform generation
        levelHasMonsters = true;
        monstersMove = false;
        platformsMove = false;
        //Set up userPlatform positions
        //Three top platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  canvasHeight / 5, 0 , 0,
                Monster.doGenerateMonster(), false, false));
        //Centre userPlatform
        platforms.add(new Platform(platformBitmap, 4 * canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                Monster.doGenerateMonster(), false, false));
        //Three bottom platforms that do not move
        platforms.add(new Platform(platformBitmap, canvasWidth / 4, 7 * (canvasHeight / 8), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  2 *(canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  7 * (canvasHeight / 8),  0 , 0,
                false, false, false));

    }

    /**Load the intermediate level, 'Grass'*/
    private void loadLevelThree() {
        //Set up params for further userPlatform generation
        levelHasMonsters = true;
        monstersMove = true;
        platformsMove = false;
        //Set up userPlatform positions
        //Three top platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), platformsMove));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  canvasHeight / 5, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), false));
        //Two centre platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, 4 * canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                false, false, false));
        //Three bottom platforms that do not move or have monsters
        platforms.add(new Platform(platformBitmap, canvasWidth / 4, 2 * (canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  2 *(canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  7 * (canvasHeight / 8),  0 , 0,
                false, false, false));

    }

    /**Load the second hardest level, 'Desert'*/
    private void loadLevelFour() {
        //Set up params for further userPlatform generation
        levelHasMonsters = true;
        monstersMove = false;
        platformsMove = true;
        //Set up userPlatform positions
        //Three top platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), false, Platform.doSetMoving()));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), false, Platform.doSetMoving()));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  canvasHeight / 5, 0 , 0,
                Monster.doGenerateMonster(), false, Platform.doSetMoving()));
        //Two centre platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                Monster.doGenerateMonster(), false, Platform.doSetMoving()));
        platforms.add(new Platform(platformBitmap, 4 * canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                Monster.doGenerateMonster(), false, Platform.doSetMoving()));
        //Three bottom platforms that do not move or have monsters on them
        platforms.add(new Platform(platformBitmap, canvasWidth / 4, 2 * (canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  2 *(canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  7 * (canvasHeight / 8),  0 , 0,
                false, false, false));

    }

    /**Load the hardest level, 'Yellow'*/
    private void loadLevelFive() {
        //Set up params for further userPlatform generation
        levelHasMonsters = true;
        monstersMove = true;
        platformsMove = true;
        //Set up userPlatform positions
        //Three top platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), Platform.doSetMoving()));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), Platform.doSetMoving()));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  canvasHeight / 5, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), Platform.doSetMoving()));
        //Two centre platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(),Platform.doSetMoving()));
        platforms.add(new Platform(platformBitmap, 4 * canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                Monster.doGenerateMonster(), Monster.doSetMoving(), Platform.doSetMoving()));
        //Three bottom platforms that do not move or have monsters on them
        platforms.add(new Platform(platformBitmap, canvasWidth / 4, 2 * (canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  2 *(canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  7 * (canvasHeight / 8),  0 , 0,
                false, false, false));

    }

    /**Load the user created level, 'Forest'. If user has not yet created a level, will default to
     * hardest difficulty (monsters, moving monsters and moving platforms) and use forest bitmap*/
    private void loadLevelSix() {
        //Three boolean variables are set by setUserParams()
        //Set up userPlatform positions
        //Three top platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                levelHasMonsters ? Monster.doGenerateMonster() : false,
                monstersMove ? Monster.doSetMoving() : false,
                platformsMove ? Platform.doSetMoving() : false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  canvasHeight / 3, 0 , 0,
                levelHasMonsters ? Monster.doGenerateMonster() : false,
                monstersMove ? Monster.doSetMoving() : false,
                platformsMove ? Platform.doSetMoving() : false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  canvasHeight / 5, 0 , 0,
                levelHasMonsters ? Monster.doGenerateMonster() : false,
                monstersMove ? Monster.doSetMoving() : false,
                platformsMove ? Platform.doSetMoving() : false));
        //Two centre platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                levelHasMonsters ? Monster.doGenerateMonster() : false,
                monstersMove ? Monster.doSetMoving() : false,
                platformsMove ? Platform.doSetMoving() : false));
        platforms.add(new Platform(platformBitmap, 4 * canvasWidth / 5,  canvasHeight / 2, 0 , 0,
                levelHasMonsters ? Monster.doGenerateMonster() : false,
                monstersMove ? Monster.doSetMoving() : false,
                platformsMove ? Platform.doSetMoving() : false));
        //Three bottom platforms
        platforms.add(new Platform(platformBitmap, canvasWidth / 4, 2 * (canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth - canvasWidth / 4,  2 *(canvasHeight / 3), 0 , 0,
                false, false, false));
        platforms.add(new Platform(platformBitmap, canvasWidth / 2,  7 * (canvasHeight / 8),  0 , 0,
                false, false, false));

    }

    /**
     * Move the entities in the level down by {@code int amount}
     * @param amount vertical distance as an int to move the entities in the world down by
     */
    public void scrollLevelDown(float amount) {
        //if player hits the offScreenPlatformsBottomY, generate another screen's worth of platforms
        if(offScreenPlatformsBottomY > canvasHeight) {
            generateNextPlatforms();
            //reset offscreenCentre
            offScreenPlatformsBottomY = 0;
        }
        //move platforms down
        for(Platform p : platforms) {
            p.yPos += amount;
        }
        //move monsters down
        if(monsters != null) {
            for(Monster m : monsters) {
                m.yPos += amount;
            }
        }
        //update offscreen (above screen) platforms' positions
        offScreenPlatformsBottomY += amount;

        //remove offscreen (below screen) platforms
        removeOffscreenPlatforms();
    }

    /**
     * Procedurally generate positions for n = {@code levelDifficulty} offscreen platforms for
     * canvas height, and add them to the this.platforms ArrayList
     */
    public void generateNextPlatforms() {
        //Create traversable route

        //get y pos of highest existing userPlatform
        float highestPlatformY = canvasHeight;
        for (int i = 0; i < platforms.size(); i++) {
            float thisPlatformY = platforms.get(i).yPos;
            if (thisPlatformY < highestPlatformY) highestPlatformY = thisPlatformY;
        }

        Random randomGen = new Random();
        do {
            //generate valid platforms
            boolean isValid;
            do {
                //generate X that ensures the userPlatform image doesn't go offscreen
                float randX = platformBitmap.getWidth() / 2 +
                        randomGen.nextInt(canvasWidth - platformBitmap.getWidth() / 2);
                //generate Y at a random distance between highestPlatformY and
                //highestPlatformY - maxJumpHeight
                float randY = highestPlatformY - randomGen.nextInt(maxJumpHeight);
                isValid = checkPositionIsValid(platforms, randX, randY);
                if(isValid) {
                    platforms.add(new Platform(platformBitmap, randX, randY, 0, 0,
                            levelHasMonsters ? Monster.doGenerateMonster() : false,
                            monstersMove ? Monster.doSetMoving() : false,
                            platformsMove ? Platform.doSetMoving() : false));
                    highestPlatformY = randY;
                }
            } while(!isValid);
            //while player cannot jump an entire screen's length
        } while(highestPlatformY - maxJumpHeight >= -canvasHeight);
    }

    /**
     * Check if a space on the canvas is valid for drawing platforms (i.e. is not already occupied
     * by another userPlatform and is not too close to the left and right edges of the screen)
     * @param platforms ArrayList of Platforms to check against
     * @param newX X position of new userPlatform
     * @param newY Y position of new userPlatform
     * @return {@code true} if space is unoccupied and new userPlatform does not overlap another
     *      userPlatform; {@code false} otherwise
     */
    private boolean checkPositionIsValid(ArrayList<Platform> platforms, float newX, float newY) {
        //check userPlatform does not go offscreen on the x axis
        if(newX + platformBitmap.getWidth() / 2 > canvasWidth ||
                newX - platformBitmap.getWidth() / 2 < 0) {
            //return 'space is offscreen'
            return false;
        }

        //if new userPlatform overlaps any other existing userPlatform
        for(Platform p : platforms) {
            //variables are assigned to improve code readability
            float nextPlatXLeft, nextPlatXRight, platXLeft, platXRight;
            float nextPlatYTop, nextPlatYBottom, platYTop, platYBottom;

            nextPlatXRight = newX + platformBitmap.getWidth() / 2;
            nextPlatXLeft = newX - platformBitmap.getWidth() / 2;
            platXRight = p.xPos + platformBitmap.getWidth() / 2;
            platXLeft = p.xPos - platformBitmap.getWidth() / 2;

            nextPlatYTop = newY - platformBitmap.getHeight() / 2;
            nextPlatYBottom = newY; //since userPlatform image is takes up less than the full height of the bitmap
            platYTop = p.yPos - platformBitmap.getHeight() / 2;
            platYBottom = p.yPos;

            //if either the top or bottom edges of the new userPlatform are inside another userPlatform's
            //top and bottom edges
            if ((nextPlatYTop <= platYBottom && nextPlatYTop >= platYTop)
                    || (nextPlatYBottom <= platYBottom && nextPlatYBottom >= platYTop)) {
                //if either the left or right edges of the new userPlatform are inside another userPlatform's
                //left and right edges
                if ((nextPlatXLeft >= platXLeft && nextPlatXLeft <= platXRight)
                        || (nextPlatXRight >= platXLeft && nextPlatXRight <= platXRight)) {

                    //return 'space is occupied'
                    return false;
                }
            }
        }
        //return 'space is unoccupied and onscreen'
        return true;
    }

    /**
     * Remove platforms that have disappeared off of the bottom of the canvas
     */
    private void removeOffscreenPlatforms() {
        //store image radius; all images are the same
        int imageRadius = platformBitmap.getHeight() / 2;
        for (int i = 0; i < platforms.size(); i++) {
            //ensure image is completely offscreen before deleting, with canvasHeight + imageRadius
            if(platforms.get(i).yPos > canvasHeight + imageRadius) {
                platforms.remove(i);
            }
        }
    }
}
