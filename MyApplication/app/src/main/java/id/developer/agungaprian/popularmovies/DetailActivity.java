package id.developer.agungaprian.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.agungaprian.popularmovies.models.MovieModel;

/**
 * Created by agungaprian on 07/11/17.
 */

public class DetailActivity extends AppCompatActivity {
    private ArrayList<MovieModel> movieModels;
    private String movieTItle;
    static Context getContext;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_detail_movie);
        ButterKnife.bind(this);

        getContext = getApplicationContext();


        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();

            movieModels = new ArrayList<>();
            movieModels = getBundle.getParcelableArrayList(getString(R.string.GET_SELECTED_RECIPE));
            movieTItle = movieModels.get(0).getOriginalTitle();

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(getBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_movie_fragment, fragment)
                    .commit();
        }else {
            movieTItle = savedInstanceState.getString(getString(R.string.GET_SELECTED_RECIPE));
        }

    }

    public static Context getContextApplication(){
        return getContext;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                supportFinishAfterTransition();
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
