package com.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.gfx.Point;
import com.gfx.Rectangle;
import com.gfx.Renderable;
import com.gfx.Renderer;

@SuppressWarnings("serial")
public class ProjectPane extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, Renderable {

	private BufferedImage plan;
	private Vector<Measure> measures;
	private JTable table;
	private MeasureTableModel model;
	private JFrame frame;
	private Measure highlighted;

	private Renderer renderer;

	private float zoom = 1;
	private Point cam = new Point(0, 0);
	private Point clip;
	private Rectangle clipRect;

	public ProjectPane(JFrame frame) {
		super();
		this.frame = frame;
		measures = new Vector<>();
		for (int i = 1; i <= 15; i++) {
			measures.add(new Measure(i, "Rayon", new BigDecimal(2 * i + 1), new BigDecimal(-0.15f),
					new BigDecimal(0.15f), new BigDecimal(2 * i + 1)));
		}
		setLayout(new GridBagLayout());

		JPanel left = new JPanel();
		left.setLayout(new GridBagLayout());

		JPanel infos = new JPanel();
		infos.setLayout(new GridBagLayout());
		infos.setBorder(BorderFactory.createTitledBorder("Product informations"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.LINE_START;

		JLabel partNumberLabel = new JLabel("Hyperion Part Number");
		partNumberLabel.setPreferredSize(new Dimension(150, 25));
		infos.add(partNumberLabel, gbc);

		JTextField partNumberField = new JTextField();
		partNumberField.setPreferredSize(new Dimension(300, 25));
		gbc.gridx++;
		infos.add(partNumberField, gbc);

		JLabel customerLabel = new JLabel("Customer Name");
		customerLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(customerLabel, gbc);

		JTextField customerField = new JTextField();
		customerField.setPreferredSize(new Dimension(300, 25));
		gbc.gridx++;
		infos.add(customerField, gbc);

		JLabel dateLabel = new JLabel("Date");
		customerLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(dateLabel, gbc);

		JTextField dateField = new JTextField();
		dateField.setPreferredSize(new Dimension(300, 25));
		gbc.gridx++;
		infos.add(dateField, gbc);

		JLabel manufactuLabel = new JLabel("Manufacturing Order");
		manufactuLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(manufactuLabel, gbc);

		JTextField manufactuField = new JTextField();
		manufactuField.setPreferredSize(new Dimension(300, 25));
		gbc.gridx++;
		infos.add(manufactuField, gbc);

		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(quantityLabel, gbc);

		JTextField quantityField = new JTextField();
		quantityField.setPreferredSize(new Dimension(300, 25));
		gbc.gridx++;
		infos.add(quantityField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		left.add(infos, gbc);

		JPanel measurePanel = new JPanel();
		measurePanel.setLayout(new GridBagLayout());
		measurePanel.setBorder(BorderFactory.createTitledBorder("Measures"));
		table = new JTable(model = new MeasureTableModel(this));
		table.setDefaultRenderer(MeasureParam.class, new MeasureTableRenderer());
		table.setFillsViewportHeight(true);
		table.setTransferHandler(new MeasureTransferHandler(this));
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		ProjectPane current = this;
		table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e) && table.getSelectedRowCount() == 1) {
					Measure target = measures.get(table.getSelectedRow());
					MeasureDialog dialog = new MeasureDialog(frame, current, target);
					dialog.setVisible(true);
				}
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		measurePanel.add(new JScrollPane(table), gbc);

		JButton addMeasure = new JButton("Add Measure");
		addMeasure.addActionListener(e -> {
			int max = measures.stream().map(measure -> measure.id).max(Integer::compare).orElseGet(() -> 0);
			measures.add(new Measure(max + 1, "", new BigDecimal(0), new BigDecimal(0), new BigDecimal(0),
					new BigDecimal(0)));
			table.revalidate();
			table.repaint();
		});
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		measurePanel.add(addMeasure, gbc);

		JButton removeMeasure = new JButton("Remove Measures");
		removeMeasure.addActionListener(e -> {
			int choice = JOptionPane.showConfirmDialog(this, "Do you really want to remove the currently "
					+ table.getSelectedRows().length + " selected measures?", "Warning", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				List<Measure> toRemove = Arrays.stream(table.getSelectedRows()).mapToObj(i -> measures.get(i)).toList();
				measures.removeAll(toRemove);
				reworkMeasuresID();
				table.revalidate();
				table.repaint();
				table.clearSelection();
				removeMeasure.setEnabled(table.getSelectedRows().length > 0);
			}
		});
		table.getSelectionModel()
				.addListSelectionListener(e -> removeMeasure.setEnabled(table.getSelectedRows().length > 0));
		gbc.gridx++;
		gbc.weightx = 1;
		measurePanel.add(removeMeasure, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		left.add(measurePanel, gbc);

		JPanel export = new JPanel();
		export.setBorder(BorderFactory.createTitledBorder("Export and saves"));
		export.setLayout(new GridBagLayout());

		JButton saveAsJson = new JButton("Save as Json");
		saveAsJson.setIcon(IconAtlas.getIcon("json", 32));
		gbc.gridy = 0;
		export.add(saveAsJson, gbc);

		JButton exportToExcel = new JButton("Export to Excel");
		exportToExcel.setIcon(IconAtlas.getIcon("excel", 32));
		gbc.gridx++;
		export.add(exportToExcel, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		left.add(export, gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		add(left, gbc);

		renderer = new Renderer();
		renderer.addRenderable(this);
		gbc.gridx++;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(renderer, gbc);

		try {
			DropTarget dropTarget = new DropTarget();
			dropTarget.addDropTargetListener(new PlanDropTargetListener(this));
			renderer.setDropTarget(dropTarget);
		} catch (TooManyListenersException e1) {
			e1.printStackTrace();
		}

		renderer.addMouseListener(this);
		renderer.addMouseMotionListener(this);
		renderer.addMouseWheelListener(this);
	}

	@Override
	public void render(Renderer renderer) {
		if (mouseIn != null)
			renderer.drawString(String.format("%.2f", mouseIn.x) + " " + String.format("%.2f", mouseIn.y), 20, 20,
					Color.BLACK);
		renderer.center();

		renderer.scale(zoom);
		renderer.translate(-cam.x, -cam.y);

		renderer.push();
		if (plan != null) {
			renderer.translate(-plan.getWidth() >> 1, -plan.getHeight() >> 1);
			renderer.drawImage(plan, 0, 0, plan.getWidth(), plan.getHeight());
		}
		renderer.pop();
		for (int i : table.getSelectedRows()) {
			Measure target = measures.get(i);
			renderer.drawRect((int) target.pos.x - 8, (int) target.pos.y - 8, 16, 16, Color.MAGENTA);
		}
		Enumeration<Measure> temp = measures.elements();
		while (temp.hasMoreElements()) {
			Measure measure = temp.nextElement();
			boolean isOk = measure.isOk();
			renderer.drawCenteredString(measure.id + "", (int) measure.pos.x, (int) measure.pos.y,
					isOk ? Color.green : Color.RED);
			renderer.drawImage(isOk ? IconAtlas.icons.get("good") : IconAtlas.icons.get("bad"), (int) measure.pos.x + 8,
					(int) measure.pos.y - 8, 16, 16);
		}
		if (highlighted != null) {
			renderer.drawRect((int) highlighted.pos.x - 10, (int) highlighted.pos.y - 10, 20, 20, Color.RED);
		}

		if (clipRect != null) {
			renderer.drawRect((int) clipRect.x, (int) clipRect.y, (int) clipRect.width, (int) clipRect.height,
					Color.BLUE);
		}

		// Drawing axis
		renderer.drawLine(0, 0, 40, 0, Color.red);
		renderer.drawLine(0, 0, 0, 40, Color.green);
	}

	public Vector<Measure> getMeasures() {
		return measures;
	}

	public void setMeasures(List<Measure> measures) {
		this.measures = new Vector<>(measures);
	}

	public void reworkMeasuresID() {
		for (int i = 0; i < measures.size(); i++) {
			measures.get(i).id = i + 1;
		}
	}

	public void refreshMeasures() {
		reworkMeasuresID();
		table.revalidate();
		table.repaint();
		table.clearSelection();
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	private Point delta, mouse, mouseIn;
	private Point[] deltas;

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		mouseIn = fromMouseToModel(mouse);
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (mouseIn != null && cam != null && highlighted != null && clip == null) {
				System.out.println(table.getSelectedRowCount());
				for (int i = 0; i < table.getSelectedRowCount(); i++) {
					measures.get(table.getSelectedRows()[i]).pos = mouseIn.add(deltas[i]);
				}
			}
			if (clip != null) {
				clipRect = new Rectangle(clip, mouseIn);
			}
		}
		if (SwingUtilities.isMiddleMouseButton(e)) {
			if (delta != null && mouse != null && cam != null) {
				cam = cam.sub(mouse.sub(delta).scale(1.0f / zoom));
				delta = mouse;
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		mouseIn = fromMouseToModel(mouse);
		highlighted = null;
		Enumeration<Measure> temp = measures.elements();
		while (temp.hasMoreElements()) {
			Measure measure = temp.nextElement();
			if (measure.contains(mouseIn)) {
				highlighted = measure;
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		mouseIn = fromMouseToModel(mouse);
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (highlighted != null) {
				if (!e.isControlDown() && !table.isRowSelected(highlighted.id - 1)) {
					table.clearSelection();
				}
				table.addRowSelectionInterval(highlighted.id - 1, highlighted.id - 1);
				deltas = new Point[table.getSelectedRowCount()];
				for (int i = 0; i < table.getSelectedRowCount(); i++) {
					deltas[i] = measures.get(table.getSelectedRows()[i]).pos.sub(mouseIn);
				}
			} else {
				clip = mouseIn;
			}
		}
		if (SwingUtilities.isMiddleMouseButton(e)) {
			delta = mouse;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (clipRect != null) {
				Enumeration<Measure> temp = measures.elements();
				ArrayList<Measure> selection = new ArrayList<Measure>(measures.size());
				while (temp.hasMoreElements()) {
					Measure target = temp.nextElement();
					if (clipRect.contains(target.pos)) {
						selection.add(target);
					}
				}
				table.clearSelection();
				for (Measure measure : selection) {
					table.addRowSelectionInterval(measure.id - 1, measure.id - 1);
				}
				clipRect = null;
				clip = null;
			} else if (highlighted == null) {
				table.clearSelection();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		float oldzoom = zoom;
		zoom *= Math.pow(1.08, e.getWheelRotation());
	}

	public synchronized void start() {
		renderer.start();
	}

	public synchronized void stop() {
		renderer.stop();
	}

	public void setPlan(BufferedImage img) {
		this.plan = img;
	}

	public Point fromMouseToModel(Point mouse) {
		return mouse.sub(new Point(renderer.getWidth() >> 1, renderer.getHeight() >> 1)).scale(1.0f / zoom).add(cam);
	}

}