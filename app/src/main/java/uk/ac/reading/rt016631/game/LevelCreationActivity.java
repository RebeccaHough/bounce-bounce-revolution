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
import android.widget.CheckedTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.FileOutputStream;

import static uk.ac.reading.rt016631.game.PlatformType.*;


public class LevelCreationActivity extends Activity {
    private PlatformType platformType;
    private boolean monsters, movingPlatforms, movingMonsters;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.level_creation_activity);

        //Labels
        final TextView titleText = (TextView) findViewById(R.id.levelCreationTitleText);
        final TextView creationText = (TextView) findViewById(R.id.levelCreationText);
        final TextView platformSelectText = (TextView) findViewById(R.id.platformSelectText);

        //Buttons
        final Button levelCreationSaveButton = (Button) findViewById(R.id.levelCreationSaveButton);
        final Button levelCreationBackButton = (Button) findViewById(R.id.levelCreationBackButton);

        //CheckedTextViews
        final CheckedTextView monstersChecked = (CheckedTextView) findViewById(R.id.monstersCheckedTextView);
        final CheckedTextView movingPlatformsChecked = (CheckedTextView) findViewById(R.id.movingPlatformsCheckedTextView);
        final CheckedTextView movingMonstersChecked = (CheckedTextView) findViewById(R.id.movingMonstersCheckedTextView);

        //SeekBar
        final SeekBar platformSelectBar = (SeekBar) findViewById(R.id.platformSelectBar);

        //Set style of text on labels and buttons
        Typeface font = Typeface.createFromAsset(getAssets(), "Luna.ttf");
        int headingFontSize = 35;
        int bodyFontSize = 25;
        int subtitleFontSize = 15;
        int colour = Color.WHITE;
        styleFont(titleText, font, headingFontSize, colour);
        styleFont(creationText, font, subtitleFontSize, colour);
        styleFont(platformSelectText, font, subtitleFontSize, colour);

        styleFont(monstersChecked, font, subtitleFontSize, colour);
        styleFont(movingPlatformsChecked, font, subtitleFontSize, colour);
        styleFont(movingMonstersChecked, font, subtitleFontSize, colour);

        styleFont(levelCreationSaveButton, font, bodyFontSize, colour);
        styleFont(levelCreationBackButton, font, bodyFontSize, colour);

        //Set default values
        monstersChecked.setChecked(true);
        platformType = PLATFORM_FOREST;
        monsters = true;
        movingPlatforms = false;
        movingMonsters = false;

        levelCreationSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current level params and save them
                String saveData = platformType.toString() + "," + monsters + "," + movingMonsters + "," + movingPlatforms;
                //attempt to write data to file
                try {
                    FileOutputStream oStream = openFileOutput("BounceBounceSave.txt", MODE_PRIVATE);
                    oStream.write(saveData.getBytes());
                    oStream.close();
                    //tell user the level was saved
                    AlertDialog.Builder builder = new AlertDialog.Builder(LevelCreationActivity.this);
                    builder.setMessage("Level saved.");
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
                } catch (Exception e) {
                    e.printStackTrace();
                    //tell user the level could not be saved
                    AlertDialog.Builder builder = new AlertDialog.Builder(LevelCreationActivity.this);
                    builder.setMessage("Level could not be saved.");
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
                }
            }
        });

        levelCreationBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start a new intent
                Intent showMainMenuScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(showMainMenuScreen);
            }
        });

        monstersChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monstersChecked.isChecked()) {
                    monstersChecked.setChecked(false);
                    //turn off the ability to make the monsters move since there are none
                    movingMonstersChecked.setChecked(false);
                    movingMonstersChecked.setEnabled(false);
                    //set text to off white to let users know this option is disabled
                    movingMonstersChecked.setAlpha(0.5f);
                    //set values to be written to file
                    monsters = false;
                    movingMonsters = false;
                } else {
                    monstersChecked.setChecked(true);
                    movingMonstersChecked.setEnabled(true);
                    movingMonstersChecked.setAlpha(1);
                    //set values to be written to file
                    monsters = true;
                    movingMonsters = false;
                }
            }
        });

        movingMonstersChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movingMonstersChecked.isChecked()) {
                    movingMonstersChecked.setChecked(false);
                    //set value to be written to file
                    movingMonsters = false;
                } else {
                    movingMonstersChecked.setChecked(true);
                    movingMonsters = true;
                }
            }
        });

        movingPlatformsChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movingPlatformsChecked.isChecked()) {
                    movingPlatformsChecked.setChecked(false);
                    //set value to be written to file
                    movingPlatforms = false;
                } else {
                    movingPlatformsChecked.setChecked(true);
                    //set value to be written to file
                    movingPlatforms = true;
                }
            }
        });

        platformSelectBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch(progress) {
                    case 0:
                        platformSelectText.setText(R.string.candy);
                        platformType = PLATFORM_CANDY;
                        break;
                    case 1:
                        platformSelectText.setText(R.string.snow);
                        platformType = PLATFORM_SNOW;
                        break;
                    case 2:
                        platformSelectText.setText(R.string.grass);
                        platformType = PLATFORM_GRASS;
                        break;
                    case 3:
                        platformSelectText.setText(R.string.desert);
                        platformType = PLATFORM_DESERT;
                        break;
                    case 4:
                        platformSelectText.setText(R.string.yellow);
                        platformType = PLATFORM_YELLOW;
                        break;
                    case 5:
                        platformSelectText.setText(R.string.forest);
                        platformType = PLATFORM_FOREST;
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Do nothing
            }
        });
    }

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
