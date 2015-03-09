package com.acpm.certpinning;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;


public class HttpUtil {

    static final int LOGIN = 0;
    static final int CHANGE = 1;

    public static void Request(final Context ctx, final String urlp, final String params, final String cookie, final int action) {

        new Thread(new Runnable()

        {
            public void run() {

                try {

                    byte[] postData = params.getBytes(Charset.forName("UTF-8"));
                    int postDataLength = postData.length;

                    URL url = new URL(urlp);

                    TrustManager tm[] = {new PubKeyManager()};

                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tm, null);

                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setSSLSocketFactory(context.getSocketFactory());

                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                    connection.setUseCaches(false);
                    connection.setRequestProperty("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.1.1; Galaxy X Build/JRO03C");
                    if (cookie != null && cookie.trim() != "") {
                        connection.setRequestProperty("Cookie", cookie);
                    }

                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.write(postData);
                    InputStreamReader instream = new InputStreamReader(connection.getInputStream());

                    int statusCode = connection.getResponseCode();

                    if (statusCode == 200) {

                        if (action == LOGIN) {

                            SharedPreferences pref = ctx.getSharedPreferences("prefs", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();

                            List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
                            String nametmp[] = cookies.get(1).trim().split(";");
                            String name = nametmp[0].trim().split("=")[1];

                            String cookie = cookies.get(0).trim().split(";")[0];

                            editor.putString("session", cookie);
                            editor.putString("name", name);

                            editor.commit();

                            Intent unlockInt = new Intent(ctx, Admin.class);
                            unlockInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(unlockInt);
                        }
                        System.out.println("200 OK");
                    }

                } catch (Exception ex) {

                    SharedPreferences pref = ctx.getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Error", ex.toString());
                    editor.commit();

                    if(action==HttpUtil.LOGIN) {
                        Intent unlockInt = new Intent(ctx, CertPinning.class);
                        unlockInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(unlockInt);
                    }else if(action==HttpUtil.CHANGE)
                    {
                        Intent unlockInt = new Intent(ctx, Admin.class);
                        unlockInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(unlockInt);
                    }
                    // Log error
                    Log.e("doInBackground", ex.toString());

                }
            }

        }).start();
    }
}
