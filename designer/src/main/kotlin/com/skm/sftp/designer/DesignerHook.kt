

package com.skm.sftp.designer

import com.inductiveautomation.ignition.common.script.ScriptManager
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider
import com.inductiveautomation.ignition.designer.model.AbstractDesignerModuleHook
import com.skm.sftp.client.ClientScriptModule
import com.skm.sftp.modulePath


@Suppress("unused")
class DesignerHook : AbstractDesignerModuleHook() {
    override fun initializeScriptManager(manager: ScriptManager) {
        super.initializeScriptManager(manager)
        manager.addScriptModule(
            modulePath,
            ClientScriptModule(),
            PropertiesFileDocProvider()
        )
    }
}