package normalize;

import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This plugin is used to test the operations "normalize" and "translate" while
 * a WaitForUserDialog is active.
 * 
 * @author m_eppi02
 * 
 */
public class Wait4User implements PlugInFilter {

    private ImagePlus imp;

    @Override
    public int setup(String arg, ImagePlus imp) {
	if (imp.getStackSize() == 1) {
	    return DONE;
	}
	this.imp = imp;
	return DOES_ALL;
    }

    @Override
    public void run(ImageProcessor ip) {
	WaitForUserDialog waitDLG = new WaitForUserDialog("This is a test");
	Button button = new Button("normalize");
	MyListener listener = new MyListener();
	button.addActionListener(listener);
	waitDLG.add(button);
	Button button2 = new Button("shift");
	MyListener2 listener2 = new MyListener2();
	button2.addActionListener(listener2);
	waitDLG.add(button2);
	waitDLG.pack();
	waitDLG.show();
    }

    private class MyListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    double mean = 0;
	    for (int i = 1; i <= imp.getStackSize(); i++) {
		mean = imp.getStack().getProcessor(i).getStatistics().mean;
		imp.getStack().getProcessor(i).multiply(1.0 / mean);
		imp.getStack().getProcessor(i).resetMinAndMax();
	    }
	    imp.resetDisplayRange();
	    imp.updateAndRepaintWindow();
	}

    }

    private class MyListener2 implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
	    int mid = Math.round(1.0f * imp.getStackSize() / 2);
	    imp.getStack().getProcessor(mid)
		    .translate(imp.getWidth() / 10, imp.getHeight() / 10);
	    imp.updateAndDraw();
	}

    }

}
