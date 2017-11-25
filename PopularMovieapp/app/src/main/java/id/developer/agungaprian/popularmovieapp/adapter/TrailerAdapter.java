package id.developer.agungaprian.popularmovieapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.agungaprian.popularmovieapp.R;
import id.developer.agungaprian.popularmovieapp.model.TrailerModel;

/**
 * Created by agungaprian on 25/11/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    Context context;
    TrailerModel [] data;
    TrailerAdapterOnClickHandler clickHandler;

    public TrailerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.movie_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        String key = data[position].getKey();
        String thumbnailURL = "http://img.youtube.com/vi/"+ key + "/hqdefault.jpg";

        Picasso.with(context)
                .load(thumbnailURL)
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.length;
    }

    public void setTrailerData(TrailerModel[] trailerModels) {
        data = trailerModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.trailer_image)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(data[getAdapterPosition()]);
        }
    }

    public interface TrailerAdapterOnClickHandler{
        void onClick (TrailerModel trailerPosition);
    }
}
