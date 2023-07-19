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
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

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
			if (files.size() > 1) {
				return;
			}
			BufferedImage img = ImageIO.read(files.get(0));
			project.setPlan(img);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}

	}

}
