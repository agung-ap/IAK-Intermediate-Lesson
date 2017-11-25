package id.developer.agungaprian.popularmovieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.agungaprian.popularmovieapp.adapter.TrailerAdapter;
import id.developer.agungaprian.popularmovieapp.model.MovieModel;
import id.developer.agungaprian.popularmovieapp.model.TrailerModel;
import id.developer.agungaprian.popularmovieapp.util.JsonUtil;
import id.developer.agungaprian.popularmovieapp.util.NetworkUtils;

/**
 * Created by agungaprian on 24/11/17.
 */

public class DetailMovieFragment extends Fragment {
    private ArrayList<MovieModel> movieModels = new ArrayList<>();
    TrailerModel[] trailerLIst ;
    String imageBackdropUrl;
    TrailerAdapter trailerAdapter;

    @BindView(R.id.toolbarImage)
    ImageView toolImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.overview)
    TextView overview;
    @BindView(R.id.releaseText)
    TextView releaseText;
    @BindView(R.id.trailersRecyclerView)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.reviewsRecyclerView)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.noReviewView)
    TextView noReviewView;
    @BindView(R.id.noTrailerView)
    TextView noTrailerView;
    @BindView(R.id.extras)
    LinearLayout extraLayout;


    private static String apiKey = "5dcd6ed59f6311eeeaeb846201f551b6";
    private static String rootUrl = "https://api.themoviedb.org/3/movie";
    private static String movieId;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, view);

        ((DetailMovieActivity)getActivity()).setSupportActionBar(toolbar);
        ((DetailMovieActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //allow menu in fragment
        setHasOptionsMenu(true);

        if (savedInstanceState != null){
            movieModels = savedInstanceState.getParcelableArrayList(getString(R.string.GET_MOVIE_DATA));
        }else {
            movieModels = getArguments().getParcelableArrayList(getString(R.string.GET_MOVIE_DATA));
        }

        movieId = String.valueOf(movieModels.get(0).getId());

        //add collapsing toolbar name
        collapsingToolbarLayout.setTitle(movieModels.get(0).getOriginalTitle());
        //add collapsing toolbar image
        Picasso.with(getContext())
                .load(movieModels.get(0).getBackdropPath())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.eror_image)
                .into(toolImage);
        //add movie poster
        Picasso.with(getContext())
                .load(movieModels.get(0).getPosterPath())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.eror_image)
                .into(imageView);
        //set title view
        titleView.setText(movieModels.get(0).getOriginalTitle());
        //set rating value
        rating.setText(String.valueOf ( movieModels.get(0).getVoteAverage() ) + " / 10");
        //set ratingbar value and maximum rating
        ratingBar.setMax(5);
        ratingBar.setRating((float) (movieModels.get(0).getVoteAverage() / 2f));
        //set overview value
        overview.setText(movieModels.get(0).getOverView());
        //set release date
        releaseText.setText("Release Date\n" + movieModels.get(0).getReleaseDate());

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());

        trailersRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerAdapter = new TrailerAdapter(getContext());
        trailersRecyclerView.setAdapter(trailerAdapter);

        loadTrailerData();
        return view;
    }

    public void loadTrailerData(){
        showTrailerData();
        new FetchTrailerTask().execute();
    }

    public void showTrailerData(){
        trailersRecyclerView.setVisibility(View.VISIBLE);
        noTrailerView.setVisibility(View.INVISIBLE);
    }

    public void showNoTrailerData(){
        trailersRecyclerView.setVisibility(View.INVISIBLE);
        noTrailerView.setVisibility(View.VISIBLE);
    }

    private class FetchTrailerTask extends AsyncTask<String , Void, TrailerModel[]> {

        @Override
        protected TrailerModel[] doInBackground(String... params) {
            NetworkUtils networkUtils = new NetworkUtils();

            String jsonData = networkUtils.makeServiceCall(NetworkUtils.trailerUrl(rootUrl, movieId, apiKey));
            if (jsonData != null){
                try {
                    TrailerModel[] jsonTrailerData = JsonUtil.getTrailerFromJson(jsonData);
                    return jsonTrailerData;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(TrailerModel[] trailerModels) {
            super.onPostExecute(trailerModels);
            if (trailerModels != null){
                showTrailerData();
                trailerAdapter.setTrailerData(trailerModels);
                trailerLIst = trailerModels;
            }else {
                showNoTrailerData();
            }
        }
    }
}
