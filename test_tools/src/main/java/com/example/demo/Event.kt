package com.example.demo


import tornadofx.FXEvent


/**
 * 消息
 */
class ToastEvent(val msg: String) : FXEvent()
/**
 * 弹出告警提示信息
 */
class DialogCenterMsgWarn(val msg: String) : FXEvent()

/**
 * 消息
 */
class RecvPortMsgEvent(val msg: ByteArray) : FXEvent()

/**
 * Mav消息
 */
class MavMsgEvent(val msg: ByteArray) : FXEvent()

/**
 * 消息
 */
class TextAreaMsgEvent(val msg: String) : FXEvent()


/**
 * 串口是否打开
 */
class PortWorkEvent(val isClose: Boolean, val index: Int = 0) : FXEvent()

class ProgressEvent(val value: Double) : FXEvent()
