package ccx.io;





import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ccx.CellCounterXMarker;
import ccx.CellCounterXMarkerVector;
import ccx.Mapping;



/**
 *
 * @author  kurt, Philipp Kainz
 *
 */
public class ReadXML {
    private boolean verbose;
    private DocumentBuilderFactory dbf;
    private DocumentBuilder db;
    private Document doc;
    private String str;
    public static final int IMAGE_FILE_PATH = 0;
    public static final int CURRENT_TYPE = 1;
	public static final int IMAGE_WIDTH = 2;
	public static final int IMAGE_HEIGHT = 3;
	
    /**
     * Creates a new instance of ODReadXMLODD
     */
    public ReadXML(String XMLFilePath) {
        setVerbose(verbose);
        try{
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            doc = db.parse(new File(XMLFilePath));
            doc.getDocumentElement().normalize();
        } catch (SAXException e) {
            System.out.println(e.getMessage());
            System.out.println(XMLFilePath + " is not well-formed.");
        } catch (IOException e) {
            System.out.println("IOException " + e.getMessage());
        } catch (ParserConfigurationException e){
            System.out.println("ParserConfigurationException " + e.getMessage());
        }
    }
    
    public String readImgProperties(int valueID){ //as URL
        switch(valueID){
            case(IMAGE_FILE_PATH):
                str = readSingleValue(doc,"Image_Filename");
                break;
            case(CURRENT_TYPE):
                str = readSingleValue(doc,"Current_Type");
                break;
            case(IMAGE_WIDTH):
                str = readSingleValue(doc,"Width");
                break;
            case(IMAGE_HEIGHT):
                str = readSingleValue(doc,"Height");
                break;
        }
        if (str !=null){
            return str;
        }
        return null;
    }
    
    public Vector<CellCounterXMarkerVector> readMarkerData(){
        Vector<CellCounterXMarkerVector> typeVector = new Vector<CellCounterXMarkerVector>();
        
        NodeList markerTypeNodeList = getNodeListFromTag(doc,"Marker_Type");
        for (int i=0; i<markerTypeNodeList.getLength(); i++){
            Element markerTypeElement = getElement(markerTypeNodeList, i);
            NodeList typeNodeList = markerTypeElement.getElementsByTagName("Type");
            CellCounterXMarkerVector markerVector = new CellCounterXMarkerVector(Integer.parseInt(readValue(typeNodeList , 0)));
            
            NodeList markerNodeList = markerTypeElement.getElementsByTagName("Marker");
            for(int j=0; j<markerNodeList.getLength(); j++){
                Element markerElement = getElement(markerNodeList, j);
                NodeList markerXNodeList = markerElement.getElementsByTagName("MarkerX");
                NodeList markerYNodeList = markerElement.getElementsByTagName("MarkerY");
                NodeList markerZNodeList = markerElement.getElementsByTagName("MarkerZ");
                CellCounterXMarker marker = new CellCounterXMarker();
                marker.setX(Integer.parseInt(readValue(markerXNodeList,0)));
                marker.setY(Integer.parseInt(readValue(markerYNodeList,0)));
                marker.setZ(Integer.parseInt(readValue(markerZNodeList,0)));
                markerVector.add(marker);
            }
            typeVector.add(markerVector);
        }
        return typeVector;
    }
    
    public ArrayList<Mapping> readMappingData(){
    	System.out.println("Reading mapping data...");
    	ArrayList<Mapping> mappingVector = new ArrayList<Mapping>();
        
        NodeList mappingNodeList = getNodeListFromTag(doc,"Mapping");
        for (int i=0; i<mappingNodeList.getLength(); i++){
        	Mapping m = new Mapping();
            
        	Element mappingElement = getElement(mappingNodeList, i);
            
        	NodeList typeNodeList = mappingElement.getElementsByTagName("MMarker_Type");
            m.setMarkerType(readValue(typeNodeList , 0));
            
            NodeList valueNodeList = mappingElement.getElementsByTagName("MValue");
            m.setValue(readValue(valueNodeList , 0));
            
            mappingVector.add(m);
        }
        return mappingVector;
    }
    
    private String readValue(NodeList nodeList, int index) throws NullPointerException{
        Element element = getElement(nodeList, index);
        debugReport("Element = "+element.getNodeName());
        NodeList elementNodeList = getChildNodes(element);
        String str = getValue(elementNodeList, 0);
        return str;
    }
    private String[] readMarker(NodeList nodeList, int index) throws NullPointerException{
        Element element = getElement(nodeList, index);
        debugReport("Element = "+element.getNodeName());
        NodeList elementNodeList = getChildNodes(element);
        String str[] = {getValue(elementNodeList, 0),getValue(elementNodeList, 1),getValue(elementNodeList, 2)};
        return str;
    }
    private String readSingleValue(Document doc, String elementName){
        NodeList nodeList = getNodeListFromTag(doc,elementName);
        Element element = getElement(nodeList, 0);
        nodeList = getChildNodes(element);
        String str = getValue(nodeList, 0);
        return str;
    }
    private NodeList getNodeListFromTag(Document doc, String elementName){
        NodeList nodeList = doc.getElementsByTagName(elementName);
        return nodeList;
    }
    private NodeList getChildNodes(Element element){
        NodeList nodeList = element.getChildNodes();
        return nodeList;
    }
    private Element getElement(NodeList nodeList, int index){
        Element element = (Element)nodeList.item(index);
        return element;
    }
    private String getValue(NodeList nodeList, int index){
        String str = ((Node)nodeList.item(index)).getNodeValue().trim();
        return str;
    }
    
    
    public void debugReport(String report){
        if (verbose)
            System.out.println(report);
    }
    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }
    public boolean isVerbose(){
        return verbose;
    }
}
