package com.app;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class LoadingDialog extends JDialog {

	private JProgressBar bar;
	private JLabel label;

	public LoadingDialog(JFrame frame, String name, String text, int steps) {
		super(frame, name, false);

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.gridx = gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		label = new JLabel(text);
		add(label, gbc);

		bar = new JProgressBar(0, steps);
		gbc.gridy++;
		add(bar, gbc);

		pack();

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void setValue(int value) {
		bar.setValue(value);
	}

	public void setText(String text) {
		label.setText(text);
	}

	public void close() {
		setVisible(false);
		dispose();
	}
}
