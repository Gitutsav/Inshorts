package inshorts.assignment.utsavmoviestmdb.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import inshorts.assignment.utsavmoviestmdb.model.Movies;
import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.Utils;
import inshorts.assignment.utsavmoviestmdb.adapters.SearchAdapter;
import inshorts.assignment.utsavmoviestmdb.database.AppDatabase;
import inshorts.assignment.utsavmoviestmdb.database.MoviesService;
import inshorts.assignment.utsavmoviestmdb.databinding.ActivitySearchBinding;
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

public class SearchActivity extends AppCompatActivity implements IOnLiked, IOnBookMarked {

    ActivitySearchBinding binding;
    SearchAdapter adapter;
    private Retrofit retrofit;
    private AppDatabase mDb;
    public ArrayList<Movies.Movie> movieArrayList = new ArrayList<>();
    public ArrayList<Movies.Movie> searchMovieArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        adapter = new SearchAdapter(this);
        binding.rvNowplaying.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SearchAdapter(this);
        binding.rvNowplaying.setAdapter(adapter);

        mDb = AppDatabase.getInstance(getApplicationContext());

        if(!Utils.isNetworkAvailable(SearchActivity.this)) {
            List<MovieModel> movieModelList =  mDb.movieDao().loadAllMovies();
            //List<MovieModel> movieModelList =  mDb.movieDao().searchMovies(newText.toLowerCase().trim());
            for(int i=0;i<movieModelList.size();i++)
                movieArrayList.add(new Movies.Movie(movieModelList.get(i)));

            //adapter.setData(moviesList);
        }

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if(Utils.isNetworkAvailable(SearchActivity.this)) {
//                    callEndpoints(query.trim());
//                } else {
////                    List<MovieModel> movieModelList =  mDb.movieDao().searchMovies(query.trim());
//                    for(int i=0;i<movieModelList.size();i++)
//                    {
//                        movieArrayList.add(new Movies.Movie(movieModelList.get(i)));
//
//                    }
//                    adapter.setData(movieArrayList);
//                }
              return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                if(!newText.trim().isEmpty()) {
                    if (Utils.isNetworkAvailable(SearchActivity.this)) {
                        callEndpoints(newText.trim());
                    } else {
                        searchMovieArrayList = (ArrayList<Movies.Movie>) search(newText.toLowerCase().trim());
                        adapter.setData(searchMovieArrayList);
                    }
                } else {
                    searchMovieArrayList.clear();
                    adapter.setData(searchMovieArrayList);
                }
                return false;
            }
        });

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

    }

    @SuppressLint("CheckResult")
    private void callEndpoints(String query) {

        binding.progressBar.setVisibility(View.VISIBLE);
        MoviesService moviesService = retrofit.create(MoviesService.class);

        //Single call
        Observable<Movies> moviesObservable = moviesService.getSeachedMovies(query);
        moviesObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .map(result -> result.movies).subscribe(this::handleResults, this::handleError);


//        Observable<List<Movies.Movie>> moviesObservable = moviesService.getSeachedMovies(query)
//                .map(result -> Observable.fromIterable(result.movies))
//                .flatMap(x -> x).filter(y -> {
//                    return true;
//                }).toList().toObservable();
//
//        moviesObservable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).
//                subscribe(this::handleResults, this::handleError);




//        Observable.merge(moviesObservable, moviesObservable2)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread());

    }


    private void handleResults(List<Movies.Movie> marketList) {

        binding.progressBar.setVisibility(View.GONE);

        if (marketList != null && marketList.size() != 0) {
            adapter.setData(marketList);

        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }



    private void handleError(Throwable t) {

        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBookmarked(Movies.Movie movie, boolean isBookmarked) {

    }

    @Override
    public void onLiked(Movies.Movie movie, boolean isLiked) {

    }

    public List<Movies.Movie> search(String query){
        List<Movies.Movie> clone = new ArrayList<>();
        try {
            for (int i = 0; i < movieArrayList.size(); i++) {
                if (query.equals(movieArrayList.get(i).originalTitle.substring(0, query.length()).toLowerCase().trim()) ||
                        query.equals(movieArrayList.get(i).title.substring(0, query.length()).toLowerCase().trim())) {
                    clone.add(movieArrayList.get(i));
                }
            }
        } catch (Exception e){

        }
        return clone;
    }
}