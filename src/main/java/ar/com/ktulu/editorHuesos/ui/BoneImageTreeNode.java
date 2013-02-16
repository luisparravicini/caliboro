package ar.com.ktulu.editorHuesos.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import ar.com.ktulu.editorHuesos.model.BoneImage;

public class BoneImageTreeNode extends DefaultMutableTreeNode {
	public BoneImageTreeNode(BoneImage boneImage) {
		setUserObject(boneImage);
	}

	public String getImagePath() {
		return ((BoneImage) getUserObject()).getPath();
	}

	@Override
	public void setUserObject(Object userObject) {
		if (String.class.isInstance(userObject))
			((BoneImage) getUserObject()).setName((String) userObject);
		else
			super.setUserObject(userObject);
	}
}
