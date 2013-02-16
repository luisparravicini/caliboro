package ar.com.ktulu.editorHuesos.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ar.com.ktulu.editorHuesos.BonesStore;

public class BoneImage {
	private String name;
	private File imagePath;
	private List<BonePoint> points;

	public BoneImage(String path) {
		this();
		imagePath = new File(path);
		name = imagePath.getName();
	}

	public BoneImage() {
		points = new ArrayList<BonePoint>();
	}

	public void setImagePath(String path) {
		this.imagePath = new File(path);
	}

	public String getName() {
		return name;
	}

	public File getImagePath() {
		return imagePath;
	}

	public List<BonePoint> getPoints() {
		return points;
	}

	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return imagePath.getAbsolutePath();
	}

	public void addPoint(BonePoint point) {
		points.add(point);
		
		BonesStore.getInstance().dirty();
	}

	public BonePoint addPoint(int x, int y) {
		BonePoint point = new BonePoint(x, y);
		addPoint(point);
		
		return point;
	}
}
