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
package examples;

import java.io.IOException;

import ij.ImagePlus;
import ij.gui.YesNoCancelDialog;
import ij.measure.ResultsTable;

/**
 * A small class to test the ImageJ class {@link ResultsTable}.
 * 
 * @author Michael Epping <michael.epping@uni-muenster.de>
 * 
 */
public class ResultTableTest {

    /**
     * Creates a small {@link ResultsTable} that uses a label and has 2 columns containing values.
     * 
     * @param args
     */
    public static void main(String[] args) {
	ResultsTable result = new ResultsTable();
	// sets the precision of all numbers of the ResultTable
	result.setPrecision(0);
	// You can address a column by an id or by its name.
	int driftXId = result.getFreeColumn("drift.x");
	if (driftXId == ResultsTable.COLUMN_IN_USE) {
	    System.out.println("A column with this name already exists.");
	    return;
	}
	// You don't need to create the column first. addValue() will create a new column if none with the given name
	// exists.
	result.getFreeColumn("drift.y");
	String[] label = { "label 1", "label 2", "label 3" };
	double[] driftArray = { 5, 10, 15 };
	result.show("This is a test");
	// results.updateResults() will only update the text window with the title "Results".
	for (int i = 0; i < driftArray.length; i++) {
	    // You should only increment the counter if you want to add values to the new row. Otherwise default values
	    // (empty String and 0) are used.
	    result.incrementCounter();
	    result.addLabel(label[i]);
	    result.addValue(driftXId, driftArray[i]);
	    result.addValue("drift.y", driftArray[i] + 5);
	}
	// This will update the existing text window called "This is a test".
	result.show("This is a test");
	System.out.println(result.getLabel(0));
	System.out.println(result.getValue("drift.x", 0));
	System.out.println(result.getValue("drift.y", 0));
	System.out.println(result.getLabel(1));
	System.out.println(result.getValue("drift.x", 1));
	System.out.println(result.getValue("drift.y", 1));
	System.out.println(result.getLabel(2));
	System.out.println(result.getValue("drift.x", 2));
	System.out.println(result.getValue("drift.y", 2));
	YesNoCancelDialog dialog = new YesNoCancelDialog(null, "Test",
		"Show the ResultTable as an Image?\nPressing no will save the image.\nSaving may take some time.");
	if (dialog.yesPressed()) {
	    // create an ImageProcessor that shows the values of the ResultTable. x=column, y=row and the value of the
	    // pixel corresponds to the value of the cell.
	    ImagePlus imp = new ImagePlus("A ResultTable", result.getTableAsImage());
	    imp.show();
	} else {
	    try {
		// You can save the content of the ResultTable. If the argument is null a SaveAsDialog is shown.
		result.saveAs(null);
	    } catch (IOException e) {
		e.printStackTrace();
		return;
	    }
	}
    }

}
