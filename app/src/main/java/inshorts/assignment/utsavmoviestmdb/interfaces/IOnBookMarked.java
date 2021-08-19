package inshorts.assignment.utsavmoviestmdb.interfaces;

import inshorts.assignment.utsavmoviestmdb.model.Movies;

public interface IOnBookMarked {

    public void onBookmarked(Movies.Movie movie, boolean isBookmarked);

}
