package com.app;

import java.awt.Desktop;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileSystemView;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class App {

	public static int DIGITS = 2;

	private JFrame frame;
	private JTabbedPane tabs;
	private ProjectPane project;
	private HashMap<String, ImageIcon> icons;
	private HashMap<String, String> categories;
	private File rootFile;
	private String print_script;

	public App() {

	}

	public void init() {
		IconAtlas.registerAllIcons();

		reloadIcons();
		loadConfig();
		loadPrintScript();

		frame = new JFrame("Hyperion Dimensional Certificate");
		frame.setJMenuBar(buildMenuBar());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabs = new JTabbedPane();
		frame.add(tabs);
		frame.setIconImage(IconAtlas.icons.get("logo"));

		frame.add(project = new ProjectPane(this));
		project.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private JMenuBar buildMenuBar() {
		JMenuBar res = new JMenuBar();

		JMenu config = new JMenu("Config");
		JMenuItem cats = new JMenuItem("Categories");
		cats.addActionListener(e -> {
			reloadIcons();
			saveConfig();
			CategoryDialog dialog = new CategoryDialog(this);
			dialog.setVisible(true);
		});
		config.add(cats);

		JMenuItem iconDir = new JMenuItem("Open icons folder");
		iconDir.addActionListener(e -> {
			try {
				Desktop.getDesktop().open(new File("./icons/"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		config.add(iconDir);

		JMenuItem setDigits = new JMenuItem("Set Digits");
		setDigits.addActionListener(e -> {
			String input = JOptionPane.showInputDialog(frame, "Input the number of digits you want", "Digits selection",
					JOptionPane.INFORMATION_MESSAGE);
			if (input != null && !input.equals("")) {
				try {
					DIGITS = Integer.parseInt(input);
					if (DIGITS < 0) {
						DIGITS = 0;
					}
					for (Measure measure : project.getMeasures()) {
						measure.resfreshDigits();
					}
					saveConfig();
					project.repaint();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		config.add(setDigits);

		JMenuItem setRootFile = new JMenuItem("Set Root File");
		setRootFile.addActionListener(e -> {
			if (rootFile == null)
				rootFile = FileSystemView.getFileSystemView().getDefaultDirectory();

			JFileChooser chooser = new JFileChooser(rootFile);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int choice = chooser.showDialog(frame, "Select root file");
			if (choice == JFileChooser.APPROVE_OPTION) {
				rootFile = chooser.getSelectedFile();
				saveConfig();
			}
		});

		config.add(setRootFile);

		res.add(config);

		return res;
	}

	public void reloadIcons() {
		icons = new HashMap<>();
		File dir = new File("./icons/");
		if (!dir.exists()) {
			dir.mkdir();
		}
		for (File file : dir.listFiles(f -> f.getName().endsWith(".png") || f.getName().endsWith(".jpg"))) {
			try {
				icons.put(file.getName().substring(0, file.getName().length() - 4),
						new ImageIcon(ImageIO.read(file).getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void saveConfig() {
		JsonObject target = new JsonObject();
		for (Entry<String, String> cat : categories.entrySet().stream().toList()) {
			target.addProperty(cat.getKey(), cat.getValue());
		}
		JsonObject res = new JsonObject();
		res.add("categories", target);
		res.addProperty("digits", DIGITS);
		res.addProperty("rootFile", rootFile.getAbsolutePath());
		File file = new File("./config.json");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Files.write(file.toPath(), res.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() {
		categories = new HashMap<>();
		try {
			JsonObject data = JsonParser.parseReader(new FileReader(new File("./config.json"))).getAsJsonObject();
			JsonObject cats = data.get("categories").getAsJsonObject();

			for (Entry<String, JsonElement> entry : cats.entrySet()) {
				categories.put(entry.getKey(), entry.getValue().getAsString());
			}
			if (data.has("digits")) {
				DIGITS = data.get("digits").getAsInt();
			}
			if (data.has("rootFile")) {
				rootFile = Path.of(data.get("rootFile").getAsString()).toFile();
			}
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadPrintScript() {
		BufferedInputStream in = new BufferedInputStream(
				Thread.currentThread().getContextClassLoader().getResourceAsStream("script.txt"));
		try {
			print_script = new String(in.readAllBytes(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getCategories() {
		return categories;
	}

	public HashMap<String, ImageIcon> getIcons() {
		return icons;
	}

	public JFrame getFrame() {
		return frame;
	}

	public File getRootFile() {
		return rootFile;
	}

	public String getPrintScript() {
		return print_script;
	}

}
