package morris.gui;


import java.util.ArrayList;

import morris.help.LogHelp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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

	private float xLeft = 10;
	private float yTop = 10;
	private float xRight;
	private float yBottom;
	private float viewWidth;
	private float viewHeight;
	private float midX;
	private float midY;
	private float boardW;
	private float calcValue;
	private float secondRect;
	private float thirdRect;
	LogHelp l = new LogHelp();
	ArrayList<Point> pointList = new ArrayList<Point>();



	public BoardView(Context context) {
		super(context);	
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		drawBoard(canvas);


	}
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		viewWidth = xNew;
		viewHeight = yNew;
		xRight = viewWidth;
		yBottom = viewWidth;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//This method is overridden in both local and network-class
		return true;
	}


	public void drawBoard(Canvas canvas){
		//board calc
		boardW = xRight-xLeft+yTop;
		calcValue = boardW/6;
		secondRect = calcValue;
		thirdRect = calcValue*2;
		Paint p = getPaint();
		//Rect r = new Rect(xLeft, yTop, viewWidth-11, viewWidth-10);
		xRight = xRight-xLeft;
		yBottom = yBottom-yTop;
		midX = xLeft+((xRight-xLeft)/2);
		midY = yTop+((yBottom-yTop)/2);
		//first Rect
		drawRect(xLeft, yTop, xRight, yBottom, canvas, p);
		//second Rect
		drawRect(xLeft+secondRect, yTop+secondRect, xRight-secondRect, yBottom-secondRect, canvas, p);
		//third Rect
		drawRect(xLeft+thirdRect, yTop+thirdRect, xRight-thirdRect, yBottom-thirdRect, canvas, p);
		//first Line
		drawLine(xLeft, midX, xLeft+thirdRect, midX, canvas, p);
		//second Line
		drawLine(xRight-thirdRect, midY, xRight, midY, canvas, p);
		//third Line
		drawLine(midX, yTop, midX, yTop+thirdRect, canvas, p);
		//4th line
		drawLine(midX, yBottom-thirdRect, midX, yBottom, canvas, p);
		p.setStyle(Style.FILL);
		drawPoints(canvas, p);
		
		for (Point point : pointList){
			l.Out(point.toString());
			if (point.getId() == 13){
				p.setStyle(Style.STROKE);
				p.setColor(Color.GREEN);
				p.setStrokeWidth(3);
				point.highLight(canvas, p);
			}
			
		}
		
		
	}

	public void drawRect(float xLeft, float yTop, float xRight, float yBottom, Canvas canvas, Paint p){
		canvas.drawRect(xLeft, yTop, xRight, yBottom, p);

	}

	public void drawCircle(float x, float y, Canvas canvas, Paint p){
		canvas.drawCircle(x, y, 10, p);
	}

	public void drawLine(double startX, double startY, double stopX, double stopY, Canvas canvas, Paint p){
		canvas.drawLine((int)startX, (int)startY, (int)stopX, (int)stopY, p);
	}

	public Paint getPaint(){
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		p.setColor(Color.BLACK);
		p.setStrokeWidth(6);
		return p;
	}

	public void drawPoints(Canvas canvas , Paint p){
		midX = midX - xLeft;
		int teller = 0;
		for(float x = xLeft; x < xRight+xLeft; x = x + midX){
			drawCircle(x, yTop, canvas, p);
			pointList.add(new Point(teller, x, yTop));
			drawCircle(x, yBottom, canvas, p);
			pointList.add(new Point(teller+3, x, yBottom));
			teller++;
		}
		midX = midX - secondRect;
		teller = 6;
		for(float x = xLeft+secondRect; x < xRight+xLeft; x = x + midX){
			drawCircle(x, yTop+secondRect, canvas, p);
			pointList.add(new Point(teller, x, yTop+secondRect));
			drawCircle(x, yBottom-secondRect, canvas, p);
			pointList.add(new Point(teller+3, x, yBottom-secondRect));
			teller++;
		}
		midX = midX - secondRect;
		teller = 12;
		for(float x = xLeft+thirdRect; x <= xRight-thirdRect; x = x + midX){
			drawCircle(x, yTop+thirdRect, canvas, p);
			pointList.add(new Point(teller, x, yTop+thirdRect));
			drawCircle(x, yBottom-thirdRect, canvas, p);
			pointList.add(new Point(teller+3, x, yBottom-thirdRect));
			teller++;
		}
		teller = 18;		
		for(float x = xLeft; x<= xLeft+thirdRect; x = x + secondRect){
			drawCircle(x, midY, canvas, p);
			pointList.add(new Point(teller, x, yTop+thirdRect));
			drawCircle(xRight-thirdRect-xLeft+x, midY, canvas, p);
			pointList.add(new Point(teller+3, xRight-thirdRect+x, midY));
			teller++;
		}
	}
}


