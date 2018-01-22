package com.studios.flipacoin.whichmovie.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.studios.flipacoin.whichmovie.MovieAdapter;
import com.studios.flipacoin.whichmovie.R;

public class DetailActivity extends AppCompatActivity {

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mAllTheRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPoster = findViewById(R.id.iv_poster);
        mTitle = findViewById(R.id.tv_title);
        mAllTheRest = findViewById(R.id.tv_all_the_rest);

        Intent intent = getIntent();

        if (intent != null) {
            String title = intent.getStringExtra("title");
            String releaseDate = intent.getStringExtra("releaseDate");
            String posterPath = intent.getStringExtra("posterPath");
            String voteAverage = intent.getStringExtra("voteAverage");
            String overview = intent.getStringExtra("overview");

            Picasso.with(this)
                    .load(MovieAdapter.POSTER_PATH_PREFIX + posterPath)
                    //.placeholder(R.mipmap.ic_launcher)
                    //.error(R.mipmap.ic_launcher_round)
                    .into(mPoster);

            mTitle.setText(title);
            mAllTheRest.append(overview + "\n\n" + "Vote Average: " + voteAverage + "\n\n" + "Release Date: " + releaseDate);
        }



    }
}
