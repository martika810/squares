package com.martocio.squares;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

public class SquarePagerAdapter extends FragmentStatePagerAdapter{
	
	public SquarePagerAdapter(FragmentManager fm){
		super(fm);
	}
	@Override
	public Fragment getItem(int position){
		Fragment fragmentToReturn=null;
		switch(position){
		case 0:
			fragmentToReturn=new SquareIniPageFragment();
			break;
		
		case 1:
			fragmentToReturn=new SquarePageFragment();
			break;
		}
		
		return fragmentToReturn;
	}
	
	@Override
	public int getCount(){
		return 2;
	}
	
	@Override
	public int getItemPosition(Object object) {
	
		return POSITION_NONE;// this refresh the fragments when notifySetDataChange
	}
	
	
	

}
