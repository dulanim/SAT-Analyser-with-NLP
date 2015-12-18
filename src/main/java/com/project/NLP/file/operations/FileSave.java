/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.file.operations;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.GUI.NewFileWindow;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
/**
 *
 * @author shiyam
 */
public class FileSave {
    
      public void saveFile() {
       
        String activePath = HomeGUI.activeTab.get(true);
        File file = new File(activePath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(NewFileWindow.codeText.getText());
            fileWriter.close();
            
            MessageBox messageBox = new MessageBox(HomeGUI.shell, SWT.SAVE
              | SWT.OK);
            messageBox.setMessage("File Modification Changed in \n" + activePath);
            messageBox.setText("Success Message");
            messageBox.open();
        } catch (IOException e) {
            MessageBox messageBox = new MessageBox(HomeGUI.shell, SWT.ICON_ERROR
              | SWT.OK);
            messageBox.setMessage("File I/O Error.");
            messageBox.setText("Error");
            messageBox.open();
          return;
        }
      }
    
}
