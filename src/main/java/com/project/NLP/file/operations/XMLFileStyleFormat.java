/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.file.operations;

import com.project.traceability.GUI.XmlRegion;
import com.project.traceability.GUI.XmlRegionAnalyzer;
import java.util.List;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

/**
 *
 * @author shiyam
 */
public class XMLFileStyleFormat {

    public XMLFileStyleFormat(String textString,StyledText codeText) {
        
        
        List<XmlRegion> regions = new XmlRegionAnalyzer()
				.analyzeXml(textString);
		List<StyleRange> styleRanges = XmlRegionAnalyzer
				.computeStyleRanges(regions);
		for (int l = 0; l < regions.size(); l++) {
			XmlRegion xr = regions.get(l);
			int regionLength = xr.getEnd() - xr.getStart();
			switch (xr.getXmlRegionType()) {
			case MARKUP: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case ATTRIBUTE: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case ATTRIBUTE_VALUE: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case MARKUP_VALUE: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case COMMENT:
				break;
			case INSTRUCTION:
				break;
			case CDATA:
				break;
			case WHITESPACE:
				break;
			default:
				break;
			}

		}
    }
    
    
    
}
