package inshorts.assignment.utsavmoviestmdb.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import inshorts.assignment.utsavmoviestmdb.model.MovieModel;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY id")
    List<MovieModel> loadAllMovies();

    @Insert(onConflict = REPLACE)
    void insertMovie(MovieModel movie);

    @Insert
    void insertAll(MovieModel... movies);

    @Update
    void updateMovie(MovieModel person);

    @Delete
    void delete(MovieModel movie);

    @Query("DELETE FROM movie")
    void deleteAll();

    @Query("SELECT * FROM movie WHERE id = :id")
    MovieModel loadMovieById(int id);

    @Query("SELECT * FROM movie WHERE id LIKE :searchQuery")
    List<MovieModel> searchMovies(String searchQuery);

}
