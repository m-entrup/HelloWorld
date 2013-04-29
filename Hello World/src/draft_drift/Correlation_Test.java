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
package draft_drift;

import java.awt.Rectangle;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

/**
 * This plugin is used to test different methods for template matching. The methods are "distance",
 * "euclidean distance", "cross-correlation", "normalised cross-correlation" and
 * "normalised cross-correlation coefficients".
 * 
 * @author Michael Epping <michael.epping@uni-muenster.de>
 * 
 */
public class Correlation_Test implements ExtendedPlugInFilter {

    private ImageProcessor ip1;
    private ImageProcessor ip2;

    /*
     * (non-Javadoc)
     * 
     * @see ij.plugin.filter.PlugInFilter#setup(java.lang.String, ij.ImagePlus)
     */
    @Override
    public int setup(String arg, ImagePlus imp) {
	return DOES_32;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ij.plugin.filter.PlugInFilter#run(ij.process.ImageProcessor)
     */
    @Override
    public void run(ImageProcessor ip) {
	Rectangle roi = new Rectangle(ip2.getWidth() / 4, ip2.getHeight() / 4, ip2.getWidth() / 2, ip2.getHeight() / 2);
	ip2.setRoi(roi);
	ip2 = ip2.crop();
	ImagePlus imp2 = new ImagePlus("Crop", ip2);
	imp2.show();
	float b = 0;
	float mean2 = 0;
	for (int j = 0; j < ip2.getHeight(); j++) {
	    for (int i = 0; i < ip2.getWidth(); i++) {
		b += ip2.getf(i, j) * ip2.getf(i, j);
		mean2 += ip2.getf(i, j) / ip2.getPixelCount();
	    }
	}
	float sigma2 = 0;
	for (int j = 0; j < ip2.getHeight(); j++) {
	    for (int i = 0; i < ip2.getWidth(); i++) {
		sigma2 += (ip2.getf(i, j) - mean2) * (ip2.getf(i, j) - mean2);
	    }
	}
	FloatProcessor ipDist = new FloatProcessor(ip2.getWidth(), ip2.getHeight());
	FloatProcessor ipDist2 = new FloatProcessor(ip2.getWidth(), ip2.getHeight());
	FloatProcessor ipCc = new FloatProcessor(ip2.getWidth(), ip2.getHeight());
	FloatProcessor ipCcN = new FloatProcessor(ip2.getWidth(), ip2.getHeight());
	FloatProcessor ipCcL = new FloatProcessor(ip2.getWidth(), ip2.getHeight());
	for (int s = 0; s < ip1.getHeight() - ip2.getHeight(); s++) {
	    for (int r = 0; r < ip1.getWidth() - ip2.getWidth(); r++) {
		float a = 0;
		float mean1 = 0;
		for (int j = 0; j < ip2.getHeight(); j++) {
		    for (int i = 0; i < ip2.getWidth(); i++) {
			a += ip1.getf(i + r, j + s) * ip1.getf(i + r, j + s);
			mean1 += ip1.getf(i + r, j + s) / ip2.getPixelCount();
		    }
		}
		float dist = 0;
		float dist2 = 0;
		float cc = 0;
		float ccL = 0;
		for (int j = 0; j < ip2.getHeight(); j++) {
		    for (int i = 0; i < ip2.getWidth(); i++) {
			dist += Math.abs(ip1.getf(i + r, j + s) - ip2.getf(i, j));
			dist2 += dist * dist;
			cc += ip1.getf(i + r, j + s) * ip2.getf(i, j);
			ccL += ip1.getf(i + r, j + s) * ip1.getf(i + r, j + s);
		    }
		}
		ipDist.setf(r, s, dist);
		ipDist2.setf(r, s, (float) Math.sqrt(dist2));
		ipCc.setf(r, s, cc);
		ipCcN.setf(r, s, (float) (cc / Math.sqrt(a * b)));
		ipCcL.setf(
			r,
			s,
			(float) ((cc - ip2.getPixelCount() * mean1 * mean2) / (Math.sqrt(ccL - ip2.getPixelCount()
				* mean1 * mean1) * Math.sqrt(sigma2))));
	    }
	}
	ImagePlus impDist = new ImagePlus("distance", ipDist);
	impDist.show();
	ImagePlus impDist2 = new ImagePlus("euclidean distance", ipDist2);
	impDist2.show();
	ImagePlus impCc = new ImagePlus("cross-correlation", ipCc);
	impCc.show();
	ImagePlus impCcN = new ImagePlus("normalised cross-correlation", ipCcN);
	impCcN.show();
	ImagePlus impCcL = new ImagePlus("normalised cross-correlation coefficients", ipCcL);
	impCcL.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ij.plugin.filter.ExtendedPlugInFilter#showDialog(ij.ImagePlus, java.lang.String,
     * ij.plugin.filter.PlugInFilterRunner)
     */
    @Override
    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) {
	GenericDialog gd = new GenericDialog("Correlation", IJ.getInstance());
	int[] wList = WindowManager.getIDList();
	String[] titles = new String[wList.length];
	for (int i = 0; i < wList.length; i++) {
	    imp = WindowManager.getImage(wList[i]);
	    titles[i] = imp != null ? imp.getTitle() : "";
	}
	gd.addChoice("image", titles, titles[0]);
	if (titles.length > 1) {
	    gd.addChoice("template", titles, titles[1]);
	} else {
	    gd.addChoice("template", titles, titles[0]);
	}
	gd.showDialog();
	int index1 = gd.getNextChoiceIndex();
	int index2 = gd.getNextChoiceIndex();
	if (gd.wasCanceled()) {
	    return DONE;
	}
	ip1 = WindowManager.getImage(wList[index1]).getProcessor();
	ip2 = WindowManager.getImage(wList[index2]).getProcessor();
	return DOES_32;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ij.plugin.filter.ExtendedPlugInFilter#setNPasses(int)
     */
    @Override
    public void setNPasses(int nPasses) {
    }

}
