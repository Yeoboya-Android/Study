package com.soo.screenrotate.agora

import io.agora.rtc.IRtcEngineEventHandler
import java.util.*

class RtcEngineEventHandlerProxy : IRtcEngineEventHandler() {
    private val mEventHandlers = ArrayList<RtcEngineEventHandler>()
    fun addEventHandler(handler: RtcEngineEventHandler) {
        if (!mEventHandlers.contains(handler)) mEventHandlers.add(handler)
    }

    fun removeEventHandler(handler: RtcEngineEventHandler) {
        mEventHandlers.remove(handler)
    }

    override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
        for (handler in mEventHandlers) {
            handler.onJoinChannelSuccess(channel, uid, elapsed)
        }
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        for (handler in mEventHandlers) {
            handler.onUserJoined(uid, elapsed)
        }
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        for (handler in mEventHandlers) {
            handler.onUserOffline(uid, reason)
        }
    }

    override fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int) {
        for (handler in mEventHandlers) {
            handler.onRemoteVideoStateChanged(uid, state, reason, elapsed)
        }
    }
}
