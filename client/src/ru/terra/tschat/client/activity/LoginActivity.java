package ru.terra.tschat.client.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import ru.terra.tschat.client.R;

/**
 * Date: 20.03.15
 * Time: 22:35
 */
public class LoginActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login);
    }

    public void loginOk(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(getString(R.string.login), ((EditText) findViewById(R.id.edtLogin)).getText().toString());
        editor.putString(getString(R.string.pass), ((EditText) findViewById(R.id.edtPass)).getText().toString());
        editor.commit();
        finish();
    }
}