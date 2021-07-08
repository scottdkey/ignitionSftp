package com.skm.sftp.client

import com.inductiveautomation.ignition.common.script.ScriptManager
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider
import com.inductiveautomation.vision.api.client.AbstractClientModuleHook
import com.skm.sftp.AbstractScriptModule.Companion.modulePath

@Suppress("unused")
class ClientHook : AbstractClientModuleHook() {
    override fun initializeScriptManager(manager: ScriptManager) {
        super.initializeScriptManager(manager)
        manager.addScriptModule(
            modulePath,
            ClientScriptModule(),
            PropertiesFileDocProvider()
        )
    }
}