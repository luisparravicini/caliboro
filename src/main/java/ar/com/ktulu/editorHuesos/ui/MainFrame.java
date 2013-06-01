package ar.com.ktulu.editorHuesos.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ar.com.ktulu.editorHuesos.BonesStore;
import ar.com.ktulu.editorHuesos.model.Bone;
import ar.com.ktulu.editorHuesos.model.BoneImage;
import ar.com.ktulu.editorHuesos.ui.images.ImageManager;
import ar.com.ktulu.editorHuesos.ui.images.ImageMouseListener;
import ar.com.ktulu.editorHuesos.ui.images.ImageView;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements TreeModelListener,
		TreeSelectionListener {

	private JPanel contentPane;
	private JTree bonesTree;
	private JToolBar toolBar;
	private JButton btnAgregar;
	private JScrollPane scrollPane;
	private JButton btnAddImages;
	private JButton btnRemove;
	private JButton btnBonePoints;
	private boolean bonePointAdding = true;
	private JButton btnExportar;
	private JButton btnPrevisualizar;
	private JPanel panel;
	private JPanel panel_1;
	private ImageManager imageManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MainFrame.startup();
	}

	public static void startup() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				EventQueueErrorCatcher.install();
				try {
					MainFrame frame = new MainFrame();
					frame.setInitialStoreFolder();
					BonesStore.getInstance().load();
					frame.setup();
					frame.setVisible(true);
				} catch (Exception e) {
					EventQueueErrorCatcher.logError(e);
				}
			}
		});
	}

	private void setInitialStoreFolder() {
		File path;
		while ((path = askForStoreFolder()) == null) {
			JOptionPane.showMessageDialog(null,
					"Debe seleccionar una carpeta para guardar los datos",
					null, JOptionPane.INFORMATION_MESSAGE);
		}

		BonesStore.getInstance().setPath(path);
	}

	private File askForStoreFolder() {
		JFileChooser chooserDlg = new JFileChooser();
		chooserDlg.setDialogTitle("Seleccionar carpeta");
		chooserDlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooserDlg.setDialogType(JFileChooser.SAVE_DIALOG);

		int result = chooserDlg.showDialog(this, "Seleccionar");

		if (result == JFileChooser.APPROVE_OPTION) {
			File path = chooserDlg.getSelectedFile().getAbsoluteFile();
			return createFolder(path) ? path : null;
		}

		return null;
	}

	private boolean createFolder(File path) {
		boolean result = true;
		if (!path.exists())
			if (!path.mkdirs()) {
				JOptionPane.showMessageDialog(null,
						"No se pudo crear la carpeta", "Error",
						JOptionPane.ERROR_MESSAGE);
				result = false;
			}

		return result;
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		imageManager = new ImageManager(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 578, 337);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(180);
		contentPane.add(splitPane, BorderLayout.CENTER);

		bonesTree = new JTree();
		bonesTree.setShowsRootHandles(true);
		splitPane.setLeftComponent(bonesTree);

		panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		imageManager.imageInfo = new JLabel();
		panel_1.add(imageManager.imageInfo, BorderLayout.WEST);

		JSlider imageZoom = new JSlider();
		imageZoom.setToolTipText("Nivel de zoom de la imagen");
		imageZoom.setMaximum(150);
		imageZoom.setMinimum(20);
		imageZoom.setValue(100);
		imageZoom.setMajorTickSpacing(10);
		imageZoom.setSnapToTicks(true);
		imageZoom.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				imageManager.sliderChangedValue();
			}
		});
		panel_1.add(imageZoom, BorderLayout.EAST);
		imageManager.imageZoom = imageZoom;

		scrollPane = new JScrollPane();
		panel.add(scrollPane);

		imageManager.imageView = new ImageView();
		scrollPane.setViewportView(imageManager.imageView);

		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		btnAgregar = new JButton("Agregar hueso");
		toolBar.add(btnAgregar);

		btnRemove = new JButton("Borrar");
		btnRemove.setEnabled(false);
		toolBar.add(btnRemove);

		btnAddImages = new JButton("Agregar fotos");
		btnAddImages.setEnabled(false);
		toolBar.add(btnAddImages);

		btnBonePoints = new JButton(getBonePointButtonLabel());
		toolBar.add(btnBonePoints);

		btnExportar = new JButton("Exportar");
		toolBar.add(btnExportar);

		btnPrevisualizar = new JButton("Previsualizar");
		toolBar.add(btnPrevisualizar);

		btnBonePoints.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				bonePointAdding = !bonePointAdding;
				btnBonePoints.setText(getBonePointButtonLabel());
			}
		});

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
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeNode();
			}
		});

		imageManager.hideImage();
	}

	private String getBonePointButtonLabel() {
		return "Puntos: " + (bonePointAdding ? "Agregar" : "Borrar");
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

		ImageMouseListener mouseListener = new ImageMouseListener(imageManager);
		scrollPane.addMouseListener(mouseListener);
		scrollPane.addMouseMotionListener(mouseListener);
	}

	protected void removeNode() {
		DefaultTreeModel model = (DefaultTreeModel) bonesTree.getModel();
		TreePath[] selection = bonesTree.getSelectionPaths();

		if (selection != null) {
			if (JOptionPane.showConfirmDialog(this,
					"¿está seguro de borrar los elementos seleccionados?",
					"Borrar", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
				return;

			for (TreePath path : selection) {
				MutableTreeNode node = (MutableTreeNode) path
						.getLastPathComponent();
				// TODO esta mal esto asi
				if (BaseBoneTreeNode.class.isInstance(node)) {
					TreeNode parent = node.getParent();
					((BaseBoneTreeNode) node).removeDataNode();
					model.reload(parent);
				}
			}
		}
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
			model.reload(selectedNode);
			bonesTree.expandPath(path);
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
		btnRemove.setEnabled(bonesExist && boneSelected);
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
			if (!isBoneImageNode(node)) {
				imageManager.hideImage();
				return;
			}

			BoneImageTreeNode imgNode = (BoneImageTreeNode) node;
			imageManager.loadBoneImage(imgNode);
		}
	}

	private boolean isBoneImageNode(DefaultMutableTreeNode node) {
		return (BoneImageTreeNode.class.isInstance(node));
	}

	public BoneImage getImageSelected() {
		BoneImage result = null;
		TreePath path = bonesTree.getSelectionPath();
		if (path != null) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (isBoneImageNode(selectedNode))
				result = (BoneImage) selectedNode.getUserObject();
		}
		return result;
	}

	public boolean isAddingPoints() {
		return bonePointAdding;
	}

}
