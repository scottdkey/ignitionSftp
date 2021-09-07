package com.skm.sftp.gateway

import com.inductiveautomation.ignition.common.licensing.LicenseState
import com.inductiveautomation.ignition.common.script.ScriptManager
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook
import com.inductiveautomation.ignition.gateway.model.GatewayContext
import com.skm.sftp.AbstractScriptModule
import com.skm.sftp.modulePath
import org.slf4j.LoggerFactory

@Suppress("unused")
class GatewayHook : AbstractGatewayModuleHook() {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val scriptModule: AbstractScriptModule = GatewayScriptModule()
    override fun setup(gatewayContext: GatewayContext) {
        logger.info("setup ()")
    }

    override fun startup(licenseState: LicenseState) {
        logger.info("startup ()")
    }

    override fun shutdown() {
        logger.info("shutdown")
    }

    override fun initializeScriptManager(manager: ScriptManager) {
        super.initializeScriptManager(manager)
        manager.addScriptModule(
            modulePath,
            scriptModule,
            PropertiesFileDocProvider()
        )
    }

    override fun getRPCHandler(session: ClientReqSession, projectName: String): AbstractScriptModule {
        return scriptModule
    }
}