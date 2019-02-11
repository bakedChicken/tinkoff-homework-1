package io.yoba.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class FirstActivity : AppCompatActivity() {
    companion object {
        const val CONTACTS_PERMISSION_REQUEST = 0x100
    }

    private val getResultButton by lazy {
        findViewById<Button>(R.id.first_activity_get_result_button)
    }

    private val resultTextView by lazy {
        findViewById<TextView>(R.id.first_activity_result_text_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)

        getResultButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@FirstActivity,
                    Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(this@FirstActivity,
                        Manifest.permission.READ_CONTACTS)) {
                    ActivityCompat.requestPermissions(this@FirstActivity,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        CONTACTS_PERMISSION_REQUEST)
                }
            } else {
                startActivity()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CONTACTS_PERMISSION_REQUEST) {
            if (permissions.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                startActivity()
            } else {
                Toast.makeText(this, R.string.first_activity_contacts_permission_denied_text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK
            && requestCode == SecondActivity.REQUEST_CODE
            && data != null) {

            val contactsCount = data.getIntExtra(SecondActivity.CONTACTS_COUNT_EXTRA, -1)
            resultTextView.visibility = View.VISIBLE
            resultTextView.text = resources.getQuantityString(R.plurals.first_activity_contacts_result, contactsCount, contactsCount)
        }
    }

    private fun startActivity() {
        val intent = Intent(this@FirstActivity, SecondActivity::class.java)
        startActivityForResult(intent, SecondActivity.REQUEST_CODE)
    }
}