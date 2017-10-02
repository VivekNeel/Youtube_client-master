package vivek.client.youtube.com.youtubeclient.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscription;
import vivek.client.youtube.com.youtubeclient.Keys;
import vivek.client.youtube.com.youtubeclient.R;
import vivek.client.youtube.com.youtubeclient.adapters.VideoListAdapter;
import vivek.client.youtube.com.youtubeclient.callbacks.INetworkCallback;
import vivek.client.youtube.com.youtubeclient.models.Channel;
import vivek.client.youtube.com.youtubeclient.models.Item;
import vivek.client.youtube.com.youtubeclient.network.NetworkCalls;
import vivek.client.youtube.com.youtubeclient.utils.AdapterUtils;

/**
 * Created by vivekneel on 12/19/16.
 */

public class VideoListActivity extends AppCompatActivity implements INetworkCallback {

    private Context context;
    @BindView(R.id.videoListRv)
    RecyclerView videoRV;
    @BindView(R.id.videoParent)
    LinearLayout videosParent;
    private VideoListAdapter videoListAdapter;
    private List<Item> videos = new ArrayList<>();
    private Subscription videoSubscription;
    private String playlistId, channelName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress)
    ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = VideoListActivity.this;
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        videoRV.setLayoutManager(new LinearLayoutManager(this));
        videoRV.addItemDecoration(new DividerItemDecoration(this, ((LinearLayoutManager) videoRV.getLayoutManager()).getOrientation()));
        Bundle bundle = getIntent().getExtras();
        playlistId = bundle.getString(Keys.PLAYLIST_ID.name(), "");
        channelName = bundle.getString(Keys.CHANNEL_NAME.name(), "");
        getSupportActionBar().setTitle((channelName != null && !channelName.isEmpty() ? channelName : "Videos"));
        if (playlistId.isEmpty()) {
            Snackbar.make(videosParent, "Something went wrong", 1000).show();
            return;
        }

        new NetworkCalls(this).fetchVideos(playlistId);

    }

    @Override
    public void willFetchChannel() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchedChannels(Channel channel, Subscription subscription) {
        videoSubscription = subscription;
        videos = channel.getItems();
        videoListAdapter = new VideoListAdapter(videos, context);
        videoRV.setAdapter(videoListAdapter);
    }

    @Override
    public void onCompleted() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribeObserver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unSubscribeObserver();
    }

    private void unSubscribeObserver() {
        if (videoSubscription != null && !videoSubscription.isUnsubscribed()) {
            videoSubscription.unsubscribe();
        }
    }
}
