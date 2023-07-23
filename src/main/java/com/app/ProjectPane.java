package com.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
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
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gfx.Point;
import com.gfx.Rectangle;
import com.gfx.Renderable;
import com.gfx.Renderer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class ProjectPane extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, Renderable {

	private BufferedImage plan;
	private Vector<Measure> measures;
	private JTable table;
	private Measure highlighted;

	private Renderer renderer;

	private float zoom = 1;
	private Point cam = new Point(0, 0);
	private Point clip;
	private Rectangle clipRect;
	private JTextField customerField;
	private JTextField partNumberField;
	private JTextField dateField;
	private JTextField manufactuField;
	private JTextField quantityField;
	private JTextField referenceField;
	private JTextField indiceField;
	private JSpinner scaleField;
	private App app;

	public ProjectPane(App app) {
		super();
		this.app = app;
		measures = new Vector<>();
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

		partNumberField = new JTextField();
		partNumberField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(partNumberField, gbc);

		JLabel customerLabel = new JLabel("Customer Name");
		customerLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(customerLabel, gbc);

		customerField = new JTextField();
		customerField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(customerField, gbc);

		JLabel dateLabel = new JLabel("Date");
		customerLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(dateLabel, gbc);

		dateField = new JTextField();
		dateField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(dateField, gbc);

		JLabel manufactuLabel = new JLabel("Manufacturing Order");
		manufactuLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(manufactuLabel, gbc);

		manufactuField = new JTextField();
		manufactuField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(manufactuField, gbc);

		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(quantityLabel, gbc);

		quantityField = new JTextField();
		quantityField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(quantityField, gbc);

		JLabel referenceLabel = new JLabel("Reference");
		referenceLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(referenceLabel, gbc);

		referenceField = new JTextField();
		referenceField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(referenceField, gbc);

		JLabel indiceLabel = new JLabel("Indice");
		indiceLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(indiceLabel, gbc);

		indiceField = new JTextField();
		indiceField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(indiceField, gbc);

		JLabel scaleLabel = new JLabel("scale");
		scaleLabel.setPreferredSize(new Dimension(150, 25));
		gbc.gridx = 0;
		gbc.gridy++;
		infos.add(scaleLabel, gbc);

		scaleField = new JSpinner(new SpinnerNumberModel(1, 0.01, 1000, 0.01));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(scaleField, "0.00");
		DecimalFormat format = editor.getFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		scaleField.setEditor(editor);
		scaleField.setPreferredSize(new Dimension(325, 25));
		gbc.gridx++;
		infos.add(scaleField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.BOTH;
		left.add(infos, gbc);

		JPanel measurePanel = new JPanel();
		measurePanel.setLayout(new GridBagLayout());
		measurePanel.setBorder(BorderFactory.createTitledBorder("Measures"));
		table = new JTable(new MeasureTableModel(this));
		table.setDefaultRenderer(MeasureParam.class, new MeasureTableRenderer(app));
		table.setFillsViewportHeight(true);
		table.setTransferHandler(new MeasureTransferHandler(this));
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		ProjectPane current = this;
		table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e) && table.getSelectedRowCount() == 1) {
					Measure target = measures.get(table.getSelectedRow());
					MeasureDialog dialog = new MeasureDialog(app, current, target);
					dialog.setVisible(true);
				}
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		measurePanel.add(new JScrollPane(table), gbc);

		JButton addMeasure = new JButton("Add Measure");
		addMeasure.setIcon(IconAtlas.getIcon("add", 32));
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
		removeMeasure.setEnabled(false);
		removeMeasure.setIcon(IconAtlas.getIcon("remove", 32));
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
		saveAsJson.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("Json Files", ".json"));
			int choice = chooser.showDialog(app.getFrame(), "Save");
			if (choice == JFileChooser.APPROVE_OPTION) {
				File target = chooser.getSelectedFile();
				if (!target.getAbsolutePath().endsWith(".json")) {
					target = new File(target.getAbsolutePath() + ".json");
				}
				toJson(target, partNumberField.getText(), customerField.getText(), dateField.getText(),
						manufactuField.getText(), quantityField.getText(), referenceField.getText(),
						indiceField.getText());
			}
		});
		gbc.gridy = 0;
		export.add(saveAsJson, gbc);

		JButton exportToExcel = new JButton("Export to Excel");
		exportToExcel.setIcon(IconAtlas.getIcon("excel", 32));
		exportToExcel.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel files", ".xlsx"));
			int choice = chooser.showDialog(app.getFrame(), "Save");
			if (choice == JFileChooser.APPROVE_OPTION) {
				File target = chooser.getSelectedFile();
				if (!target.getAbsolutePath().endsWith(".xlsx")) {
					target = new File(target.getAbsolutePath() + ".xlsx");
				}
				toExcel(target, partNumberField.getText(), customerField.getText(), dateField.getText(),
						manufactuField.getText(), quantityField.getText(), referenceField.getText(),
						indiceField.getText());
			}
		});
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

		float scale = ((Number) scaleField.getValue()).floatValue();
		renderer.deriveFont(scale * 16);
		for (int i : table.getSelectedRows()) {
			Measure target = measures.get(i);
			renderer.drawRect((int) (target.pos.x - 8 * scale), (int) (target.pos.y - 8 * scale), (int) (16 * scale),
					(int) (16 * scale), Color.MAGENTA);
		}
		Enumeration<Measure> temp = measures.elements();
		while (temp.hasMoreElements()) {
			Measure measure = temp.nextElement();
			boolean isOk = measure.isOk();
			renderer.drawCenteredString(measure.id + "", (int) measure.pos.x, (int) measure.pos.y, Color.BLACK);
			renderer.drawImage(isOk ? IconAtlas.icons.get("good") : IconAtlas.icons.get("bad"),
					(int) (measure.pos.x + 15 * scale), (int) (measure.pos.y - 8 * scale), (int) (16 * scale),
					(int) (16 * scale));
			renderer.drawRect((int) (measure.pos.x - 10 * scale), (int) (measure.pos.y - 10 * scale),
					(int) (20 * scale), (int) (20 * scale), isOk ? Color.GREEN : Color.RED);
		}
		if (highlighted != null) {
			renderer.drawRect((int) (highlighted.pos.x - 10 * scale), (int) (highlighted.pos.y - 10 * scale),
					(int) (20 * scale), (int) (20 * scale), Color.BLUE);
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
		if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && highlighted != null) {
			MeasureDialog dialog = new MeasureDialog(app, this, highlighted);
			dialog.setVisible(true);
		}
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
				clipRect = null;
				clip = null;
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

	public void toExcel(File target, String partNumber, String customerName, String date, String manu, String quantity,
			String reference, String indice) {
		Workbook wb = new XSSFWorkbook();
		Sheet main = wb.createSheet("Plan");
		// Setting column sizes
		// Styles

		CellStyle infoStyle = wb.createCellStyle();
		infoStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		infoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		infoStyle.setBorderTop(BorderStyle.THIN);
		infoStyle.setBorderBottom(BorderStyle.THIN);
		infoStyle.setBorderLeft(BorderStyle.THIN);
		infoStyle.setBorderRight(BorderStyle.THIN);
		infoStyle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle goodStyle = wb.createCellStyle();
		goodStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		goodStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		goodStyle.setBorderTop(BorderStyle.THIN);
		goodStyle.setBorderBottom(BorderStyle.THIN);
		goodStyle.setBorderLeft(BorderStyle.THIN);
		goodStyle.setBorderRight(BorderStyle.THIN);
		goodStyle.setAlignment(HorizontalAlignment.CENTER);

		CellStyle badStyle = wb.createCellStyle();
		badStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		badStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		badStyle.setBorderTop(BorderStyle.THIN);
		badStyle.setBorderBottom(BorderStyle.THIN);
		badStyle.setBorderLeft(BorderStyle.THIN);
		badStyle.setBorderRight(BorderStyle.THIN);
		badStyle.setAlignment(HorizontalAlignment.CENTER);

		XSSFFont font = ((XSSFWorkbook) wb).createFont();
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 11);
		font.setBold(true);
		infoStyle.setFont(font);

		//// Creating the info table

		Row partNumberRow = main.createRow(1);
		createInfoCell(1, 2, partNumberRow, main, wb, "HYPERION PART NUMBER:").setCellStyle(infoStyle);
		createInfoCell(3, 6, partNumberRow, main, wb, partNumber).setCellStyle(infoStyle);

		Row customerNameRow = main.createRow(2);
		createInfoCell(1, 2, customerNameRow, main, wb, "CUSTOMER NAME:").setCellStyle(infoStyle);
		createInfoCell(3, 6, customerNameRow, main, wb, customerName).setCellStyle(infoStyle);

		Row dateRow = main.createRow(3);
		createInfoCell(1, 2, dateRow, main, wb, "DATE:").setCellStyle(infoStyle);
		createInfoCell(3, 6, dateRow, main, wb, date).setCellStyle(infoStyle);

		Row manuRow = main.createRow(4);
		createInfoCell(1, 2, manuRow, main, wb, "MANUFACTURING ORDER:").setCellStyle(infoStyle);
		createInfoCell(3, 6, manuRow, main, wb, manu).setCellStyle(infoStyle);

		Row quantityRow = main.createRow(5);
		createInfoCell(1, 2, quantityRow, main, wb, "QUANTITY:").setCellStyle(infoStyle);
		createInfoCell(3, 6, quantityRow, main, wb, quantity).setCellStyle(infoStyle);

		Row titleRow = main.createRow(7);
		createInfoCell(1, 9, titleRow, main, wb, "DIMENSIONAL CERTIFICATE").setCellStyle(infoStyle);

		Cell referenceTitle = manuRow.createCell(8);
		referenceTitle.setCellValue("REFERENCE:");
		referenceTitle.setCellStyle(infoStyle);
		Cell referenceVal = manuRow.createCell(9);
		referenceVal.setCellValue(reference);
		referenceVal.setCellStyle(infoStyle);

		Cell indiceTitle = quantityRow.createCell(8);
		indiceTitle.setCellValue("Indice:");
		indiceTitle.setCellStyle(infoStyle);
		Cell indiceVal = quantityRow.createCell(9);
		indiceVal.setCellValue(indice);
		indiceVal.setCellStyle(infoStyle);

		// Adding annotated plan
		if (plan != null) {
			try {
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ImageIO.write(drawMeasures(plan), "png", byteOut);
				byte[] data = byteOut.toByteArray();
				int planIndex = wb.addPicture(data, Workbook.PICTURE_TYPE_PNG);
				XSSFDrawing drawing = (XSSFDrawing) main.createDrawingPatriarch();
				XSSFClientAnchor planAnchor = new XSSFClientAnchor();
				planAnchor.setRow1(9);
				planAnchor.setCol1(1);
				planAnchor.setRow2(42);
				planAnchor.setCol2(10);
				drawing.createPicture(planAnchor, planIndex);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// Drawing measures
		font.setBold(false);
		infoStyle.setFont(font);

		Row measureTitles = main.createRow(58);
		String[] names = new String[] { "Measure", "Description", "Nominal", "Lower Tolerance", "Upper Tolerance",
				"Min", "Max", "Value" };
		for (int i = 0; i < names.length; i++) {
			Cell title = measureTitles.createCell(1 + i);
			title.setCellValue(names[i]);
			title.setCellStyle(infoStyle);
		}

		for (Measure measure : measures) {
			Row row = main.createRow(58 + measure.id);
			Cell id = row.createCell(1);
			id.setCellValue(measure.id);
			id.setCellStyle(measure.isOk() ? goodStyle : badStyle);
			for (int i = 1; i < 8; i++) {
				MeasureParam param = new MeasureParam(measure, i);
				Cell cell = row.createCell(i + 1);
				cell.setCellValue(param.toStringHypFormat());
				cell.setCellStyle(infoStyle);
			}
		}

		// Auto size
		for (int i = 0; i < 10; i++) {
			main.setColumnWidth(i, 4000);
		}

		// Saving work to the file

		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
			wb.write(out);
			wb.close();
			out.close();
			System.out.println("Export done!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Cell createInfoCell(int firstCol, int lastCol, Row row, Sheet sheet, Workbook wb, String value,
			BorderStyle border) {
		CellRangeAddress range = new CellRangeAddress(row.getRowNum(), row.getRowNum(), firstCol, lastCol);
		sheet.addMergedRegion(range);
		Cell cell = CellUtil.createCell(row, firstCol, value);
		RegionUtil.setBorderTop(border, range, sheet);
		RegionUtil.setBorderLeft(border, range, sheet);
		RegionUtil.setBorderBottom(border, range, sheet);
		RegionUtil.setBorderRight(border, range, sheet);
		return cell;
	}

	private Cell createInfoCell(int firstCol, int lastCol, Row row, Sheet sheet, Workbook wb, String value) {
		return createInfoCell(firstCol, lastCol, row, sheet, wb, value, BorderStyle.THIN);
	}

	private BufferedImage drawMeasures(BufferedImage in) {
		BufferedImage img = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.drawImage(in, 0, 0, img.getWidth(), img.getHeight(), null);
		g.translate(img.getWidth() >> 1, img.getHeight() >> 1);
		Enumeration<Measure> temp = measures.elements();
		float scale = ((Number) scaleField.getValue()).floatValue();
		g.setFont(g.getFont().deriveFont(16 * scale));
		while (temp.hasMoreElements()) {
			Measure target = temp.nextElement();
			boolean isOk = target.isOk();

			g.setColor(isOk ? Color.GREEN : Color.RED);
			String s = target.id + "";
			FontMetrics metrics = g.getFontMetrics();
			g.drawString(s, (int) target.pos.x - (metrics.stringWidth(s) >> 1),
					(int) target.pos.y + (metrics.getAscent() - ((metrics.getAscent() + metrics.getDescent()) >> 1)));
			g.drawImage(isOk ? IconAtlas.icons.get("good") : IconAtlas.icons.get("bad"),
					(int) (target.pos.x + 8 * scale), (int) (target.pos.y - 8 * scale), (int) (16 * scale),
					(int) (16 * scale), null);
		}
		return img;
	}

	private void toJson(File target, String partNumber, String customerName, String date, String manu, String quantity,
			String reference, String indice) {
		JsonObject res = new JsonObject();
		res.addProperty("partNumber", partNumber);
		res.addProperty("customerName", customerName);
		res.addProperty("date", date);
		res.addProperty("manufact", manu);
		res.addProperty("quantity", quantity);
		res.addProperty("reference", reference);
		res.addProperty("indice", indice);
		res.addProperty("scale", ((Number) scaleField.getValue()).floatValue());

		JsonArray array = new JsonArray();
		for (Measure m : measures) {
			array.add(m.toJson());
		}
		res.add("measures", array);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(target));
			writer.write(res.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setValuesForFields(String partNumber, String customerName, String date, String manu, String quantity,
			String reference, String indice, float scale) {
		partNumberField.setText(partNumber);
		customerField.setText(customerName);
		dateField.setText(date);
		manufactuField.setText(manu);
		quantityField.setText(quantity);
		referenceField.setText(reference);
		indiceField.setText(indice);
		scaleField.setValue(scale);
	}
}