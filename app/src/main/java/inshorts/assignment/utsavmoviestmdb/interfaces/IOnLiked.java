package inshorts.assignment.utsavmoviestmdb.interfaces;

import inshorts.assignment.utsavmoviestmdb.model.Movies;

public interface IOnLiked {
    public void onLiked(Movies.Movie movie, boolean isLiked);
}
