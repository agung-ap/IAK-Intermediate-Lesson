package id.developer.agungaprian.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.developer.agungaprian.popularmovies.models.MovieModel;
import id.developer.agungaprian.popularmovies.models.TrailerModel;

/**
 * Created by agungaprian on 07/11/17.
 */

public class JsonUtils {

    public static MovieModel[] getMovieFromJson(String jsonMovie) throws JSONException {
        //get array
        JSONObject jsonObject = new JSONObject(jsonMovie);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        //buat array dari objek moviemodel
        MovieModel[] movieModels = new MovieModel[jsonArray.length()];

        //add movie json to moviemodel one by one
        for (int i = 0 ;i < jsonArray.length(); i++){
            //initialisasi objek
            movieModels [i]= new MovieModel();

            JSONObject object = jsonArray.getJSONObject(i);

            //taruh data ke movie objek
            movieModels[i].setId(object.getInt(/*tulis key json nya*/ "id"));
            movieModels[i].setOriginalTitle(object.getString(/*tulis key json nya*/ "original_title"));
            movieModels[i].setPosterPath(object.getString(/*tulis key json nya*/ "poster_path"));
            movieModels[i].setOverView(object.getString(/*tulis key json nya*/ "overview"));
            movieModels[i].setVoteAverage(object.getDouble(/*tulis key json nya*/ "vote_average"));
            movieModels[i].setReleaseDate(object.getString(/*tulis key json nya*/ "release_date"));
            movieModels[i].setBackdropPath(object.getString("backdrop_path"));
        }

        return movieModels;
    }

    public static TrailerModel[] getTrailerFromJson(String jsonTrailer) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonTrailer);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        TrailerModel [] trailerModels = new TrailerModel[jsonArray.length()];

        for (int i = 0 ; i < jsonArray.length(); i++){
            trailerModels [i] = new TrailerModel();

            JSONObject object = jsonArray.getJSONObject(i);

            trailerModels[i].setId(object.getString("id"));
            trailerModels[i].setKey(object.getString("key"));
        }

        return trailerModels;
    }
}
