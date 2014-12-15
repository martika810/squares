package com.martocio.squares;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SquarePageFragment extends Fragment implements OnTouchListener,
		AnimationListener {
	private GridView imageGrid;
	private Animation animLeftToRight;
	private Animation animRightToLeft;
	private Animation animDownToUp;
	private Animation animUpToDown;
	private ImageMatrix matrix;
	private int secondPlaying = 0;

	private int lastSquareTouched;
	private int lastViewTouched;
	private String lastMove;
	private ImageAdapter gridAdapter;
	private static final int MILLI_IN_SECOND = 1000;
	private final Handler timerHandler = new Handler();
	private Timer timer = null;
	private boolean isTimerRunning = false;

	// Hold a reference to the CUrrent Animator
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;

	final Runnable timerRunnable = new Runnable() {
		public void run() {
			updateTimerTextView(secondPlaying);

		}
	};

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
		addButtonListeners();

		populateMatrix();
	}

	public ImageMatrix getMatrix() {
		return matrix;
	}

	private void addButtonListeners() {
		addSeeSolutionListener();
		addPlayListener();
		addPauseListener();

	}

	private void initTimerHandling() throws InterruptedException {
		if (!isTimerRunning) {
			timer = new Timer();

			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					updateTimerGUI();

				}
			}, 0, 1000);
			isTimerRunning = true;
		}

	}

	private void updateTimerGUI() {
		secondPlaying++;

		timerHandler.post(timerRunnable);
	}

	private void addPlayListener() {
		Button btnPlay = (Button) getView().findViewById(R.id.play_btn);
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					
					initTimerHandling();
					swapPauseStartBtn();
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		});

	}

	private void addPauseListener() {
		Button btnPause = (Button) getView().findViewById(R.id.pause_btn);
		btnPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				
				timer.cancel();
				isTimerRunning = false;
				swapPauseStartBtn();
			}
		});

	}

	private void addSeeSolutionListener() {
		final View btnSeeSolution = getView().findViewById(
				R.id.btn_see_solution);
		btnSeeSolution.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				zoomImageFromThumb(btnSeeSolution);
				// hidding central panel in zoom function
			}
		});
		mShortAnimationDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

	}

	private void populateMatrix() {

		imageGrid = (GridView) getView().findViewById(R.id.imageGrid);

		matrix = new ImageMatrix();
		// Grab the file from activity
		String currentImageSelected = ((MainActivity) getActivity())
				.getCurrentSelectedImage();
		matrix.populateMatrixImages(getResources(), 4, currentImageSelected);

		// matrix.mockMatrixState();
		matrix.initMatrixState();
		gridAdapter = new ImageAdapter(this.getActivity(), this);
		imageGrid.setAdapter(gridAdapter);
		imageGrid.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int colTouched = (int) (event.getX() / (v.getWidth() / matrix
				.getMatrixDimension()));
		int rowTouched = (int) (event.getY() / (v.getHeight() / matrix
				.getMatrixDimension()));
		lastSquareTouched = ImageMatrix.getVectorPosition(rowTouched,
				colTouched, matrix.getMatrixDimension());

		String nextEmptyCell = matrix.getNextEmptyCell(lastSquareTouched);
		if (nextEmptyCell.equals(Constants.NO_CELL)
				|| !matrix.validIndex(lastSquareTouched)) {
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

		if (matrix.isMatrixResolved()) {
			Toast.makeText(getActivity(), "Congratulations!!!",
					Toast.LENGTH_LONG).show();
			if (isTimerRunning)
				timer.cancel();
		} else {
			try {
				initTimerHandling();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			swapPauseStartBtn();
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

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// Update the matrixState and maybe populate again

		Bitmap squareToMove = matrix.getPictureInPosition(lastSquareTouched);

		// int idViewTouched=matrix.getViewsState().get(lastSquareTouched);

		int posTargetView = matrix.getTargetPosition(lastSquareTouched,
				lastMove);

		boolean notMatrixAlreadyUpdated = false;
		if (posTargetView != Constants.EMPTY_CELL) {
			notMatrixAlreadyUpdated = matrix
					.getPictureInPosition(posTargetView) == null;
		}
		if (posTargetView != Constants.EMPTY_CELL
				&& !lastMove.equals(Constants.NO_CELL)
				&& posTargetView < matrix.size() && notMatrixAlreadyUpdated) {

			Bitmap squareEmptyToMove = matrix
					.getPictureInPosition(posTargetView);
			((ImageView) imageGrid.getChildAt(lastSquareTouched))
					.setImageBitmap(squareEmptyToMove);
			((ImageView) imageGrid.getChildAt(posTargetView))
					.setImageBitmap(squareToMove);
			matrix.updateStateAfterSwipe(lastSquareTouched, lastMove);

		}
		if (posTargetView != Constants.EMPTY_CELL) {
			((ImageView) imageGrid.getChildAt(posTargetView)).clearAnimation();
		}
		if (lastSquareTouched != Constants.EMPTY_CELL) {
			((ImageView) imageGrid.getChildAt(lastSquareTouched))
					.clearAnimation();
		}

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	private void updateTimerTextView(long seconds) {

		TextView timerTxt = (TextView) getView().findViewById(R.id.timer_txt);
		String timeFormat = "mm:ss";
		timerTxt.setText(DateFormat.format(timeFormat, seconds
				* MILLI_IN_SECOND));

	}
	
	private void swapPauseStartBtn(){
		Button playBtn=(Button)getView().findViewById(R.id.play_btn);
		Button pauseBtn=(Button)getView().findViewById(R.id.pause_btn);
		if(!isTimerRunning){
			playBtn.setVisibility(View.VISIBLE);
			pauseBtn.setVisibility(View.GONE);
		}else{
			playBtn.setVisibility(View.GONE);
			pauseBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {

		super.setUserVisibleHint(isVisibleToUser);
		if (!isVisibleToUser && timer != null) {

			timer.cancel();
			isTimerRunning = false;
			swapPauseStartBtn();
		}
	}

	private void zoomImageFromThumb(final View thumbView) {
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		final ImageView expandedImageView = (ImageView) getView().findViewById(
				R.id.expanded_image);
		// Set the expanded view to the currentImage
		Bitmap srcBitmap = null;
		String selectedImagePath = ((MainActivity) getActivity())
				.getCurrentSelectedImage();

		if (selectedImagePath != null && !selectedImagePath.isEmpty()
				&& (new File(selectedImagePath).exists())) {
			File imgFile = new File(selectedImagePath);
			srcBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			expandedImageView.setImageBitmap(srcBitmap);
		} else {

			expandedImageView.setImageResource(R.drawable.flower);
		}

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		getView().findViewById(R.id.top_btn_container).getGlobalVisibleRect(
				finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		thumbView.setAlpha(0f);

		expandedImageView.setVisibility(View.VISIBLE);
		getView().findViewById(R.id.centralpanel).setVisibility(View.GONE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set.play(
				ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						startScale, 1f))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
						startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down to the
		// original bounds
		// and show the thumbnail instead of the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their
				// original values.
				AnimatorSet set = new AnimatorSet();
				set.play(
						ObjectAnimator.ofFloat(expandedImageView, View.X,
								startBounds.left))
						.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
								startBounds.top))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_X, startScaleFinal))
						.with(ObjectAnimator.ofFloat(expandedImageView,
								View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						getView().findViewById(R.id.centralpanel)
								.setVisibility(View.VISIBLE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}

}
