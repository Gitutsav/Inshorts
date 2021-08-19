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

import inshorts.assignment.utsavmoviestmdb.interfaces.IOnBookMarked;
import inshorts.assignment.utsavmoviestmdb.interfaces.IOnLiked;
import inshorts.assignment.utsavmoviestmdb.model.Movies;
import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.Utils;

import static inshorts.assignment.utsavmoviestmdb.database.MoviesService.BASE_URL_IMAGE;

public class NowPlayingRecyclerViewAdapter extends RecyclerView.Adapter<NowPlayingRecyclerViewAdapter.ViewHolder> {

    private List<Movies.Movie> movieList;
    Context context;
    IOnLiked iOnLiked;
    IOnBookMarked iOnBookMarked;
    ArrayList<Movies.Movie> likedMovies;
    ArrayList<Movies.Movie> bookmarkedMovies;
    private int SHARE_PAYMENT = 123456;

    public NowPlayingRecyclerViewAdapter(Context context, IOnLiked iOnLiked, IOnBookMarked iOnBookMarked) {
        movieList = new ArrayList<>();
        this.context = context;
        this.iOnLiked = iOnLiked;
        this.iOnBookMarked = iOnBookMarked;
        likedMovies = Utils.getLikedMoviesList(context);
        bookmarkedMovies = Utils.getBookmarkedMoviesList(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movies.Movie movie = movieList.get(position);

        if(likedMovies!=null && likedMovies.size()>0)
        {
            for(int i = 0;i<likedMovies.size();i++)
            {
                if(movie.id.equals(likedMovies.get(i).id)){
                    holder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_24));
                    holder.likeButton.setTag(2);
                    break;
                }
            }
        }

        if(bookmarkedMovies!=null && bookmarkedMovies.size()>0)
        {
            for(int i = 0;i<bookmarkedMovies.size();i++)
            {
                if(movie.id.equals(bookmarkedMovies.get(i).id)){
                    holder.bookmarkButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_24));
                    holder.bookmarkButton.setTag(2);
                    break;
                }
            }
        }

        holder.moviename.setText(movie.originalTitle);
        String imageUrl = BASE_URL_IMAGE + movie.backdropPath;
        Picasso.get().load(imageUrl).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.movieImage);

        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(holder.bookmarkButton.getTag().toString()) == 1)
                {
                    holder.bookmarkButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_24));
                    iOnBookMarked.onBookmarked(movie,true);
                    holder.bookmarkButton.setTag(2);

                } else {
                    holder.bookmarkButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_bookmark_border_24));
                    iOnBookMarked.onBookmarked(movie,false);
                    holder.bookmarkButton.setTag(1);

                }

            }
        });

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(holder.likeButton.getTag().toString()) == 1)
                {
                    holder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_24));
                    iOnLiked.onLiked(movie,true);
                    holder.likeButton.setTag(2);

                } else {
                    holder.likeButton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_favorite_border_24));
                    iOnLiked.onLiked(movie,false);
                    holder.likeButton.setTag(1);

                }

            }
        });

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
