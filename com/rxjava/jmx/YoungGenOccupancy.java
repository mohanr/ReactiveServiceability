package com.rxjava.jmx;


public class YoungGenOccupancy {

	private long used;
	public long getUsed() {
		return used;
	}

	public long getMax() {
		return max;
	}

	private long max;

	public YoungGenOccupancy(long used, long max) {
		this.used = used;
		this.max = max;
	}

	public YoungGenOccupancy() {
		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Used [" + used + "]").
		   append("Max [" + max + "]");
		return sb.toString();
	}
}