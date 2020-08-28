package uk.ac.reading.rt016631.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HighScoresActivity extends Activity {
    private ArrayList<HighScore> highScores;
    private ListView highScoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.high_scores_activity);

        //Labels
        final TextView highScoresText = (TextView) findViewById(R.id.highScoresText);

        //Buttons
        final Button backButton = (Button) findViewById(R.id.highScoresBackButton);

        //ListView
        highScoresList = (ListView) findViewById(R.id.highScoresList);

        //Set style of text on buttons
        Typeface font = Typeface.createFromAsset(getAssets(), "Luna.ttf");
        int headingFontSize = 35;
        int bodyFontSize = 25;
        int colour = Color.WHITE;
        styleFont(highScoresText, font, headingFontSize, colour);
        styleFont(backButton, font, bodyFontSize, colour);

        //get highscores as HighScore objects
        Type type = new TypeToken<ArrayList<HighScore>>() {}.getType();
        highScores = (ArrayList<HighScore>) HighScoresClient.readURLSerializable(type, "highScores.json");

        //turn highscores into listview item rows that populate the listview
        populateListView();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to menu
                Intent showMainMenuScreen = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(showMainMenuScreen);
            }
        });
    }

    /**
     * Load a list view with names and high scores
     */
    private void populateListView() {
        ScoresAdapter listAdapter = new ScoresAdapter(this, R.layout.high_scores_listview_item_row, highScores, getAssets());
        highScoresList.setAdapter(listAdapter);
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

class ScoresAdapter extends ArrayAdapter<HighScore> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<HighScore> highScores = null;
    private Typeface font;
    private int fontSize;
    private int colour;

    /**
     * ScoresAdapter constructor
     * @param context the context
     * @param layoutResourceId the id of the layout the adapter will be used for
     * @param highScores the ArrayList of HighScore objects that will be displayed in the ListView
     */
    public ScoresAdapter(Context context, int layoutResourceId, ArrayList<HighScore> highScores, AssetManager assets) {
        super(context, layoutResourceId, highScores);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.highScores = highScores;
        this.font = Typeface.createFromAsset(assets, "Luna.ttf");;
        this.fontSize = 25;
        this.colour = Color.WHITE;
    }

    /**
     * Set up the row at {@code position} in ListView with name and score
     * @param position the row to be set up
     * @param convertView the View to convert
     * @param parent the parent ViewGroup
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HighScoreHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new HighScoreHolder();
            holder.highScoresRowName = (TextView)row.findViewById(R.id.highScoresRowName);
            holder.highScoresRowScore = (TextView)row.findViewById(R.id.highScoresRowScore);

            row.setTag(holder);
        }
        else
        {
            holder = (HighScoreHolder)row.getTag();
        }

        HighScore highScore = highScores.get(position);
        holder.highScoresRowName.setText(highScore.name);
        styleFont(holder.highScoresRowName, font, fontSize, colour);
        holder.highScoresRowScore.setText(String.valueOf(highScore.score));
        styleFont(holder.highScoresRowScore, font, fontSize, colour);

        return row;
    }

    private static class HighScoreHolder
    {
        TextView highScoresRowName;
        TextView highScoresRowScore;
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
