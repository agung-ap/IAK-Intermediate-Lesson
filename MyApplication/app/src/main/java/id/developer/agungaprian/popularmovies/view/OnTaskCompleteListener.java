package id.developer.agungaprian.popularmovies.view;

import id.developer.agungaprian.popularmovies.models.MovieModel;

/**
 * Created by agungaprian on 05/11/17.
 */

public interface OnTaskCompleteListener {
    void onFetchMoviesTaskCompleted(MovieModel[] movies);
}
