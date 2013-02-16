package ar.com.ktulu.editorHuesos.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import ar.com.ktulu.editorHuesos.BonesStore;
import ar.com.ktulu.editorHuesos.model.Bone;
import ar.com.ktulu.editorHuesos.model.BoneImage;
import ar.com.ktulu.editorHuesos.model.BonePoint;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements TreeModelListener,
		TreeSelectionListener {

	private JPanel contentPane;
	private JTree bonesTree;
	private JToolBar toolBar;
	private JButton btnAgregar;
	private JScrollPane scrollPane;
	private ImageView imageView;
	private JButton btnAddImages;
	private JButton btnRemoveBone;
	private JButton btnAgregarPunto;
	private BonePoint draggingPoint;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		BonesStore.getInstance().load();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(180);
		contentPane.add(splitPane, BorderLayout.CENTER);

		bonesTree = new JTree();
		splitPane.setLeftComponent(bonesTree);

		scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		imageView = new ImageView();
		scrollPane.setViewportView(imageView);

		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		btnAgregar = new JButton("Agregar hueso");
		toolBar.add(btnAgregar);

		btnRemoveBone = new JButton("Borrar hueso");
		btnRemoveBone.setEnabled(false);
		toolBar.add(btnRemoveBone);

		btnAddImages = new JButton("Agregar fotos");
		btnAddImages.setEnabled(false);
		toolBar.add(btnAddImages);

		btnAgregarPunto = new JButton("Agregar punto");
		toolBar.add(btnAgregarPunto);

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addBone();
			}
		});
		btnAddImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBoneImages();
			}
		});
		btnRemoveBone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeBone();
			}
		});
	}

	protected void setup() {
		DefaultTreeModel model = new DefaultTreeModel(
				new DefaultMutableTreeNode());
		model.addTreeModelListener(this);
		bonesTree.setRootVisible(false);
		bonesTree.setModel(model);
		bonesTree.setEditable(true);
		bonesTree.addTreeSelectionListener(this);

		BonesStore store = BonesStore.getInstance();
		store.freeze();
		try {
			for (Bone bone : BonesStore.getInstance().data())
				addBone(bone);
		} finally {
			store.unfreeze();
		}

		ImageMouseListener mouseListener = new ImageMouseListener(this);
		scrollPane.addMouseListener(mouseListener);
		scrollPane.addMouseMotionListener(mouseListener);
	}

	protected void removeBone() {
		DefaultTreeModel model = (DefaultTreeModel) bonesTree.getModel();
		TreePath[] selection = bonesTree.getSelectionPaths();

		if (selection != null)
			for (TreePath path : selection)
				model.removeNodeFromParent((MutableTreeNode) path
						.getLastPathComponent());
	}

	protected void addBoneImages() {
		FileDialog fileDlg = new FileDialog(this);
		fileDlg.setMode(FileDialog.LOAD);
		fileDlg.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String nameLower = name.toLowerCase();
				return new File(dir, name).isFile()
						&& (nameLower.endsWith(".jpg")
								|| nameLower.endsWith(".png") || nameLower
									.endsWith(".jpeg"));
			}
		});
		fileDlg.setVisible(true);

		// TODO recien en jdk7 puedo seleccionar varios archivos a la vez

		String filename = fileDlg.getFile();
		if (filename != null)
			addBoneImage(new File(fileDlg.getDirectory(), filename));
	}

	private void addBoneImage(File filePath) {
		DefaultTreeModel model = (DefaultTreeModel) bonesTree.getModel();

		TreePath path = bonesTree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (isBoneImageNode(selectedNode))
				selectedNode = (DefaultMutableTreeNode) selectedNode
						.getParent();

			BoneTreeNode node = (BoneTreeNode) selectedNode;
			node.addBoneImage(filePath.getAbsolutePath());
			model.reload();
		}
	}

	protected void addBone() {
		addBone(new Bone("Nombre del hueso"));
	}

	private void addBone(Bone bone) {
		BoneTreeNode node = new BoneTreeNode(bone);

		DefaultTreeModel model = (DefaultTreeModel) bonesTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		root.add(node);
		model.reload();
	}

	public void treeNodesChanged(TreeModelEvent e) {
		updateButtonsStatus((DefaultTreeModel) e.getSource());
	}

	public void treeNodesInserted(TreeModelEvent e) {
		updateButtonsStatus((DefaultTreeModel) e.getSource());
	}

	public void treeNodesRemoved(TreeModelEvent e) {
		updateButtonsStatus((DefaultTreeModel) e.getSource());
	}

	public void treeStructureChanged(TreeModelEvent e) {
		updateButtonsStatus((DefaultTreeModel) e.getSource());
	}

	private void updateButtonsStatus(DefaultTreeModel model) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		boolean bonesExist = root.getChildCount() > 0;
		boolean boneSelected = (bonesTree.getSelectionCount() > 0);

		btnAddImages.setEnabled(bonesExist && boneSelected);
		btnRemoveBone.setEnabled(bonesExist && boneSelected);
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		updateButtonsStatus((DefaultTreeModel) bonesTree.getModel());
		updateBoneImageView(event.getPath());
	}

	private void updateBoneImageView(TreePath path) {
		if (path != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (!isBoneImageNode(node))
				return;

			BoneImageTreeNode imgNode = (BoneImageTreeNode) node;
			imageView.loadImage(imgNode);
		}
	}

	private boolean isBoneImageNode(DefaultMutableTreeNode node) {
		return (BoneImageTreeNode.class.isInstance(node));
	}

	public void mousePressed(int x, int y) {
		TreePath path = bonesTree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (!isBoneImageNode(selectedNode))
				return;

			BoneImage img = (BoneImage) selectedNode.getUserObject();
			imageView.addPoint(img.addPoint(x, y));
		}
	}

	public void mouseDragged(int x, int y) {
		if (draggingPoint == null)
			draggingPoint = findIfPointIn(x, y);

		if (draggingPoint != null) {
		}
	}

	private BonePoint findIfPointIn(int x, int y) {
		TreePath path = bonesTree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (!isBoneImageNode(selectedNode))
				return null;

			BoneImage img = (BoneImage) selectedNode.getUserObject();
			int d = 5;
			Rectangle hitBox = new Rectangle(x - d, y - d, d * 2, d * 2);
			for (BonePoint point : img.getPoints()) {
				if (hitBox.contains(point.x,  point.y))
					return point;
			}
			img.addPoint(x, y);
		}

		return null;
	}

	public void finishDragging() {
		draggingPoint = null;
	}
}
