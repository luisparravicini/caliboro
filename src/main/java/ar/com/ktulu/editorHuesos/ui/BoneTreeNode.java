package ar.com.ktulu.editorHuesos.ui;

import ar.com.ktulu.editorHuesos.BonesStore;
import ar.com.ktulu.editorHuesos.model.Bone;
import ar.com.ktulu.editorHuesos.model.BoneImage;

@SuppressWarnings("serial")
public class BoneTreeNode extends BaseBoneTreeNode {

	@Override
	public void removeDataNode() {
		BonesStore.getInstance().remove(((Bone) getUserObject()));
		super.removeDataNode();
	}

	public BoneTreeNode(Bone bone) {
		setUserObject(bone);
		for (BoneImage img : bone.getImages()) {
			BoneImageTreeNode node = new BoneImageTreeNode(img);
			add(node);
		}

		BonesStore.getInstance().add(bone);
	}

	public void addBoneImage(String file) {
		Bone bone = (Bone) getUserObject();
		bone.addImage(file);
		add(new BoneImageTreeNode(bone.lastImage()));

		BonesStore.getInstance().dirty();
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public void setUserObject(Object userObject) {
		if (String.class.isInstance(userObject)) {
			((Bone) getUserObject()).setName((String) userObject);
			BonesStore.getInstance().dirty();
		} else
			super.setUserObject(userObject);
	}

}
