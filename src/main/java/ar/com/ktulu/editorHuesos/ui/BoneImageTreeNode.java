package ar.com.ktulu.editorHuesos.ui;

import java.util.List;

import ar.com.ktulu.editorHuesos.BonesStore;
import ar.com.ktulu.editorHuesos.model.BoneImage;
import ar.com.ktulu.editorHuesos.model.BonePoint;

@SuppressWarnings("serial")
public class BoneImageTreeNode extends BaseBoneTreeNode {
	public BoneImageTreeNode(BoneImage boneImage) {
		setUserObject(boneImage);
	}

	public String getImagePath() {
		return ((BoneImage) getUserObject()).imageAbsolutePath();
	}

	@Override
	public void removeDataNode() {
		((BoneImage) getUserObject()).removeFromParent();
		BonesStore.getInstance().dirty();
		super.removeDataNode();
	}

	@Override
	public void setUserObject(Object userObject) {
		if (String.class.isInstance(userObject)) {
			((BoneImage) getUserObject()).setName((String) userObject);
			BonesStore.getInstance().dirty();
		} else
			super.setUserObject(userObject);
	}

	public List<BonePoint> getPoints() {
		return ((BoneImage) getUserObject()).getPoints();
	}
}
