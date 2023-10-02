package com.example.flutterxprintersdk.esepos

interface OnPrintProcess {
    fun onSuccess()
    fun onError(msg: String?)
}