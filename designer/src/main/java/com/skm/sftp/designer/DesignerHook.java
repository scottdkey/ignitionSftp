package com.skm.apachepoi.designer;

import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.skm.apachepoi.client.ClientScriptModule;

public class DesignerHook extends AbstractDesignerModuleHook {

@Override
public void initializeScriptManager(ScriptManager manager) {
  super.initializeScriptManager(manager);

  manager.addScriptModule(
      "system.excel",
      new ClientScriptModule(),
      new PropertiesFileDocProvider());
}

}
