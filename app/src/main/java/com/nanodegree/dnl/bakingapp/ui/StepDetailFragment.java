package com.nanodegree.dnl.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.nanodegree.dnl.bakingapp.R;
import com.nanodegree.dnl.bakingapp.data.Recipe;
import com.nanodegree.dnl.bakingapp.data.Step;
import com.nanodegree.dnl.bakingapp.utilities.StringHelpers;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailFragment extends Fragment {
    @BindView(R.id.player_container)
    RelativeLayout playContainer;

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;

    @BindView(R.id.no_media_iv)
    ImageView noMediaIv;

    @BindView(R.id.step_desc_tv)
    TextView stepDescription;

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private static final String SELECTED_POSITION = "player_selected_position";
    private static final String PLAY_STATE = "play_state";

    private Recipe mRecipe;
    private Step mStep;

    private SimpleExoPlayer mExoPlayer;
    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;

    private long mPlayerPosition;
    boolean isPlayWhenReady = true;

    public StepDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = Parcels.unwrap(getArguments().getParcelable("recipe"));
        int stepPosition = getArguments().getInt("step_position", 0);
        mStep = mRecipe.getSteps().get(stepPosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        stepDescription.setText(mStep.getDescription());

        if (savedInstanceState == null) {
            createMediaPlayer();
        } else {
            mPlayerPosition = savedInstanceState.getLong(SELECTED_POSITION, 0);
            isPlayWhenReady = savedInstanceState.getBoolean(PLAY_STATE);
        }
        if (!getResources().getBoolean(R.bool.is_tablet))
            resizeMediaPlayer();

        return rootView;
    }

    private void resizeMediaPlayer() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int actionBarHeight = 0;
            TypedValue tv = new TypedValue();
            if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            height -= actionBarHeight;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            playerView.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            playerView.setLayoutParams(layoutParams);
        }
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
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            isPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, mPlayerPosition);
        outState.putBoolean(PLAY_STATE, isPlayWhenReady);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            isPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

        if (mTrackSelector != null) {
            mTrackSelector = null;
        }
    }

    public void createMediaPlayer() {

        if (!StringHelpers.isNullOrEmpty(mStep.getVideoURL())) {
            playerView.setVisibility(View.VISIBLE);
            noMediaIv.setVisibility(View.GONE);

            initializePlayer(Uri.parse(mStep.getVideoURL()));

        } else {
            playerView.setVisibility(View.GONE);
            noMediaIv.setVisibility(View.VISIBLE);

            if (!StringHelpers.isNullOrEmpty(mStep.getThumbnailURL())) {
                Picasso.with(getContext())
                        .load(mStep.getThumbnailURL())
                        .placeholder(R.drawable.ic_recipe_placeholder)
                        .error(R.drawable.ic_recipe_placeholder)
                        .into(noMediaIv);
            } else {
                noMediaIv.setImageResource(R.drawable.ic_recipe_placeholder);
            }
        }
    }

    private void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            playerView.setPlayer(mExoPlayer);

            mBandwidthMeter = new DefaultBandwidthMeter();

            String userAgent = Util.getUserAgent(getContext(), TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(isPlayWhenReady);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }
}
