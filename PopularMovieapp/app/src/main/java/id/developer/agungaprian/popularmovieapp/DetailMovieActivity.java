package id.developer.agungaprian.popularmovieapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import id.developer.agungaprian.popularmovieapp.model.MovieModel;

/**
 * Created by agungaprian on 24/11/17.
 */

public class DetailMovieActivity extends AppCompatActivity{
    private ArrayList<MovieModel> movieModels;
    private String movieTitle;
    static Context getContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        getContext = getApplicationContext();

        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();

            movieModels = new ArrayList<>();
            movieModels = getBundle.getParcelableArrayList(getString(R.string.GET_MOVIE_DATA));

            movieTitle = movieModels.get(0).getOriginalTitle();

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(getBundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_movie_fragment, fragment)
                    .commit();
        }else {
            movieTitle = savedInstanceState.getString(getString(R.string.GET_MOVIE_DATA));
        }
    }

    public Context getApplicationContext(){
        return getContext;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                supportFinishAfterTransition();
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
