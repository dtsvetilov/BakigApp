package com.example.baking_app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking_app.R;
import com.example.baking_app.data.Recipe;
import com.example.baking_app.data.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {
    public static final String ARG_STEP = "step";
    public static final String ARG_RECIPE = "recipe";

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private Step mStep;
    private Recipe mRecipe;

    @BindView(R.id.previous_fab)
    FloatingActionButton mPreviousFab;

    @BindView(R.id.next_fab)
    FloatingActionButton mNextFab;

    @BindView(R.id.video_ep)
    SimpleExoPlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;

    private long mPlayerPosition;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_STEP) && arguments.containsKey(ARG_RECIPE)) {
            mStep = Parcels.unwrap(arguments.getParcelable(ARG_STEP));
            mRecipe = Parcels.unwrap(arguments.getParcelable(ARG_RECIPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            // Create exoplayer to show recipe video
            createMediaPlayer();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            createMediaPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) mPlayerPosition = mExoPlayer.getCurrentPosition();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }

        if (mTrackSelector != null) {
            mTrackSelector = null;
        }
    }

    public void createMediaPlayer() {

        boolean hasVideoUrl = mStep.getVideoURL() != null && !mStep.getVideoURL().isEmpty();
        boolean hasThumbnail = mStep.getThumbnailURL() != null && !mStep.getThumbnailURL().isEmpty();

        if (hasVideoUrl) {
            //noMediaIV.setVisibility(View.GONE);

            initializeMediaSession();
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        } else {
            //noMediaIV.setVisibility(View.VISIBLE);

            /*if (hasThumbnail) {
                Picasso.with(getContext())
                        .load(mStep.getThumbnailURL())
                        .placeholder(R.drawable.ic_recipe_placeholder)
                        .into(noMediaIV);
            } else {
                noMediaIV.setImageResource(R.drawable.default_recipe_image);
            }*/
        }
    }

    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            mBandwidthMeter = new DefaultBandwidthMeter();

            String userAgent = Util.getUserAgent(getContext(), TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MediaSessionCallback());
        mMediaSession.setActive(true);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }
}
