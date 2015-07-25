package ccx.io;

import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Vector;

import ccx.CellCounterXMarker;
import ccx.CellCounterXMarkerVector;
import ccx.Mapping;

/**
 * 
 * @author kurt, Philipp Kainz
 */
public class WriteXML {
	private OutputStream XMLFileOut;
	private OutputStream XMLBuffOut;
	private OutputStreamWriter out;

	/**
	 * Creates a new instance of ODWriteXMLODD
	 */
	public WriteXML(String XMLFilepath) {
		try {
			XMLFileOut = new FileOutputStream(XMLFilepath); // add FilePath
			XMLBuffOut = new BufferedOutputStream(XMLFileOut);
			out = new OutputStreamWriter(XMLBuffOut, "UTF-8");
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out
					.println("This VM does not support the UTF-8 character set. "
							+ e.getMessage());
		}
	}

	public boolean writeXML(String imgFilename, int width, int height,
			ArrayList<Mapping> mappings, Vector typeVector, int currentType, RenderedImage image) {
		try {
			out.write("<?xml version=\"1.0\" ");
			out.write("encoding=\"UTF-8\"?>\r\n");
			out.write("<CellCounterX_Marker_File>\r\n");

			// write the image properties
			out.write(" <Image_Properties>\r\n");
			out.write("     <Image_Filename>" + imgFilename
					+ "</Image_Filename>\r\n");
			out.write("     <Width>" + width + "</Width>\r\n");
			out.write("     <Height>" + height + "</Height>\r\n");
			out.write(" </Image_Properties>\r\n");

			// write the marker data
			out.write(" <Marker_Data>\r\n");
			out.write("     <Current_Type>" + currentType
					+ "</Current_Type>\r\n");
			ListIterator it = typeVector.listIterator();
			while (it.hasNext()) {
				CellCounterXMarkerVector markerVector = (CellCounterXMarkerVector) it
						.next();
				int type = markerVector.getType();
				out.write("     <Marker_Type>\r\n");
				out.write("         <Type>" + type + "</Type>\r\n");
				ListIterator lit = markerVector.listIterator();
				while (lit.hasNext()) {
					CellCounterXMarker marker = (CellCounterXMarker) lit.next();
					int x = marker.getX();
					int y = marker.getY();
					int z = marker.getZ();
					out.write("         <Marker>\r\n");
					out.write("             <MarkerX>" + x + "</MarkerX>\r\n");
					out.write("             <MarkerY>" + y + "</MarkerY>\r\n");
					out.write("             <MarkerZ>" + z + "</MarkerZ>\r\n");
					out.write("         </Marker>\r\n");
				}
				out.write("     </Marker_Type>\r\n");
			}

			out.write(" </Marker_Data>\r\n");

			// write the mappings
			out.write(" <Mapping_Data>\r\n");
			for (Mapping m : mappings) {
				String type = m.getMarkerType();
				out.write("     <Mapping>\r\n");
				out.write("         <MMarker_Type>" + type
						+ "</MMarker_Type>\r\n");
				String value = m.getValue();
				out.write("         <MValue>" + value + "</MValue>\r\n");
				out.write("     </Mapping>\r\n");
			}
			out.write(" </Mapping_Data>\r\n");
			out.write("</CellCounterX_Marker_File>\r\n");

			out.flush(); // Don't forget to flush!
			out.close();
			return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
