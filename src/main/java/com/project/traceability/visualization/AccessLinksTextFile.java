/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Aarthika <>
 */
public class AccessLinksTextFile {
    public static void addNewLinkstoGraph() {
        String newLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "NewGraphLinks.txt";
        //String source = start + " " + end + " " + relType + "\n";
        File file = new File(newLinkFile);
        try (FileReader reader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] newLink = line.split(" ");
                if (newLink.length > 2) {
                    String id = line.substring(0, line.indexOf(":"));
                    line = line.replaceAll(line.substring(0, line.indexOf(":") + 1), "");
                    String start = line.substring(0, line.indexOf(" ")).trim();
                    line = line.replaceAll(start, "").trim();
                    String end = line.substring(0, line.indexOf(" "));
                    String type = line.substring(line.indexOf(" ")).trim();
                    System.out.println("Entering " + start + " " + end + " " + type);
                    if (Integer.parseInt(id) != -1) {
                        if (!AccessGexfFile.getIDFromGexf(Integer.parseInt(id))) {
                            AccessGexfFile.addToGEXF(start, end, type);
                        }
                    }
                    //GraphMouseListener.addToGEXF(start, end, type);
                }
            }
            bufferedReader.close();
            reader.close();

        } catch (IOException e) {
        }
    }

    public static void deleteRemovalLinkstoGraph() {
        String newLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "DeletedGraphLinks.txt";
        //String source = start + " " + end + " " + relType + "\n";
        File file = new File(newLinkFile);
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() > 1) {
                    String ids[] = line.split(" ");
                    System.out.println("NumberL " + ids.length);
                    for (String id : ids) {
                        AccessGexfFile.removeEdgeFromGexf(Integer.parseInt(id));
                    }
                }
            }
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
        }
    }

}
