package chat.rocket.android.push

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.RemoteInput
import android.widget.Toast
import chat.rocket.android.R
import chat.rocket.android.server.infraestructure.ConnectionManagerFactory
import chat.rocket.common.RocketChatException
import chat.rocket.core.internal.rest.sendMessage
import dagger.android.AndroidInjection
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * BroadcastReceiver for direct reply on notifications.
 */
class DirectReplyReceiver : BroadcastReceiver() {

    @Inject
    lateinit var factory: ConnectionManagerFactory
    @Inject
    lateinit var groupedPushes: GroupedPush

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)
        if (ACTION_REPLY == intent.action) {
            val message = intent.getParcelableExtra<PushMessage>(EXTRA_PUSH_MESSAGE)
            message?.let {
                launch(UI) {
                    try {
                        println(it)
                        sendMessage(it, extractReplyMessage(intent))
                        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        clearNotificationsByHostAndNotificationId(it.info.host, it.notificationId.toInt())
                        manager.cancel(it.notificationId.toInt())
                        val feedback = context.getString(R.string.notif_success_sending, it.title)
                        Toast.makeText(context, feedback, Toast.LENGTH_SHORT).show()
                    } catch (ex: RocketChatException) {
                        Timber.e(ex)
                        val feedback = context.getString(R.string.notif_error_sending)
                        Toast.makeText(context, feedback, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private suspend fun sendMessage(message: PushMessage, replyText: CharSequence?) {
        replyText?.let { reply ->
            val currentServer = message.info.hostname
            val roomId = message.info.roomId
            val connectionManager = factory.create(currentServer)
            val client = connectionManager.client
            val id = UUID.randomUUID().toString()
            client.sendMessage(id, roomId, reply.toString())
            // Do we need to disconnect here?
        }
    }

    private fun extractReplyMessage(intent: Intent): CharSequence? {
        val bundle = RemoteInput.getResultsFromIntent(intent)
        if (bundle != null) {
            return bundle.getCharSequence(REMOTE_INPUT_REPLY)
        }
        return null
    }

    /**
     * Clear notifications by the host they belong to and its unique id.
     */
    private fun clearNotificationsByHostAndNotificationId(host: String, notificationId: Int) {
        if (groupedPushes.hostToPushMessageList.isNotEmpty()) {
            val notifications = groupedPushes.hostToPushMessageList[host]
            notifications?.let {
                notifications.removeAll {
                    it.notificationId.toInt() == notificationId
                }
            }
        }
    }
}