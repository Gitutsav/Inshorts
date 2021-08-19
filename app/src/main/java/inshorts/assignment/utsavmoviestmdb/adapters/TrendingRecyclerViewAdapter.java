package inshorts.assignment.utsavmoviestmdb.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import inshorts.assignment.utsavmoviestmdb.model.Movies;
import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.Utils;

import static inshorts.assignment.utsavmoviestmdb.database.MoviesService.BASE_URL_IMAGE;

public class TrendingRecyclerViewAdapter extends RecyclerView.Adapter<TrendingRecyclerViewAdapter.ViewHolder> {

    private List<Movies.Movie> movieList;
    Context context;
    private int SHARE_PAYMENT = 123456;


    public TrendingRecyclerViewAdapter(Context context) {
        movieList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movies.Movie movie = movieList.get(position);
        holder.moviename.setText(movie.originalTitle);
        String imageUrl = BASE_URL_IMAGE + movie.backdropPath;
        Picasso.get().load(imageUrl).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.movieImage);

        holder.likeButton.setVisibility(View.GONE);
        holder.bookmarkButton.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openMovie(movie,context);
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(String.valueOf(movie.id));
            }
        });

//        holder.txtPrice.setText("$" + String.format("%.2f", Double.parseDouble(movie.price)));
//        if (movie.coinName.equalsIgnoreCase("eth")) {
//            holder.cardView.setCardBackgroundColor(Color.GRAY);
//        } else {
//            holder.cardView.setCardBackgroundColor(Color.GREEN);
//        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setData(List<Movies.Movie> data) {
        this.movieList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView moviename;
        public ImageView movieImage, likeButton, bookmarkButton, shareButton;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            moviename = view.findViewById(R.id.tv_movie_name);
            movieImage = view.findViewById(R.id.iv_movie);
            likeButton = view.findViewById(R.id.iv_liked);
            bookmarkButton = view.findViewById(R.id.iv_bookmarked);
            cardView = view.findViewById(R.id.cardView);
            shareButton = view.findViewById(R.id.iv_share);

        }
    }
    private void share(String movieId) {
        Uri uri = Uri.parse("https://www.utsavmovietmdb.com").buildUpon()
                .appendQueryParameter("mid", movieId)
                .build();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        // will always show a dialog to user to choose an app

        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri.toString());

        Intent chooser = Intent.createChooser(shareIntent, "Share with");
        // check if intent resolves
        if (null != chooser.resolveActivity(context.getPackageManager())) {
            ((Activity)context).startActivityForResult(chooser, SHARE_PAYMENT);
        } else {
        }
    }
}
