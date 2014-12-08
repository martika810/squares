package com.martocio.squares;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	 private Context mContext;
	 private ImageMatrix imageMatrix;
	 
	 
	 public ImageAdapter(Context c,SquarePageFragment caller) {
	        mContext = c;
	        imageMatrix=caller.getMatrix();
	    }

	    public int getCount() {
	        return imageMatrix.getMatrixDimension()*imageMatrix.getMatrixDimension();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }
	    
	 // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, parent.getWidth()/4));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(0, 0, 0, 0);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        if(imageMatrix.getPictureInPosition(position)!=null){
	        	imageView.setImageBitmap(imageMatrix.getPictureInPosition(position));
	        }
	        return imageView;
	    }

	
		
	

}
