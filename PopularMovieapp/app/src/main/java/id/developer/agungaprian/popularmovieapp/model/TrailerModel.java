package id.developer.agungaprian.popularmovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by agungaprian on 25/11/17.
 */

public class TrailerModel implements Parcelable {
    private String id;
    private String key;

    public TrailerModel() {
    }

    protected TrailerModel(Parcel in) {
        id = in.readString();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {
        @Override
        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        @Override
        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static Creator<TrailerModel> getCREATOR() {
        return CREATOR;
    }
}
