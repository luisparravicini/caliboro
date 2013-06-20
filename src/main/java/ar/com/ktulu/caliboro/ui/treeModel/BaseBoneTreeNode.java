package ar.com.ktulu.caliboro.ui.treeModel;

import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class BaseBoneTreeNode extends DefaultMutableTreeNode {

	public void removeDataNode() {
		removeFromParent();
	}

}
