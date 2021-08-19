package inshorts.assignment.utsavmoviestmdb.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import inshorts.assignment.utsavmoviestmdb.model.Movies;
import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.Utils;
import inshorts.assignment.utsavmoviestmdb.adapters.NowPlayingRecyclerViewAdapter;
import inshorts.assignment.utsavmoviestmdb.adapters.TrendingRecyclerViewAdapter;
import inshorts.assignment.utsavmoviestmdb.database.AppDatabase;
import inshorts.assignment.utsavmoviestmdb.database.MoviesService;
import inshorts.assignment.utsavmoviestmdb.databinding.ActivityMainBinding;
import inshorts.assignment.utsavmoviestmdb.interfaces.IOnBookMarked;
import inshorts.assignment.utsavmoviestmdb.interfaces.IOnLiked;
import inshorts.assignment.utsavmoviestmdb.model.MovieModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static inshorts.assignment.utsavmoviestmdb.database.MoviesService.BASE_URL;

public class MainActivity extends AppCompatActivity implements IOnBookMarked, IOnLiked {

    RecyclerView recyclerViewNowPlaying, recyclerViewTrending;
    Retrofit retrofit;
    NowPlayingRecyclerViewAdapter nowPlayingRecyclerViewAdapter;
    TrendingRecyclerViewAdapter trendingRecyclerViewAdapter;

    ArrayList<Movies.Movie> nowPlayingMoviesList = new ArrayList<>();
    ArrayList<Movies.Movie> trendingMoviesList = new ArrayList<>();


    ActivityMainBinding binding;
    int likeCounter = 0, bookmarkCounter = 0;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        binding.rvNowplaying.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nowPlayingRecyclerViewAdapter = new NowPlayingRecyclerViewAdapter(this, this::onLiked, this::onBookmarked);
        binding.rvNowplaying.setAdapter(nowPlayingRecyclerViewAdapter);

        binding.rvTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingRecyclerViewAdapter = new TrendingRecyclerViewAdapter(this);
        binding.rvTrending.setAdapter(trendingRecyclerViewAdapter);


        mDb = AppDatabase.getInstance(getApplicationContext());




       // retrieveTasks();
        if (Utils.getLikedMoviesList(MainActivity.this) != null &&
                Utils.getLikedMoviesList(MainActivity.this).size() > 0) {
            likeCounter = Utils.getLikedMoviesList(MainActivity.this).size();
            binding.tvLikeCounter.setText(likeCounter + "");
            binding.tvLikeCounter.setVisibility(View.VISIBLE);
        }

        if (Utils.getBookmarkedMoviesList(MainActivity.this) != null &&
                Utils.getBookmarkedMoviesList(MainActivity.this).size() > 0) {
            bookmarkCounter = Utils.getBookmarkedMoviesList(MainActivity.this).size();
            binding.tvBookmarkCounter.setText(bookmarkCounter + "");
            binding.tvBookmarkCounter.setVisibility(View.VISIBLE);
        }

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


        if(!Utils.isNetworkAvailable(this)){
            List<MovieModel> movieModelList =  retrieveTasks();
            for(int i=0;i<movieModelList.size();i++)
            {
                if(movieModelList.get(i).movieType == 1) {
                    nowPlayingMoviesList.add(new Movies.Movie(movieModelList.get(i)));
                } else {
                    trendingMoviesList.add(new Movies.Movie(movieModelList.get(i)));
                }
            }
            nowPlayingRecyclerViewAdapter.setData(nowPlayingMoviesList);
            trendingRecyclerViewAdapter.setData(trendingMoviesList);
        } else {
            mDb.movieDao().deleteAll();
            callEndpoints();
        }


        binding.ivLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeCounter > 0) {
                    startActivity(new Intent(MainActivity.this, LikedMovieActivity.class));
                }
            }
        });

        binding.ivBookmarked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookmarkCounter > 0) {
                    startActivity(new Intent(MainActivity.this, BookmarkMovieActivity.class));
                }
            }
        });

        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    @SuppressLint("CheckResult")
    private void callEndpoints() {

        MoviesService moviesService = retrofit.create(MoviesService.class);

        //Single call
        /*Observable<Crypto> cryptoObservable = cryptocurrencyService.getCoinData("btc");
        cryptoObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(result -> result.ticker).subscribe(this::handleResults, this::handleError);*/

        binding.progressBar.setVisibility(View.VISIBLE);

        Observable<List<Movies.Movie>> moviesObservable = moviesService.getNowPlayingMoviesData()
                .map(result -> Observable.fromIterable(result.movies))
                .flatMap(x -> x).filter(y -> {
                    return true;
                }).toList().toObservable();

        Observable<List<Movies.Movie>> moviesObservable2 = moviesService.getTrendingMoviesData()
                .map(result -> Observable.fromIterable(result.movies))
                .flatMap(x -> x).filter(y -> {
                    return true;
                }).toList().toObservable();

        moviesObservable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::handleResults, this::handleError);

        moviesObservable2.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::handleResults2, this::handleError);


//        Observable.merge(moviesObservable, moviesObservable2)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread());

    }


    private void handleResults(List<Movies.Movie> marketList) {

        if (marketList != null && marketList.size() != 0) {
            nowPlayingRecyclerViewAdapter.setData(marketList);
            for (int i = 0; i < marketList.size(); i++) {
                Movies.Movie movie = marketList.get(i);
                MovieModel movieModel = new MovieModel(movie.adult,
                        movie.backdropPath,
                        movie.id,
                        movie.originalLanguage,
                        movie.originalTitle,
                        movie.overview,
                        movie.popularity,
                        movie.posterPath,
                        movie.releaseDate,
                        movie.title,
                        movie.video,
                        movie.voteAverage,
                        movie.voteCount,
                        1);

                saveMovieInDb(movieModel);
            }


        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void saveMovieInDb(MovieModel movieModel) {

        mDb.movieDao().insertMovie(movieModel);

//        try {
//            AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    finish();
//                }
//            });
//        } catch (Exception e){
//
//        }
    }

    private void handleResults2(List<Movies.Movie> marketList) {


        if (marketList != null && marketList.size() != 0) {
            trendingRecyclerViewAdapter.setData(marketList);
            for (int i = 0; i < marketList.size(); i++) {
                Movies.Movie movie = marketList.get(i);
                MovieModel movieModel = new MovieModel(movie.adult,
                        movie.backdropPath,
                        movie.id,
                        movie.originalLanguage,
                        movie.originalTitle,
                        movie.overview,
                        movie.popularity,
                        movie.posterPath,
                        movie.releaseDate,
                        movie.title,
                        movie.video,
                        movie.voteAverage,
                        movie.voteCount,
                        2);
                saveMovieInDb(movieModel);
            }
        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
        binding.progressBar.setVisibility(View.GONE);
    }

    private void handleError(Throwable t) {

        binding.progressBar.setVisibility(View.GONE);

        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBookmarked(Movies.Movie movie, boolean isBookmarked) {

        if (isBookmarked) {
            bookmarkCounter = bookmarkCounter + 1;
        } else {
            if (bookmarkCounter > 0) {
                bookmarkCounter = bookmarkCounter - 1;
            }
        }

        if (bookmarkCounter > 0) {
            binding.tvBookmarkCounter.setVisibility(View.VISIBLE);
            binding.tvBookmarkCounter.setText(bookmarkCounter + "");
        } else {
            binding.tvBookmarkCounter.setVisibility(View.INVISIBLE);
        }
        updateBookmarkedMoviesList(movie, isBookmarked);
    }

    @Override
    public void onLiked(Movies.Movie movie, boolean isLiked) {

        if (isLiked) {
            likeCounter = likeCounter + 1;
        } else {
            if (likeCounter > 0) {
                likeCounter = likeCounter - 1;
            }
        }
        if (likeCounter > 0) {
            binding.tvLikeCounter.setVisibility(View.VISIBLE);
            binding.tvLikeCounter.setText(likeCounter + "");
        } else {
            binding.tvLikeCounter.setVisibility(View.INVISIBLE);
        }
        updateLikedMoviesList(movie, isLiked);
    }


    public void updateLikedMoviesList(Movies.Movie movie, boolean isLiked) {


        ArrayList<Movies.Movie> savedLikedMovies = new ArrayList<>();
        savedLikedMovies = Utils.getLikedMoviesList(MainActivity.this);

        if (savedLikedMovies == null) {
            savedLikedMovies = new ArrayList<>();
        }

        if (isLiked) {
            savedLikedMovies.add(movie);
        } else {
            for (int i = 0; i < savedLikedMovies.size(); i++) {
                if (savedLikedMovies.get(i).id.equals(movie.id)) {
                    savedLikedMovies.remove(i);
                    break;
                }
            }
        }
        SharedPreferences moviesData = getSharedPreferences("MoviesData", 0);
        SharedPreferences.Editor editorCityData = moviesData.edit();
        editorCityData.putString("liked_movies", new Gson().toJson(savedLikedMovies));
        editorCityData.apply();

    }

    public void updateBookmarkedMoviesList(Movies.Movie movie, boolean isBookmarked) {


        ArrayList<Movies.Movie> savedBookMarkedMovies = new ArrayList<>();
        savedBookMarkedMovies = Utils.getBookmarkedMoviesList(MainActivity.this);

        if (savedBookMarkedMovies == null) {
            savedBookMarkedMovies = new ArrayList<>();
        }

        if (isBookmarked) {
            savedBookMarkedMovies.add(movie);
        } else {
            for (int i = 0; i < savedBookMarkedMovies.size(); i++) {
                if (savedBookMarkedMovies.get(i).id.equals(movie.id)) {
                    savedBookMarkedMovies.remove(i);
                    break;
                }
            }
        }
        SharedPreferences moviesData = getSharedPreferences("MoviesData", 0);
        SharedPreferences.Editor editorCityData = moviesData.edit();
        editorCityData.putString("bookmarked_movies", new Gson().toJson(savedBookMarkedMovies));
        editorCityData.apply();

    }

    private List<MovieModel> retrieveTasks() {

         List<MovieModel> persons = new ArrayList<>();
        persons = mDb.movieDao().loadAllMovies();

       /* AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //mAdapter.setTasks(persons);
                    }
                });
            }
        });*/

        return persons;

    }


}