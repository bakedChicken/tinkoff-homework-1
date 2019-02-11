package io.yoba.components

import android.app.IntentService
import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ContactService : IntentService("contact") {
    companion object {
        const val CONTACTS_COUNT_EVENT_NAME = "contacts-count-event"
        const val CONTACTS_COUNT_EXTRA = "contacts-count-extra"
    }

    private fun <T> Cursor.toList(block: Cursor.() -> T): List<T> {
        return arrayListOf<T>().apply {
            if (moveToFirst()) {
                do {
                    add(block(this@toList))
                } while (moveToNext())
            }
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        contentResolver.query(ContactsContract.Contacts.CONTENT_URI, arrayOf(ContactsContract.Contacts.DISPLAY_NAME), null, null, null).use {
            val contactsIntent = Intent(CONTACTS_COUNT_EVENT_NAME).apply {
                putExtra(CONTACTS_COUNT_EXTRA, it?.toList { getString(getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) }?.count())
            }

            LocalBroadcastManager.getInstance(this).sendBroadcast(contactsIntent)
        }
    }
}