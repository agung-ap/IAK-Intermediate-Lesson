package id.developer.agungaprian.constrainlayoutlesson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView teamA, teamB;
    private TextView scoreTeamA, scoreTeamB;
    private Button plus3TeamA, plus2TeamA, freeThrowTeamA,
            plus3TeamB, plus2TeamB, freeThrowTeamB,
            resetScore;
    private int teamAValue, teamBValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamA = (TextView)findViewById(R.id.title_team_a);
        teamB = (TextView)findViewById(R.id.title_team_b);

        //display team name
        teamA.setText(getIntent().getStringExtra("team_a").toUpperCase());
        teamB.setText(getIntent().getStringExtra("team_b").toUpperCase());
    }
}
