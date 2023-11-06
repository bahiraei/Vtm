package ir.adak.Vect.UI.Activities.ShareFilesActivity

import android.content.Intent
import android.os.Bundle
import ir.adak.Vect.R
import ir.adak.Vect.UI.Activities.BaseActivity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.io.File


class ShareFilesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_files)

        val receiverdIntent = intent
        val receivedAction = receiverdIntent.action
        val receivedType = receiverdIntent.type

        if (receivedAction.equals(Intent.ACTION_SEND)) {

            // check mime type
            if (receivedType?.startsWith("text/") == true) {

                val receivedText = receiverdIntent
                    .getStringExtra(Intent.EXTRA_TEXT)
                if (receivedText != null) {
                    //do your stuff
                    Toast.makeText(this, receivedText, Toast.LENGTH_LONG).show()
                }
            } else if (receivedType?.startsWith("image/") == true) {

                val receiveUri = receiverdIntent.getParcelableExtra(Intent.EXTRA_STREAM) as Uri

                //do your stuff
                val fileUri = receiveUri // save to your own Uri object
                Toast.makeText(this, File(fileUri.path).name, Toast.LENGTH_LONG).show()
                Log.e(TAG, receiveUri.toString())
            }
        }
    }
}
