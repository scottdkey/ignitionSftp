package com.skm.sftp.client

import com.skm.sftp.AbstractScriptModule
import com.skm.sftp.SFTPBlackBox
import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory

class ClientScriptModule : AbstractScriptModule() {
    private val rpc: SFTPBlackBox = ModuleRPCFactory.create(
        moduleId,
        SFTPBlackBox::class.java
    )

    override fun multiplyImpl(arg0: Int, arg1: Int): Int {
        return rpc.multiply(arg0, arg1)
    }

    override fun addImpl(arg0: Int, arg1: Int): Int {
        return rpc.add(arg0, arg1)
    }

    override fun subImpl(arg0: Int, arg1: Int): Int {
        return rpc.sub(arg0, arg1)
    }

    override fun divideImpl(arg0: Int, arg1: Int): Int {
        return rpc.divide(arg0, arg1)
    }

}