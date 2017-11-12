package com.dozingcatsoftware.dodge;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/** Custom preference item that allows the user to select a background image for the field view, and shows
 * the image on the preferences screen. DodgePreferences handles the touch event to select an image, and 
 * this class updates the ImageView which displays the image.
 */

public class BackgroundImagePreference extends Preference {
	
	ImageView imageView;

	public BackgroundImagePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/* onBindView is called repeatedly, for example when a different preference is clicked, so we need to set the image here.
	 */
	@Override
	public void onBindView(View view) {
		super.onBindView(view);
		// custom layout for this preference is image_preference.xml, set in preferences.xml
		imageView = (ImageView)view.findViewById(R.id.prefs_image_view);
		updateBackgroundImage();
	}

	/** Updates the ImageView which displays the image, based on the image URI set in SharedPreferences.
	 */
	void updateBackgroundImage() {
    	if (imageView!=null) {
    		Bitmap bitmap = null;
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
    		String imageURIString = prefs.getString(DodgePreferences.IMAGE_URI_KEY, null);
    		if (imageURIString!=null) {
    			Uri imageURI = Uri.parse(imageURIString);
    			try {
    				bitmap = AndroidUtils.scaledBitmapFromURIWithMinimumSize(getContext(), imageURI, Math.max(128, imageView.getWidth()), Math.max(128, imageView.getHeight()));
    			}
    			catch(Exception ignored) {}
    		}
        	imageView.setImageBitmap(bitmap);
    	}
	}
	

}
