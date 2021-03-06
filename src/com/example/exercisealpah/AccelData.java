package com.example.exercisealpah;

import java.io.Serializable;

public class AccelData implements Serializable{
	private long timestamp;
	private long syncStamp;
	private double x;
	private double y;
	private double z;



	public AccelData(long timestamp, double x, double y, double z) {
		this.timestamp = timestamp;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getSyncStamp(){
		return syncStamp;
	}
	public void setSyncStamp(long syncStamp){
		this.syncStamp = syncStamp;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public String toString()
	{
		return "t="+timestamp+", x="+x+", y="+y+", z="+z;
	}


}
