package id.developer.agungaprian.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import id.developer.agungaprian.popularmovies.models.MovieModel;

/**
 * Created by agungaprian on 07/11/17.
 */

public class DetailMovieFragment extends Fragment {
    private ArrayList<MovieModel> movieModels = new ArrayList<>();
    String imageBackdropUrl;

    @BindView(R.id.toolbarImage)
    ImageView toolImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        ButterKnife.bind(this, view);

        ((DetailActivity)getActivity()).setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            movieModels = savedInstanceState.getParcelableArrayList(getString(R.string.GET_SELECTED_RECIPE));
        }else {
            movieModels = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_RECIPE));
        }
            imageBackdropUrl = movieModels.get(0).getBackdropPath();
            Picasso.with(getContext())
                    .load(imageBackdropUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.eror_image)
                    .into(toolImage);

        return view;
    }
}
