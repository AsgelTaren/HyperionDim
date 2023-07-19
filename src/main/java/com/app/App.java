package com.app;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class App {

	private JFrame frame;
	private JTabbedPane tabs;
	private ProjectPane project;

	public App() {

	}

	public void init() {
		IconAtlas.registerAllIcons();
		frame = new JFrame("Hyperion Dimensional Certificate");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabs = new JTabbedPane();
		frame.add(tabs);
		frame.setIconImage(IconAtlas.icons.get("logo"));

		frame.add(project = new ProjectPane(frame));
		project.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

}
