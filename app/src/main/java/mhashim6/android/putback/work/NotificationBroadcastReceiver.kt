package mhashim6.android.putback.work

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import mhashim6.android.putback.RandomStrings.randomComment
import mhashim6.android.putback.data.NotionsRealm
import mhashim6.android.putback.debug
import mhashim6.android.putback.ui.MainActivity
import mhashim6.android.putback.ui.MainActivity.Companion.MAIN_ACTIVITY_SHOW_NOTION_ACTION

/**
 * Created by mhashim6 on 02/09/2018.
 */
class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notionId = intent.getStringExtra(NOTION_ID_EXTRA)
        val actionType = intent.getIntExtra(ACTION_TYPE, ACTION_TYPE_PUTBACK)
        debug("vroad $notionId")
        when (actionType) {
            ACTION_TYPE_PUTBACK -> {
                Toast.makeText(context, randomComment(), Toast.LENGTH_SHORT).show()
            }

            ACTION_TYPE_ARCHIVE -> {
                NotionsRealm.changeIdleState(notionId, true)
            }

            ACTION_TYPE_SHOW_CONTENT -> {
                val activityStarter = Intent(context, MainActivity::class.java).apply {
                    action = MAIN_ACTIVITY_SHOW_NOTION_ACTION
                    putExtra(NOTION_ID_EXTRA, notionId)
                }

                context.startActivity(activityStarter)
            }
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NotionsReminder.NOTION_NOTIFICATION_ID)

    }

    companion object {
        const val ACTION = "mhashim6.putback.NOTIFICATION"
        const val NOTION_ID_EXTRA = "NOTION_ID"
        const val ACTION_TYPE = "ACTION_TYPE"

        const val ACTION_TYPE_PUTBACK = 0
        const val ACTION_TYPE_ARCHIVE = 1
        const val ACTION_TYPE_SHOW_CONTENT = 2
    }

}