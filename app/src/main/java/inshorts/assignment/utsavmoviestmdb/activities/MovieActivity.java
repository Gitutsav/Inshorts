package inshorts.assignment.utsavmoviestmdb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import inshorts.assignment.utsavmoviestmdb.model.Movies;
import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.database.MoviesService;
import inshorts.assignment.utsavmoviestmdb.databinding.ActivityMovieBinding;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static inshorts.assignment.utsavmoviestmdb.database.MoviesService.BASE_URL;
import static inshorts.assignment.utsavmoviestmdb.database.MoviesService.BASE_URL_IMAGE;

public class MovieActivity extends AppCompatActivity {

    ActivityMovieBinding binding;
    public Retrofit retrofit;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        if (Intent.ACTION_VIEW.equals(getIntent().getAction()) && getIntent().getType() == null) {

            Uri uri = getIntent().getData();
            String mId = uri.getQueryParameter("mid");

           callEndpoints(mId);

        } else {
            String movieData = getIntent().getStringExtra("movie");
            displayMovie(movieData);
        }
    }
    public void displayMovie(String movieData)
    {
        Movies.Movie movie = new Gson().fromJson(movieData, Movies.Movie.class);

        Picasso.get().load(BASE_URL_IMAGE+movie.posterPath).error(R.drawable.ic_baseline_error_outline_24).placeholder(R.drawable.placeholder).into(binding.ivMovie);

        binding.tvTitle.setText(movie.originalTitle);
        binding.tvOverview.setText(movie.overview);
        binding.tvReleasedate.setText("Released on: "+movie.releaseDate);
    }

    private void handleResults(List<Movies.Movie> marketList) {

        displayMovie(marketList.get(0).toString());
    }

    private void handleError(Throwable t) {

        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @SuppressLint("CheckResult")
    private void callEndpoints(String query) {

        MoviesService moviesService = retrofit.create(MoviesService.class);

        //Single call
        Observable<Movies> moviesObservable = moviesService.getSeachedMovies(query);
        moviesObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .map(result -> result.movies).subscribe(this::handleResults, this::handleError);

    }
}