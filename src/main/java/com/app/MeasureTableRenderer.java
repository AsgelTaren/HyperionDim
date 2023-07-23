package com.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class MeasureTableRenderer extends DefaultTableCellRenderer {

	private ImageIcon bad, good;
	private App app;

	public MeasureTableRenderer(App app) {
		super();
		this.app = app;
		bad = IconAtlas.getIcon("bad", 16);
		good = IconAtlas.getIcon("good", 16);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel res = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		res.setForeground(UIManager.getColor("Label.foreground"));
		res.setHorizontalAlignment(SwingConstants.CENTER);
		res.setFont(UIManager.getFont("Label.font").deriveFont(Font.BOLD));
		res.setIcon(null);
		if (value instanceof MeasureParam param) {
			if (param.type == 0) {
				if (param.target.value.compareTo(param.target.nominal.add(param.target.lower)) >= 0
						&& param.target.value.compareTo(param.target.nominal.add(param.target.upper)) <= 0) {
					res.setForeground(Color.GREEN);
				} else {
					res.setForeground(Color.RED);
				}
			}
			if (param.type == 5) {
				if (param.target.value.compareTo(param.target.nominal.add(param.target.lower)) >= 0
						&& param.target.value.compareTo(param.target.nominal.add(param.target.upper)) <= 0) {
					res.setForeground(Color.GREEN);
					res.setIcon(good);
				} else {
					res.setForeground(Color.RED);
					res.setIcon(bad);
				}
			}
			if (param.type == 1) {
				res.setIcon(app.getIcons().get(app.getCategories().get(param.target.description)));
			}
		}
		return res;
	}

}
