package com.michal.nowicki.issk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class NoticeEditActivity extends AppCompatActivity {
    private String id;

    private String[] getEditData(){
        EditText edit_title = findViewById(R.id.edit_title);
        EditText edit_content = findViewById(R.id.edit_content);
        EditText edit_date = findViewById(R.id.date_picker);

        return new String[] {edit_title.getText().toString().trim(), edit_content.getText().toString().trim(), edit_date.getText().toString().trim()};
    }

    private void sendButton(){
        Button sendBttn = findViewById(R.id.bttn_send);

        sendBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] editData = getEditData();

                if(!(editData[0].equals("") || editData[1].equals("") || editData[2].equals(""))){
                    NoticeEditActivityTask NEAT = new NoticeEditActivityTask();

                    if(id != null)
                        NEAT.execute(new String[] { getEditData()[0], getEditData()[1], getEditData()[2], id });
                    else
                        NEAT.execute(getEditData());

                    try {
                        String data = NEAT.get();

                        if((data.equals("false")))
                            finish();
                        else
                            Toast.makeText(getBaseContext(), data, Toast.LENGTH_LONG).show();
                    }
                    catch(InterruptedException | ExecutionException e){
                        Toast.makeText(getBaseContext(), "Exception: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_edit);

        TextView info_logged = findViewById(R.id.info_logged);
        info_logged.setText(getResources().getString(R.string.logged_as, MainActivity.getPermsString().split(",")[0]));

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        if(data != null){
            try {
                EditText edit_title = findViewById(R.id.edit_title);
                EditText edit_content = findViewById(R.id.edit_content);
                EditText edit_date = findViewById(R.id.date_picker);
                JSONObject jsonObjectData = new JSONObject(data);

                edit_title.setText(jsonObjectData.optString("title"));
                edit_content.setText(jsonObjectData.optString("content"));
                edit_date.setText(BasicMethods.StringDateToNumberDate(jsonObjectData.optString("archive")));
                id = jsonObjectData.optString("id");
            }
            catch (JSONException e) {
                Toast.makeText(getBaseContext(), "JSONObject parsing error!", Toast.LENGTH_LONG).show();
            }
        }

        sendButton();
    }
}
