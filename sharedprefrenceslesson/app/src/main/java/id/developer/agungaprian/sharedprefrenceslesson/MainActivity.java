package id.developer.agungaprian.sharedprefrenceslesson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button save, retrieve, clear;
    EditText name, email;

    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = (Button)findViewById(R.id.btnSave);
        retrieve = (Button)findViewById(R.id.btnRetr);
        clear = (Button)findViewById(R.id.btnClear);

        name = (EditText)findViewById(R.id.etName);
        email = (EditText)findViewById(R.id.etEmail);

        setSave();
        setClear();
        getData();
    }

    public void setSave(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveName = name.getText().toString();
                String saveEmail = email.getText().toString();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString(Name , saveName);
                editor.putString(Email, saveEmail);
                editor.apply();
            }
        });
    }

    public void setClear(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText("");
                email.setText("");
            }
        });
    }

    public void getData(){
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                if (preferences.contains(Name)){
                    name.setText(preferences.getString(Name, ""));
                }
                if (preferences.contains(Email)){
                    email.setText(preferences.getString(Email, ""));
                }
            }
        });
    }
}
