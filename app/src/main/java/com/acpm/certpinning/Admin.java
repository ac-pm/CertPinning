package com.acpm.certpinning;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;


public class Admin extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = pref.edit();

        String name = pref.getString("name", null);

        if(name != null && name != "") {
            TextView successtxt = (TextView) findViewById(R.id.name_txt);
            successtxt.setText("Bem vindo, "+name.toUpperCase()+"!");
        }

        String error = pref.getString("Error", null);
        editor.putString("Error", "");
        editor.commit();
        if(error != null && error != "") {
            TextView successtxt = (TextView) findViewById(R.id.error_adm_txt);
            successtxt.setText(error);
        }

    }

    public boolean change(View button) {
        final EditText agField = (EditText) findViewById(R.id.ag_txt);
        String ag = agField.getText().toString();
        final EditText ccField = (EditText) findViewById(R.id.cc_txt);
        String cc = ccField.getText().toString();
        final EditText passField = (EditText) findViewById(R.id.passw_txt);
        String passwd = passField.getText().toString();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = pref.edit();

        String cookie = pref.getString("session", null);

        String url = "https://exemplo-cert-pinning.herokuapp.com/update-account.php";
        String parameters = "agencia=" + ag + "&cc=" + cc + "&password=" + passwd;
        HttpUtil.Request(getApplicationContext(), url, parameters, cookie, HttpUtil.CHANGE);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
