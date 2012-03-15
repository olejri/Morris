package morris.gui;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Point {
	private int id;
	private float xCoor;
	private float yCoor;
	
	
	
	public Point(int id, float x, float y){
		this.id = id;
		this.xCoor = x;
		this.yCoor = y;
	}
	
	
	public String toString(){
		return "ID " + id + " X " + xCoor + " Y " + yCoor;
		
	}
	
	public int getId(){
		return id;
	}
	
	public void highLight(Canvas canvas, Paint p){
		canvas.drawCircle(xCoor, yCoor, 11, p);
	}

}
