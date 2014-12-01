package com.martocio.squares;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SquarePageFragment extends Fragment implements OnTouchListener, AnimationListener{
	private ImageView squareImage1;
	private ImageView squareImage2;
	private ImageView squareImage3;
	private ImageView squareImage4;
	private ImageView squareImage5;
	private ImageView squareImage6;
	private ImageView squareImage7;
	private ImageView squareImage8;
	private ImageView squareImage9;
	private ImageView squareImage10;
	private ImageView squareImage11;
	private ImageView squareImage12;
	private ImageView squareImage13;
	private ImageView squareImage14;
	private ImageView squareImage15;
	private ImageView squareImage16;
	private Animation animLeftToRight;
	private Animation animRightToLeft;
	private Animation animDownToUp;
	private Animation animUpToDown;
	private ImageMatrix matrix;
	
	private int lastSquareTouched;
	private int lastViewTouched;
	private String lastMove;
	
	public SquarePageFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState ){
		ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.matrix_fragment, container,false);
		return rootView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState){
		
		super.onActivityCreated(savedInstanceState);
		initAnimations();
		populateMatrix();	
	}
	
	public ImageMatrix getMatrix() {
		return matrix;
	}
	
	private void populateMatrix(){
		matrix=new ImageMatrix();
		matrix.populateMatrixImages(getResources(),4, R.drawable.monkey);
		matrix.mockMatrixState();
		
		matrix.setViewsState(buildViewIdsArray());
		
		squareImage1=(ImageView)getView().findViewById(R.id.square1);
		squareImage2=(ImageView)getView().findViewById(R.id.square2);
		squareImage3=(ImageView)getView().findViewById(R.id.square3);
		squareImage4=(ImageView)getView().findViewById(R.id.square4);
		squareImage5=(ImageView)getView().findViewById(R.id.square5);
		squareImage6=(ImageView)getView().findViewById(R.id.square6);
		squareImage7=(ImageView)getView().findViewById(R.id.square7);
		squareImage8=(ImageView)getView().findViewById(R.id.square8);
		squareImage9=(ImageView)getView().findViewById(R.id.square9);
		squareImage10=(ImageView)getView().findViewById(R.id.square10);
		squareImage11=(ImageView)getView().findViewById(R.id.square11);
		squareImage12=(ImageView)getView().findViewById(R.id.square12);
		squareImage13=(ImageView)getView().findViewById(R.id.square13);
		squareImage14=(ImageView)getView().findViewById(R.id.square14);
		squareImage15=(ImageView)getView().findViewById(R.id.square15);
		squareImage16=(ImageView)getView().findViewById(R.id.square16);
		
		squareImage1.setImageBitmap(matrix.getPictureInPosition(0));
		squareImage2.setImageBitmap(matrix.getPictureInPosition(1));
		squareImage3.setImageBitmap(matrix.getPictureInPosition(2));
		squareImage4.setImageBitmap(matrix.getPictureInPosition(3));
		squareImage5.setImageBitmap(matrix.getPictureInPosition(4));
		squareImage6.setImageBitmap(matrix.getPictureInPosition(5));
		squareImage7.setImageBitmap(matrix.getPictureInPosition(6));
		squareImage8.setImageBitmap(matrix.getPictureInPosition(7));
		squareImage9.setImageBitmap(matrix.getPictureInPosition(8));
		squareImage10.setImageBitmap(matrix.getPictureInPosition(9));
		squareImage11.setImageBitmap(matrix.getPictureInPosition(10));
		squareImage12.setImageBitmap(matrix.getPictureInPosition(11));
		squareImage13.setImageBitmap(matrix.getPictureInPosition(12));
		squareImage14.setImageBitmap(matrix.getPictureInPosition(13));
		squareImage15.setImageBitmap(matrix.getPictureInPosition(14));
		
		addCellListeners();
	
		
	}
	
	private void swipeSquare(){
		
	}

	private void addCellListeners() {
		
		squareImage1.setOnTouchListener(this);
		squareImage2.setOnTouchListener(this);
		squareImage3.setOnTouchListener(this);
		squareImage4.setOnTouchListener(this);
		squareImage5.setOnTouchListener(this);
		squareImage6.setOnTouchListener(this);
		squareImage7.setOnTouchListener(this);
		squareImage8.setOnTouchListener(this);
		squareImage9.setOnTouchListener(this);
		squareImage10.setOnTouchListener(this);
		squareImage11.setOnTouchListener(this);
		squareImage12.setOnTouchListener(this);
		squareImage13.setOnTouchListener(this);
		squareImage14.setOnTouchListener(this);
		squareImage15.setOnTouchListener(this);
		
		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int positionSquareTouched=matrix.getViewsState().indexOf(v.getId());
		lastSquareTouched=positionSquareTouched;
		lastViewTouched=v.getId();
		String nextEmptyCell=matrix.getNextEmptyCell(positionSquareTouched);
		if(nextEmptyCell.equals(Constants.UP)){
			((ImageView)v).clearAnimation();
			((ImageView)v).setAnimation(animDownToUp);
			((ImageView)v).startAnimation(animDownToUp);
			lastMove=Constants.UP;
			
		}
		else if(nextEmptyCell.equals(Constants.RIGHT)){
			((ImageView)v).clearAnimation();
			((ImageView)v).setAnimation(animLeftToRight);
			((ImageView)v).startAnimation(animLeftToRight);
			lastMove=Constants.RIGHT;
		}
		else if(nextEmptyCell.equals(Constants.DOWN)){
			((ImageView)v).clearAnimation();
			((ImageView)v).setAnimation(animUpToDown);
			((ImageView)v).startAnimation(animUpToDown);
			lastMove=Constants.DOWN;
		}
		else if(nextEmptyCell.equals(Constants.LEFT)){
			((ImageView)v).clearAnimation();
			((ImageView)v).setAnimation(animRightToLeft);
			((ImageView)v).startAnimation(animRightToLeft);
			lastMove=Constants.LEFT;
		}
		else{
			//nothing to do
		}
		
		return true;
	}
	
	private List<Integer> buildViewIdsArray(){
		List<Integer> viewIds=new ArrayList<Integer>();
		viewIds.add(R.id.square1);
		viewIds.add(R.id.square2);
		viewIds.add(R.id.square3);
		viewIds.add(R.id.square4);
		viewIds.add(R.id.square5);
		viewIds.add(R.id.square6);
		viewIds.add(R.id.square7);
		viewIds.add(R.id.square8);
		viewIds.add(R.id.square9);
		viewIds.add(R.id.square10);
		viewIds.add(R.id.square11);
		viewIds.add(R.id.square12);
		viewIds.add(R.id.square13);
		viewIds.add(R.id.square14);
		viewIds.add(R.id.square15);
		viewIds.add(R.id.square16);
		return viewIds;
		
		
	}
	
	private void initAnimations() {
		
		animLeftToRight=AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.left_right);
		animLeftToRight.setAnimationListener(this);
		animRightToLeft=AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.right_left);
		animRightToLeft.setAnimationListener(this);
		animUpToDown=AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.up_down);
		animUpToDown.setAnimationListener(this);
		animDownToUp=AnimationUtils.loadAnimation(getActivity().getBaseContext(), R.anim.down_up);
		animDownToUp.setAnimationListener(this);
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Update the matrixState and maybe populate again
		
		Bitmap squareToMove=matrix.getPictureInPosition(lastSquareTouched);
		int idViewTouched=matrix.getViewsState().get(lastSquareTouched);
		int idTargetAnimationView=Constants.EMPTY_CELL;
		((ImageView) getView().findViewById(idViewTouched)).setImageBitmap(null);
		
		if(lastMove.equals(Constants.UP)){
			idTargetAnimationView=matrix.getViewsState().get(lastSquareTouched-matrix.getMatrixDimension());
			((ImageView) getView().findViewById(idTargetAnimationView)).setImageBitmap(squareToMove);
		}
		else if(lastMove.equals(Constants.RIGHT)){
			idTargetAnimationView=matrix.getViewsState().get(lastSquareTouched+1);
			
		}
		else if(lastMove.equals(Constants.DOWN)){
			idTargetAnimationView=matrix.getViewsState().get(lastSquareTouched+matrix.getMatrixDimension());
			
		}
		else if(lastMove.equals(Constants.LEFT)){
			idTargetAnimationView=matrix.getViewsState().get(lastSquareTouched+1);
		}
		else{}
		if(idTargetAnimationView!=Constants.EMPTY_CELL){
			((ImageView) getView().findViewById(idTargetAnimationView)).setImageBitmap(squareToMove);
			matrix.updateStateAfterSwipe(lastSquareTouched, lastMove);
		}
		
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
