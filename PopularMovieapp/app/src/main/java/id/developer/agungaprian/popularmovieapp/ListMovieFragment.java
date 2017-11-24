package id.developer.agungaprian.popularmovieapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private static String rootUrl = "https://api.themoviedb.org/3/movie/popular?";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_movie_fragment, container, false);
        ButterKnife.bind(getActivity(), view);

        errorMessageDisplay = (TextView)view.findViewById(R.id.eror_message);

        refreshButton = (ImageButton)view.findViewById(R.id.refresh_button);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView)view.findViewById(R.id.grid_movie);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //init adapter
        adapter = new ListMovieAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        loadMovieData();
        return view;
    }

    public void loadMovieData(){
        showMovie();

        //load data dari internet
        new FetchMovieTask().execute();
    }

    public void showMovie(){
        recyclerView.setVisibility(View.VISIBLE);
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
    }

    public void showEror(){
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieModel position) {

    }

    private class FetchMovieTask extends AsyncTask<String, Void, MovieModel[]>{

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
