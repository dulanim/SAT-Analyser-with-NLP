package com.project.traceability.GUI;

import java.util.Vector;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeViewBasedOnNode {
	static Display display = new Display();

	static Shell shell = new Shell(display);

	static final Tree tree = new Tree(shell, SWT.BORDER);

	@SuppressWarnings("rawtypes")
	static Vector nodes = new Vector();

	public static void traditional() {
		for (int i = 0; nodes != null && i < nodes.size(); i++) {
			Node node = (Node) nodes.elementAt(i);
			addNode(null, node);
		}
	}

	@SuppressWarnings("rawtypes")
	private static void addNode(TreeItem parentItem, Node node) {
		TreeItem item = null;
		if (parentItem == null)
			item = new TreeItem(tree, SWT.NONE);
		else
			item = new TreeItem(parentItem, SWT.NONE);

		item.setText(node.getName());

		Vector subs = node.getSubCategories();
		for (int i = 0; subs != null && i < subs.size(); i++)
			addNode(item, (Node) subs.elementAt(i));
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Node category = new Node("A", null);
		nodes.add(category);

		category = new Node("a1", category);
		new Node("a11", category);
		new Node("a12", category);

		category = new Node("B", null);
		nodes.add(category);

		new Node("b1", category);
		new Node("b2", category);

		TreeViewer treeViewer = new TreeViewer(tree);

		treeViewer.setContentProvider(new MyTreeContentProvider());

		treeViewer.setLabelProvider(new MyLabelProvider());
		treeViewer.setInput(nodes);
		tree.setSize(300, 200);
		shell.setSize(300, 200);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
