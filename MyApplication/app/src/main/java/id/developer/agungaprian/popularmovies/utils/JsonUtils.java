package id.developer.agungaprian.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.developer.agungaprian.popularmovies.models.MovieModel;

/**
 * Created by agungaprian on 07/11/17.
 */

public class JsonUtils {

    public static MovieModel[] getDataFromJson(String jsonMovie) throws JSONException {
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
            movieModels[i].setOriginalTitle(object.getString(/*tulis key json nya*/ "original_title"));
            movieModels[i].setPosterPath(object.getString(/*tulis key json nya*/ "poster_path"));
            movieModels[i].setOverView(object.getString(/*tulis key json nya*/ "overview"));
            movieModels[i].setVoteAverage(object.getDouble(/*tulis key json nya*/ "vote_average"));
            movieModels[i].setReleaseDate(object.getString(/*tulis key json nya*/ "release_date"));
            movieModels[i].setBackdropPath(object.getString("backdrop_path"));
        }

        return movieModels;
    }
}
