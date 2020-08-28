package uk.ac.reading.rt016631.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainMenuActivity extends Activity {

    //TODO end screen, showing score, option to restart game and option to return to main menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.main_menu_activity);

        //Labels
        final TextView titleText = (TextView) findViewById(R.id.menuTitleText);

        //Buttons
        final Button levelSelectBtn = (Button) findViewById(R.id.levelSelectButton);
        final Button highScoresBtn = (Button) findViewById(R.id.highScoreListButton);
        final Button levelCreationBtn = (Button) findViewById(R.id.levelCreationButton);

        //Set style of text on labels and buttons
        Typeface font = Typeface.createFromAsset(getAssets(), "Luna.ttf");
        int headingFontSize = 35;
        int bodyFontSize = 25;
        int colour = Color.WHITE;
        styleFont(titleText, font, headingFontSize, colour);
        styleFont(levelSelectBtn, font, bodyFontSize, colour);
        styleFont(highScoresBtn, font, bodyFontSize, colour);
        styleFont(levelCreationBtn, font, bodyFontSize, colour);

        //start game button
        levelSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start a new intent
                Intent showGameScreen = new Intent(getApplicationContext(), LevelSelectActivity.class);
                startActivity(showGameScreen);
            }
        });

        //high scores button
        highScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start a new intent
                Intent showHighScoresScreen = new Intent(getApplicationContext(), HighScoresActivity.class);
                startActivity(showHighScoresScreen);
            }
        });

        //user level creation button
        levelCreationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start a new intent
                Intent showLevelCreationScreen = new Intent(getApplicationContext(), LevelCreationActivity.class);
                startActivity(showLevelCreationScreen);
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
