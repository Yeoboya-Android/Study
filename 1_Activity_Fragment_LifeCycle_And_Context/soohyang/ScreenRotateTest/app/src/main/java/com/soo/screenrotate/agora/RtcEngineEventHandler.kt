package com.soo.screenrotate.agora

interface RtcEngineEventHandler {
    fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int)
    fun onUserOffline(uid: Int, reason: Int)
    fun onUserJoined(uid: Int, elapsed: Int)
    fun onRemoteVideoStateChanged(uid: Int, state: Int, reason: Int, elapsed: Int)
}