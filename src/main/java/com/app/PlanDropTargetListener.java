package com.app;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PlanDropTargetListener implements DropTargetListener {

	private ProjectPane project;

	public PlanDropTargetListener(ProjectPane project) {
		this.project = project;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	@Override
	public void dragExit(DropTargetEvent dte) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void drop(DropTargetDropEvent dtde) {
		if (!dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.rejectDrop();
			return;
		}
		dtde.acceptDrop(dtde.getDropAction());
		Transferable transferable = dtde.getTransferable();
		try {
			List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
			for (File file : files) {
				if (file.getName().endsWith(".png")) {
					BufferedImage img = ImageIO.read(file);
					project.setPlan(img);
				}
				if (file.getName().endsWith("json")) {
					JsonObject data = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
					project.setValuesForFields(data.get("partNumber").getAsString(),
							data.get("customerName").getAsString(), data.get("date").getAsString(),
							data.get("manufact").getAsString(), data.get("quantity").getAsString(),
							data.get("reference").getAsString(), data.get("indice").getAsString());
					JsonArray array = data.get("measures").getAsJsonArray();
					Vector<Measure> measures = new Vector<>(array.size());
					for (int i = 0; i < array.size(); i++) {
						measures.add(new Measure(array.get(i).getAsJsonObject()));
					}
					project.setMeasures(measures);
					project.refreshMeasures();
				}
			}
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}

	}

}
