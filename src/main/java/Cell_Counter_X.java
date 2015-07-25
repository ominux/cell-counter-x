
import ccx.CellCounterXGUI;
import ij.plugin.frame.PlugInFrame;

/**
 * 
 * @author Philipp Kainz
 */
public class Cell_Counter_X extends PlugInFrame {

	/** Creates a new instance of Cell_Counter_X */
	public Cell_Counter_X() {
		super("Cell Counter X");
		new CellCounterXGUI();
	}

	public void run(String arg) {
	}

}
