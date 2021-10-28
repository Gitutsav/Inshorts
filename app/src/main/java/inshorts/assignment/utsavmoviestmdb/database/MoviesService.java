package inshorts.assignment.utsavmoviestmdb.database;

import inshorts.assignment.utsavmoviestmdb.model.Movies;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesService {

    String BASE_URL = "https://api.themoviedb.org/3/";
    String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w185";

    @GET("movie/now_playing?api_key=f7023c84aae1d6f497df71062f32fe7b")
    Observable<Movies> getTrendingMoviesData();

    @GET("trending/movie/day?api_key=f7023c84aae1d6f497df71062f32fe7b")
    Observable<Movies> getNowPlayingMoviesData();

    @GET("search/movie?api_key=f7023c84aae1d6f497df71062f32fe7b")
    Observable<Movies> getSeachedMovies(@Query("query") String query);
}
