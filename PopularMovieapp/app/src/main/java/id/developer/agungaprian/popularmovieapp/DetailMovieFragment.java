package id.developer.agungaprian.popularmovieapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.agungaprian.popularmovieapp.model.MovieModel;

/**
 * Created by agungaprian on 24/11/17.
 */

public class DetailMovieFragment extends Fragment {
    private ArrayList<MovieModel> movieModels = new ArrayList<>();
    //TrailerModel[] trailerLIst ;
    String imageBackdropUrl;
    //TrailerAdapter trailerAdapter;

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
        rating.setText(String.valueOf ( movieModels.get(0).getVoteAverage() ) );
        //set ratingbar value and maximum rating
        ratingBar.setMax(5);
        ratingBar.setRating((float) (movieModels.get(0).getVoteAverage() / 2f));
        //set overview value
        overview.setText(movieModels.get(0).getOverView());
        //set release date
        releaseText.setText("Release Date\n" + movieModels.get(0).getReleaseDate());
        return view;
    }
}
