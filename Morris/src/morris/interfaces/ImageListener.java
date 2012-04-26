package morris.interfaces;

import morris.game.Network;

import android.util.Log;

import com.skiller.api.items.SKImage;
import com.skiller.api.listeners.SKOnGetImageListener;
import com.skiller.api.responses.SKGetImageResponse;

public class ImageListener extends SKOnGetImageListener{

	@Override
	public void onResponse(SKGetImageResponse st) {
		
		if(st.getStatusCode() == 0){
			Log.i("images", "ImageListener");
			SKImage [] imagesArray = st.getImageItemArray();
			Log.i("images", ""+imagesArray[1].toString());
			Network.getInstance().setImageArray(imagesArray);
		}
		
	}

}
