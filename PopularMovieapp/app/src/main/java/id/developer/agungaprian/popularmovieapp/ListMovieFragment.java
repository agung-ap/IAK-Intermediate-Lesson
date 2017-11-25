package id.developer.agungaprian.popularmovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import id.developer.agungaprian.popularmovieapp.adapter.ListMovieAdapter;
import id.developer.agungaprian.popularmovieapp.model.MovieModel;
import id.developer.agungaprian.popularmovieapp.util.JsonUtil;
import id.developer.agungaprian.popularmovieapp.util.NetworkUtils;

/**
 * Created by agungaprian on 18/11/17.
 */

public class ListMovieFragment extends Fragment implements ListMovieAdapter.MovieAdapterOnClickHandler{

    private ListMovieAdapter adapter;

    RecyclerView recyclerView;
    TextView errorMessageDisplay;
    ImageButton refreshButton;
    ProgressBar progressBar;

    private static String apiKey = "5dcd6ed59f6311eeeaeb846201f551b6";
    private static String popularUrl = "https://api.themoviedb.org/3/movie/popular?";
    private static String topRatedUrl = "https://api.themoviedb.org/3/movie/top_rated?";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_movie_fragment, container, false);
        //allow menu in fragment
        setHasOptionsMenu(true);

        errorMessageDisplay = (TextView)view.findViewById(R.id.eror_message);

        refreshButton = (ImageButton)view.findViewById(R.id.refresh_button);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView)view.findViewById(R.id.grid_movie);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //init adapter
        adapter = new ListMovieAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
        //load movie data when refresh data isTouched
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change title actionbar by sorting menu or default sortMethod
                if (getSortMethod().equals("popular")){
                    loadMovieData(popularUrl);
                    ((ListMovieActivity)getActivity()).getSupportActionBar().setTitle("Popular");
                }else {
                    loadMovieData(topRatedUrl);
                    ((ListMovieActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
                }

            }
        });

        //change title actionbar by sorting menu or default sortMethod
        if (getSortMethod().equals("popular")){
            loadMovieData(popularUrl);
            ((ListMovieActivity)getActivity()).getSupportActionBar().setTitle("Popular");
        }else {
            loadMovieData(topRatedUrl);
            ((ListMovieActivity)getActivity()).getSupportActionBar().setTitle("Top Rated");
        }

        return view;
    }
    //load movie data from internet to recycler view
    public void loadMovieData(String rootUrl){
        showMovie();

        //load data dari internet
        new FetchMovieTask(rootUrl).execute();
    }

    //show movie when internet connection is good
    public void showMovie(){
        recyclerView.setVisibility(View.VISIBLE);
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
    }

    //show message when internet connection is not good
    public void showEror(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    //onClick method
    @Override
    public void onClick(MovieModel position) {
        Bundle bundle = new Bundle();
        ArrayList<MovieModel> movieModels = new ArrayList<>();

        movieModels.add(position);
        bundle.putParcelableArrayList(getString(R.string.GET_MOVIE_DATA), movieModels);

        Intent intent = new Intent(this.getActivity(), DetailMovieActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    //inflate menu layout
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    //give command to every menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_by_popular:
                //update preferences
                updatePrefrences("popular");
                //set actionbar title
                ((ListMovieActivity)getActivity())
                        .getSupportActionBar().setTitle("Popular");
                //load movie data
                loadMovieData(popularUrl);
                break;
            case R.id.sort_by_top_rated:
                //update prefrences
                updatePrefrences("top rated");
                //set acion bar title
                ((ListMovieActivity)getActivity())
                        .getSupportActionBar().setTitle("Top Rated");
                //load movie data
                loadMovieData(topRatedUrl);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //get default prefrences
    private String getSortMethod(){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        return preferences.getString(getString(R.string.PREFRENCES_KEY),"popular");
    }

    //update prefrences
    private void updatePrefrences(String sortMethod){
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.PREFRENCES_KEY), sortMethod);
        editor.apply();
    }

    //fecth movie class
    private class FetchMovieTask extends AsyncTask<String, Void, MovieModel[]>{
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

            String jsonData = networkUtils.makeServiceCall(NetworkUtils.buildUrl(rootUrl, apiKey));

            if (jsonData != null){
                try {
                    MovieModel [] jsonMovieData = JsonUtil.getJsonData(jsonData);
                    return jsonMovieData;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieModel[] movieModels) {
            super.onPostExecute(movieModels);

            progressBar.setVisibility(View.INVISIBLE);
            if (movieModels != null){
                showMovie();

                adapter.setMovieAdapter(movieModels);
            }else {
                showEror();
            }
        }
    }
}
