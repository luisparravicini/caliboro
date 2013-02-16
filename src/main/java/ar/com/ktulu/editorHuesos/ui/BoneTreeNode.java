package ar.com.ktulu.editorHuesos.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import ar.com.ktulu.editorHuesos.model.Bone;

public class BoneTreeNode extends DefaultMutableTreeNode {

	public BoneTreeNode(Bone bone) {
		setUserObject(bone);
	}

	public void addBoneImage(String file) {
		Bone bone = (Bone) getUserObject();
		bone.addImage(file);
		add(new BoneImageTreeNode(bone.lastImage()));
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public void setUserObject(Object userObject) {
		if (String.class.isInstance(userObject))
			((Bone) getUserObject()).setName((String) userObject);
		else
			super.setUserObject(userObject);
	}

}
