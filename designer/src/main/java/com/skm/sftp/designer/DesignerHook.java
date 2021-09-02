package com.skm.sftp.designer;

import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider;
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook;
import com.skm.sftp.client.ClientScriptModule;

@SuppressWarnings("unused")
public class DesignerHook extends AbstractDesignerModuleHook {
  @Override
  public void initializeScriptManager(ScriptManager manager){
    super.initializeScriptManager(manager);

    manager.addScriptModule(
        "system.sftp",
        new ClientScriptModule(),
        new PropertiesFileDocProvider());
  }
}
