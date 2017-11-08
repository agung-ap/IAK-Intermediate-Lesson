package id.developer.agungaprian.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.developer.agungaprian.popularmovies.R;
import id.developer.agungaprian.popularmovies.models.MovieModel;

/**
 * Created by agungaprian on 05/11/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    Context context;
    MovieModel [] data;
    MovieAdapterOnClickHandler clickHandler;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.context = context;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Picasso.with(context)
                .load(data[position].getPosterPath())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.eror_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.length;
    }

    public void setMovieData(MovieModel[] movieModels) {
        data = movieModels;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.movie_poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(data[adapterPosition]);
        }
    }

    public interface MovieAdapterOnClickHandler{
        void onClick (MovieModel moviePosition);
    }
}
