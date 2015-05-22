package com.games.vishalanand23.bullsandcowsandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.games.vishalanand23.bullsandcowsandroid.db.DbStorageHelper;

public class ScoresActivity extends AppCompatActivity {

    private int numberOfDigits = 2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action postRequest if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action postRequest item clicks here. The action postRequest will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scores) {
            setValues(2);
            findViewById(R.id.score_button_2).setEnabled(false);
            findViewById(R.id.score_button_3).setEnabled(true);
            findViewById(R.id.score_button_4).setEnabled(true);
            findViewById(R.id.score_button_5).setEnabled(true);
            findViewById(R.id.score_button_6).setEnabled(true);
            return true;
        }

        if (id == R.id.action_rules) {
            Intent i = new Intent(ScoresActivity.this, RulesActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Bundle extras = getIntent().getExtras();
        numberOfDigits = extras.getInt("numberOfDigits");
        setValues(numberOfDigits);

        initializeButton(2, (Button) findViewById(R.id.score_button_2));
        initializeButton(3, (Button) findViewById(R.id.score_button_3));
        initializeButton(4, (Button) findViewById(R.id.score_button_4));
        initializeButton(5, (Button) findViewById(R.id.score_button_5));
        initializeButton(6, (Button) findViewById(R.id.score_button_6));

        initializePlayBackButton((Button) findViewById(R.id.play_game_in_scores));
        switch (numberOfDigits) {
            case 2:
                findViewById(R.id.score_button_2).setEnabled(false);
                break;
            case 3:
                findViewById(R.id.score_button_3).setEnabled(false);
                break;
            case 4:
                findViewById(R.id.score_button_4).setEnabled(false);
                break;
            case 5:
                findViewById(R.id.score_button_5).setEnabled(false);
                break;
            case 6:
                findViewById(R.id.score_button_6).setEnabled(false);
                break;
            default:
        }
    }

    private void setValues(int number) {
        DbStorageHelper storageHelper = new DbStorageHelper(this);
        int gamesPlayed = storageHelper.numberOfGames(number);
        int gamesWon = storageHelper.numberOfWins(number);
        String fastestTimeString = storageHelper.fastestTime(number) < 0
                ? "N.A." : String.valueOf((storageHelper.fastestTime(number)) / 1000f);
        String scoreString = storageHelper.fastestTime(number) < 0
                ? "N.A." : String.valueOf((storageHelper.score(number)) / 1000f);
        String scoreExplanation = "Score for " + number +
                " digits is average time of fastest 50 games won for " + number + " digits.";

        ((TextView) findViewById(R.id.number_of_digits_value)).setText(String.valueOf(number));
        ((TextView) findViewById(R.id.games_played_value)).setText(String.valueOf(gamesPlayed));
        ((TextView) findViewById(R.id.games_won_value)).setText(String.valueOf(gamesWon));
        ((TextView) findViewById(R.id.fastest_time_value)).setText(fastestTimeString);
        ((TextView) findViewById(R.id.scores_value)).setText(scoreString);
        ((TextView) findViewById(R.id.scores_explanation)).setText(scoreExplanation);
    }

    private void initializePlayBackButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initializeButton(final int number, final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValues(number);
                findViewById(R.id.score_button_2).setEnabled(true);
                findViewById(R.id.score_button_3).setEnabled(true);
                findViewById(R.id.score_button_4).setEnabled(true);
                findViewById(R.id.score_button_5).setEnabled(true);
                findViewById(R.id.score_button_6).setEnabled(true);
                button.setEnabled(false);
            }
        });
    }
}