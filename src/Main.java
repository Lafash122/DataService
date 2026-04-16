import graphic.*;
import managers.*;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		DataInfoListener dim = new DataInfoManager();
		GraphicInterface gf = new GraphicInterface(dim);
		SwingUtilities.invokeLater(() -> gf.setVisible(true));
	}
}