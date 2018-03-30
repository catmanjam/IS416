package is416.is416;

/**
 * Created by aispa on 3/29/2018.
 */

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Random;

/**
 * Performs a bounce animation on a {@link Marker} when it is clicked.
 */
public class MapMarkerBounce implements GoogleMap.OnMarkerClickListener {

    private final Handler mHandler;
    private Runnable mAnimation;

    public MapMarkerBounce() {
        mHandler = new Handler();
    }

    public void makeBounce(Marker marker){
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        Random rand = new Random();
        long randDuration = rand.nextLong();
        if (randDuration<1500L){
            randDuration += 1500L;
        }

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);
        mAnimation = new BounceAnimation(start, 1500L, marker, mHandler);
        mHandler.post(mAnimation);
//        mHandler.removeCallbacksAndMessages(mAnimation);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        mAnimation = new BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return true;
    }

    /**
     * Performs a bounce animation on a {@link Marker}.
     */
    private static class BounceAnimation implements Runnable {

        private final long mStart, mDuration;
        private final Interpolator mInterpolator;
        private final Marker mMarker;
        private final Handler mHandler;

        private BounceAnimation(long start, long duration, Marker marker, Handler handler) {
            mStart = start;
            mDuration = duration;
            mMarker = marker;
            mHandler = handler;
            mInterpolator = new BounceInterpolator();
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - mStart;
            float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
            mMarker.setAnchor(0.5f, 1.0f + 1.2f * t);

            if (t > 0.0) {
                // Post again 16ms later.
                mHandler.postDelayed(this, 16L);
            }
        }
    }
}
