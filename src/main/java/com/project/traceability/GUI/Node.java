package com.project.traceability.GUI;

import java.util.Vector;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class Node {

	private String name;

	private Vector subCategories;

	private Node parent;

	public Node(String name, Node parent) {
		this.name = name;
		this.parent = parent;
		if (parent != null)
			parent.addSubCategory(this);
	}

	public Vector getSubCategories() {
		return subCategories;
	}

	private void addSubCategory(Node subcategory) {
		if (subCategories == null)
			subCategories = new Vector();
		if (!subCategories.contains(subcategory))
			subCategories.add(subcategory);
	}

	public String getName() {
		return name;
	}

	public Node getParent() {
		return parent;
	}
}

class MyLabelProvider implements ILabelProvider {
	public String getText(Object element) {
		return ((Node) element).getName();
	}

	public Image getImage(Object arg0) {
		return null;
	}

	public void addListener(ILabelProviderListener arg0) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}
}

class MyTreeContentProvider implements ITreeContentProvider {
	public Object[] getChildren(Object parentElement) {
		Vector subcats = ((Node) parentElement).getSubCategories();
		return subcats == null ? new Object[0] : subcats.toArray();
	}

	public Object getParent(Object element) {
		return ((Node) element).getParent();
	}

	public boolean hasChildren(Object element) {
		return ((Node) element).getSubCategories() != null;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof Vector) {
			return ((Vector) inputElement).toArray();
		}
		return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
