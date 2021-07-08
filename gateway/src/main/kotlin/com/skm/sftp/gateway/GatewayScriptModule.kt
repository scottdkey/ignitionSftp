package com.skm.sftp.gateway

import com.skm.sftp.AbstractScriptModule

class GatewayScriptModule : AbstractScriptModule() {
    override fun multiplyImpl(arg0: Int, arg1: Int): Int {
        return arg0 * arg1
    }

    override fun addImpl(arg0: Int, arg1: Int): Int {
        return arg0 + arg1
    }

    override fun subImpl(arg0: Int, arg1: Int): Int {
        return arg0 - arg1
    }

    override fun divideImpl(arg0: Int, arg1: Int): Int {
        return arg0 / arg1
    }
}