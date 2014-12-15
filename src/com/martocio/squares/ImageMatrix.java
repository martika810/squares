package com.martocio.squares;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.martocio.squares.BitmapUtils.ScalingLogic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageMatrix {

	private Map<Integer, Cell> matrixImages;
	private List<Integer> matrixState;
	private int matrixDimension;
	private List<Integer> viewsState;

	public ImageMatrix() {
		matrixImages = new HashMap<Integer, Cell>();
		matrixState = new ArrayList<Integer>();

	}

	public Map<Integer, Cell> getMatrixImages() {
		return matrixImages;
	}

	public List<Integer> getViewsState() {
		return viewsState;
	}

	public int getMatrixDimension() {
		return matrixDimension;
	}

	public void setViewsState(List<Integer> viewsState) {
		this.viewsState = viewsState;
	}

	public void populateViewState(List<Integer> views) {
		for (Integer it : views) {
			viewsState.add(it);
		}
	}

	public void populateMatrixImages(Resources resources, int matrixDimension,
			String imagePath) {
		this.matrixDimension = matrixDimension;
		Bitmap srcBitmap = extractBitmap(resources, imagePath);
		Bitmap unscaledBitmap = null;
		if (imagePath != null && !imagePath.isEmpty()) {
			unscaledBitmap = BitmapUtils.decodeFile(imagePath,
					srcBitmap.getWidth(), srcBitmap.getHeight(),
					ScalingLogic.CROP);
		} else {
			unscaledBitmap = BitmapUtils.decodeResource(resources,
					R.drawable.flower, srcBitmap.getWidth(),
					srcBitmap.getHeight(), ScalingLogic.CROP);
		}

		int num_matrix_cells = matrixDimension * matrixDimension;
		Bitmap squarePicture = makeImageSquare(unscaledBitmap);

		int tam_cell = squarePicture.getWidth() / matrixDimension;

		for (int itRow = 0; itRow < matrixDimension; itRow++) {

			for (int itCol = 0; itCol < matrixDimension; itCol++) {
				int x = itCol * tam_cell;
				int y = itRow * tam_cell;
				int width = tam_cell;// (itCol+1)*tam_cell;
				int height = tam_cell;// (itRow+1)*tam_cell;
				Cell cell = new Cell(getVectorPosition(itRow, itCol,
						matrixDimension), Bitmap.createBitmap(unscaledBitmap,
						x, y, width, height));
				matrixImages.put(cell.getId(), cell);

			}

		}
		matrixImages.put(matrixDimension * matrixDimension - 1, new Cell(
				matrixDimension * matrixDimension - 1, null));// empty square

	}

	private Bitmap extractBitmap(Resources resources, String path) {
		Bitmap srcBitmap = null;

		if (path != null && !path.isEmpty() && (new File(path).exists())) {
			File imgFile = new File(path);
			srcBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		} else {
			srcBitmap = BitmapFactory.decodeResource(resources,
					R.drawable.flower);
		}

		return srcBitmap;

	}

	public Bitmap getPictureInPosition(int pos) {
		if (!validIndex(pos)) {
			throw new RuntimeException(
					"ImageMatrix:getPictureInPosition:Index Out");
		}
		int numCell = matrixState.get(pos);
		Cell cell = matrixImages.get(new Integer(numCell));
		System.out.println("squares pos:" + pos + " cell:" + cell.getId());
		return cell.getPicture();

	}

	public boolean isMatrixResolved() {
		int index = 0;
		for (Integer cell : matrixState) {
			if (cell != null && !cell.equals(new Integer(index))) {
				return false;
			}
			index++;
		}

		return true;
	}

	public boolean validIndex(int index) {
		return index > -1 && index < size();
	}

	public void mockMatrixState() {

		matrixState.add(new Integer(3));// 0
		matrixState.add(new Integer(1));// 1
		matrixState.add(new Integer(14));// 2
		matrixState.add(new Integer(2));// 3
		matrixState.add(new Integer(13));// 4
		matrixState.add(new Integer(4));// 5
		matrixState.add(new Integer(12));// 6
		matrixState.add(new Integer(5));// 7
		matrixState.add(new Integer(11));// 9
		matrixState.add(new Integer(6));// 10
		matrixState.add(new Integer(10));// 11
		matrixState.add(new Integer(7));// 12
		matrixState.add(new Integer(9));// 13
		matrixState.add(new Integer(0));// 14
		matrixState.add(new Integer(8));// 15
		matrixState.add(new Integer(15));// 15

	}

	public void initMatrixState() {
		List<Integer> arrayAllCells = new ArrayList<Integer>();
		arrayAllCells.add(new Integer(3));// 0
		arrayAllCells.add(new Integer(1));// 1
		arrayAllCells.add(new Integer(14));// 2
		arrayAllCells.add(new Integer(2));// 3
		arrayAllCells.add(new Integer(13));// 4
		arrayAllCells.add(new Integer(4));// 5
		arrayAllCells.add(new Integer(12));// 6
		arrayAllCells.add(new Integer(5));// 7
		arrayAllCells.add(new Integer(11));// 8
		arrayAllCells.add(new Integer(6));// 9
		arrayAllCells.add(new Integer(10));// 10
		arrayAllCells.add(new Integer(7));// 11
		arrayAllCells.add(new Integer(9));// 12
		arrayAllCells.add(new Integer(0));// 13
		arrayAllCells.add(new Integer(8));// 14
		

		Random r = new Random();
		while (arrayAllCells.size() > 0) {
			int pickedCell = r.nextInt(arrayAllCells.size());
			matrixState.add(arrayAllCells.get(pickedCell));
			arrayAllCells.remove(pickedCell);
		}
		
		matrixState.add(new Integer(15));// 15

	}

	public static int getVectorPosition(int numRow, int numCol, int matrixDim) {
		return numRow * matrixDim + numCol;
	}

	private int getColFromVectorPosition(int vectorPosition) {

		return (vectorPosition % matrixDimension) + 1;

	}

	public int findCell(Cell cellToFind) {
		int position = Constants.EMPTY_CELL;
		for (int it = 0; it < size(); it++) {
			int idCellToFind = cellToFind.getId();
			int idCurrentCell = matrixState.get(it);
			if (idCurrentCell == idCellToFind
					&& matrixImages.get(idCurrentCell).equals(cellToFind)) {
				position = it;
			}
		}
		return position;
	}

	private int getRowFromVectorPosition(int vectorPosition) {

		return (vectorPosition / matrixDimension) + 1;

	}

	private Bitmap makeImageSquare(Bitmap fullPicture) {

		if (fullPicture.getWidth() > fullPicture.getHeight()) {
			fullPicture = BitmapUtils.createScaledBitmap(fullPicture,
					fullPicture.getHeight(), fullPicture.getHeight(),
					ScalingLogic.CROP);
		} else {
			fullPicture = BitmapUtils.createScaledBitmap(fullPicture,
					fullPicture.getWidth(), fullPicture.getWidth(),
					ScalingLogic.CROP);
		}

		return fullPicture;
	}

	private boolean isEmptyCell(int position) {

		if (position > -1 && position < size()) {
			int numCell = matrixState.get(position);
			Cell selectedCell = matrixImages.get(new Integer(numCell));
			if (selectedCell.getPicture() == null) {
				return true;
			}
		}
		return false;

	}

	public String getNextEmptyCell(int touchedCellPosition) {
		if (touchedCellPosition > -1 && touchedCellPosition < size()) {
			int aboveCellPos = touchedCellPosition - matrixDimension;
			int belowCellPos = touchedCellPosition + matrixDimension;
			int rightCellPos = touchedCellPosition + 1;
			int leftCellPos = touchedCellPosition - 1;
			if (isEmptyCell(aboveCellPos)) {
				return Constants.UP;
			}
			if (isEmptyCell(rightCellPos)) {
				return Constants.RIGHT;
			}
			if (isEmptyCell(belowCellPos)) {
				return Constants.DOWN;
			}
			if (isEmptyCell(leftCellPos)) {
				return Constants.LEFT;
			}
		}
		return Constants.NO_CELL;

	}

	public void updateStateAfterSwipe(int posSquareTouched, String direction) {
		int posTargetSquare = getTargetPosition(posSquareTouched, direction);

		if (posTargetSquare != Constants.EMPTY_CELL
				&& isEmptyCell(posTargetSquare)) {
			// swap the cell in matrixState
			int cellTouched = matrixState.get(posSquareTouched);
			int cellTarget = matrixState.get(posTargetSquare);
			matrixState.set(posTargetSquare, cellTouched);
			matrixState.set(posSquareTouched, cellTarget);
		}

	}

	public List<String> getEdgeCell(int position) {
		List<String> edges = new ArrayList<String>();
		if (position % matrixDimension == 0) {
			edges.add(Constants.LEFT);
		}
		if (position < matrixDimension) {
			edges.add(Constants.UP);
		}
		if (position >= size() - matrixDimension) {
			edges.add(Constants.DOWN);
		}
		if (position % matrixDimension == matrixDimension - 1) {
			edges.add(Constants.RIGHT);
		}
		return edges;

	}

	public int getTargetPosition(int positionTouched, String direction) {
		List<String> edges = getEdgeCell(positionTouched);
		if (direction.equals(Constants.UP) && !edges.contains(Constants.UP)) {
			return positionTouched - matrixDimension;
		} else if (direction.equals(Constants.RIGHT)
				&& !edges.contains(Constants.RIGHT)) {
			return positionTouched + 1;
		} else if (direction.equals(Constants.DOWN)
				&& !edges.contains(Constants.DOWN)) {
			return positionTouched + matrixDimension;
		} else if (direction.equals(Constants.LEFT)
				&& !edges.contains(Constants.LEFT)) {
			return positionTouched - 1;
		}
		return Constants.EMPTY_CELL;
	}

	public int size() {
		return matrixDimension * matrixDimension;
	}

}
