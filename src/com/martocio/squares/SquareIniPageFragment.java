package com.martocio.squares;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class SquareIniPageFragment extends Fragment{
	
	private String selectedImagePath;
    private String fileManager;
	
	public SquareIniPageFragment() {

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.ini_fragment, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		addListener();
	
	}
	private void addListener() {
		
		Button startBtn=(Button) getView().findViewById(R.id.btn_start);
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).getmPager().setCurrentItem(Constants.INDEX_PAGE_GRID);
				
			}
		});
		
		Button pickImgBtn=(Button)getView().findViewById(R.id.btn_pick_image);
		pickImgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			   //Select image from gallery
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
				
			}
		});
		
	}
	
	public void onActivityResult(int requestCode,int resultCode, Intent data){
		 if (requestCode == Constants.PICK_IMAGE
		            && resultCode == Activity.RESULT_OK) {
			 
			 Uri selectedImageUri = data.getData();
			 selectedImagePath=getPath(selectedImageUri);
			 ((MainActivity)getActivity()).setCurrentSelectedImage(selectedImagePath);
			 
			 
		 }
		
		
	}
	
	
	 public String getSelectedImagePath() {
		return selectedImagePath;
	}
	public String getPath(Uri uri) {
         // just some safety built in 
         if( uri == null ) {
             // TODO perform some logging or show user feedback
             return null;
         }
         // try to retrieve the image from the media store first
         // this will only work for images selected from gallery
         String[] projection = { MediaStore.Images.Media.DATA };
         Cursor cursor =getActivity().getContentResolver().query(uri, projection, null, null, null);
         if( cursor != null ){
             int column_index = cursor
             .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
             cursor.moveToFirst();
             return cursor.getString(column_index);
         }
         // this is our fallback here
         return uri.getPath();
 }

}
