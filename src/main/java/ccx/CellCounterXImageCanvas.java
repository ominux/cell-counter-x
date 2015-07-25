package ccx;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Roi;
import ij.gui.Toolbar;
import ij.process.ImageProcessor;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;

/**
 * 
 * @author Kurt De Vos
 */
public class CellCounterXImageCanvas extends ImageCanvas {
	private Vector<CellCounterXMarkerVector> typeVector;
	private CellCounterXMarkerVector currentMarkerVector;
	private CellCounterXGUI cc;
	private ImagePlus img;
	private boolean delmode = false;
	private boolean showNumbers = true;
	private boolean showAll = false;
	private Font font = new Font("SansSerif", Font.PLAIN, 10);

	private ToolTipDialog ttDlg = new ToolTipDialog();

	/** Creates a new instance of CellCntrImageCanvas */
	public CellCounterXImageCanvas(ImagePlus img, Vector typeVector,
			CellCounterXGUI cc, Vector displayList) {
		super(img);
		this.img = img;
		this.typeVector = typeVector;
		this.cc = cc;
		if (displayList != null)
			this.setDisplayList(displayList);

		// TOOLTIP info
		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	public void mousePressed(MouseEvent e) {
		if (IJ.spaceBarDown() || Toolbar.getToolId() == Toolbar.MAGNIFIER
				|| Toolbar.getToolId() == Toolbar.HAND) {
			super.mousePressed(e);
			return;
		}

		if (currentMarkerVector == null) {
			IJ.error("Select a counter type first!");
			return;
		}

		int x = super.offScreenX(e.getX());
		int y = super.offScreenY(e.getY());
		if (!delmode) {
			CellCounterXMarker m = new CellCounterXMarker(x, y,
					img.getCurrentSlice());
			currentMarkerVector.addMarker(m);
		} else {
			CellCounterXMarker m = currentMarkerVector.getMarkerFromPosition(
					new Point(x, y), img.getCurrentSlice());
			currentMarkerVector.remove(m);
		}
		repaint();
		cc.populateTxtFields();
	}

	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
	}

	public void mouseMoved(MouseEvent e) {
		super.mouseMoved(e);

		// ################################## MOUSE OVER
		// get the mappings from the GUI
		JLabel label = cc.getHiddenToolTipLabel();
		ArrayList<Mapping> mappings = cc.getMapping();

		int x = super.offScreenX(e.getX());
		int y = super.offScreenY(e.getY());

		boolean valueFound = false;

		// search all vectors and their elements for matching points
		// get each vector
		for (CellCounterXMarkerVector ccMV : typeVector) {
			// get each marker item
			for (Object mkr : ccMV) {
				CellCounterXMarker marker = (CellCounterXMarker) mkr;

				// compare the marker's x|y entries to the current x|y positions
				// design a rectangle 4x4
				Rectangle2D rect = new Rectangle(x - 3, y - 3, 6, 6);

				Point markerPoint = new Point(marker.getX(), marker.getY());

				if (rect.contains(markerPoint)) {
//					System.out.println("Mouse Position: x=" + x + ", y=" + y);
//					System.out.println("getX()=" + e.getX() + ", getY()="
//							+ e.getY());
//					System.out.println("getXOnScreen()=" + e.getXOnScreen()
//							+ ", getYOnScreen()=" + e.getYOnScreen());
//					// System.out.println("Mouse moved over marker type: " +
//					// ccMV.getType());
//					System.out
//							.println("Label position: " + label.getLocation());
//					System.out.println("Label position on screen: "
//							+ label.getLocationOnScreen());

					int mappingIndex = -1;
					for (int idx = 0; idx < mappings.size(); idx++) {
						// search for the object
						Mapping m = mappings.get(idx);
						if (m.getMarkerType().equals(
								String.valueOf(ccMV.getType()))) {
							mappingIndex = idx;
							break;
						}
					}
					String text;
					if (mappingIndex != -1) {
						text = mappings.get(mappingIndex).getValue() + " ("
								+ ccMV.getType() + ")";
					} else {
						text = CellCounterXGUI.DEFAULT_TTP;
					}

					int posX = e.getXOnScreen();
					int posY = e.getYOnScreen();

					label.setToolTipText(text);
					ttDlg.getLblToolTip().setText(text);
					ttDlg.validate();
					ttDlg.pack();
					ttDlg.setLocation(posX+10, posY+10);
					ttDlg.setVisible(true);

//					ToolTipManager.sharedInstance()
//							.mouseMoved(
//									new MouseEvent(ttDlg.getLblToolTip(), 0, 0, 0, posX, posY,
//											0, false));
					valueFound = true;
					break;
				}
			}
			if (valueFound)
				break;
			else
				ttDlg.setVisible(false);
		}
	}

	public void mouseExited(MouseEvent e) {
		super.mouseExited(e);
	}

	public void mouseEntered(MouseEvent e) {
		super.mouseEntered(e);
		if (!IJ.spaceBarDown() | Toolbar.getToolId() != Toolbar.MAGNIFIER
				| Toolbar.getToolId() != Toolbar.HAND)
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}

	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
	}

	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
	}

	private Point point;
	private Rectangle srcRect = new Rectangle(0, 0, 0, 0);

	public void paint(Graphics g) {
		super.paint(g);
		srcRect = getSrcRect();
		Roi roi = img.getRoi();
		double xM = 0;
		double yM = 0;

		/*
		 * double magnification = super.getMagnification();
		 * 
		 * try { if (imageUpdated) { imageUpdated = false; img.updateImage(); }
		 * Image image = img.getImage(); if (image!=null) g.drawImage(image, 0,
		 * 0, (int)(srcRect.width*magnification),
		 * (int)(srcRect.height*magnification), srcRect.x, srcRect.y,
		 * srcRect.x+srcRect.width, srcRect.y+srcRect.height, null); if (roi !=
		 * null) roi.draw(g); } catch(OutOfMemoryError e) {
		 * IJ.outOfMemory("Paint "+e.getMessage()); }
		 */

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1f));
		g2.setFont(font);

		ListIterator it = typeVector.listIterator();
		while (it.hasNext()) {
			CellCounterXMarkerVector mv = (CellCounterXMarkerVector) it.next();
			int typeID = mv.getType();
			g2.setColor(mv.getColor());
			ListIterator mit = mv.listIterator();
			while (mit.hasNext()) {
				CellCounterXMarker m = (CellCounterXMarker) mit.next();
				boolean sameSlice = m.getZ() == img.getCurrentSlice();
				if (sameSlice || showAll) {
					xM = ((m.getX() - srcRect.x) * magnification);
					yM = ((m.getY() - srcRect.y) * magnification);
					if (sameSlice)
						g2.fillOval((int) xM - 2, (int) yM - 2, 4, 4);
					else
						g2.drawOval((int) xM - 2, (int) yM - 2, 4, 4);
					if (showNumbers)
						g2.drawString(Integer.toString(typeID), (int) xM + 3,
								(int) yM - 3);
				}
			}
		}
	}

	public void removeLastMarker() {
		currentMarkerVector.removeLastMarker();
		repaint();
		cc.populateTxtFields();
	}

	public ImagePlus imageWithMarkers() {
		Image image = this.createImage(img.getWidth(), img.getHeight());
		Graphics gr = image.getGraphics();

		double xM = 0;
		double yM = 0;

		try {
			if (imageUpdated) {
				imageUpdated = false;
				img.updateImage();
			}
			Image image2 = img.getImage();
			if (image != null)
				gr.drawImage(image2, 0, 0, img.getWidth(), img.getHeight(),
						null);
		} catch (OutOfMemoryError e) {
			IJ.outOfMemory("Paint " + e.getMessage());
		}

		Graphics2D g2r = (Graphics2D) gr;
		g2r.setStroke(new BasicStroke(1f));

		ListIterator it = typeVector.listIterator();
		while (it.hasNext()) {
			CellCounterXMarkerVector mv = (CellCounterXMarkerVector) it.next();
			int typeID = mv.getType();
			g2r.setColor(mv.getColor());
			ListIterator mit = mv.listIterator();
			while (mit.hasNext()) {
				CellCounterXMarker m = (CellCounterXMarker) mit.next();
				if (m.getZ() == img.getCurrentSlice()) {
					xM = m.getX();
					yM = m.getY();
					g2r.fillOval((int) xM - 2, (int) yM - 2, 4, 4);
					if (showNumbers)
						g2r.drawString(Integer.toString(typeID), (int) xM + 3,
								(int) yM - 3);
				}
			}
		}

		Vector displayList = getDisplayList();
		if (displayList != null && displayList.size() == 1) {
			Roi roi = (Roi) displayList.elementAt(0);
			if (roi.getType() == Roi.COMPOSITE)
				roi.draw(gr);
		}

		return new ImagePlus("Markers_" + img.getTitle(), image);
	}

	public void measure() {
		IJ.setColumnHeadings("Type\tSlice\tX\tY\tValue");
		for (int i = 1; i <= img.getStackSize(); i++) {
			img.setSlice(i);
			ImageProcessor ip = img.getProcessor();

			ListIterator it = typeVector.listIterator();
			while (it.hasNext()) {
				CellCounterXMarkerVector mv = (CellCounterXMarkerVector) it
						.next();
				int typeID = mv.getType();
				ListIterator mit = mv.listIterator();
				while (mit.hasNext()) {
					CellCounterXMarker m = (CellCounterXMarker) mit.next();
					if (m.getZ() == i) {
						int xM = m.getX();
						int yM = m.getY();
						int zM = m.getZ();
						double value = ip.getPixelValue(xM, yM);
						IJ.write(typeID + "\t" + zM + "\t" + xM + "\t" + yM
								+ "\t" + value);
					}
				}
			}
		}
	}

	public Vector getTypeVector() {
		return typeVector;
	}

	public void setTypeVector(Vector typeVector) {
		this.typeVector = typeVector;
	}

	public CellCounterXMarkerVector getCurrentMarkerVector() {
		return currentMarkerVector;
	}

	public void setCurrentMarkerVector(
			CellCounterXMarkerVector currentMarkerVector) {
		this.currentMarkerVector = currentMarkerVector;
	}

	public boolean isDelmode() {
		return delmode;
	}

	public void setDelmode(boolean delmode) {
		this.delmode = delmode;
	}

	public boolean isShowNumbers() {
		return showNumbers;
	}

	public void setShowNumbers(boolean showNumbers) {
		this.showNumbers = showNumbers;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

}
