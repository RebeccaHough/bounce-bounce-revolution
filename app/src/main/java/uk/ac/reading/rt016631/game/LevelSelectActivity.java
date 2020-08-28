package uk.ac.reading.rt016631.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import static uk.ac.reading.rt016631.game.PlatformType.*;

public class LevelSelectActivity extends Activity {
    private ToggleButton candyButton = null;
    private ToggleButton snowButton;
    private ToggleButton grassButton;
    private ToggleButton desertButton;
    private ToggleButton yellowButton;
    private ToggleButton forestButton;

    PlatformType selectedPlatformType;

    TextView levelDescriptionText;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.level_select_activity);

        //Labels
        final TextView titleText = (TextView) findViewById(R.id.levelSelectText);
        levelDescriptionText = (TextView) findViewById(R.id.levelDescriptionText);

        //Buttons
        candyButton = (ToggleButton) findViewById(R.id.candyButton);
        snowButton = (ToggleButton) findViewById(R.id.snowButton);
        grassButton = (ToggleButton) findViewById(R.id.grassButton);
        desertButton = (ToggleButton) findViewById(R.id.desertButton);
        yellowButton = (ToggleButton) findViewById(R.id.yellowButton);
        forestButton = (ToggleButton) findViewById(R.id.forestButton);
        Button startButton = (Button) findViewById(R.id.startButton);
        Button backButton = (Button) findViewById(R.id.levelSelectBackButton);

        //Set style of text
        Typeface font = Typeface.createFromAsset(getAssets(), "Luna.ttf");
        int headingFontSize = 35;
        int bodyFontSize = 25;
        int subtitleFontSize = 15;
        int colour = Color.WHITE;
        styleFont(titleText, font, headingFontSize, colour);
        styleFont(levelDescriptionText, font, subtitleFontSize, colour);
        styleFont(startButton, font, bodyFontSize, colour);
        styleFont(backButton, font, bodyFontSize, colour);

        //Set what buttons do
        candyButton.setOnCheckedChangeListener(changedChecker);
        snowButton.setOnCheckedChangeListener(changedChecker);
        grassButton.setOnCheckedChangeListener(changedChecker);
        desertButton.setOnCheckedChangeListener(changedChecker);
        yellowButton.setOnCheckedChangeListener(changedChecker);
        forestButton.setOnCheckedChangeListener(changedChecker);

        //set initially selected level
        candyButton.setChecked(true);
        candyButton.setBackgroundResource(R.drawable.zigzagcandy_round_selected);
        selectedPlatformType = PLATFORM_CANDY;
        levelDescriptionText.setText(R.string.candy_difficulty);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPlatformType == PLATFORM_FOREST) {
                    try {
                        //check if save data for user level exists
                        FileInputStream iStream = openFileInput("BounceBounceSave.txt");
                    } catch (FileNotFoundException e) {
                        //create dialog to tell user level cannot be loaded
                        AlertDialog.Builder builder = new AlertDialog.Builder(LevelSelectActivity.this);
                        builder.setMessage("User level save data not found.");
                        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //style dialogue
                        TextView message = (TextView) dialog.findViewById(android.R.id.message);
                        message.setTypeface(Typeface.createFromAsset(getAssets(), "Luna.ttf"));
                        message.setTextSize(15);
                        message.setTextColor(Color.WHITE);
                        //do not start level
                        return;
                    }
                }
                Intent showGameScreen = new Intent(getApplicationContext(), MainActivity.class);
                //pass platform type selected to next activity
                showGameScreen.putExtra("PlatformType", selectedPlatformType.toString());
                //start game
                startActivity(showGameScreen);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to menu
                Intent showMainMenuScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(showMainMenuScreen);
            }
        });
    }

    CompoundButton.OnCheckedChangeListener changedChecker = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                //for selected button
                if (buttonView == candyButton) {
                    candyButton.setChecked(true);
                    setSelectedBackground(PLATFORM_CANDY);
                    selectedPlatformType = PLATFORM_CANDY;
                    setLevelDescriptionText(PLATFORM_CANDY);
                }
                if (buttonView == snowButton) {
                    snowButton.setChecked(true);
                    setSelectedBackground(PLATFORM_SNOW);
                    selectedPlatformType = PLATFORM_SNOW;
                    setLevelDescriptionText(PLATFORM_SNOW);
                }
                if (buttonView == grassButton) {
                    grassButton.setChecked(true);
                    setSelectedBackground(PLATFORM_GRASS);
                    selectedPlatformType = PLATFORM_GRASS;
                    setLevelDescriptionText(PLATFORM_GRASS);
                }
                if (buttonView == desertButton) {
                    desertButton.setChecked(true);
                    setSelectedBackground(PLATFORM_DESERT);
                    selectedPlatformType = PLATFORM_DESERT;
                    setLevelDescriptionText(PLATFORM_DESERT);
                }
                if (buttonView == yellowButton) {
                    yellowButton.setChecked(true);
                    setSelectedBackground(PLATFORM_YELLOW);
                    selectedPlatformType = PLATFORM_YELLOW;
                    setLevelDescriptionText(PLATFORM_YELLOW);
                }
                if(buttonView == forestButton) {
                    forestButton.setChecked(true);
                    setSelectedBackground(PLATFORM_FOREST);
                    selectedPlatformType = PLATFORM_FOREST;
                    setLevelDescriptionText(PLATFORM_FOREST);
                }
                //for all other (unselected) buttons
                if (buttonView != candyButton) {
                    candyButton.setChecked(false);
                    setUnselectedBackground(PLATFORM_CANDY);
                }
                if (buttonView != snowButton) {
                    snowButton.setChecked(false);
                    setUnselectedBackground(PLATFORM_SNOW);
                }
                if (buttonView != grassButton) {
                    grassButton.setChecked(false);
                    setUnselectedBackground(PLATFORM_GRASS);
                }
                if (buttonView != desertButton) {
                    desertButton.setChecked(false);
                    setUnselectedBackground(PLATFORM_DESERT);
                }
                if (buttonView != yellowButton) {
                    yellowButton.setChecked(false);
                    setUnselectedBackground(PLATFORM_YELLOW);
                }
                if (buttonView != forestButton) {
                    forestButton.setChecked(false);
                    setUnselectedBackground(PLATFORM_FOREST);
                }
            }
        }

        /**
         * Set the background of the button specified to its selected variant
         * @param platformType the platform type whose corresponding button will have its background
         *               set
         */
        private void setSelectedBackground(PlatformType platformType) {
            switch(platformType) {
                case PLATFORM_CANDY:
                    candyButton.setBackgroundResource(R.drawable.zigzagcandy_round_selected);
                    break;
                case PLATFORM_SNOW:
                    snowButton.setBackgroundResource(R.drawable.wavesnow_round_selected);
                    break;
                case PLATFORM_GRASS:
                    grassButton.setBackgroundResource(R.drawable.zigzaggrass_round_selected);
                    break;
                case PLATFORM_DESERT:
                    desertButton.setBackgroundResource(R.drawable.wavedesert_round_selected);
                    break;
                case PLATFORM_YELLOW:
                    yellowButton.setBackgroundResource(R.drawable.zigzagyellow_round_selected);
                    break;
                case PLATFORM_FOREST:
                    forestButton.setBackgroundResource(R.drawable.waveforest_round_selected);
                    break;
            }
        }

        /**
         * Set the background of the button specified to its unselected variant
         * @param platformType the platform type whose corresponding button will have its background
         *               set
         */
        private void setUnselectedBackground(PlatformType platformType) {
            switch(platformType) {
                case PLATFORM_CANDY:
                    candyButton.setBackgroundResource(R.drawable.zigzagcandy_round);
                    break;
                case PLATFORM_SNOW:
                    snowButton.setBackgroundResource(R.drawable.wavesnow_round);
                    break;
                case PLATFORM_GRASS:
                    grassButton.setBackgroundResource(R.drawable.zigzaggrass_round);
                    break;
                case PLATFORM_DESERT:
                    desertButton.setBackgroundResource(R.drawable.wavedesert_round);
                    break;
                case PLATFORM_YELLOW:
                    yellowButton.setBackgroundResource(R.drawable.zigzagyellow_round);
                    break;
                case PLATFORM_FOREST:
                    forestButton.setBackgroundResource(R.drawable.waveforest_round);
                    break;
            }
        }

        /**
         * Set the level description text according to the platform type selected by user
         * @param platformType the platform type (hence level type) of the currently selected button
         */
        private void setLevelDescriptionText(PlatformType platformType) {
            switch(platformType) {
                case PLATFORM_CANDY:
                    levelDescriptionText.setText(R.string.candy_difficulty);
                    break;
                case PLATFORM_SNOW:
                    levelDescriptionText.setText(R.string.snow_difficulty);
                    break;
                case PLATFORM_GRASS:
                    levelDescriptionText.setText(R.string.grass_difficulty);
                    break;
                case PLATFORM_DESERT:
                    levelDescriptionText.setText(R.string.desert_difficulty);
                    break;
                case PLATFORM_YELLOW:
                    levelDescriptionText.setText(R.string.yellow_difficulty);
                    break;
                case PLATFORM_FOREST:
                    levelDescriptionText.setText(R.string.forest_difficulty);
                    break;
            }
        }
    };

    /**
     * Style text according to parameters specified
     * @param textView the text to be styled
     * @param font the font to be applied to the text
     * @param fontSize the size the text will be set to
     * @param colour the colour to be applied to the text
     */
    private void styleFont(TextView textView, Typeface font, int fontSize, int colour) {
        textView.setTypeface(font);
        textView.setTextColor(colour);
        textView.setTextSize(fontSize);
    }
}
