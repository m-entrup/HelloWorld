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

import ij.gui.GenericDialog;

/**
 * This program shows all components that can be added to a {@link GenericDialog}. By using
 * <code>add(Component comp)</code> additional components (AWT & Swing) are possible.
 * 
 * @author Michael Epping <michael.epping@uni-muenster.de>
 * 
 */
public class GenericDialogTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
	GenericDialog gd = new GenericDialog("GenericDialog GUI-Elements");
	gd.addMessage("addMessage(String text)");
	gd.addTextAreas("addTextAreas(String text1, String text2, int rows, int columns)", "10 rows, 20 columns", 10,
		20);
	gd.addStringField("label 50 columns", "addStringField(String label, String defaultText, int columns)", 50);
	gd.addNumericField("addNumericField()", 0, 3, 20, "[a.u.]");
	gd.addCheckbox("addCheckbox()", true);
	String[] labels = { "label 1", "label 2", "label 3", "label 4", "label 5", "label 6" };
	boolean[] defaultValues = { true, false, false, true, true, false };
	String[] headings = { "head 1", "head 2" };
	gd.addCheckboxGroup(3, 2, labels, defaultValues, headings);
	String[] items = { "item 1", "item 2", "item 3" };
	gd.addChoice("addChoise()", items, items[0]);
	String html = "<html>" + "<h2>HTML formatted help</h2>" + "<font size=+1>"
		+ "In ImageJ 1.46b or later, dialog boxes<br>" + "can have a <b>Help</b> button that displays<br>"
		+ "<font color=red>HTML</font> formatted  messages.<br>" + "</font>";
	gd.addHelp(html);
	gd.addPreviewCheckbox(null);
	gd.addSlider("addSlider()", 0, 100, 50);
	gd.showDialog();
	System.exit(0);
    }

}