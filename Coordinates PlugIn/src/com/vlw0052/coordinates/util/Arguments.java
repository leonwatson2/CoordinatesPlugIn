package com.vlw0052.coordinates.util;
/*
 * Arguments enum holds values for the index of an argument in
 * the arguments variable passed to the onCommand function in CoordinatesCommand.java
 */


public enum Arguments {
	NAME(1), //Name of location
	PLAYERNAME(1),
	PAGENUMBER(2), 
	X(2),
	HERE(2),
	Y(3),
	Z(4);
	
	private final int index;
	private Arguments(int ind) {
		this.index = ind;
	}
	
	public int val(){
		return this.index;
	}
}
