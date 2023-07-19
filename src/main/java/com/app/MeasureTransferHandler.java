package com.app;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

@SuppressWarnings("serial")
public class MeasureTransferHandler extends TransferHandler {

	public static final DataFlavor MEASURE_FLAVOR = new DataFlavor(Measure.class, "Measure Data Flavor");

	private ProjectPane project;

	public MeasureTransferHandler(ProjectPane project) {
		super();
		this.project = project;
	}

	@Override
	public Transferable createTransferable(JComponent c) {
		if (c instanceof JTable table) {
			List<Measure> data = Arrays.stream(table.getSelectedRows()).mapToObj(i -> project.getMeasures().get(i))
					.toList();
			return new MeasureTransferable(data);
		}
		return null;
	}

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.MOVE;
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		return support.isDataFlavorSupported(MEASURE_FLAVOR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}
		try {
			Transferable transferable = support.getTransferable();
			JTable.DropLocation location = (JTable.DropLocation) support.getDropLocation();
			List<Measure> data = (List<Measure>) transferable.getTransferData(MEASURE_FLAVOR);
			List<Measure> other = new Vector<>(project.getMeasures());
			int count = 0;
			for (Measure measure : data) {
				if (measure.id < location.getRow()) {
					count++;
				}
			}
			count = location.getRow() - count;
			other.removeAll(data);
			other.addAll(count, data);
			project.setMeasures(other);
			project.refreshMeasures();
			return true;
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private class MeasureTransferable implements Transferable {

		private List<Measure> data;

		public MeasureTransferable(List<Measure> data) {
			this.data = data;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { MEASURE_FLAVOR };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return MEASURE_FLAVOR.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (isDataFlavorSupported(flavor))
				return data;
			return null;
		}
	}

}