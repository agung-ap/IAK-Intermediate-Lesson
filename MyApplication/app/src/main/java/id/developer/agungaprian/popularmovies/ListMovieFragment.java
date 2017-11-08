package id.developer.agungaprian.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import id.developer.agungaprian.popularmovies.adapter.MovieAdapter;
import id.developer.agungaprian.popularmovies.models.MovieModel;
import id.developer.agungaprian.popularmovies.utils.JsonUtils;
import id.developer.agungaprian.popularmovies.utils.NetworkUtils;

/**
 * Created by agungaprian on 05/11/17.
 */

public class ListMovieFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private TextView errorMessageDisplay, refreshTextButton;
    private ImageButton refreshButton;

    private ProgressBar progressBar;

    private static String apiKey = "5dcd6ed59f6311eeeaeb846201f551b6";
    private static String rootUrl = "https://api.themoviedb.org/3/movie/popular?";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_movie, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_list_movie);

        errorMessageDisplay = (TextView)view.findViewById(R.id.error_message);
        refreshTextButton = (TextView)view.findViewById(R.id.refresh_text_button);

        refreshButton = (ImageButton)view.findViewById(R.id.refresh_button);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(getContext(), this);

        recyclerView.setAdapter(movieAdapter);

        progressBar = (ProgressBar)view.findViewById(R.id.pb_loading_indicator);

        loadMovieData();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovieData();
            }
        });
        return view;
    }


    private void loadMovieData() {
        showMovieDataView();
        //String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchMovieTask().execute();

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
        refreshTextButton.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        errorMessageDisplay.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        refreshTextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieModel moviePosition) {
        Bundle bundle = new Bundle();
        ArrayList<MovieModel> movieModels = new ArrayList<>();
        movieModels.add(moviePosition);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_RECIPE), movieModels);

        Intent intent = new Intent(this.getActivity(), DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class FetchMovieTask extends AsyncTask<String, Void, MovieModel[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieModel[] doInBackground(String... params) {
            NetworkUtils networkUtils = new NetworkUtils();

            String jsonData = networkUtils.makeServiceCall(NetworkUtils.buildUrl(rootUrl, apiKey));
            //cek json data kosong atau tidak
            Log.d("TAG" , "json data " + jsonData);

            if (jsonData != null){
                try {
                    MovieModel[] jsonMovieData = JsonUtils.getDataFromJson(jsonData);
                    return jsonMovieData;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieModel [] movieModels) {
            super.onPostExecute(movieModels);
            progressBar.setVisibility(View.INVISIBLE);
            if (movieModels != null) {
                showMovieDataView();

                movieAdapter.setMovieData(movieModels);
            }else {
                showErrorMessage();
            }
        }
    }
}
