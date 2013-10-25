package ar.com.ktulu.caliboro.model;

public class BoneScale {
	private int ax;
	private int ay;
	private int bx;
	private int by;
	private int distance;

	/**
	 * 
	 * @return La distancia en mm entre los dos puntos
	 */
	public int getDistance() {
		return distance;
	}

	public void setDistance(int d) {
		distance = d;
	}

	public int getAx() {
		return ax;
	}

	public void setAx(int ax) {
		this.ax = ax;
	}

	public int getAy() {
		return ay;
	}

	public void setAy(int ay) {
		this.ay = ay;
	}

	public int getBx() {
		return bx;
	}

	public void setBx(int bx) {
		this.bx = bx;
	}

	public int getBy() {
		return by;
	}

	public void setBy(int by) {
		this.by = by;
	}

}
