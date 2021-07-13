package com.android.fb

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.android.fb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create channel to show notifications.
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW))

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
            AlertDialog.Builder(this)
                .setTitle(it.get("message_title").toString())
                .setMessage(it.get("message_body").toString())
                .setPositiveButton("OK") { _: DialogInterface, _: Int ->}
                .setCancelable(true)
                .create().show()
        }

        binding.logTokenButton.setOnClickListener {
            if (checkGooglePlayServices()) {
                Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    val token = task.result
                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.i(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
            } else {
                //You won't be able to send notifications to this device
                Log.w(TAG, "Device doesn't have google play services")
            }
        }
    }

    private fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this)
        return if (status != ConnectionResult.SUCCESS) {
            Log.e("MainActivity: ", "Error")
            false
        } else {
            Log.i("MainActivity: ", "Google play services updated")
            true
        }
    }

    companion object {

        private const val TAG = "MainActivity: "
    }
}