package com.michal.nowicki.issk;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieHandler;

public class LoginActivity extends AppCompatActivity {

    public Button button1;
    public static final String DATA = "data";

    String returnData(String requiredData){
        EditText emailText = findViewById(R.id.editEmail);
        EditText passText = findViewById(R.id.editPass);
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
        Object[] data;
        TextView errText = findViewById(R.id.textView2);
        String email = returnData("email");
        String pass = returnData("pass");

        if(!(email.equals("") || pass.equals(""))){
            errText.setText("");
            CommonInternetTask CIT = new CommonInternetTask();
            try {
                CIT.execute("login", true, new Object[]{"mail", "haslo"}, new Object[]{email, pass});
                data = CIT.get();

                if(!(boolean) data[0]){
                    //DatabaseHandler.update

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra(DATA, (data[1].toString()).substring(1, (data[1].toString().length() - 1)));
                    startActivity(intent);
                    finish();
                }
                else if(data[1].toString().contains("\"error\":\"Błędny login lub hasło!\"")){
                    errText.setText(R.string.loginerror_badpass);
                }
                else if(data[1].toString().contains("\"error\":\"Błąd połączenia z bazą danych!\"") || data[1].toString().contains("Access denied for user")){
                    errText.setText(R.string.database_error);
                }
                else {
                    errText.setText(R.string.loginerror);
                    Toast.makeText(getApplicationContext(), (String) data[1], Toast.LENGTH_LONG).show();
                }
            }
            catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e){
                if (e.toString().contains("java.lang.InterruptedException")){
                    errText.setText(R.string.interrupted_exception);
                }
                else if (e.toString().contains("java.util.concurrent.ExecutionException")){
                    errText.setText(R.string.execution_exception);
                }
            }
        }
        else {
            errText.setText(R.string.fill_every_edit);
        }
    }

    private void openDatabaseFillEmailEdit(){
        String result = DatabaseHandler.readFromDatabase(new String[]{"settings", "rownum < 2", null, null, null}, new String[][]{{"email"}, {null}}, getApplication().getFilesDir().toString());
        if(result != null) {
            EditText emailText = findViewById(R.id.editEmail);
            emailText.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CookieHandler.setDefault(MainActivity.getCookieManager());

        //openDatabaseFillEmailEdit();

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton();
            }
        });
    }
}
