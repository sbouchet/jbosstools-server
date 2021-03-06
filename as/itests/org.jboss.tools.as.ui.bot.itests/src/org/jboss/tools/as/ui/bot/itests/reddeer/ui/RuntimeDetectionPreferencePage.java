package org.jboss.tools.as.ui.bot.itests.reddeer.ui;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.label.DefaultLabel;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;

/**
 * 
 * @author ljelinkova
 * @author psuchy
 * @author Radoslav Rabara
 *
 */
public class RuntimeDetectionPreferencePage extends PreferencePage {

	public static final String[] PATH = {"JBoss Tools", "JBoss Runtime Detection"};

	public RuntimeDetectionPreferencePage(ReferencedComposite composite) {
		super(composite, PATH);
	}

	public void removeAllPaths(){
		Table table = new DefaultTable(referencedComposite);

		int pathsNumber = table.rowCount();
		for (int i = 0; i < pathsNumber; i++){
			table.select(0);
			Button removeButton = new PushButton(referencedComposite, "Remove");
			assertTrue("Remove button is not enabled", removeButton.isEnabled());
			removeButton.click();
		}
	}

	public List<String> getAllPaths() {
		Table table = new DefaultTable(referencedComposite);
		List<String> paths = new ArrayList<String>();
		for(TableItem ti : table.getItems()) {
			paths.add(ti.getText(0));
		}
		return paths;
	}

	public SearchingForRuntimesDialog search(){
		new PushButton(referencedComposite, "Search...").click();
		new DefaultShell("Searching for runtimes...");
		new WaitUntil(new RuntimeSearchedFinished(), TimePeriod.LONG);
		return new SearchingForRuntimesDialog();
	}

	private static class RuntimeSearchedFinished extends AbstractWaitCondition {

		@Override
		public boolean test() {
			try {
				new DefaultLabel("Searching runtimes is finished.");
				return true;
			} catch (CoreLayerException e){
				return false;
			}
		}

		@Override
		public String description() {
			return "The runtime search has not finished in the specified amount of time";
		}
	}
}
