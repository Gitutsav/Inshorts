package inshorts.assignment.utsavmoviestmdb.adapters;

import android.content.Context;
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

import static inshorts.assignment.utsavmoviestmdb.database.MoviesService.BASE_URL_IMAGE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Movies.Movie> movieList;
    Context context;

    public SearchAdapter(Context context) {
        movieList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout_full, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movies.Movie movie = movieList.get(position);
        holder.moviename.setText(movie.originalTitle);
        String imageUrl = BASE_URL_IMAGE + movie.backdropPath;
        Picasso.get().load(imageUrl).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.movieImage);


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
        this.movieList.clear();
        this.movieList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView moviename;
        public ImageView movieImage;
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);

            moviename = view.findViewById(R.id.tv_movie_name);
            movieImage = view.findViewById(R.id.iv_movie);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
