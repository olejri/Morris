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
		
		help_box.addView(getTitleTextView("General",android.R.drawable.ic_menu_info_details));
		String general = "Each player has nine pieces. The game can be won in two different ways. If you can trap your opponent, " +
				"making it impossible for him to move, and if your opponent has only two pieces left.";
		help_box.addView(getDescriptionView(general));
		
		help_box.addView(getTitleTextView("Placement",android.R.drawable.ic_menu_info_details));
		String placement = "In the starting phase of the game, each player gets to place all nine pieces. If you get three in a row, " +
				"you are allowed to remove any piece from the opponent that aren't forming three in a row";
		help_box.addView(getDescriptionView(placement));
		
		help_box.addView(getTitleTextView("Select",android.R.drawable.ic_menu_info_details));
		String select = "When the placement phase is over, the player can choose one of his pieces that he wants to move. " +
				"This is only possible if neighbouring board points are free, or you have three pieces left and have entered " +
				"the flying phase.";
		help_box.addView(getDescriptionView(select));
	
		help_box.addView(getTitleTextView("Movement",android.R.drawable.ic_menu_info_details));
		String movement = "When a piece has been chosen, the player can move it to any free neighbouring board points.";
		help_box.addView(getDescriptionView(movement));
		
		help_box.addView(getTitleTextView("Remove",android.R.drawable.ic_menu_info_details));
		String removal = "When you have three pieces in a row, you are allowed to remove one piece from your opponent, as long as " +
				"it is not currently forming a mill (three in a row).";
		help_box.addView(getDescriptionView(removal));
		
		help_box.addView(getTitleTextView("Flying",android.R.drawable.ic_menu_info_details));
		String flying = "When a player has only three pieces left, he is allowed to move them to any free board point.";
		help_box.addView(getDescriptionView(flying));
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
