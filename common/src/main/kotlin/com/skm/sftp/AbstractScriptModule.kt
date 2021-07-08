package com.skm.sftp

import com.skm.sftp.ExcelFunctionsBlackBox
import com.skm.sftp.AbstractScriptModule
import com.inductiveautomation.ignition.common.BundleUtil
import com.inductiveautomation.ignition.common.script.hints.ScriptArg
import com.inductiveautomation.ignition.common.script.hints.ScriptFunction
import org.slf4j.LoggerFactory

abstract class AbstractScriptModule : ExcelFunctionsBlackBox {
    companion object {
        const val MODULE_ID = "sftp"
        const val modulePath = "skm.sftp"
        val logger = LoggerFactory.getLogger(MODULE_ID)

        init {
            BundleUtil.get().addBundle(
                AbstractScriptModule::class.java.simpleName,
                AbstractScriptModule::class.java.classLoader,
                AbstractScriptModule::class.java.name.replace('.', '/')
            )
        }
    }

    @ScriptFunction(docBundlePrefix = "ScriptModule")
    override fun multiply(
        @ScriptArg("arg0") arg0: Int,
        @ScriptArg("arg1") arg1: Int
    ): Int {
        return multiplyImpl(arg0, arg1)
    }

    protected abstract fun multiplyImpl(arg0: Int, arg1: Int): Int
    @ScriptFunction(docBundlePrefix = "ScriptModule")
    override fun add(
        @ScriptArg("arg0") arg0: Int,
        @ScriptArg("arg1") arg1: Int
    ): Int {
        return addImpl(arg0, arg1)
    }

    protected abstract fun addImpl(arg0: Int, arg1: Int): Int
    @ScriptFunction(docBundlePrefix = "ScriptModule")
    override fun sub(
        @ScriptArg("arg0") arg0: Int,
        @ScriptArg("arg1") arg1: Int
    ): Int {
        return subImpl(arg0, arg1)
    }

    protected abstract fun subImpl(arg0: Int, arg1: Int): Int
    @ScriptFunction(docBundlePrefix = "ScriptModule")
    override fun divide(
        @ScriptArg("arg0") arg0: Int,
        @ScriptArg("arg1") arg1: Int
    ): Int {
        return divideImpl(arg0, arg1)
    }

    protected abstract fun divideImpl(arg0: Int, arg1: Int): Int
}