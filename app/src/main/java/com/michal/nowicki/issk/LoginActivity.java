package com.michal.nowicki.issk;

import android.net.http.HttpResponseCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.CookieHandler;

public class LoginActivity extends AppCompatActivity {

    public Button button1;
    public static final String DATA = "data";

    public String returnData(String requiredData){
        EditText emailText = (EditText) findViewById(R.id.editEmail);
        EditText passText = (EditText) findViewById(R.id.editPass);
        switch (requiredData){
            case "email":
                return emailText.getText().toString().trim();
            case "pass":
                return passText.getText().toString().trim();
            default:
                return null;
        }
    }

    private void loginButton(){
        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Object data;
                TextView errText = (TextView) findViewById(R.id.textView2);
                String email = returnData("email");
                String pass = returnData("pass");

                if (!(email.equals("") || pass.equals(""))){
                    errText.setText("");
                    LoginActivityTask LAT = new LoginActivityTask();
                    try {
                        LAT.execute(email, pass);
                        data = LAT.get();

                        if(!((data.toString().contains("\"error\"")) || (data.toString().contains("Error")) || (data.toString().contains("Exception")))){
                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                            intent.putExtra(DATA, (data.toString()).substring(1, (data.toString().length() - 1)));
                            //errText.setText(data.toString());
                            startActivity(intent);
                            finish();
                        }
                        else if(data.toString().equals("Error: 1")){
                            errText.setText(R.string.loginerror_badpass);
                        }
                        else if(data.toString().equals("Error: 2")){
                            errText.setText(R.string.loginerror_database);
                        }
                        else if(data.toString().equals("Error: 3")){
                            errText.setText(R.string.loginerror);
                        }
                        else if(data.toString().contains("Exception:")){
                            errText.setText(R.string.loginexception);
                        }
                    }
                    catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e){
                        if (e.toString().contains("java.lang.InterruptedException")){
                            errText.setText(R.string.interrupted_login_exception);
                        }
                        else if (e.toString().contains("java.util.concurrent.ExecutionException")){
                            errText.setText(R.string.execution_login_exception);
                        }
                    }
                }
                else {
                    errText.setText(R.string.fill_every_edit);
                }
            }
        });
    }

    protected HttpResponseCache httpCacheFileCreate(){
        try {
            File httpCacheDir = new File(getApplicationContext().getCacheDir(), "http");
            long httpCacheSize = 5 * 1024 * 1024;
            return HttpResponseCache.install(httpCacheDir, httpCacheSize);
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(), "Nie udało się utworzyć pliku cache.", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        stringFromJNI();

        MainActivity MA = new MainActivity();
        CookieHandler.setDefault(MA.getCookieManager());

        loginButton();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
