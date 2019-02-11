package io.yoba.components

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class SecondActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 0x100
        const val CONTACTS_COUNT_EXTRA = "contacts-count-extra"
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, contactsIntent: Intent?) {
            val count = contactsIntent?.getIntExtra(ContactService.CONTACTS_COUNT_EXTRA, -1)
            val resultIntent = intent.apply {
                putExtra(CONTACTS_COUNT_EXTRA, count)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver, IntentFilter(ContactService.CONTACTS_COUNT_EVENT_NAME))

        val contactServiceIntent = Intent(this, ContactService::class.java)
        startService(contactServiceIntent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }
}