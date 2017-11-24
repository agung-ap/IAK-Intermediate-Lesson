package id.developer.agungaprian.popularmovieapp.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.developer.agungaprian.popularmovieapp.model.MovieModel;

/**
 * Created by agungaprian on 18/11/17.
 */

public class JsonUtil {
    public static MovieModel[] getJsonData(String json) throws JSONException {
        //get array
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        MovieModel [] movie = new MovieModel[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject object = jsonArray.getJSONObject(i);

            movie [i] = new MovieModel();

            movie[i].setOriginalTitle(object.getString("original_title"));
            movie[i].setPosterPath(object.getString("poster_path"));
            movie[i].setOverView(object.getString("overview"));
            movie[i].setVoteAverage(object.getDouble("vote_average"));
            movie[i].setReleaseDate(object.getString("release_date"));
            movie[i].setBackdropPath(object.getString("backdrop_path"));
            movie[i].setId(object.getInt("id"));
        }

        return movie;
    }

}
