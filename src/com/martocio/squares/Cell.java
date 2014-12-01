package com.martocio.squares;

import android.graphics.Bitmap;

public class Cell {
	private int id;
	private Bitmap picture;
	
	public Cell(int id,Bitmap picture){
		this.id=id;
		this.picture=picture;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Bitmap getPicture() {
		return picture;
	}
	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
	

}
