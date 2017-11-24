package id.developer.agungaprian.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import id.developer.agungaprian.popularmovies.adapter.MovieAdapter;
import id.developer.agungaprian.popularmovies.models.MovieModel;
import id.developer.agungaprian.popularmovies.utils.JsonUtils;
import id.developer.agungaprian.popularmovies.utils.NetworkUtils;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by agungaprian on 05/11/17.
 */

public class ListMovieFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private TextView errorMessageDisplay, refreshTextButton;
    private ImageButton refreshButton;

    private ProgressBar progressBar;

    private LayoutAnimationController animation;

    Menu menu;
    Realm realm;

    private static String apiKey = "5dcd6ed59f6311eeeaeb846201f551b6";
    private static String popularUrl = "https://api.themoviedb.org/3/movie/popular?";
    private static String topRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_movie, container, false);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_list_movie);

        errorMessageDisplay = (TextView)view.findViewById(R.id.error_message);
        refreshTextButton = (TextView)view.findViewById(R.id.refresh_text_button);

        refreshButton = (ImageButton)view.findViewById(R.id.refresh_button);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(getContext(),2);
        animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(getContext(), this);

        recyclerView.setAdapter(movieAdapter);

        progressBar = (ProgressBar)view.findViewById(R.id.pb_loading_indicator);



        if (getSortMethod().equals(getString(R.string.SORT_BY_POPULAR))){
            loadMovieData(popularUrl);
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Popular");
        }else {
            loadMovieData(topRatedUrl);
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
        }

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadMovieData();
            }
        });

        return view;
    }


    private void loadMovieData(String rootUrl) {
        showMovieDataView();
        FetchMovieTask movieTask = new FetchMovieTask(rootUrl);
        movieTask.execute();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_by_popular:
                updateSharedPrefs(getString(R.string.SORT_BY_POPULAR));
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Popular");
                loadMovieData(popularUrl);
                return true;
            case R.id.sort_by_top_rated:
                updateSharedPrefs(getString(R.string.SORT_BY_TOP_RATED));
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
                loadMovieData(topRatedUrl);
                return true;
            case R.id.sort_by_favourite:
                updateSharedPrefs(getString(R.string.SORT_BY_FAVOURITE));
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("Favourite");
                getFavourites();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getFavourites(){
        RealmResults<MovieModel> realmResults = realm.where(MovieModel.class).findAll();
        MovieModel [] movieModels = new MovieModel[realmResults.size()];

        for (int i = 0; i < realmResults.size(); i++){
            movieModels [i] = new MovieModel();

        }

        movieAdapter.setMovieData(movieModels);
   }

    private String getSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return prefs.getString(getString(R.string.PREF_SORT_METHOD_KEY),
                getString(R.string.SORT_BY_POPULAR));
    }


    private void updateSharedPrefs(String sortMethod) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.PREF_SORT_METHOD_KEY), sortMethod);
        editor.apply();
    }

    private class FetchMovieTask extends AsyncTask<String, Void, MovieModel[]> {
        String rootUrl;

        public FetchMovieTask(String rootUrl) {
            this.rootUrl = rootUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieModel[] doInBackground(String... params) {
            NetworkUtils networkUtils = new NetworkUtils();

            String jsonData = networkUtils.makeServiceCall(NetworkUtils.movieUrl(rootUrl, apiKey));
            //cek json data kosong atau tidak
            Log.d("TAG" , "json data " + jsonData);

            if (jsonData != null){
                try {
                    MovieModel[] jsonMovieData = JsonUtils.getMovieFromJson(jsonData);
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
