package ru.terra.tschat.client.activity;

import android.app.Activity;
import android.os.Bundle;
import roboguice.activity.RoboActivity;
import ru.terra.tschat.client.R;

/**
 * Date: 22.03.15
 * Time: 17:48
 */
public class RegActivity extends RoboActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_reg);
    }
}