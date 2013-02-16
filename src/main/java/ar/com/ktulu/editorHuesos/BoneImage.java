package ar.com.ktulu.editorHuesos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoneImage {
	private File imagePath;
	private List<BonePoint> points;

	public BoneImage(String path) {
		imagePath = new File(path);
		points = new ArrayList<BonePoint>();
	}

	public String toString() {
		return imagePath.getName();
	}

	public String getPath() {
		return imagePath.getAbsolutePath();
	}
}

class BonePoint {
	public String name;
	public int x;
	public int y;
}