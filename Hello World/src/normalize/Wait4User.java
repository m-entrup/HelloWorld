/**
 * m-epping/HelloWorld - A repository for testing purposes
 * 
 * Copyright (c) 2013, Michael Epping
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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