package com.illuminz.application.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.illuminz.application.R
import com.illuminz.data.models.common.FcmNotification
import timber.log.Timber

object NotificationUtils {
    // Notification parameters
    const val PARAM_TYPE = "type"
    private const val PARAM_TITLE = "title"
    private const val PARAM_MESSAGE = "body"
    private const val PARAM_MESSAGE_ID = "messageId"
    private const val PARAM_IMAGE_URL = "imageUrl"
    private const val PARAM_ACTION_ID = "actionId"
    private const val PARAM_TAG = "tag"
    private const val PARAM_LAST_DASHBOARD = "lastDashboardState"

    // Notification types
    const val NOTIFICATION_TEACHER_CATEGORY_REJECTED = "teacher_category_rejected"
    const val NOTIFICATION_TEACHER_CLASS_ABOUT_TO_END = "teacher_class_about_to_end"
    const val NOTIFICATION_TEACHER_SOMEONE_STARTED_FOLLOWING =
        "teacher_someone_started_following"
    const val NOTIFICATION_TEACHER_BOOKING_RECEIVED = "teacher_booking_received"
    const val NOTIFICATION_TEACHER_PROFILE_APPROVED = "teacher_profile_approved"
    const val NOTIFICATION_TEACHER_PROFILE_REJECTED = "teacher_profile_rejected"
    const val NOTIFICATION_TEACHER_CATEGORY_APPROVED = "teacher_category_approved"
    const val NOTIFICATION_STUDENT_INSTANT_CLASS_AVAILABLE = "instant_class_available"
    const val NOTIFICATION_STUDENT_CLASS_ABOUT_TO_END = "student_class_about_to_end"
    const val NOTIFICATION_STUDENT_TEACHER_DID_NOT_JOIN = "student_teacher_did_not_join"
    const val NOTIFICATION_TEACHER_CLASS_CANCELLED = "teacher_class_cancelled"
    private const val NOTIFICATION_NEW_MESSAGE = "common_new_message"
    const val NOTIFICATION_TEACHER_CLASS_EXPIRED = "teacher_class_expired"
    const val NOTIFICATION_TEACHER_SCHEDULE_BOOKING_RECEIVED =
        "teacher_schedule_booking_received"
    const val NOTIFICATION_TEACHER_SCHEDULE_CLASS_ABOUT_TO_START =
        "teacher_schedule_class_about_to_start"
    const val NOTIFICATION_STUDENT_SCHEDULE_CLASS_ABOUT_TO_START =
        "student_schedule_class_about_to_start"
    const val NOTIFICATION_STUDENT_TEACHER_HAS_INSTANT_CLASS =
        "student_teacher_has_instant_class"
    const val NOTIFICATION_STUDENT_TEACHER_HAS_SCHEDULE_CLASS =
        "student_teacher_has_schedule_class"
    const val NOTIFICATION_TEACHER_SCHEDULE_CLASS_STARTED =
        "teacher_schedule_class_started"
    const val NOTIFICATION_STUDENT_SCHEDULE_CLASS_STARTED = "student_schedule_class_started"
    private const val NOTIFICATION_STUDENT_RESCHEDULE_CLASS_REQUEST = "student_reschedule_request"
    private const val NOTIFICATION_TEACHER_RESCHEDULE_CLASS_REQUEST = "teacher_reschedule_request"
    private const val NOTIFICATION_STUDENT_RESCHEDULE_CLASS_APPROVAL = "student_reschedule_approval"
    private const val NOTIFICATION_TEACHER_RESCHEDULE_CLASS_APPROVAL = "teacher_reschedule_approval"
    private const val NOTIFICATION_ID = 0

    /**
     * @return FcmNotification from the provided map that was received from the backend
     * */
    private fun getFcmNotification(map: Map<String, String>): FcmNotification {
        return FcmNotification(
            map[PARAM_TYPE],
            map[PARAM_TITLE],
            map[PARAM_MESSAGE],
            map[PARAM_MESSAGE_ID],
            map[PARAM_IMAGE_URL],
            map[PARAM_TAG],
            map[PARAM_ACTION_ID],
            map[PARAM_LAST_DASHBOARD]
        )
    }

    /**
     * @return FcmNotification from the bundle of the activity which was launched from notification click
     * when app was in background and system created the notification. This bundle contains the notification data payload.
     * */
    fun getFcmNotification(extras: Bundle): FcmNotification {
        Timber.d(extras.toString())
        return FcmNotification(
            extras.getString(PARAM_TYPE),
            extras.getString(PARAM_TITLE),
            extras.getString(PARAM_MESSAGE),
            extras.getString(PARAM_MESSAGE_ID),
            extras.getString(PARAM_IMAGE_URL),
            extras.getString(PARAM_TAG),
            extras.getString(PARAM_ACTION_ID),
            extras.getString(PARAM_LAST_DASHBOARD)
        )
    }

    /**
     * Used when the notification is received in firebase service
     *
     * @param context - Should be application context otherwise notification might not work properly
     * @param map - Map that is received in the data of remote message.
     * */
    fun handleRemoteNotification(context: Context, map: Map<String, String>) {
        val notification = getFcmNotification(map)
        // val imageBitmap = getBitmapFromImageUrl(context, notification.imageUrl)
        showNotification(context, notification, null)
    }

    private fun showNotification(
        context: Context,
        notification: FcmNotification,
        image: Bitmap? = null
    ) {
        val pendingIntent = getNotificationPendingIntent(context, notification)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                .also {
                    notificationManager.createNotificationChannel(it)
                }
        }

        val notificationStyle: NotificationCompat.Style =
            if (image != null) {
                NotificationCompat.BigPictureStyle()
                    .bigPicture(image)
                    .bigLargeIcon(null)
            } else {
                NotificationCompat.BigTextStyle()
                    .bigText(notification.message)
            }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setShowWhen(true)
            /*.setLargeIcon(image)*/
            .setStyle(notificationStyle)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notification.tag, NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationPendingIntent(
        context: Context,
        notification: FcmNotification
    ): PendingIntent {
        val stackBuilder = getNotificationStackBuilder(context, notification)
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            ?: throw IllegalArgumentException("Pending intent for notification is null")
    }

    fun getNotificationStackBuilder(
        context: Context,
        notification: FcmNotification
    ): TaskStackBuilder {
        val stackBuilder = TaskStackBuilder.create(context)
        /* when (notification.type) {
             NOTIFICATION_STUDENT_INSTANT_CLASS_AVAILABLE, NOTIFICATION_STUDENT_TEACHER_HAS_INSTANT_CLASS -> {
                 val mainIntent = Intent(context, StudentMainActivity::class.java)
                 stackBuilder.addNextIntent(mainIntent)
                 val classDetailIntent =
                     InstantClassDetailsActivity.getStartIntent(context, notification.actionId)
                 stackBuilder.addNextIntent(classDetailIntent)
             }

             NOTIFICATION_STUDENT_TEACHER_HAS_SCHEDULE_CLASS -> {
                 val mainIntent = Intent(context, StudentMainActivity::class.java)
                 stackBuilder.addNextIntent(mainIntent)
                 val classDetailIntent =
                     ScheduleClassDetailActivity.getStartIntent(
                         context,
                         notification.actionId.orEmpty()
                     )
                 stackBuilder.addNextIntent(classDetailIntent)
             }

             NOTIFICATION_STUDENT_CLASS_ABOUT_TO_END,
             NOTIFICATION_STUDENT_SCHEDULE_CLASS_ABOUT_TO_START -> {
                 val mainIntent = Intent(context, StudentMainActivity::class.java)
                 stackBuilder.addNextIntent(mainIntent)
                 *//*   val classDetailIntent =
                       InstantClassDetailsActivity.getStartIntent(context, notification.actionId)
                   stackBuilder.addNextIntent(classDetailIntent)*//*
            }

            NOTIFICATION_TEACHER_PROFILE_APPROVED,
            NOTIFICATION_TEACHER_PROFILE_REJECTED, NOTIFICATION_TEACHER_CATEGORY_APPROVED,
            NOTIFICATION_TEACHER_SOMEONE_STARTED_FOLLOWING,
            NOTIFICATION_TEACHER_CLASS_EXPIRED,
            NOTIFICATION_TEACHER_SCHEDULE_BOOKING_RECEIVED,
            NOTIFICATION_TEACHER_SCHEDULE_CLASS_ABOUT_TO_START,
            NOTIFICATION_TEACHER_CATEGORY_REJECTED, NOTIFICATION_TEACHER_CLASS_ABOUT_TO_END -> {
                val mainIntent = Intent(context, TeacherMainActivity::class.java)
                stackBuilder.addNextIntent(mainIntent)
            }

            NOTIFICATION_TEACHER_BOOKING_RECEIVED, NOTIFICATION_TEACHER_SCHEDULE_CLASS_STARTED -> {
                val mainIntent = Intent(context, TeacherMainActivity::class.java)
                stackBuilder.addNextIntent(mainIntent)
                *//*val onlineClassIntent = OnlineClassActivity.getIntent(
                    context,
                    UserType.TEACHER.name,
                    notification.actionId
                )
                stackBuilder.addNextIntent(onlineClassIntent)*//*
                val connectionCheckIntent = ConnectionCheckActivity.getIntent(
                    context, UserType.TEACHER.name, notification.actionId
                )
                stackBuilder.addNextIntent(connectionCheckIntent)
            }

            NOTIFICATION_STUDENT_SCHEDULE_CLASS_STARTED -> {
                val mainIntent = Intent(context, StudentMainActivity::class.java)
                stackBuilder.addNextIntent(mainIntent)
                *//* val onlineClassIntent = OnlineClassActivity.getIntent(
                     context,
                     UserType.STUDENT.name,
                     notification.actionId
                 )
                 stackBuilder.addNextIntent(onlineClassIntent)*//*
                val connectionCheckIntent = ConnectionCheckActivity.getIntent(
                    context, UserType.STUDENT.name, notification.actionId
                )
                stackBuilder.addNextIntent(connectionCheckIntent)
            }

            NOTIFICATION_STUDENT_TEACHER_DID_NOT_JOIN -> {
                //open student detail page
                val mainIntent = Intent(context, StudentMainActivity::class.java)
                mainIntent.putExtra(
                    StudentMainActivity.KEY_TAB_INDEX,
                    MainTabLayout.TAB_INDEX_MY_CLASSES
                )
                mainIntent.putExtra("type", "past")
                mainIntent.putExtra(AppConstants.BOOKING_ID, notification.actionId)
                stackBuilder.addNextIntent(mainIntent)
            }

            NOTIFICATION_TEACHER_RESCHEDULE_CLASS_APPROVAL -> {
                //open student detail page
                val mainIntent = Intent(context, StudentMainActivity::class.java)
                mainIntent.putExtra(
                    StudentMainActivity.KEY_TAB_INDEX,
                    MainTabLayout.TAB_INDEX_MY_CLASSES
                )
                mainIntent.putExtra("type", "upcoming")
                mainIntent.putExtra(AppConstants.BOOKING_ID, notification.actionId)
                stackBuilder.addNextIntent(mainIntent)
            }

            NOTIFICATION_STUDENT_RESCHEDULE_CLASS_APPROVAL -> {
                //open teacher detail page
                val mainIntent = Intent(context, TeacherMainActivity::class.java)
                mainIntent.putExtra(
                    TeacherMainActivity.KEY_TAB_INDEX,
                    TeacherMainActivity.TAB_INDEX_BOOKING
                )
                mainIntent.putExtra("type", "upcoming")
                mainIntent.putExtra(AppConstants.BOOKING_ID, notification.actionId)
                stackBuilder.addNextIntent(mainIntent)
            }

            NOTIFICATION_TEACHER_CLASS_CANCELLED -> {
                //open teacher detail page
                val mainIntent = Intent(context, TeacherMainActivity::class.java)
                mainIntent.putExtra(
                    TeacherMainActivity.KEY_TAB_INDEX,
                    TeacherMainActivity.TAB_INDEX_BOOKING
                )
                mainIntent.putExtra("type", "past")
                mainIntent.putExtra(AppConstants.BOOKING_ID, notification.actionId)
                stackBuilder.addNextIntent(mainIntent)
            }

            NOTIFICATION_STUDENT_RESCHEDULE_CLASS_REQUEST -> {
                //open teacher detail page
                val mainIntent = Intent(context, TeacherMainActivity::class.java)
                mainIntent.putExtra(
                    TeacherMainActivity.KEY_TAB_INDEX,
                    TeacherMainActivity.TAB_INDEX_BOOKING
                )
                mainIntent.putExtra("type", "upcoming")
                mainIntent.putExtra(AppConstants.BOOKING_ID, notification.actionId)
                stackBuilder.addNextIntent(mainIntent)
            }
            NOTIFICATION_TEACHER_RESCHEDULE_CLASS_REQUEST -> {
                //open teacher detail page
                val mainIntent = Intent(context, StudentMainActivity::class.java)
                mainIntent.putExtra(
                    StudentMainActivity.KEY_TAB_INDEX,
                    MainTabLayout.TAB_INDEX_MY_CLASSES
                )
                mainIntent.putExtra("type", "upcoming")
                mainIntent.putExtra(AppConstants.BOOKING_ID, notification.actionId)
                stackBuilder.addNextIntent(mainIntent)
            }
            NOTIFICATION_NEW_MESSAGE -> {
                //open Chat activity
                var mainIntent: Intent? = null
                mainIntent = if (UserType.TEACHER.type == notification.lastDashboard) {
                    Intent(context, TeacherMainActivity::class.java)
                } else {
                    Intent(context, StudentMainActivity::class.java)
                }
                stackBuilder.addNextIntent(mainIntent)
                val chatIntent = ChatMessageActivity.getChatIntent(
                    context,
                    notification.actionId.orEmpty(),
                    null
                )
                stackBuilder.addNextIntent(chatIntent)
            }
        }*/
        return stackBuilder
    }
}