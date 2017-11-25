package id.developer.agungaprian.popularmovieapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class ListMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        if (savedInstanceState == null){
            //define objek kelas fragmenya
            ListMovieFragment fragmet = new ListMovieFragment();
            //define objek fragment manager
            FragmentManager fragmentManager = getSupportFragmentManager();

            //panggil begintransaction method dengan objek fragmentManager
            fragmentManager.beginTransaction()
                    .add(/*id containernya*/R.id.movie_fragment_container,
                            /*kelas fragmen yang
                            * akan dipanggil*/fragmet)
                    .commit();
        }
    }
}
