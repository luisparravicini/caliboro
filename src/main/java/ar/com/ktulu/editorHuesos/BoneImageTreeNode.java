package ar.com.ktulu.editorHuesos;

import javax.swing.tree.DefaultMutableTreeNode;

public class BoneImageTreeNode extends DefaultMutableTreeNode {
	public BoneImageTreeNode(BoneImage boneImage) {
		setUserObject(boneImage);
	}

	public String getImagePath() {
		return ((BoneImage)getUserObject()).getPath();
	}
}
