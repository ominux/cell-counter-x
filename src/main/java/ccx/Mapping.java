package ccx;


public class Mapping {

	private String markerType;
	private String value;

	public final static Mapping[] DEFAULT_CLASSIFICATION_MAPPING= {
		new Mapping("1", "Granulocyte"),
		new Mapping("2", "BandCell"),
		new Mapping("3", "Metamyelocyte"),
		new Mapping("4", "Myelocyte"),
		new Mapping("5", "Promyelocyte"),
		new Mapping("6", "Myeloblast"),
		new Mapping("7", "Erythrocyte"),
		new Mapping("8", "Reticulocyte"),
		new Mapping("9", "OrthochromaticNormoblast"),
		new Mapping("10", "PolychromatophilicNormoblast"),
		new Mapping("11", "BasophilicNormoblast"),
		new Mapping("12", "Pronormoblast"),
		new Mapping("13", "Megakaryocyte")
	};
	
	public final static Mapping[] DEFAULT_DETECTION_MAPPING= {
		new Mapping("1", "foreground"),
		new Mapping("2", "background"),
		new Mapping("3", "unknown"),
	};
	
	public Mapping(String markerType, String value) {
		this.markerType = markerType;
		this.value = value;
	}

	public Mapping() {
	}
	
	public void setMarkerType(String key) {
		this.markerType = key;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getMarkerType() {
		return markerType;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "markerType=" + this.markerType + ", value=" + this.value;
	}
}
