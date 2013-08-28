package ar.com.ktulu.caliboro.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import ar.com.ktulu.caliboro.ui.treeModel.BaseBoneTreeNode;

//TODO no estoy usando esta clase (si la referencio, pero obtengo el objeto del drag and drop de otro lado)
public class TransferableItem implements Transferable {

	private BaseBoneTreeNode node;

	public TransferableItem(BaseBoneTreeNode node) {
		this.node = node;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (TransferableItem.getBaseBoneTreeNode().equals(flavor))
			return node.getPath();
		else
			return null;
	}

	public static DataFlavor getBaseBoneTreeNode() {
		return new DataFlavor(BaseBoneTreeNode.class, "TransferibleNode");
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { TransferableItem.getBaseBoneTreeNode(), };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return TransferableItem.getBaseBoneTreeNode().equals(flavor);
	}

}
