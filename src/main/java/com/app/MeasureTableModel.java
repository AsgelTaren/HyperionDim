package com.app;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class MeasureTableModel extends AbstractTableModel {

	private ProjectPane project;

	public MeasureTableModel(ProjectPane project) {
		this.project = project;
	}

	@Override
	public int getRowCount() {
		return project.getMeasures().size();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return new MeasureParam(project.getMeasures().get(rowIndex), columnIndex);
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return MeasureParam.class;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "ID";
		case 1:
			return "Description";
		case 2:
			return "Nominal";
		case 3:
			return "Lower";
		case 4:
			return "Upper";
		case 5:
			return "Value";
		}
		return null;
	}

}
