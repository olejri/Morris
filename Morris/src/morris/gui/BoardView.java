package morris.gui;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class BoardView extends View {

	public BoardView(Context context) {
		super(context);	
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		//For scaling

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//This method is overridden in both local and network-class
		return true;
	}


	

}


