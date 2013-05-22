package ar.com.ktulu.editorHuesos.model;

import java.util.ArrayList;
import java.util.List;

public class StoreRootNode {

	private List<Bone> bones;
	private int lastImageId;

	public StoreRootNode() {
		bones = new ArrayList<Bone>();
	}

	public List<Bone> getBones() {
		return bones;
	}

	public int getLastImageId() {
		return lastImageId;
	}

	public void addBone(Bone bone) {
		bones.add(bone);
	}

	public void removeBone(Bone bone) {
		bones.remove(bone);
	}

	public int nextImage() {
		return lastImageId++;
	}

	public void setLastImageId(int id) {
		lastImageId = id;
	}

	public void clearBones() {
		bones.clear();
	}

}
//
//
// jsonData.put("lastImageId",lastImageId);
//
// JSONArray bones = new JSONArray();
// for (Bone bone : bones) {
// JSONObject obj = new JSONObject(bone);
// bones.put(obj);
// }
// jsonData.put(bones);
//
// return jsonData;
