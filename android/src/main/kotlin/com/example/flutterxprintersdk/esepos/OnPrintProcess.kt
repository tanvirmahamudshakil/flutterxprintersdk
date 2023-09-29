package com.example.xprinter.esepos

interface OnPrintProcess {
    fun onSuccess()
    fun onError(msg: String?)
}