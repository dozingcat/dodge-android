package com.dozingcatsoftware.dodge;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class AndroidUtils {
	
	/** Returns a BitmapFactory.Options object containing the size of the image at the given URI,
	 * without actually loading the image.
	 */
	public static BitmapFactory.Options computeBitmapSizeFromURI(Context context, Uri imageURI) throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageURI), null, options);
		return options;
	}
	
	/** Returns a Bitmap from the given URI that may be scaled by an integer factor to reduce its size,
	 * while staying as least as large as the width and height parameters.
	 */
	public static Bitmap scaledBitmapFromURIWithMinimumSize(Context context, Uri imageURI, int width, int height) throws FileNotFoundException {
		BitmapFactory.Options options = computeBitmapSizeFromURI(context, imageURI);
		options.inJustDecodeBounds = false;
		
		float wratio = 1.0f*options.outWidth / width;
		float hratio = 1.0f*options.outHeight / height;		
		options.inSampleSize = (int)Math.min(wratio, hratio);
		
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageURI), null, options);		
	}

	/** Returns the result of Display.getRotation, or Surface.ROTATION_0 if the getRotation method isn't available in
	 * the current Android API.
	 */
	public static int getDeviceRotation(Context context) {
		try {
			Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			Method rotationMethod = null;
			// look for getRotation or the deprecated getOrientation
			for(String methodName : new String[] {"getRotation", "getOrientation"}) {
				try {
					rotationMethod = Display.class.getMethod(methodName);
					break;
				}
				catch(Exception ignored) {}
			}
			if (rotationMethod!=null) {
				return (Integer)rotationMethod.invoke(display);
			}
		}
		catch(Exception ignored) {}
		return Surface.ROTATION_0;
	}
}
