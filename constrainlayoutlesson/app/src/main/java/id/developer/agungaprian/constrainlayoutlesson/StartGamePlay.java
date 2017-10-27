package id.developer.agungaprian.constrainlayoutlesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by agungaprian on 27/10/17.
 */

public class StartGamePlay extends AppCompatActivity {
    private EditText inputTeamA, inputTeamB;
    private Button startGame;
    private String teamA, teamB;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        inputTeamA = (EditText)findViewById(R.id.input_team_a);
        inputTeamB = (EditText)findViewById(R.id.input_team_b);

        startGame = (Button)findViewById(R.id.start_game);


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartGamePlay.this , MainActivity.class);
                intent.putExtra("team_a", String.valueOf(inputTeamA.getText().toString().trim()));
                intent.putExtra("team_b", String.valueOf(inputTeamB.getText().toString().trim()));

                startActivity(intent);
            }
        });
    }
}
