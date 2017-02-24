/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import org.gephi.preview.api.Item;
import org.gephi.preview.api.PDFTarget;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.api.SVGTarget;
import org.gephi.preview.spi.ItemBuilder;
import org.gephi.preview.spi.MouseResponsiveRenderer;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.preview.spi.Renderer;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Thanu
 */
@ServiceProvider(service = Renderer.class)
public class GraphRenderer implements MouseResponsiveRenderer, Renderer {

    @Override
    public boolean needsPreviewMouseListener(PreviewMouseListener pl) {
        return pl instanceof GraphMouseListener;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void preProcess(PreviewModel pm) {
        
    }

    @Override
    public void render(Item item, RenderTarget target, PreviewProperties properties) {
        if (target instanceof ProcessingTarget) {
        } else if (target instanceof PDFTarget) {
        } else if (target instanceof SVGTarget) {
        }
    }

    @Override
    public PreviewProperty[] getProperties() {
        return new PreviewProperty[0];
    }

    @Override
    public boolean isRendererForitem(Item item, PreviewProperties pp) {
        return item.getType().equals(Item.NODE);
    }

    @Override
    public boolean needsItemBuilder(ItemBuilder ib, PreviewProperties pp) {
        return false;
    }

}
