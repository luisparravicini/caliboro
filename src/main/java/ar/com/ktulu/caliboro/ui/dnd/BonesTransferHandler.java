package ar.com.ktulu.caliboro.ui.dnd;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import ar.com.ktulu.caliboro.BonesStore;
import ar.com.ktulu.caliboro.model.Bone;
import ar.com.ktulu.caliboro.ui.treeModel.BaseBoneTreeNode;
import ar.com.ktulu.caliboro.ui.treeModel.BoneTreeNode;

public class BonesTransferHandler extends TransferHandler {

	private static final long serialVersionUID = -7783801799223806678L;
	private BaseBoneTreeNode sourceNode;

	@Override
	public boolean canImport(TransferSupport support) {
		if (!support.isDrop())
			return false;

		JTree tree = (JTree) support.getComponent();
		TreePath srcPath = tree.getSelectionPath();
		if (srcPath == null)
			return false;

		support.setShowDropLocation(true);

		return true;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		TreePath path = ((JTree) c).getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (BaseBoneTreeNode.class.isInstance(selectedNode)) {
				this.sourceNode = (BaseBoneTreeNode) selectedNode;
				return new TransferableItem((BaseBoneTreeNode) selectedNode);
			}
		}

		return null;
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		this.sourceNode = null;
	}

	public int getSourceActions(JComponent c) {
		return MOVE;
	}

	public boolean importData(TransferHandler.TransferSupport support) {
		JTree tree = (JTree) support.getComponent();
		JTree.DropLocation dropLocation = (JTree.DropLocation) support
				.getDropLocation();

		TreePath dropPath = dropLocation.getPath();
		BaseBoneTreeNode dstNode = null;
		if (dropPath != null)
			dstNode = (BaseBoneTreeNode) dropPath.getLastPathComponent();

		BaseBoneTreeNode source = this.sourceNode;
		boolean sourceIsBone = (BoneTreeNode.class.isInstance(source));
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		BonesStore store = BonesStore.getInstance();
		boolean dstIsBone = (BoneTreeNode.class.isInstance(dstNode));

		if (sourceIsBone) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) model
					.getRoot();
			if (dstNode == null) {
				model.removeNodeFromParent(source);
				model.insertNodeInto(source, parent,
						model.getChildCount(parent));
				store.moveBoneLast((Bone) source.getUserObject());
			} else if (dstIsBone) {
				int index = model.getIndexOfChild(parent, dstNode);
				if (index >= 0) {
					model.removeNodeFromParent(source);
					model.insertNodeInto(source, parent, index);
					store.moveBone((Bone) source.getUserObject(), index);
				}
			}
		} else if (dstNode != null) {
			if (dstIsBone) {
				DefaultMutableTreeNode parent = dstNode;
				model.removeNodeFromParent(source);
				model.insertNodeInto(source, parent,
						model.getChildCount(parent));
			} else {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dstNode
						.getParent();
				int index = model.getIndexOfChild(parent, dstNode);
				if (index >= 0) {
					model.removeNodeFromParent(source);
					model.insertNodeInto(source, parent, index);
				}
			}
		}
		store.save();

		return true;
	}

}
