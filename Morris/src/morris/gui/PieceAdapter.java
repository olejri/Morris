package morris.gui;

import morris.game.GameHandler;
import morris.help.Constant;
import morris.models.Player;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PieceAdapter extends BaseAdapter {
	private Context context;
	private String color;

	public PieceAdapter(Context context,String color) {
		this.context = context;
		this.color = color;
	}

	@Override
	public int getCount() { // lagt til getInstance()
		if(GameHandler.getMorrisGame().getPlayer1().getColor().equals(color)){
			return GameHandler.getMorrisGame().getPlayer1().getPieces().size();
		}else if(GameHandler.getMorrisGame().getPlayer2().getColor().equals(color)){
			Log.i("GameHandler", "Size: " + GameHandler.getMorrisGame().getPlayer1().getPieces().size());
			return GameHandler.getMorrisGame().getPlayer2().getPieces().size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(30, 30));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(2, 2, 2, 2);
		} else {
			imageView = (ImageView) convertView;
		}
		if (color.equals(Constant.WHITE)) {
			imageView.setImageResource(morris.game.R.drawable.piece_white);
		}else{
			imageView.setImageResource(morris.game.R.drawable.piece_black);
		}
		return imageView;
	}
}
