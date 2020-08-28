package uk.ac.reading.rt016631.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public abstract class GameThread extends Thread {
	//Different mMode states
	public static final int STATE_LOSE = 1;
	public static final int STATE_PAUSE = 2;
	public static final int STATE_READY = 3;
	public static final int STATE_RUNNING = 4;
	public static final int STATE_WIN = 5;

	//Control variable for the mode of the game (e.g. STATE_WIN)
	protected int mMode = 1;

	//Control of the actual running inside run()
	private boolean mRun = false;
		
	//The surface this thread (and only this thread) writes upon
	private SurfaceHolder mSurfaceHolder;
	
	//the message handler to the View/Activity thread
	private Handler mHandler;
	
	//Android Context - this stores almost all we need to know
	private Context mContext;
	
	//The view
	public GameView mGameView;

	//We might want to extend this call - therefore protected
	protected int mCanvasWidth = 1;
	protected int mCanvasHeight = 1;

	//Last time we updated the game physics
	protected long mLastTime = 0;
 
	//Two background images
	protected Bitmap mScrollingBackground;
	private Bitmap mScrollingBackgroundFinal;

	//Y positions of two seamlessly scrolling backgrounds
	protected int mBackgroundAYPos = 0, mBackgroundBYPos = 0;

	protected long heightScore = 0;
	protected long monsterScore = 0;
	protected long totalScore = 0;

    //Used for time keeping
	private long now;
	private float elapsed;

    //Rotation vectors used to calculate orientation
    private float[] mGravity;
    private float[] mGeomagnetic;

    //Used to ensure appropriate threading
    private static final Integer monitor = 1;



	public GameThread(GameView gameView) {		
		mGameView = gameView;
		
		mSurfaceHolder = gameView.getHolder();
		mHandler = gameView.getmHandler();
		mContext = gameView.getContext();

		//Prepare the image of the scrolling background so we can draw them on the screen (using a canvas)
		mScrollingBackground =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(),
						R.drawable.background_clouds);
	}
	
	/*
	 * Called when app is destroyed, so not really that important here
	 * But if (later) the game involves more thread, we might need to stop a thread, and then we would need this
	 * Dare I say memory leak...
	 */
	public void cleanup() {		
		this.mContext = null;
		this.mGameView = null;
		this.mHandler = null;
		this.mSurfaceHolder = null;
	}
	
	//Pre-begin a game
	abstract public void setupBeginning();
	
	//Starting up the game
	public void doStart() {
		synchronized(monitor) {
			
			setupBeginning();
			
			mLastTime = System.currentTimeMillis() + 100;

			setState(STATE_RUNNING);
			
			setScore(0);
		}
	}
	
	//The thread start
	@Override
	public void run() {
		Canvas canvasRun;
		while (mRun) {
			canvasRun = null;
			try {
				canvasRun = mSurfaceHolder.lockCanvas(null);
				synchronized (monitor) {
					if (mMode == STATE_RUNNING) {
						updatePhysics();
					}
					doDraw(canvasRun);
				}
			} 
			finally {
				if (canvasRun != null) {
					if(mSurfaceHolder != null)
						mSurfaceHolder.unlockCanvasAndPost(canvasRun);
				}
			}
		}
	}
	
	/*
	 * Surfaces and drawing
	 */
	public void setSurfaceSize(int width, int height) {
		synchronized (monitor) {
			mCanvasWidth = width;
			mCanvasHeight = height;

			//Set scrolling backgrounds to screen size
			mScrollingBackgroundFinal = Bitmap.createScaledBitmap(mScrollingBackground, mCanvasWidth, mCanvasHeight, true);
		}
	}

	protected void doDraw(Canvas canvas) {
		if(canvas == null) return;

		//Draw scrolling background
		if(mScrollingBackgroundFinal != null) canvas.drawBitmap(mScrollingBackgroundFinal, 0, mBackgroundAYPos, null);
		if(mScrollingBackgroundFinal != null) canvas.drawBitmap(mScrollingBackgroundFinal, 0, mBackgroundBYPos, null);

	}

	private void updatePhysics() {
		now = System.currentTimeMillis();
		elapsed = (now - mLastTime) / 1000.0f;

		updateGame(elapsed);

		mLastTime = now;
	}
	
	abstract protected void updateGame(float secondsElapsed);
	
	/*
	 * Control functions
	 */
	
	//Finger touches the screen
	public boolean onTouch(MotionEvent e) {
		if(e.getAction() != MotionEvent.ACTION_DOWN) return false;

        if(mMode == STATE_LOSE) {
            return true;
        }

		if(mMode == STATE_READY) {
			doStart();
			return true;
		}
		
		if(mMode == STATE_PAUSE) {
			unpause();
			return true;
		}
		
		synchronized (monitor) {
			this.actionOnTouch(e.getRawX(), e.getRawY());
		}
		 
		return false;
	}
	
	protected void actionOnTouch(float x, float y) {
		//Override to do something
	}

	//The Orientation has changed
	@SuppressWarnings("deprecation")
	public void onSensorChanged(SensorEvent event) {
		synchronized (monitor) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    actionWhenPhoneMoved(orientation[2],orientation[1],orientation[0]);
                }
            }
		}
	}
	
	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
		//Override to do something
	}
	
	/*
	 * Game states
	 */
	public void pause() {
		synchronized (monitor) {
			if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
		}
	}
	
	public void unpause() {
		// Move the real time clock up to now
		synchronized (monitor) {
			mLastTime = System.currentTimeMillis();
		}
		setState(STATE_RUNNING);
	}	

	//Send messages to View/Activity thread
	public void setState(int mode) {
		synchronized (monitor) {
			setState(mode, null);
		}
	}

	public void setState(int mode, CharSequence message) {
		synchronized (monitor) {
			mMode = mode;

			if (mMode == STATE_RUNNING) {
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("text", "");
				b.putInt("viz", View.INVISIBLE);
				b.putBoolean("showAd", false);	
				msg.setData(b);
				mHandler.sendMessage(msg);
			} else if(mMode == STATE_LOSE) {
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putBoolean("lose", true);
				b.putString("text", "");
				b.putInt("viz", View.INVISIBLE);
				msg.setData(b);
				mHandler.sendMessage(msg);
			} else {
				Message msg = mHandler.obtainMessage();
				Bundle b = new Bundle();
				
				Resources res = mContext.getResources();
				CharSequence str = "";
				if (mMode == STATE_READY)
					str = res.getText(R.string.mode_ready);
				else 
					if (mMode == STATE_PAUSE)
						str = res.getText(R.string.mode_pause);
					else 
						if (mMode == STATE_WIN) {
							str = res.getText(R.string.mode_win);
						}

				if (message != null) {
					str = message + "\n" + str;
				}

				b.putString("text", str.toString());
				b.putInt("viz", View.VISIBLE);

				msg.setData(b);
				mHandler.sendMessage(msg);
			}
		}
	}
	
	/*
	 * Getters and setters
	 */	
	public void setSurfaceHolder(SurfaceHolder h) {
		mSurfaceHolder = h;
	}
	
	public boolean isRunning() {
		return mRun;
	}
	
	public void setRunning(boolean running) {
		mRun = running;
	}
	
	public int getMode() {
		return mMode;
	}

	public void setMode(int mMode) {
		this.mMode = mMode;
	}

	/*
	 * Scores functions
     */
	
	//Send a score to the View to view 
	//Would it be better to do this inside this thread writing it manually on the screen?
	public void setScore(long score) {
		this.totalScore = score;
		synchronized (monitor) {
			Message msg = mHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putBoolean("score", true);
			b.putString("text", getScoreString().toString());
			msg.setData(b);
			mHandler.sendMessage(msg);
		}
	}

	public float getHeightScore() {
		return heightScore;
	}

	public float getMonsterScore() {
		return monsterScore;
	}

	public float getTotalScore() {
		return totalScore;
	}

	public void setHeightScore(long heightScore) {
		this.heightScore = heightScore;
	}

	public void setMonsterScore(long monsterScore) {
		this.monsterScore = monsterScore;
	}

	public void setTotalScore(long totalScore) {
		this.totalScore = totalScore;
	}
	
	public void updateTotalScore() {
		this.setScore(this.heightScore + this.monsterScore);
	}

	public void updateHeightScore(long score) {
		this.heightScore += score;
		updateTotalScore();
	}

	public void updateMonsterScore(long score) {
		this.monsterScore += score;
		updateTotalScore();
	}

	protected CharSequence getScoreString() {
		return Long.toString(Math.round(this.totalScore));
	}
	
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.