package com.example.baking_app.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import butterknife.OnClick;

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

    @BindView(R.id.title_tv)
    TextView mTitleTv;

    @BindView(R.id.description_tv)
    TextView mDescriptionTv;

    private SimpleExoPlayer mExoPlayer;
    private BandwidthMeter mBandwidthMeter;
    private TrackSelector mTrackSelector;

    private long mPlayerPosition;

    public RecipeStepDetailFragment() {
    }

    private View.OnClickListener onClickListener;

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

        if (savedInstanceState == null)
            fillData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null) {
            fillData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null)
            mPlayerPosition = mExoPlayer.getCurrentPosition();
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

        if (mTrackSelector != null) {
            mTrackSelector = null;
        }
    }

    public void fillData() {
        boolean hasVideoUrl = mStep.getVideoURL() != null && !mStep.getVideoURL().isEmpty();

        if (hasVideoUrl) {
            //noMediaIV.setVisibility(View.GONE);

            //initializeMediaSession();
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

        mTitleTv.setText(mStep.getShortDescription());
        mDescriptionTv.setText(mStep.getDescription());
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwidthMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            mBandwidthMeter = new DefaultBandwidthMeter();

            String userAgent = Util.getUserAgent(getContext(), TAG);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    @OnClick(R.id.previous_fab)
    public void onPreviousStepClick(View view) {

    }

    @OnClick(R.id.next_fab)
    public void onNextStepClick(View view) {

    }

    public interface IStepChangeListener {
        void onStepChange(Step stepToShow);
    }
}
