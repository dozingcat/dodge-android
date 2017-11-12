package com.dozingcatsoftware.dodge;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

/** Class which listens for orientation change events and delivers callback events with orientation values.
 * Getting orientation values requires reading gravitational and magnetic field values and calling a bunch of SensorManager methods
 * with rotation matrices; this class handles all of that and just provides a callback method with azimuth, pitch, and roll values.
 */

public class OrientationListener implements SensorEventListener {
	
	public static interface Delegate {
		/** Callback method for orientation updates. All values are in radians.
		 * @param azimuth rotation around the Z axis. 0=north, pi/2=east.
		 * @param pitch rotation around the X axis. 0=flat, negative=titled up, positive=tilted down.
		 * @param roll rotation around the Y axis. 0=flat, negative=tilted left, positive=tilted right.
		 */
		public void receivedOrientationValues(float azimuth, float pitch, float roll);
	}
	
	Context context;
	int rate;
	Delegate delegate;
	SensorManager sensorManager;
	int deviceRotation = Surface.ROTATION_0;
	
	/** Creates an OrientationListener with the given rate and callback delegate. Does not start listening for sensor events until start() is called.
	 * @param context Context object, typically either an Activity or getContext() from a View 
	 * @param rate constant from SensorManager, e.g. SensorManager.SENSOR_DELAY_GAME
	 * @param delegate callback object implementing the Delegate interface method.
	 */
	public OrientationListener(Context context, int rate, Delegate delegate) {
		this.context = context;
		this.rate = rate;
		this.delegate = delegate;
		sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	/** Sets the rotation of the screen from its natural orientation, so that pitch and roll values can be properly 
	 *  adjusted. For example, if the natural orientation of a tablet is landscape but it's being used in portrait mode,
	 *  the rotation value should be Surface.ROTATION_90 or Surface.ROTATION_270.
	 *  @see http://android-developers.blogspot.com/2010/09/one-screen-turn-deserves-another.html
	 *  @param rotation one of Surface.ROTATION_0, Surface.ROTATION_90, Surface.ROTATION_180, or Surface.ROTATION_270.
	 */
	public void setDeviceRotation(int rotation) {
		this.deviceRotation = rotation;
	}
	
	/** Starts listening for sensor events and making callbacks to the delegate.
	 */
	public void start() {
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), rate);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), rate);
	}
	
	/** Stops listening for sensor events and making callbacks to the delegate.
	 */
	public void stop() {
		sensorManager.unregisterListener(this);
	}


	// values used to compute orientation based on gravitational and magnetic fields
	float[] R = new float[16];
	float[] I = new float[16];
	// initialize magnetic field with dummy values so we can get pitch and roll even when there's no compass
	float[] mags = new float[] {1, 0, 0};
	float[] accels = null;
	
	float[] orientationValues = {0f, 0f, 0f};

	/** SensorEventListener method called when sensor values are updated. Reads gravitational and magnetic field information, and when both are available
	 * computes the orientation values and calls the delegate with them.
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()) {
		case Sensor.TYPE_MAGNETIC_FIELD:
			mags = event.values.clone();
			break;
		case Sensor.TYPE_ACCELEROMETER:
			accels = event.values.clone();
			break;
		}
		
		if (mags!=null && accels!=null) {
			SensorManager.getRotationMatrix(R, I, accels, mags);
			SensorManager.getOrientation(R, orientationValues);
			
			// adjust for device rotation if needed
			float pitch = orientationValues[1];
			float roll = orientationValues[2];
			switch (this.deviceRotation) {
				case Surface.ROTATION_90:
					float tmp1 = pitch;
					pitch = roll;
					roll = -tmp1;
					break;
				case Surface.ROTATION_180:
					pitch = -pitch;
					roll = -roll;
					break;
				case Surface.ROTATION_270:
					float tmp2 = pitch;
					pitch = -roll;
					roll = tmp2;
					break;
			}
					
			delegate.receivedOrientationValues(orientationValues[0], pitch, roll);
		}
	}

	// ignored SensorListener method
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
