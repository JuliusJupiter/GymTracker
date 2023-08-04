package com.example.gymtracker.Control;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private GestureDetector gestureDetector;
    private ListView listView;

    public OnSwipeTouchListener(Context context, ListView listView) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.listView = listView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY) &&
                        Math.abs(diffX) > SWIPE_THRESHOLD &&
                        Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Swipe zur rechten Seite
                    } else {
                        // Swipe zur linken Seite
                        int position = listView.pointToPosition((int) e1.getX(), (int) e1.getY());
                        onSwipeLeft(position);
                    }
                    return true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }
    }

    public void onSwipeLeft(int position) {

    }
}

