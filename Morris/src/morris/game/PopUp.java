package morris.game;

/*
 * *****************************************************************************
 * Copyright (c) 2007-2011 Skiller LTD. All rights reserved.
 * 
 * All source code is the copyright of Skiller LTD. and
 * is protected under copyright laws. This source code may
 * not be distributed or copied without Skiller's express, prior, written
 * permission.
 *
 * We have made every effort and taken great care in making sure that the source
 * code and other content included with the SDK software is correct, but we
 * disclaim any and all responsibility for any loss, damage or destruction of
 * data or any other property which may arise from relying on it. Skiller LTD. 
 * will in no case be liable for any monetary or any other damages arising from 
 * such loss, damage or destruction.
 * 
 * *****************************************************************************
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.skiller.api.items.SKImage;
import com.skiller.api.items.SKUser;


public class PopUp extends Dialog  {
	
	private Context context = null;
	
	//PopUp attributes
	private SKUser 	useritem1;  //Title of the popup
	private SKUser    useritem2;  //Title of the popup
	private String 	    textString;	  	
	private byte[] image1ByteArray;
	private byte[] image2ByteArray;
	private byte[] flag1ByteArray;
	private byte[] flag2ByteArray;
	//layouts and views
	private LinearLayout linear;
	private TableLayout tableLayout;
	private TableRow  tablerow1;
	private TableRow  tablerow2;
	private TableRow  tablerow3;
	private TableRow  tablerow4;
	private TextView  username1;
	private TextView  username2;
	private ImageView flag1;
	private ImageView flag2;
	private ImageView image1;
	private ImageView image2;
	private ImageView vsimage;
	private TextView  textline;
	//popup bitmap
	private Bitmap 		bmp;

	// a constractor that gets the data for a pop up of online game (user1 vs user2 is showed)
	public PopUp(Context context, SKUser user1,SKUser user2,SKImage [] imagesArray,String textString)
	{
		super(context);
		this.context = context;
		this.useritem1 = user1;
		this.useritem2 = user2;
		//ssetContentView(R.layout.vssscreen);
		
		for(int i=0; i < imagesArray.length; i++)
		{
			if (imagesArray[i].getId().compareTo(this.useritem1.getAvatarFullImageId()) == 0)
			{
				this.image1ByteArray=imagesArray[i].getByteArray();
			}
			
			if (imagesArray[i].getId().compareTo(this.useritem2.getAvatarFullImageId()) == 0)
			{
				this.image2ByteArray=imagesArray[i].getByteArray();
			}
			
			if (imagesArray[i].getId().compareTo(this.useritem1.getCountryImageId()) == 0)
			{
				this.flag1ByteArray=imagesArray[i].getByteArray();
			}
			
			if (imagesArray[i].getId().compareTo(this.useritem2.getCountryImageId()) == 0)
			{
				this.flag2ByteArray=imagesArray[i].getByteArray();
			}
			
		}
			
		this.textString=textString;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vssscreen);
		username1=(TextView)findViewById(R.id.vsusername1);
		username1.setText(this.useritem1.getUserName());
		username1.setTextColor(Color.WHITE);
		username1.setTextSize(20);
		username2=(TextView)findViewById(R.id.vsusername2);
		username2.setText(this.useritem2.getUserName());
		username2.setTextColor(Color.WHITE);
		username2.setTextSize(20);
        
        this.flag1=(ImageView)findViewById(R.id.vsflag1);   
        bmp=BitmapFactory.decodeByteArray(this.flag1ByteArray,0,this.flag1ByteArray.length);
        this.flag1.setImageBitmap(bmp);
        
        this.flag2=(ImageView)findViewById(R.id.vsflag2);   
        bmp=BitmapFactory.decodeByteArray(this.flag2ByteArray,0,this.flag2ByteArray.length);
        this.flag2.setImageBitmap(bmp);
        
        this.image1=(ImageView)findViewById(R.id.vsImage1);
        bmp=BitmapFactory.decodeByteArray(this.image1ByteArray,0,this.image1ByteArray.length);
        //flipping the left image
        // Getting width and height of the given image.  
        int w = bmp.getWidth();  
        int h = bmp.getHeight();  
        // Setting a flip matrix
        Matrix mtx = new Matrix();  
        mtx.preScale(-1.0f, 1.0f);  
        // flipping Bitmap  
        Bitmap flippedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);  
        this.image1.setImageBitmap(flippedBMP);
    
        this.image2=(ImageView)findViewById(R.id.vsImage2);     
        bmp=BitmapFactory.decodeByteArray(this.image2ByteArray,0,this.image2ByteArray.length);
        this.image2.setImageBitmap(bmp);
       
        this.textline=(TextView)findViewById(R.id.vstextline);
        this.textline.setText(textString);  
        this.textline.setTextColor(Color.BLACK);
        this.textline.setTextSize(20);
	}
	
	// a popup that gets a title and text string to show in a pop up
	public PopUp(Context context,String title,String text)
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout linear=new LinearLayout(context);
		linear.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		TableLayout tableLayout=new TableLayout(context);
        linear.addView(tableLayout);
        TableRow tablerow1=new TableRow(context);
        tableLayout.addView(tablerow1);
        tablerow1.setGravity(Gravity.CENTER);
        TableRow tablerow2=new TableRow(context);
        tableLayout.addView(tablerow2);
        tablerow2.setGravity(Gravity.CENTER);
		TextView titletext= new TextView(context);
		titletext.setText(title);
		titletext.setTextSize(18);
		titletext.setGravity(Gravity.CENTER);
		titletext.setTextColor(Color.WHITE);
		TextView messagetext= new TextView(context);
		messagetext.setText(text);
		messagetext.setTextSize(18);
		messagetext.setTextColor(Color.WHITE);
		tablerow1.addView(titletext);
		tablerow1.setGravity(Gravity.CENTER);
		tablerow2.addView(messagetext);
		tablerow1.setGravity(Gravity.CENTER);
		setContentView(linear);
	}


}

