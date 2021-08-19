package inshorts.assignment.utsavmoviestmdb.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import inshorts.assignment.utsavmoviestmdb.model.MovieModel;

public class Movies {

    @SerializedName("results")
    public List<Movie> movies;

    public class Dates {

        @SerializedName("maximum")

        public String maximum;
        @SerializedName("minimum")
        
        public String minimum;

        public String getMaximum() {
            return maximum;
        }

        public void setMaximum(String maximum) {
            this.maximum = maximum;
        }

        public String getMinimum() {
            return minimum;
        }

        public void setMinimum(String minimum) {
            this.minimum = minimum;
        }

    }

    public class Example {

        @SerializedName("dates")
        
        public Dates dates;
        @SerializedName("page")
        
        public Integer page;
        @SerializedName("results")
        
        public List<Movie> movies = null;
        @SerializedName("total_pages")
        
        public Integer totalPages;
        @SerializedName("total_results")
        
        public Integer totalResults;

        public Dates getDates() {
            return dates;
        }

        public void setDates(Dates dates) {
            this.dates = dates;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public List<Movie> getResults() {
            return movies;
        }

        public void setResults(List<Movie> movies) {
            this.movies = movies;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

    }

    public static class Movie {

        @SerializedName("adult")
        
        public Boolean adult;
        @SerializedName("backdrop_path")
        
        public String backdropPath;
        @SerializedName("genre_ids")
        
        public List<Integer> genreIds = null;
        @SerializedName("id")
        
        public Integer id;
        @SerializedName("original_language")
        
        public String originalLanguage;
        @SerializedName("original_title")
        
        public String originalTitle;
        @SerializedName("overview")
        
        public String overview;
        @SerializedName("popularity")
        
        public Double popularity;
        @SerializedName("poster_path")
        
        public String posterPath;
        @SerializedName("release_date")
        
        public String releaseDate;
        @SerializedName("title")
        
        public String title;
        @SerializedName("video")
        
        public Boolean video;
        @SerializedName("vote_average")
        
        public Double voteAverage;
        @SerializedName("vote_count")
        
        public Integer voteCount;

        public Boolean getAdult() {
            return adult;
        }

        public Movie(MovieModel movieModel) {
            this.adult = movieModel.adult ;
            this.backdropPath = movieModel.backdropPath;
            this.genreIds = new ArrayList<>();
            this.id = movieModel.id;
            this.originalLanguage = movieModel.originalLanguage;
            this.originalTitle = movieModel.originalTitle;
            this.overview = movieModel.overview;
            this.popularity = movieModel.popularity;
            this.posterPath = movieModel.posterPath;
            this.releaseDate = movieModel.releaseDate;
            this.title = movieModel.title;
            this.video = movieModel.video;
            this.voteAverage = movieModel.voteAverage;
            this.voteCount = movieModel.voteCount;
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

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
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
}