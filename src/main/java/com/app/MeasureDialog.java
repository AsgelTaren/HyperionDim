package com.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

@SuppressWarnings("serial")
public class MeasureDialog extends JDialog {

	private JFormattedTextField maxField, minField, lowerField, upperField, nominalField, valueField;
	private JRadioButton tol, minMax;

	public MeasureDialog(App app, ProjectPane project, Measure measure) {
		super(app.getFrame(), "Measure Editing", true);
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		format.setMaximumFractionDigits(App.DIGITS);
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Float.class);

		setLayout(new GridBagLayout());

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Measure infos"));

		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.insets = new Insets(1, 5, 2, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;

		JLabel descLabel = new JLabel("Description");
		gbc.gridwidth = 2;
		descLabel.setPreferredSize(new Dimension(300, 25));
		panel.add(descLabel, gbc);

		JComboBox<String> descField = new JComboBox<String>(
				new Vector<String>(app.getCategories().entrySet().stream().map(entry -> entry.getKey()).toList()));
		descField.setEditable(true);
		descField.setSelectedItem(measure.description);
		descField.setPreferredSize(new Dimension(300, 25));
		gbc.gridy++;
		panel.add(descField, gbc);

		JLabel nominalLabel = new JLabel("Nominal");
		nominalLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		gbc.gridwidth = 1;
		panel.add(nominalLabel, gbc);

		nominalField = new JFormattedTextField(formatter);
		nominalField.setText(measure.nominal.toPlainString());
		nominalField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(nominalField, gbc);

		JLabel valueLabel = new JLabel("Value");
		valueLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy = 2;
		gbc.gridx++;
		panel.add(valueLabel, gbc);

		valueField = new JFormattedTextField(formatter);
		valueField.setText(measure.value.toPlainString());
		valueField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(valueField, gbc);

		ButtonGroup group = new ButtonGroup();
		tol = new JRadioButton("Use upper/lower tolerance");
		group.add(tol);
		gbc.gridy++;
		gbc.gridx = 0;
		panel.add(tol, gbc);

		JLabel upperLabel = new JLabel("Upper");
		upperLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		gbc.gridx = 0;
		panel.add(upperLabel, gbc);

		upperField = new JFormattedTextField(formatter);
		upperField.setText(measure.upper.toPlainString());
		upperField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(upperField, gbc);

		JLabel lowerLabel = new JLabel("Lower");
		lowerLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy = 5;
		gbc.gridx++;
		panel.add(lowerLabel, gbc);

		lowerField = new JFormattedTextField(formatter);
		lowerField.setText(measure.lower.toPlainString());
		lowerField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(lowerField, gbc);

		minMax = new JRadioButton("Use Min/Max tolerance");
		group.add(minMax);
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(minMax, gbc);

		JLabel maxLabel = new JLabel("Max");
		maxLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		gbc.gridx = 0;
		panel.add(maxLabel, gbc);

		maxField = new JFormattedTextField(formatter);
		maxField.setText(measure.nominal.add(measure.upper).toPlainString());
		maxField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(maxField, gbc);

		JLabel minLabel = new JLabel("Min");
		minLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy = 8;
		gbc.gridx++;
		panel.add(minLabel, gbc);

		minField = new JFormattedTextField(formatter);
		minField.setText(measure.nominal.add(measure.lower).toPlainString());
		minField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(minField, gbc);

		tol.addActionListener(e -> {
			modeChanged();
		});

		minMax.addActionListener(e -> {
			modeChanged();
		});

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2, 5, 5, 5);
		add(panel, gbc);

		JButton apply = new JButton("Apply");
		apply.addActionListener(e -> {
			if (checkForValue(nominalField) & checkForValue(valueField) & checkForValue(upperField)
					& checkForValue(lowerField)) {
				measure.nominal = new BigDecimal(nominalField.getText());
				measure.value = new BigDecimal(valueField.getText());
				measure.upper = new BigDecimal(upperField.getText());
				measure.lower = new BigDecimal(lowerField.getText());
				measure.description = (String) descField.getSelectedItem();
				close();
				project.refreshMeasures();
			}
		});
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		add(apply, gbc);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> close());
		gbc.gridx++;
		add(cancel, gbc);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"cancel");
		getRootPane().getActionMap().put("cancel", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				close();

			}
		});
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK), "apply");
		getRootPane().getActionMap().put("apply", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkForValue(nominalField) & checkForValue(valueField) & checkForValue(upperField)
						& checkForValue(lowerField)) {
					measure.nominal = new BigDecimal(nominalField.getText());
					measure.value = new BigDecimal(valueField.getText());
					measure.upper = new BigDecimal(upperField.getText());
					measure.lower = new BigDecimal(lowerField.getText());
					measure.description = (String) descField.getSelectedItem();
					close();
					project.refreshMeasures();
				}
			}
		});
		tol.setSelected(true);
		modeChanged();

		upperField.addActionListener(e -> updateMinMax());
		lowerField.addActionListener(e -> updateMinMax());
		minField.addActionListener(e -> updateUpperLower());
		maxField.addActionListener(e -> updateUpperLower());
		nominalField.addActionListener(e -> {
			if (tol.isSelected()) {
				updateMinMax();
			} else {
				updateUpperLower();
			}
		});

		valueField.addActionListener(e -> valueChanged());
	}

	private void modeChanged() {
		upperField.setEditable(tol.isSelected());
		lowerField.setEditable(tol.isSelected());
		minField.setEditable(minMax.isSelected());
		maxField.setEditable(minMax.isSelected());
	}

	private void updateMinMax() {
		try {
			if (!nominalField.getText().equals("") && !lowerField.getText().equals("")) {
				minField.setText(new BigDecimal(nominalField.getText()).add(new BigDecimal(lowerField.getText()))
						.toPlainString());
			}

			if (!nominalField.getText().equals("") && !upperField.getText().equals("")) {
				maxField.setText(new BigDecimal(nominalField.getText()).add(new BigDecimal(upperField.getText()))
						.toPlainString());
			}
		} catch (Exception e) {

		}
		valueChanged();
	}

	private void updateUpperLower() {
		try {
			if (!nominalField.getText().equals("") && !minField.getText().equals("")) {
				lowerField.setText(new BigDecimal(minField.getText()).subtract(new BigDecimal(nominalField.getText()))
						.toPlainString());
			}

			if (!nominalField.getText().equals("") && !maxField.getText().equals("")) {
				upperField.setText(new BigDecimal(maxField.getText()).subtract(new BigDecimal(nominalField.getText()))
						.toPlainString());
			}
		} catch (Exception e) {

		}
		valueChanged();
	}

	private void valueChanged() {
		try {
			if (!maxField.getText().equals("") && !minField.getText().equals("")) {
				BigDecimal max = new BigDecimal(maxField.getText());
				BigDecimal min = new BigDecimal(minField.getText());
				BigDecimal value = new BigDecimal(valueField.getText());
				boolean result = max.compareTo(value) >= 0 && min.compareTo(value) <= 0;
				valueField.setBorder(BorderFactory.createLineBorder(result ? Color.GREEN : Color.RED));
				valueField.repaint();
			}
		} catch (Exception e) {

		}
	}

	private boolean checkForValue(JFormattedTextField field) {
		if (field.getText() == null || field.getText().equals("")) {
			field.setBorder(BorderFactory.createLineBorder(Color.RED));
			return false;
		} else {
			field.setBorder(UIManager.getBorder("TextField.border"));
			return true;
		}
	}

	public void close() {
		dispose();
		setVisible(false);
	}

}
