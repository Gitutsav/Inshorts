package inshorts.assignment.utsavmoviestmdb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.Utils;
import inshorts.assignment.utsavmoviestmdb.adapters.BookmarkedMovieAdapter;
import inshorts.assignment.utsavmoviestmdb.databinding.ActivityBookmarkMovieBinding;

public class BookmarkMovieActivity extends AppCompatActivity {

    ActivityBookmarkMovieBinding binding;
    BookmarkedMovieAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmark_movie);

        binding.rvNowplaying.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new BookmarkedMovieAdapter(this);
        binding.rvNowplaying.setAdapter(adapter);
        adapter.setData(Utils.getBookmarkedMoviesList(BookmarkMovieActivity.this));
    }
}