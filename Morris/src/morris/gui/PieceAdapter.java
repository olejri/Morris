package morris.gui;

import android.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PieceAdapter extends BaseAdapter {
	private Context context;
	private boolean white = false;
	private int number;

	public PieceAdapter(Context context,boolean white,int number) {
		this.context = context;
		this.white = white;
		this.number = number;
	}

	@Override
	public int getCount() {
		return number;
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
		if (white) {
			imageView.setImageResource(morris.game.R.drawable.piece_white);
		}else{
			imageView.setImageResource(morris.game.R.drawable.piece_black);
		}
		return imageView;
	}

	private Integer[] BLACK = { morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black,
			morris.game.R.drawable.piece_black };

}
