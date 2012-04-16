package morris.game;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpActivity extends SuperActivity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help_layout);
		
		setTextFont();
		
		LinearLayout help_box = (LinearLayout)findViewById(R.id.help_box);
		
		help_box.addView(getTitleTextView("Rules",android.R.drawable.ic_menu_info_details));
		help_box.addView(getDescriptionView("Here are some text about rules. Some more rules about the pieces etc. Some text. Yea."));
		
		help_box.addView(getTitleTextView("Pieces",android.R.drawable.ic_menu_info_details));
		help_box.addView(getDescriptionView("Here are some text about the pieces. Some more rules about the pieces etc. Some text. Yea."));
	
		help_box.addView(getTitleTextView("More",android.R.drawable.ic_menu_info_details));
		help_box.addView(getDescriptionView("Scrolling works. Yeas. ladjkfals kdfl ksdnf lksdnf lskdnf lskdf lsdf k"));
	}
	
	private void setTextFont(){
		Typeface button_font = Typeface.createFromAsset(getAssets(), "fonts/text-font.otf");
		((TextView)((Activity)this).findViewById(R.id.toolbar_title)).setTypeface(button_font);
	}
	
	private View getTitleTextView(String title,int titleImage){
		TextView titleView = new TextView(this);
		titleView.setText(title);
		titleView.setTextSize(25);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT );
	    para.setMargins(10, 25, 10, 20);
	    			//left,top,right, bottom
	    titleView.setLayoutParams(para);
		titleView.setTextColor(Color.BLACK);
		
		if(titleImage!=0){
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.addView(getImage(titleImage));
			layout.addView(titleView);
			return layout;
		}else{
			return titleView;
		}
		
	}
	
	private View getDescriptionView(String text){
		TextView titleView = new TextView(this);
		titleView.setText(text);
		titleView.setTextSize(20);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT );
	    para.setMargins(10, 15, 0, 15); //left,top,right, bottom
	    titleView.setLayoutParams(para);
	    titleView.setTextColor(Color.BLACK);
		return titleView;
	}
	
	private View getImage(int imageId){
		ImageView image = new ImageView(this);
		image.setBackgroundResource(imageId);
		LinearLayout.LayoutParams para=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
	    para.setMargins(0, 20, 0, 20); //left,top,right, bottom
	    image.setLayoutParams(para);
	    return image;
		
	}

}
