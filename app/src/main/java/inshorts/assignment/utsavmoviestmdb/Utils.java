package inshorts.assignment.utsavmoviestmdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import inshorts.assignment.utsavmoviestmdb.activities.MovieActivity;
import inshorts.assignment.utsavmoviestmdb.model.Movies;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static ArrayList<Movies.Movie> getLikedMoviesList(Context context) {

        Gson gson = new Gson();
        SharedPreferences applicationPrefs= context.getSharedPreferences("MoviesData", MODE_PRIVATE);
        String json = applicationPrefs.getString("liked_movies", "");
        TypeToken<ArrayList<Movies.Movie>> token = new TypeToken<ArrayList<Movies.Movie>>() {
        };
        ArrayList<Movies.Movie> list = gson.fromJson(json, token.getType());
        return list;

    }

    public static ArrayList<Movies.Movie> getBookmarkedMoviesList(Context context) {

        Gson gson = new Gson();
        SharedPreferences applicationPrefs= context.getSharedPreferences("MoviesData", MODE_PRIVATE);
        String json = applicationPrefs.getString("bookmarked_movies", "");
        TypeToken<ArrayList<Movies.Movie>> token = new TypeToken<ArrayList<Movies.Movie>>() {
        };
        ArrayList<Movies.Movie> list = gson.fromJson(json, token.getType());
        return list;

    }

    public static void openMovie(Movies.Movie movie,Context context)
    {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra("movie",new Gson().toJson(movie));
        context.startActivity(intent);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }
}
