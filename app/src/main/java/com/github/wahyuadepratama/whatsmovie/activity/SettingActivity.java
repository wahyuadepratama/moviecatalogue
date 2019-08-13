package com.github.wahyuadepratama.whatsmovie.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.wahyuadepratama.whatsmovie.R;
import com.github.wahyuadepratama.whatsmovie.receiver.DailyReminderApps;
import com.github.wahyuadepratama.whatsmovie.receiver.DailyReminderMovie;
import com.github.wahyuadepratama.whatsmovie.utils.Interfaces;
import com.onurkaganaldemir.ktoastlib.KToast;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {

    private DailyReminderApps appsAlarm = new DailyReminderApps();
    private DailyReminderMovie movieAlarm = new DailyReminderMovie();
    private Boolean currentSettingApplication, currentSettingMovie;
    private Switch switchApp, switchMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        customActionBar();

        checkCurrentSettingReminderApplication();
        checkCurrentSettingReminderMovie();
        settingReminderApplicationListener();
        settingReminderMovieListener();
    }

    private void customActionBar(){
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingReminderApplicationListener(){
        switchApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    appsAlarm.setRepeatingAlarm(SettingActivity.this, "We miss you.. Please Comeback");

                    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putInt(Interfaces.SETTING_REMINDER_APPLICATION, Interfaces.STATUS_SETTING_COMEBACK_ACTIVE).apply();

                    TextView status = findViewById(R.id.status_application);
                    status.setText(getResources().getString(R.string.on));

                    KToast.successToast(SettingActivity.this, "Daily Reminder is Turn On", Gravity.BOTTOM, KToast.LENGTH_LONG);
                }else{
                    appsAlarm.cancelAlarm(SettingActivity.this);

                    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putInt(Interfaces.SETTING_REMINDER_APPLICATION, Interfaces.STATUS_SETTING_NON_ACTIVE).apply();

                    TextView status = findViewById(R.id.status_application);
                    status.setText(getResources().getString(R.string.off));

                    KToast.infoToast(SettingActivity.this, "Daily Reminder is Turn Off", Gravity.BOTTOM, KToast.LENGTH_SHORT);
                }
            }
        });
    }

    private void settingReminderMovieListener(){
        switchMovie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    movieAlarm.setRepeatingAlarm(SettingActivity.this, "We miss you.. Please Comeback");

                    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putInt(Interfaces.SETTING_REMINDER_MOVIE, Interfaces.STATUS_SETTING_MOVIES_ACTIVE).apply();

                    TextView status = findViewById(R.id.status_movie);
                    status.setText(getResources().getString(R.string.on));

                    KToast.successToast(SettingActivity.this, "Movie Release Reminder is Turn On", Gravity.BOTTOM, KToast.LENGTH_LONG);
                }else{
                    movieAlarm.cancelAlarm(SettingActivity.this);

                    SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
                    editor.putInt(Interfaces.SETTING_REMINDER_MOVIE, Interfaces.STATUS_SETTING_NON_ACTIVE).apply();

                    TextView status = findViewById(R.id.status_movie);
                    status.setText(getResources().getString(R.string.off));

                    KToast.infoToast(SettingActivity.this, "Movie Release Reminder is Turn Off", Gravity.BOTTOM, KToast.LENGTH_SHORT);
                }
            }
        });
    }

    private void checkCurrentSettingReminderApplication(){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int mode = preferences.getInt(Interfaces.SETTING_REMINDER_APPLICATION, Interfaces.STATUS_SETTING_NON_ACTIVE);
        TextView status = findViewById(R.id.status_application);
        switch (mode){
            case(Interfaces.STATUS_SETTING_COMEBACK_ACTIVE):
                currentSettingApplication = true;
                status.setText(getResources().getString(R.string.on));
                break;
            case(Interfaces.STATUS_SETTING_NON_ACTIVE):
                currentSettingApplication = false;
                status.setText(getResources().getString(R.string.off));
                break;
        }

        switchApp = findViewById(R.id.sw_app);
        switchApp.setChecked(currentSettingApplication);
    }

    private void checkCurrentSettingReminderMovie(){
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int mode = preferences.getInt(Interfaces.SETTING_REMINDER_MOVIE, Interfaces.STATUS_SETTING_NON_ACTIVE);
        TextView status = findViewById(R.id.status_movie);
        switch (mode){
            case(Interfaces.STATUS_SETTING_MOVIES_ACTIVE):
                currentSettingMovie = true;
                status.setText(getResources().getString(R.string.on));
                break;
            case(Interfaces.STATUS_SETTING_NON_ACTIVE):
                currentSettingMovie = false;
                status.setText(getResources().getString(R.string.off));
                break;
        }

        switchMovie = findViewById(R.id.sw_movie);
        switchMovie.setChecked(currentSettingMovie);
    }
}
