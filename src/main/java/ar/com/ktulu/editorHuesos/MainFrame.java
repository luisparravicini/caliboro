package ar.com.ktulu.editorHuesos;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class MainFrame extends JFrame implements TreeModelListener {

	private JPanel contentPane;
	private JTree bonesTree;
	private JToolBar toolBar;
	private JButton btnAgregar;
	private JScrollPane scrollPane;
	private JLabel imageLabel;
	private JButton btnAddImages;
	private JButton btnRemoveBone;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.loadBones();
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
		splitPane.setDividerLocation(150);
		contentPane.add(splitPane, BorderLayout.CENTER);

		bonesTree = new JTree();
		splitPane.setLeftComponent(bonesTree);

		scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		imageLabel = new JLabel();
		scrollPane.setViewportView(imageLabel);
		imageLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

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

	protected void removeBone() {
		DefaultTreeModel model = (DefaultTreeModel) bonesTree.getModel();
		TreePath[] selection = bonesTree.getSelectionPaths();

		if (selection != null)
			for (TreePath path : selection)
				model.removeNodeFromParent((MutableTreeNode) path
						.getLastPathComponent());
	}

	protected void addBoneImages() {
	}

	protected void addBone() {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				"Nombre del hueso");

		DefaultTreeModel model = (DefaultTreeModel) bonesTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		root.add(node);
		model.reload();
	}

	protected void loadBones() throws IOException {
		try {
			new BonesLoader().load();
		} catch (LoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DefaultTreeModel model = new DefaultTreeModel(
				new DefaultMutableTreeNode());
		model.addTreeModelListener(this);
		bonesTree.setRootVisible(false);
		bonesTree.setModel(model);
		bonesTree.setEditable(true);

		ImageIcon img = new ImageIcon(
				"/Users/xrm0/Documents/dev/huesos/initializr/huesos/canines-lg.jpg");
		imageLabel.setIcon(img);
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

		btnAddImages.setEnabled(bonesExist);
		btnRemoveBone.setEnabled(bonesExist);
	}
}
