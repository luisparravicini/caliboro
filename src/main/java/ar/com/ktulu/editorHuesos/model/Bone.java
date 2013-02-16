package ar.com.ktulu.editorHuesos.model;

import java.util.ArrayList;
import java.util.List;

public class Bone {
	private String name;
	private List<BoneImage> images;

	public Bone(String name) {
		setName(name);
		images = new ArrayList<BoneImage>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return getName();
	}

	public void addImage(String path) {
		images.add(new BoneImage(path));
	}

	public BoneImage lastImage() {
		if (images.isEmpty())
			return null;
		else
			return images.get(images.size() - 1);
	}
}
