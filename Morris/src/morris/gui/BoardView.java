package morris.gui;

import java.util.ArrayList;

import morris.game.controller.GameController;
import morris.help.Constant;
import morris.help.LogHelp;
import morris.models.Piece;
import morris.models.Player;
import morris.models.Slot;
import morris.states.MoveState;
import morris.states.PlacementState;
import morris.states.RemovalState;
import morris.states.SelectState;
import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class BoardView extends View {
	private float xLeft;
	private float yTop;
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
	private float xRightOld;
	private float yBottomOld;
	private int pieceSize;
	private boolean makePointList = true;
	LogHelp l = new LogHelp();
	ArrayList<Point> pointList = new ArrayList<Point>();

	// Bitmaps pieces

	public BoardView(Context context) {
		super(context);
		init();
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Draw board
		drawBoard(canvas);

		drawPieces(canvas);

	}

	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		viewWidth = xNew;
		viewHeight = yNew;
		xRight = viewWidth;
		yBottom = viewWidth;
		float calc = xRight / 30;
		xLeft = calc;
		yTop = calc;
		pieceSize = (int) (calc * 2);
		xRightOld = xRight;
		yBottomOld = yBottom;
		System.out.println("xNew: " + xNew + " yNew :" + yNew);

	}

	/**
	 * Player pressed the player board
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (GameController.getInstance().getMorrisGame().isYourTurn()) {
				Point p = getPressedPoint(event.getX(), event.getY());
				if(p!=null){
				if (GameController.getInstance().getMorrisGame().getState() instanceof PlacementState) {	
					Log.i(Constant.STATE_DEBUG, "Placement state");
					for (int i = 0; i < GameController.getInstance().getMorrisGame().getPlayer1().getPieces().size(); i++) {
						Piece piece = GameController.getInstance().getMorrisGame().getPlayer1().getPieces().get(i);
						if (piece.getPosition() < 0) {
							piece.setPosition(p.getId());
							GameController.getInstance().getMorrisGame().playerPlacedPiece(GameController.getInstance().getMorrisGame().getPlayer1(),piece);
							GameController.getInstance().getMorrisGame().getBoard().getSlotByID(p.getId()).setTaken(true);  // STEINAR 19.03
							GameController.getInstance().getMorrisGame().getBoard().printTakenSlots(); // STEINAR 19.03
							System.out.println("ID set to:"+piece.getPosition());
							break;
						}
					}
					
				// STEINAR 19.03
				} else if(GameController.getInstance().getMorrisGame().getState() instanceof SelectState){
					Log.i(Constant.STATE_DEBUG, "Select state");
					if(GameController.getInstance().getMorrisGame().selectable(GameController.getInstance().getMorrisGame().getPlayer1(), p.getId())){
						GameController.getInstance().getMorrisGame().updatePieceImages(GameController.getInstance().getMorrisGame().getPlayer1(), p.getId());
						GameController.getInstance().getMorrisGame().setState(new MoveState());
					}
				} else if(GameController.getInstance().getMorrisGame().getState() instanceof MoveState){
					Log.i(Constant.STATE_DEBUG, "Move state");
						GameController.getInstance().getMorrisGame().updatePieceImages(GameController.getInstance().getMorrisGame().getPlayer1(), p.getId());
						if(GameController.getInstance().getMorrisGame().getPlayer1().getSelectedPiece().getPosition()==p.getId()){
							GameController.getInstance().getMorrisGame().setState(new SelectState());
						}
				} else if(GameController.getInstance().getMorrisGame().getState() instanceof RemovalState){
					
				}
			}
			postInvalidate();
		}
		}
		// Update screen

		return true;
	}
	
	// Add needed parameters. I want a piece highlighting plz. -Steinar
	public void highlightPieces(Player player){
		//TODO
	}

	public void highlightPoints(Canvas c, Paint p) {
		// GET PLAYER ONE FOR TESTING PURPOSES
		// STEINAR 19.03
		ArrayList<Slot> highlights = new ArrayList<Slot>();
		if(GameController.getInstance().getMorrisGame().getPlayer1().getSelectedPiece() != null){
			highlights = GameController.getMorrisGame().getHighlightList(GameController.getInstance().getMorrisGame().getPlayer1().getSelectedPiece().getPosition(), GameController.getInstance().getMorrisGame().getPlayer1());  
		} else {
			highlights = GameController.getMorrisGame().getHighlightList(-1, GameController.getInstance().getMorrisGame().getPlayer1());  
		}
		for (int i = 0; i < highlights.size(); i++) {
			for (int j = 0; j < pointList.size(); j++) {
				if (highlights.get(i).getId() == pointList.get(j).getId()) {
					pointList.get(j).highLight(c, p);
				}
			}
		}
	}

	/**
	 * Get Point the player has pressed
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Point getPressedPoint(float x, float y) {
		for (Point p : pointList) {
			if (p.getX() - x > -50 && p.getX() - x < 50) {
				if (p.getY() - y > -50 && p.getY() - y < 50) {
					System.out.println("POINT FOUND: " + p.getId() + " X: "
							+ p.getX());

					return p;
				}
			}
		}
		return null;
	}

	/**
	 * Draw all pieces on the board
	 * 
	 * @param canvas
	 */
	private void drawPieces(Canvas canvas) {
		// Draw player 1 pieces
		for (Piece p : GameController.getInstance().getMorrisGame().getPlayer1().getPieces()) {
			if (p.getPosition() >= 0) {
				Point position = getPointFromId(p.getPosition());
				if (position != null) {
					drawPieceImage(canvas, position,p.getResource());
				}
			}
		}
		// Draw player 2 pieces
		for (Piece p : GameController.getInstance().getMorrisGame().getPlayer2().getPieces()) {
			if (p.getPosition() >= 0) {
				Point position = getPointFromId(p.getPosition());
				if (position != null) {
					drawPieceImage(canvas, position,p.getResource());
				}
			}
		}

	}

	/**
	 * Return Point from ID
	 * 
	 * @param id
	 * @return
	 */
	private Point getPointFromId(int id) {
		for (Point p : pointList) {
			if (id == p.getId()) {
				return p;

			}
		}
		return null;
	}

	/**
	 * Draw white piece image on current point position
	 * 
	 * @param canvas
	 * @param point
	 */
	private void drawPieceImage(Canvas canvas, Point point,int resource) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
		Bitmap b = Bitmap.createScaledBitmap(bitmap, pieceSize, pieceSize,false);
		canvas.drawBitmap(b, point.getX() - (b.getWidth() / 2), point.getY()- (b.getHeight() / 2), null);
	}

	private void resetVar() {
		xRight = xRightOld;
		yBottom = yBottomOld;

	}

	private void drawBoard(Canvas canvas) {
		// board calc
		resetVar();
		boardW = xRight - xLeft + yTop;
		calcValue = boardW / 6;
		secondRect = calcValue;
		thirdRect = calcValue * 2;
		Paint p = getPaint();
		// Rect r = new Rect(xLeft, yTop, viewWidth-11, viewWidth-10);
		xRight = xRight - xLeft;
		yBottom = yBottom - yTop;
		midX = xLeft + ((xRight - xLeft) / 2);
		midY = yTop + ((yBottom - yTop) / 2);
		// first Rect
		drawRect(xLeft, yTop, xRight, yBottom, canvas, p);
		// second Rect
		drawRect(xLeft + secondRect, yTop + secondRect, xRight - secondRect,
				yBottom - secondRect, canvas, p);
		// third Rect
		drawRect(xLeft + thirdRect, yTop + thirdRect, xRight - thirdRect,
				yBottom - thirdRect, canvas, p);
		// first Line
		drawLine(xLeft, midX, xLeft + thirdRect, midX, canvas, p);
		// second Line
		drawLine(xRight - thirdRect, midY, xRight, midY, canvas, p);
		// third Line
		drawLine(midX, yTop, midX, yTop + thirdRect, canvas, p);
		// 4th line
		drawLine(midX, yBottom - thirdRect, midX, yBottom, canvas, p);
		p.setStyle(Style.FILL);
		drawPoints(canvas, p);

		/*
		 * for (Point point : pointList){ l.Out(point.toString()); if
		 * (point.getId() == 13){ p.setStyle(Style.STROKE);
		 * p.setColor(Color.GREEN); p.setStrokeWidth(3); point.highLight(canvas,
		 * p); }
		 * 
		 * }
		 */
		p.setStyle(Style.STROKE);
		p.setColor(Color.GREEN);
		p.setStrokeWidth(3);
		highlightPoints(canvas, p);
	}

	private void drawRect(float xLeft, float yTop, float xRight, float yBottom,
			Canvas canvas, Paint p) {
		canvas.drawRect(xLeft, yTop, xRight, yBottom, p);

	}

	private void drawCircle(float x, float y, Canvas canvas, Paint p) {
		canvas.drawCircle(x, y, 10, p);
	}

	private void drawLine(double startX, double startY, double stopX,
			double stopY, Canvas canvas, Paint p) {
		canvas.drawLine((int) startX, (int) startY, (int) stopX, (int) stopY, p);
	}

	private Paint getPaint() {
		Paint p = new Paint();
		p.setStyle(Style.STROKE);
		p.setColor(Color.BLACK);
		p.setStrokeWidth(6);
		return p;
	}

	private void drawPoints(Canvas canvas, Paint p) {
		midX = midX - xLeft;
		int teller = 0;
		for (float x = xLeft; x < xRight + xLeft; x = x + midX) {
			drawCircle(x, yTop, canvas, p);
			drawCircle(x, yBottom, canvas, p);
			if (makePointList) {
				pointList.add(new Point(teller, x, yTop));
				pointList.add(new Point(teller + 3, x, yBottom));
			}
			teller++;
		}
		midX = midX - secondRect;
		teller = 6;
		for (float x = xLeft + secondRect; x < xRight + xLeft; x = x + midX) {
			drawCircle(x, yTop + secondRect, canvas, p);
			drawCircle(x, yBottom - secondRect, canvas, p);
			if (makePointList) {
				pointList.add(new Point(teller, x, yTop + secondRect));
				pointList.add(new Point(teller + 3, x, yBottom - secondRect));
			}
			teller++;
		}
		midX = midX - secondRect;
		teller = 12;
		for (float x = xLeft + thirdRect; x <= xRight - thirdRect; x = x + midX) {
			drawCircle(x, yTop + thirdRect, canvas, p);
			drawCircle(x, yBottom - thirdRect, canvas, p);
			if (makePointList) {
				pointList.add(new Point(teller, x, yTop + thirdRect));
				pointList.add(new Point(teller + 3, x, yBottom - thirdRect));
			}
			teller++;
		}
		teller = 18;
		for (float x = xLeft; x <= xLeft + thirdRect; x = x + secondRect) {
			drawCircle(x, midY, canvas, p);
			drawCircle(xRight - thirdRect - xLeft + x, midY, canvas, p);
			if (makePointList) {
				pointList.add(new Point(teller, x, midY));
				pointList.add(new Point(teller + 3, xRight - thirdRect - xLeft
						+ x, midY));
			}
			teller++;
		}
		makePointList = false;
	}
}
