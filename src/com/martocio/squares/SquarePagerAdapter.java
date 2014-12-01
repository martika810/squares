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
		return new SquarePageFragment();
	}
	
	@Override
	public int getCount(){
		return 2;
	}
	

}
