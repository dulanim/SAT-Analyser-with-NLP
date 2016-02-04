package com.project.traceability.GUI;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;

public class ContextMenuAppears implements ICondition{

	private SWTBotTree swtBotTree;
	private String mMenuText;
	public ContextMenuAppears(SWTBotTree pSwtBotTree, String menuText) {
	    swtBotTree = pSwtBotTree;
	    mMenuText = menuText;
	}

	@Override
	public boolean test() throws Exception {
	    try {           
	        return swtBotTree.contextMenu(mMenuText).isVisible();   
	    } catch (WidgetNotFoundException e) {
	        return false;
	    }

	}

	@Override
	public void init(SWTBot bot) {
	    // TODO Auto-generated method stub

	}

	@Override
	public String getFailureMessage() {
	    // TODO Auto-generated method stub
	    return null;
	}


}
