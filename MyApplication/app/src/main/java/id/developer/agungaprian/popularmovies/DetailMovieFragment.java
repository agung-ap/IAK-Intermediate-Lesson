package id.developer.agungaprian.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import id.developer.agungaprian.popularmovies.adapter.TrailerAdapter;
import id.developer.agungaprian.popularmovies.models.MovieModel;
import id.developer.agungaprian.popularmovies.models.TrailerModel;
import id.developer.agungaprian.popularmovies.utils.JsonUtils;
import id.developer.agungaprian.popularmovies.utils.NetworkUtils;
import io.realm.Realm;

/**
 * Created by agungaprian on 07/11/17.
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

    private Menu mMenu;
    Realm realm = Realm.getDefaultInstance();

    private static String apiKey = "5dcd6ed59f6311eeeaeb846201f551b6";
    private static String rootUrl = "https://api.themoviedb.org/3/movie";
    private static String movieId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        ((DetailActivity)getActivity()).setSupportActionBar(toolbar);
        ((DetailActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        if (savedInstanceState != null){
            movieModels = savedInstanceState.getParcelableArrayList(getString(R.string.GET_SELECTED_RECIPE));
        }else {
            movieModels = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_RECIPE));
        }

            movieId = String.valueOf(movieModels.get(0).getId());

            collapsingToolbarLayout.setTitle(movieModels.get(0).getOriginalTitle());


            imageBackdropUrl = movieModels.get(0).getBackdropPath();
            Picasso.with(getContext())
                    .load(imageBackdropUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.eror_image)
                    .into(toolImage);

            titleView.setText(movieModels.get(0).getOriginalTitle());

            Picasso.with(getActivity())
                .load(movieModels.get(0).getPosterPath())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.eror_image)
                .into(imageView);

            rating.setText(String.valueOf(movieModels.get(0).getVoteAverage()).concat("/10"));
            ratingBar.setMax(5);
            ratingBar.setRating((float) (movieModels.get(0).getVoteAverage() / 2f));

            overview.setText(movieModels.get(0).getOverView());
            releaseText.setText("Release Date: ".concat(movieModels.get(0).getReleaseDate()));

            LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());

            trailersRecyclerView.setLayoutManager(trailerLayoutManager);
            trailerAdapter = new TrailerAdapter(getContext());
            trailersRecyclerView.setAdapter(trailerAdapter);

            loadTrailerData();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_movie, menu);
        mMenu = menu;

        super.onCreateOptionsMenu(mMenu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.set_as_favourite:
                if (realm.isInTransaction()){
                    realm.cancelTransaction();
                }
                if (!isFavourite()){
                    realm.beginTransaction();
                    item.setIcon(R.drawable.favourite);

                    realm.copyToRealm(movieModels);
                    realm.commitTransaction();

                }else {
                    realm.beginTransaction();
                    item.setIcon(R.drawable.non_favourite);
                    realm.where(MovieModel.class)
                            .contains("id" , String.valueOf(movieModels.get(0).getId()))
                            .findFirst()
                            .deleteFromRealm();
                    realm.commitTransaction();
                }
                break;
            case R.id.share_movie:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, movieModels.get(0).getOriginalTitle());
                share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=".concat(trailerLIst[0].getKey()));
                startActivity(Intent.createChooser(share, "Share Trailer"));

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isFavourite(){
        return realm.where(MovieModel.class)
                .contains("id", String.valueOf(movieModels.get(0).getId()))
                .findAll()
                .size() != 0;
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

    private class FetchTrailerTask extends AsyncTask<String , Void, TrailerModel[]>{

        @Override
        protected TrailerModel[] doInBackground(String... params) {
            NetworkUtils networkUtils = new NetworkUtils();

            String jsonData = networkUtils.makeServiceCall(NetworkUtils.trailerUrl(rootUrl, movieId, apiKey));
            if (jsonData != null){
                try {
                    TrailerModel[] jsonTrailerData = JsonUtils.getTrailerFromJson(jsonData);
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
