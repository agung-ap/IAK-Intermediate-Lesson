package id.developer.agungaprian.popularmovieapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by agungaprian on 24/11/17.
 */

public class DetailMovieFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null){

        }
        return view;
    }
}
