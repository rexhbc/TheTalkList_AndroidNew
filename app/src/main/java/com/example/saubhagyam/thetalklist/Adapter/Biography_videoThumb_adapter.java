package com.example.saubhagyam.thetalklist.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.saubhagyam.thetalklist.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saubhagyam on 20/09/2017.
 */

public class Biography_videoThumb_adapter extends RecyclerView.Adapter<Biography_videoThumb_adapter.MyViewHolder> {

    Context context;
    JSONArray biography_video_ary;


    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ExoPlayer";


    SimpleExoPlayer player;
    SimpleExoPlayerView playerView;

    ComponentListener componentListener;

    long PlayBackPosition;
    int CurrentWindow;
    boolean playWhenReady = true;



    public Biography_videoThumb_adapter(Context context, JSONArray biography_video_ary,SimpleExoPlayerView playerView) {
        this.context = context;
        this.biography_video_ary = biography_video_ary;
        this.playerView=playerView;
        componentListener=new ComponentListener();
//        player=this.playerView.getPlayer();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumblines_layout, parent, false);
        return new Biography_videoThumb_adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            final JSONObject thumbObj=biography_video_ary.getJSONObject(position);
            Glide.with(context).load("https://www.thetalklist.com/uploads/video/"+thumbObj.getString("source")+".jpg").into(holder.thumb);

            holder.thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    try {
//                        String  link = "https://www.thetalklist.com/uploads/video/" + thumbObj.getString("source");
//                        String  link = "http://52.8.60.79/uploads/video/" + thumbObj.getString("source");
                        String  link = "https://www.thetalklist.com/uploads/video/" + thumbObj.getString("source");
                        InitializePLayer(link);


                        final SharedPreferences bio_vid_url=context.getSharedPreferences("biography_video",Context.MODE_PRIVATE);
                        SharedPreferences.Editor bio_edit=bio_vid_url.edit();

                        bio_edit.putString("videourl",link).apply();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return biography_video_ary.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb;

        public MyViewHolder(View itemView) {
            super(itemView);

            thumb= (ImageView) itemView.findViewById(R.id.thumb);


        }
    }

    private void InitializePLayer(String link) throws ParseException {
        if (player==null){
            TrackSelection.Factory factory=new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player= ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), new DefaultTrackSelector(factory),new DefaultLoadControl());
            player.addListener(componentListener);
            player.setAudioDebugListener(componentListener);
            player.setVideoDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(false);
            player.seekTo(CurrentWindow,PlayBackPosition);

        }

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "yourApplicationName"), BANDWIDTH_METER);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(link),dataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource,true,false);
    }







    private class ComponentListener implements ExoPlayer.EventListener, VideoRendererEventListener, AudioRendererEventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object o) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

        }

        @Override
        public void onLoadingChanged(boolean b) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            String stateString;
            switch (playbackState){
                case ExoPlayer.STATE_IDLE:
                    stateString="ExoPlayer.STATE_IDLE   -";
                    break;


                case ExoPlayer.STATE_BUFFERING:
                    stateString="ExoPlayer.STATE_BUFFERING   -";
                    break;


                case ExoPlayer.STATE_READY:
                    stateString="ExoPlayer.STATE_READY  -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString="ExoPlayer.STATE_ENDED  -";
                    break;

                default:
                    stateString="Unknown State  -";
                    break;
            }

            Log.e(TAG,"changed state to "+stateString+"PLay when ready "+playWhenReady);

        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onVideoEnabled(DecoderCounters decoderCounters) {

        }

        @Override
        public void onVideoDecoderInitialized(String s, long l, long l1) {

        }

        @Override
        public void onVideoInputFormatChanged(Format format) {

        }

        @Override
        public void onDroppedFrames(int i, long l) {

        }

        @Override
        public void onVideoSizeChanged(int i, int i1, int i2, float v) {

        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {

        }

        @Override
        public void onVideoDisabled(DecoderCounters decoderCounters) {

        }

        @Override
        public void onAudioEnabled(DecoderCounters decoderCounters) {

        }

        @Override
        public void onAudioSessionId(int i) {

        }

        @Override
        public void onAudioDecoderInitialized(String s, long l, long l1) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioTrackUnderrun(int i, long l, long l1) {

        }

        @Override
        public void onAudioDisabled(DecoderCounters decoderCounters) {

        }
    }
}
