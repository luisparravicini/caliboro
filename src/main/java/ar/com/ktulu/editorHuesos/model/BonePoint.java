package ar.com.ktulu.editorHuesos.model;


public class BonePoint {
	public String name;
	public int x;
	public int y;

	public BonePoint() {
		this(0, 0);
	}

	public BonePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}