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

}
