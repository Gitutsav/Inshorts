package inshorts.assignment.utsavmoviestmdb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import inshorts.assignment.utsavmoviestmdb.R;
import inshorts.assignment.utsavmoviestmdb.Utils;
import inshorts.assignment.utsavmoviestmdb.adapters.LikedMovieAdapter;
import inshorts.assignment.utsavmoviestmdb.databinding.ActivityLikedMovieBinding;

public class LikedMovieActivity extends AppCompatActivity {

    ActivityLikedMovieBinding binding;
    LikedMovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_liked_movie);

        binding.rvNowplaying.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new LikedMovieAdapter(this);
        binding.rvNowplaying.setAdapter(adapter);
        adapter.setData(Utils.getLikedMoviesList(LikedMovieActivity.this));
    }
}