package com.studios.flipacoin.whichmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.studios.flipacoin.whichmovie.data.Movie;

import java.util.ArrayList;

/**
 * Created by Vicente_Lee on 1/19/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    public final static String POSTER_PATH_PREFIX = "http://image.tmdb.org/t/p/w500";
    private ArrayList<Movie> mMovies;
    private final MovieAdapterOnItemClickListener mItemClickListener;

    public interface MovieAdapterOnItemClickListener {
        void onClick(Movie movie);
    }

    public MovieAdapter(ArrayList<Movie> moviePaths, MovieAdapterOnItemClickListener itemClickListener) {
        mMovies = moviePaths;
        mItemClickListener = itemClickListener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {

        if (mMovies != null) {
            Context context = holder.mPoster.getContext();
            Picasso.with(context)
                    .load(POSTER_PATH_PREFIX + mMovies.get(position).getPosterPath())
                    .into(holder.mPoster);
        }
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setMovieThumbnails(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mPoster;
        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPoster = itemView.findViewById(R.id.iv_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mItemClickListener.onClick(movie);

        }
    }
}
