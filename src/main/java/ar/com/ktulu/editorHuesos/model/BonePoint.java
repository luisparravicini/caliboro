package ar.com.ktulu.editorHuesos.model;


public class BonePoint {
	public String name;
	public int x;
	public int y;

	public BonePoint(String name, int x, int y) {
		this(x, y);
		this.name = name;
	}

	public BonePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public BonePoint() {
		this(0, 0);
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