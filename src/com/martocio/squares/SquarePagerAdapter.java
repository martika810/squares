package com.martocio.squares;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
	

}
