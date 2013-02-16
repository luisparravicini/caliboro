package ar.com.ktulu.editorHuesos.model;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoneImage {
	private String name;
	private File imagePath;
	private List<BonePoint> points;

	public BoneImage(String path) {
		imagePath = new File(path);
		points = new ArrayList<BonePoint>();
		name = imagePath.getName();
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
}

class BonePoint {
	public String name;
	public Point pos;
}