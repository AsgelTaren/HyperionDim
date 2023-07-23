package com.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.NumberFormatter;

@SuppressWarnings("serial")
public class MeasureDialog extends JDialog {

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

		JFormattedTextField nominalField = new JFormattedTextField(formatter);
		nominalField.setText(measure.nominal.toPlainString());
		nominalField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(nominalField, gbc);

		JLabel valueLabel = new JLabel("Value");
		valueLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy = 2;
		gbc.gridx++;
		panel.add(valueLabel, gbc);

		JFormattedTextField valueField = new JFormattedTextField(formatter);
		valueField.setText(measure.value.toPlainString());
		valueField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(valueField, gbc);

		JLabel upperLabel = new JLabel("Upper");
		upperLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		gbc.gridx = 0;
		panel.add(upperLabel, gbc);

		JFormattedTextField upperField = new JFormattedTextField(formatter);
		upperField.setText(measure.upper.toPlainString());
		upperField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(upperField, gbc);

		JLabel lowerLabel = new JLabel("Lower");
		lowerLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridy = 4;
		gbc.gridx++;
		panel.add(lowerLabel, gbc);

		JFormattedTextField lowerField = new JFormattedTextField(formatter);
		lowerField.setText(measure.lower.toPlainString());
		lowerField.setPreferredSize(new Dimension(150, 25));
		gbc.gridy++;
		panel.add(lowerField, gbc);

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
