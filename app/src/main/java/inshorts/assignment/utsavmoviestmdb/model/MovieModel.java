package inshorts.assignment.utsavmoviestmdb.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class MovieModel {


    @PrimaryKey(autoGenerate = true)
    public int sno;

    public Boolean adult;

    public String backdropPath;

    public Integer id;

    public String originalLanguage;

    public String originalTitle;

    public String overview;

    public Double popularity;

    public String posterPath;

    public String releaseDate;

    public String title;

    public Boolean video;

    public Double voteAverage;

    public Integer voteCount;

    public Integer movieType;

    public MovieModel(int sno, Boolean adult, String backdropPath, Integer id, String originalLanguage, String originalTitle, String overview, Double popularity, String posterPath, String releaseDate, String title, Boolean video, Double voteAverage, Integer voteCount, Integer movieType) {
        this.sno = sno;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.movieType = movieType;
    }

    @Ignore
    public MovieModel(Boolean adult,
                      String backdropPath,
                      Integer id,
                      String originalLanguage,
                      String originalTitle,
                      String overview,
                      Double popularity,
                      String posterPath,
                      String releaseDate,
                      String title,
                      Boolean video,
                      Double voteAverage,
                      Integer voteCount,
                      Integer movieType) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.movieType = movieType;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }


}


