package ar.com.ktulu.editorHuesos;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;


public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JList bonesList;
	private JToolBar toolBar;
	private JButton btnAgregar;
	private JScrollPane scrollPane;
	private JLabel imageLabel;

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

		bonesList = new JList();
		splitPane.setLeftComponent(bonesList);

		scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		imageLabel = new JLabel();
		scrollPane.setViewportView(imageLabel);
		imageLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		btnAgregar = new JButton("Agregar");
		toolBar.add(btnAgregar);
	}

	protected void loadBones() throws IOException {
		try {
			new BonesLoader().load();
		} catch (LoaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DefaultListModel model = new DefaultListModel();
		bonesList.setModel(model);
		model.addElement(new Hueso("asdsadasd"));
		model.addElement(new Hueso("asdsadasd"));
		model.addElement(new Hueso("asdsadasd"));
		model.addElement(new Hueso("asdsadasd"));

		ImageIcon img = new ImageIcon(
						"/Users/xrm0/Documents/dev/huesos/initializr/huesos/canines-lg.jpg");
		imageLabel.setIcon(img);
	}
}
