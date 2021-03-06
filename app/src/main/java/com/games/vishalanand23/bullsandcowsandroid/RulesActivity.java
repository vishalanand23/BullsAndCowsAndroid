package com.games.vishalanand23.bullsandcowsandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.games.vishalanand23.bullsandcowsandroid.db.DbStorageHelper;

public class RulesActivity extends AppCompatActivity {

    private Language language;

    private void styleActionBar() {
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.medium_blue)));
        Spannable text = new SpannableString(bar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        bar.setTitle(text);
    }

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
            Bundle sendBundle = new Bundle();
            sendBundle.putInt("numberOfDigits", 2);
            Intent i = new Intent(RulesActivity.this, ScoresActivity.class);
            i.putExtras(sendBundle);
            startActivity(i);
            finish();
            return true;
        }

        if (id == R.id.action_rules) {
            return true;
        }

        if (id == R.id.action_share) {
            startActivity(new ShareHandler(this).shareApp());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        styleActionBar();
        initializeCheckBox();
        language = Language.ENGLISH;
        initializeLanguageButton((Button) findViewById(R.id.english), Language.ENGLISH);
        findViewById(R.id.english).setEnabled(false);
        initializeLanguageButton((Button) findViewById(R.id.hindi), Language.HINDI);
//        initializeLanguageButton((Button) findViewById(R.id.spanish), Language.SPANISH);
        initializePlayBackButton();
        addRulesText();
    }

    private void addRulesText() {
        switch (language) {
            case ENGLISH:
                ((TextView) findViewById(R.id.actual_rules_pre))
                        .setText(getResources().getString(R.string.rules_in_english_pre));
                ((TextView) findViewById(R.id.actual_rules_post))
                        .setText(getResources().getString(R.string.rules_in_english_post));
                ((TextView) findViewById(R.id.common_0_right_pos))
                        .setText(getResources().getString(R.string.common_0_right_pos_english));
                ((TextView) findViewById(R.id.common_7_wrong_pos))
                        .setText(getResources().getString(R.string.common_7_wrong_pos_english));
                ((TextView) findViewById(R.id.no_digit_common_label_1))
                        .setText(getResources().getString(R.string.no_digit_common_english));
                ((TextView) findViewById(R.id.no_digit_common_label_2))
                        .setText(getResources().getString(R.string.no_digit_common_english));
                ((TextView) findViewById(R.id.no_digit_common_label_3))
                        .setText(getResources().getString(R.string.no_digit_common_english));
                ((TextView) findViewById(R.id.no_digit_common_label_4))
                        .setText(getResources().getString(R.string.no_digit_common_english));
                break;
            case HINDI:
                ((TextView) findViewById(R.id.actual_rules_pre))
                        .setText(getResources().getString(R.string.rules_in_hindi_pre));
                ((TextView) findViewById(R.id.actual_rules_post))
                        .setText(getResources().getString(R.string.rules_in_hindi_post));
                ((TextView) findViewById(R.id.common_0_right_pos))
                        .setText(getResources().getString(R.string.common_0_right_pos_hindi));
                ((TextView) findViewById(R.id.common_7_wrong_pos))
                        .setText(getResources().getString(R.string.common_7_wrong_pos_hindi));
                ((TextView) findViewById(R.id.no_digit_common_label_1))
                        .setText(getResources().getString(R.string.no_digit_common_hindi));
                ((TextView) findViewById(R.id.no_digit_common_label_2))
                        .setText(getResources().getString(R.string.no_digit_common_hindi));
                ((TextView) findViewById(R.id.no_digit_common_label_3))
                        .setText(getResources().getString(R.string.no_digit_common_hindi));
                ((TextView) findViewById(R.id.no_digit_common_label_4))
                        .setText(getResources().getString(R.string.no_digit_common_hindi));
                break;
//            case SPANISH:
//                rulesViewPre.setText(Html.fromHtml(getResources().getString(R.string.rules_in_spanish)));
//                break;
        }
    }

    private void initializeLanguageButton(final Button button, final Language l) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.english).setEnabled(true);
                findViewById(R.id.hindi).setEnabled(true);
//                findViewById(R.id.spanish).setEnabled(true);
                button.setEnabled(false);
                language = l;
                addRulesText();
            }
        });
    }

    private void initializeCheckBox() {
        final CheckBox checkBox = (CheckBox) findViewById(R.id.check_rules);
        final DbStorageHelper dbStorageHelper = new DbStorageHelper(this);
        checkBox.setChecked(dbStorageHelper.checkRules());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dbStorageHelper.setRulesCheckedTable(b);
            }
        });
    }

    private void initializePlayBackButton() {
        Button button = (Button) findViewById(R.id.play_game_in_rules);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private enum Language {
        ENGLISH, HINDI, SPANISH
    }

}
