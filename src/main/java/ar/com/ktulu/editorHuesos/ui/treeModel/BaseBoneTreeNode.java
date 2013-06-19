package ar.com.ktulu.editorHuesos.ui.treeModel;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class BaseBoneTreeNode extends DefaultMutableTreeNode {

	public void removeDataNode() {
		removeFromParent();
	}

}
