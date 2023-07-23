package com.app;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class CategoryDialog extends JDialog {

	public CategoryDialog(App app) {
		super(app.getFrame(), "Categories", true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Categories"));
		panel.setBorder(BorderFactory.createTitledBorder("Categories"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		CategoryModel model = new CategoryModel(app);
		model.updateValues();
		JTable table = new JTable(model);
		table.setDefaultRenderer(App.class, new CategoryCellRenderer(app));
		JComboBox<String> icons = new JComboBox<String>(
				new Vector<String>(app.getIcons().entrySet().stream().map(entry -> entry.getKey()).toList()));
		icons.setRenderer(new IconListRenderer(app));
		table.setDefaultEditor(App.class, new DefaultCellEditor(icons));
		gbc.gridwidth = 2;
		panel.add(new JScrollPane(table), gbc);

		JButton add = new JButton("Add Category");
		add.setIcon(IconAtlas.getIcon("add", 16));
		add.addActionListener(e -> {

			String res = JOptionPane.showInputDialog(app.getFrame(), "Enter the name of the new category",
					"Category adding", JOptionPane.QUESTION_MESSAGE);
			if (res != null && !res.equals("")) {
				app.getCategories().put(res, "");
			}
			model.updateValues();
			table.revalidate();
			table.repaint();
		});
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		panel.add(add, gbc);

		JButton remove = new JButton("Remove");
		remove.setIcon(IconAtlas.getIcon("remove", 16));
		remove.addActionListener(e -> {
			int choice = JOptionPane.showConfirmDialog(app.getFrame(),
					"Do you really want to remove " + table.getSelectedRowCount() + " categories?", "Warning",
					JOptionPane.WARNING_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				Arrays.stream(table.getSelectedRows()).mapToObj(i -> model.getValues().get(i).getKey()).toList()
						.forEach(key -> app.getCategories().remove(key));
				model.updateValues();
				table.revalidate();
				table.repaint();
			}
		});
		gbc.gridx++;
		panel.add(remove, gbc);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				app.saveCategories();
			}
		});

		add(panel);
		pack();
	}

	private class CategoryModel extends AbstractTableModel {

		private App app;
		private ArrayList<Entry<String, String>> values;

		private CategoryModel(App app) {
			super();
			this.app = app;
		}

		public void updateValues() {
			values = new ArrayList<>();
			values.addAll(app.getCategories().entrySet());
		}

		public ArrayList<Entry<String, String>> getValues() {
			return values;
		}

		@Override
		public int getRowCount() {
			return values.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public Class<?> getColumnClass(int col) {
			return col == 0 ? String.class : App.class;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Entry<String, String> target = values.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return target.getKey();
			case 1:
				return target.getValue();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return true;
		}

		@Override
		public String getColumnName(int col) {
			switch (col) {
			case 0:
				return "Category";
			case 1:
				return "Icon";
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			Entry<String, String> target = values.get(row);
			String data = (String) value;
			if (col == 0) {
				app.getCategories().remove(target.getKey());
				app.getCategories().put(data, target.getValue());
			}
			if (col == 1) {
				app.getCategories().put(target.getKey(), data);
			}
			updateValues();
		}

	}

	private class CategoryCellRenderer extends DefaultTableCellRenderer {

		private App app;

		private CategoryCellRenderer(App app) {
			super();
			this.app = app;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel res = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			res.setIcon(app.getIcons().get(value));
			return res;
		}
	}

	private class IconListRenderer extends DefaultListCellRenderer {

		private App app;

		private IconListRenderer(App app) {
			super();
			this.app = app;
		}

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel res = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			res.setIcon(app.getIcons().get(value));
			return res;

		}

	}
}
