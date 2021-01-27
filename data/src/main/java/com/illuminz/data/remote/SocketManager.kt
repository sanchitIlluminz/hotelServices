package com.illuminz.data.remote

import com.illuminz.data.repository.AuthorizationRepository
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import timber.log.Timber

class SocketManager(
    private val baseUrl: String,
    private val authorizationRepository: AuthorizationRepository
) {
    companion object {
        val EVENT_JOIN_MEETING: String = "join_meeting"
        val EVENT_RECORDER_READY: String = "recorder_ready"
        val EVENT_TEACHER_JOINED: String = "teacher_joined"
        val EVENT_USER_LEFT: String = "user_left"
        val RATE_CLASS: String = "rate_class"
        val TEACHER_DID_NOT_JOIN: String = "teacher_did_not_join"
        val CLASS_ENDED: String = "class_ended"
        val CLASS_CANCELLED: String = "class_cancelled"
        val NOTIFICATION_MARK_READ: String = "notification_mark_read"
        val NOTIFICATION_HAS_PENDING: String = "notification_has_pending"
        val NOTIFICATION_NEW: String = "notification_new"
        val GET_SAVED_LIST_COUNT: String = "get_saved_list_count"

        //for chat
        val CHAT_NEW_MESSAGE: String = "chat_new_message"
        val CHAT_NEW_REQUEST: String = "chat_new_request"
        val CHAT_JOIN_CONVERSATION: String = "chat_join_conversation"
        val CHAT_HANDLE_REQUEST: String = "chat_handle_request"
        val CHAT_LEAVE_CONVERSATION: String = "chat_leave_conversation"
        val CHAT_TYPING_STARTED: String = "chat_user_started_typing"
        val CHAT_TYPING_STOPED: String = "chat_user_stopped_typing"
        val CHAT_USER_ONLINE: String = "user_online"
        val CHAT_USER_OFFLINE: String = "user_offline"
        val CHAT_GET_UNREAD_COUNT: String = "chat_get_section_count"
    }

    private val options by lazy {
        IO.Options().apply {
            reconnection = true
        }
    }

    private val socket by lazy {
        IO.socket(baseUrl, options)
    }

    fun connect() {
        if (socket.connected()) {
            Timber.i("Socket is already connected")
            return
        }

        options.query = "token=${authorizationRepository.getAccessToken()}"

        socket.off(Socket.EVENT_CONNECTING)
        socket.off(Socket.EVENT_CONNECT)
        socket.off(Socket.EVENT_RECONNECT)
        socket.off(Socket.EVENT_RECONNECT_ERROR)
        socket.off(Socket.EVENT_RECONNECT_FAILED)
        socket.off(Socket.EVENT_RECONNECT_ATTEMPT)
        socket.off(Socket.EVENT_DISCONNECT)
        socket.off(Socket.EVENT_CONNECT_TIMEOUT)
        socket.off(Socket.EVENT_ERROR)
        socket.off(Socket.EVENT_CONNECT_ERROR)

        socket.on(Socket.EVENT_CONNECTING) { Timber.i("Socket connecting") }
        socket.on(Socket.EVENT_CONNECT) { Timber.i("Socket connected with id ${socket.id()}") }
        socket.on(Socket.EVENT_RECONNECT) { Timber.i("Socket reconnect") }
        socket.on(Socket.EVENT_RECONNECT_ERROR) { args -> Timber.i("Socket reconnect error ${args.firstOrNull()}") }
        socket.on(Socket.EVENT_RECONNECT_FAILED) { args -> Timber.i("Socket reconnect failed ${args.firstOrNull()}") }
        socket.on(Socket.EVENT_RECONNECT_ATTEMPT) { args -> Timber.i("Socket reconnect attempt ${args.firstOrNull()}") }
        socket.on(Socket.EVENT_DISCONNECT) { Timber.i("Socket disconnect") }
        socket.on(Socket.EVENT_CONNECT_TIMEOUT) { Timber.i("Socket connect timeout") }
        socket.on(Socket.EVENT_ERROR) { args -> Timber.i("Socket error ${args.firstOrNull()}") }
        socket.on(Socket.EVENT_CONNECT_ERROR) { args -> Timber.i("Socket connect error ${args.firstOrNull()}") }

        Timber.i("Connect")
        socket.disconnect()
        socket.connect()
    }

    fun disconnect() {
        Timber.i("Disconnect")
        socket.disconnect()
        socket.off()
    }

    fun onEvent(event: String, listener: Emitter.Listener) {
        Timber.i("Turning on event : $event")
        socket.on(event, listener)
    }

    fun offEvent(event: String) {
        Timber.i("Turning off event : $event")
        socket.off(event)
    }

    fun offEvent(event: String, listener: Emitter.Listener) {
        Timber.i("Turning off event : $event")
        socket.off(event, listener)
    }

    fun emit(
        event: String,
        argument: JSONObject,
        ack: Ack? = null
    ) {
        Timber.i("Emit on event: $event, $argument")
        socket.emit(event, argument, ack)
    }
}