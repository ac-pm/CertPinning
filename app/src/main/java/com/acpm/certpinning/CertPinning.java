package com.acpm.certpinning;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;


public class CertPinning extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cert_pinning);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = pref.edit();

        String error = pref.getString("Error", null);
        editor.putString("Error", "");
        editor.commit();
        if(error != null && error != "") {
            TextView successtxt = (TextView) findViewById(R.id.error_txt);
            successtxt.setText(error);
        }
    }

    public boolean login(View button) {
        final EditText loginField = (EditText) findViewById(R.id.login_txt);
        String login = loginField.getText().toString();
        final EditText passwField = (EditText) findViewById(R.id.pass_txt);
        String password = passwField.getText().toString();

        String url = "https://exemplo-cert-pinning.herokuapp.com/login.php";
        String parameters = "name="+login+"&pass="+password+"";
        HttpUtil.Request(getApplicationContext(), url, parameters, "", HttpUtil.LOGIN);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cert_pinning, menu);
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
