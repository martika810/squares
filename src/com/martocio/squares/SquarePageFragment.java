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
import android.webkit.WebView.FindListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class SquarePageFragment extends Fragment implements OnTouchListener,
		AnimationListener {
	private GridView imageGrid;
	private Animation animLeftToRight;
	private Animation animRightToLeft;
	private Animation animDownToUp;
	private Animation animUpToDown;
	private ImageMatrix matrix;

	private int lastSquareTouched;
	private int lastViewTouched;
	private String lastMove;
	private ImageAdapter gridAdapter;

	public SquarePageFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.matrix_fragment, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		initAnimations();
		populateMatrix();
	}

	public ImageMatrix getMatrix() {
		return matrix;
	}

	private void populateMatrix() {

		imageGrid = (GridView) getView().findViewById(R.id.imageGrid);

		matrix = new ImageMatrix();
		//Grab the file from activity
		String currentImageSelected=((MainActivity)getActivity()).getCurrentSelectedImage();
		matrix.populateMatrixImages(getResources(), 4, currentImageSelected);
		matrix.mockMatrixState();
		gridAdapter = new ImageAdapter(this.getActivity(), this);
		imageGrid.setAdapter(gridAdapter);
		imageGrid.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// int positionSquareTouched=matrix.getViewsState().indexOf(v.getId());
		int colTouched = (int) (event.getX() / (v.getWidth() / matrix
				.getMatrixDimension()));
		int rowTouched = (int) (event.getY() / (v.getHeight() / matrix
				.getMatrixDimension()));
		lastSquareTouched = ImageMatrix.getVectorPosition(rowTouched,
				colTouched, matrix.getMatrixDimension());
	

		String nextEmptyCell = matrix.getNextEmptyCell(lastSquareTouched);
		if (nextEmptyCell.equals(Constants.NO_CELL) || !matrix.validIndex(lastSquareTouched)) {
			// Toast.makeText(getActivity(),
			// "No Empty space around!! Try again!", Toast.LENGTH_SHORT).show();
			lastMove = Constants.NO_CELL;
			return false;
		}
		View selectedView = ((ViewGroup) v).getChildAt(lastSquareTouched);
		if (nextEmptyCell.equals(Constants.UP)) {
			selectedView.clearAnimation();
			selectedView.setAnimation(animDownToUp);
			selectedView.startAnimation(animDownToUp);
			lastMove = Constants.UP;

		} else if (nextEmptyCell.equals(Constants.RIGHT)) {
			selectedView.clearAnimation();
			selectedView.setAnimation(animLeftToRight);
			selectedView.startAnimation(animLeftToRight);
			lastMove = Constants.RIGHT;
		} else if (nextEmptyCell.equals(Constants.DOWN)) {
			selectedView.clearAnimation();
			selectedView.setAnimation(animUpToDown);
			selectedView.startAnimation(animUpToDown);
			lastMove = Constants.DOWN;
		} else if (nextEmptyCell.equals(Constants.LEFT)) {
			selectedView.clearAnimation();
			selectedView.setAnimation(animRightToLeft);
			selectedView.startAnimation(animRightToLeft);
			lastMove = Constants.LEFT;
		} else {
			lastMove = Constants.NO_CELL;
			return false;
		}

		return true;
	}

	private void initAnimations() {

		animLeftToRight = AnimationUtils.loadAnimation(getActivity()
				.getBaseContext(), R.anim.left_right);
		animLeftToRight.setAnimationListener(this);
		animRightToLeft = AnimationUtils.loadAnimation(getActivity()
				.getBaseContext(), R.anim.right_left);
		animRightToLeft.setAnimationListener(this);
		animUpToDown = AnimationUtils.loadAnimation(getActivity()
				.getBaseContext(), R.anim.up_down);
		animUpToDown.setAnimationListener(this);
		animDownToUp = AnimationUtils.loadAnimation(getActivity()
				.getBaseContext(), R.anim.down_up);
		animDownToUp.setAnimationListener(this);

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// Update the matrixState and maybe populate again

		Bitmap squareToMove = matrix.getPictureInPosition(lastSquareTouched);
		
		// int idViewTouched=matrix.getViewsState().get(lastSquareTouched);

		int posTargetView = matrix.getTargetPosition(lastSquareTouched, lastMove);
		
		
		boolean notMatrixAlreadyUpdated=false;
		if(posTargetView != Constants.EMPTY_CELL){
			notMatrixAlreadyUpdated=matrix.getPictureInPosition(posTargetView)==null;
		}
		if (posTargetView != Constants.EMPTY_CELL
				&& !lastMove.equals(Constants.NO_CELL)
				&& posTargetView < matrix.size()  && notMatrixAlreadyUpdated) {
			
			Bitmap squareEmptyToMove = matrix.getPictureInPosition(posTargetView);
			((ImageView) imageGrid.getChildAt(lastSquareTouched))
					.setImageBitmap(squareEmptyToMove);
			((ImageView) imageGrid.getChildAt(posTargetView))
					.setImageBitmap(squareToMove);
			matrix.updateStateAfterSwipe(lastSquareTouched, lastMove);
			
		}
		if (posTargetView!=Constants.EMPTY_CELL) {
			((ImageView) imageGrid.getChildAt(posTargetView)).clearAnimation();
		}
		if(lastSquareTouched!=Constants.EMPTY_CELL){
			((ImageView) imageGrid.getChildAt(lastSquareTouched)).clearAnimation();
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}
