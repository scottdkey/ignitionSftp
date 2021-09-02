package com.skm.sftp.gateway;

import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider;
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.skm.sftp.AbstractScriptModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unused")
public class GatewayHook extends AbstractGatewayModuleHook {

  private  final Logger logger = LoggerFactory.getLogger(getClass());
  private final AbstractScriptModule scriptModule = new GatewayScriptModule();

  @Override
  public void setup(GatewayContext gatewayContext){logger.info("setup()");}

  @Override
  public void startup(LicenseState licenseState) {
    logger.info("startup()");
  }

  @Override
  public void shutdown(){
    logger.info("shutdown");
  }

  @Override
  public void initializeScriptManager(ScriptManager manager){
    super.initializeScriptManager(manager);
    manager.addScriptModule(
        "system.sftp",
        scriptModule,
        new PropertiesFileDocProvider());
  }

  @Override
  public Object getRPCHandler(ClientReqSession session, String projectName){
    return scriptModule;
  }
}
