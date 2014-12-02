package com.martocio.squares;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.martocio.squares.BitmapUtils.ScalingLogic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageMatrix {
	
	private Map<Integer,Cell> matrixImages;
	private List<Integer> matrixState;
	private int matrixDimension;
	private List<Integer> viewsState;
	
	public ImageMatrix(){
		matrixImages=new HashMap<Integer,Cell>();
		matrixState=new ArrayList<Integer>();
		
		
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
		this.viewsState=viewsState;
	}

	public void populateViewState(List<Integer> views){
		for(Integer it:views){
			viewsState.add(it);
		}
	}
	public void populateMatrixImages(Resources resources, int matrixDimension,
			int fullPictureId) {
		 this.matrixDimension=matrixDimension;
		 Bitmap srcBitmap=BitmapFactory.decodeResource(resources,fullPictureId);
		 Bitmap unscaledBitmap = BitmapUtils.decodeResource(resources, fullPictureId,
				 srcBitmap.getWidth(),srcBitmap.getHeight(), ScalingLogic.CROP);
		 
		int num_matrix_cells=matrixDimension*matrixDimension;
		Bitmap squarePicture=makeImageSquare(unscaledBitmap);
		
	
		int tam_cell=squarePicture.getWidth()/matrixDimension;
		
		for(int itRow=0;itRow<matrixDimension;itRow++){
			
			for(int itCol=0;itCol<matrixDimension;itCol++){
				int x=itCol*tam_cell;
			    int y=itRow*tam_cell;
			    int width=tam_cell;//(itCol+1)*tam_cell;
			    int height=tam_cell;//(itRow+1)*tam_cell;
				Cell cell=new Cell(getVectorPosition(itRow, itCol,matrixDimension),Bitmap.createBitmap(unscaledBitmap, x, y, width, height));
				matrixImages.put(cell.getId(),cell );
				
			}
			
		}
		
	}
	
	
	
	public Bitmap getPictureInPosition(int pos){
		int numCell=matrixState.get(pos);
		Cell cell=matrixImages.get(new Integer(numCell));
		return cell.getPicture();
	}
	public void mockMatrixState(){
		
		matrixState.add(new Integer(3));
		matrixState.add(new Integer(15));
		matrixState.add(new Integer(1));
		matrixState.add(new Integer(14));
		matrixState.add(new Integer(2));
		matrixState.add(new Integer(13));
		matrixState.add(new Integer(4));
		matrixState.add(new Integer(12));
		matrixState.add(new Integer(5));
		matrixState.add(new Integer(11));
		matrixState.add(new Integer(6));
		matrixState.add(new Integer(10));
		matrixState.add(new Integer(7));
		matrixState.add(new Integer(9));
		matrixState.add(new Integer(3));
		matrixState.add(new Integer(Constants.EMPTY_CELL));
		
	}
	
	public static int getVectorPosition(int numRow,int numCol,int matrixDim){
		return numRow*matrixDim+numCol;
	}
	
	private int getColFromVectorPosition(int vectorPosition){
		
		return (vectorPosition % matrixDimension)+1;
		
	}
	
	private int getRowFromVectorPosition(int vectorPosition){
		
		return (vectorPosition / matrixDimension)+1;
		
	}
	
	private Bitmap makeImageSquare(Bitmap fullPicture){
		
		if(fullPicture.getWidth()>fullPicture.getHeight()){
			fullPicture= BitmapUtils.createScaledBitmap(fullPicture,fullPicture.getHeight(), fullPicture.getHeight(),ScalingLogic.CROP);
		}else{
			fullPicture= BitmapUtils.createScaledBitmap(fullPicture, fullPicture.getWidth(), fullPicture.getWidth(),ScalingLogic.CROP);
		}
		
		return fullPicture;
	}
	
	private boolean isEmptyCell(int position){
		int numCell=matrixState.get(position);
		if(numCell==Constants.EMPTY_CELL){
			return true;
		}
		return false;
		
	}
	
	public String getNextEmptyCell(int touchedCellPosition){
		int aboveCellPos=touchedCellPosition-matrixDimension;
		int belowCellPos=touchedCellPosition+matrixDimension;
		int rightCellPos=touchedCellPosition+1;
		int leftCellPos=touchedCellPosition-1;
		if(isEmptyCell(aboveCellPos)){
			return Constants.UP;
		}
		if(isEmptyCell(rightCellPos)){
			return Constants.RIGHT;
		}
		if(isEmptyCell(belowCellPos)){
			return Constants.DOWN;
		}
		if(isEmptyCell(leftCellPos)){
			return Constants.LEFT;
		}
		return Constants.NO_CELL;
				
	}
	public void updateStateAfterSwipe(int posSquareTouched,String direction){
		int posTargetSquare=getTargetPosition(posSquareTouched, direction);
		//swap the cell in matrixState
		int cellTouched=matrixState.get(posSquareTouched);
		int cellTarget=matrixState.get(posTargetSquare);
		matrixState.add(posTargetSquare, cellTouched);
		matrixState.add(posSquareTouched,cellTarget);
		//swap viewState array
		int viewTouched=viewsState.get(posSquareTouched);
		int viewTarget=viewsState.get(posTargetSquare);
		viewsState.add(posSquareTouched,viewTarget );
		viewsState.add(posTargetSquare,viewTouched);
		
	}

	public int getTargetPosition(int positionTouched,String direction){
		if(direction.equals(Constants.UP)){
			return positionTouched-matrixDimension;
		}
		else if(direction.equals(Constants.RIGHT)){
			return positionTouched+1;
		}
		else if(direction.equals(Constants.DOWN)){
			return positionTouched+matrixDimension;
		}
		else if(direction.equals(Constants.LEFT)){
			return positionTouched-1;
		}
		return Constants.EMPTY_CELL;
	}
	
	

}
