package app.tauri.cloud_storage

import android.app.Activity
import android.content.Intent
import android.provider.DocumentsContract
import androidx.activity.result.ActivityResult
import app.tauri.Logger
import app.tauri.annotation.ActivityCallback
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke
// import com.google.api.services.drive.DriveScopes;

@InvokeArg
class PingArgs {
  var value: String? = null
}

@TauriPlugin
class CloudStoragePlugin(private val activity: Activity): Plugin(activity) {
    // private val implementation = Example()

    companion object {
        // const val SCOPE = DriveScopes.DRIVE_METADATA_READONLY
        // const val CREATE_FILE = 1
        const val ACTION_OPEN_DOCUMENT_TREE = "ACTION_OPEN_DOCUMENT_TREE"
    }

    @Command
    override fun checkPermissions(invoke: Invoke) {
        // val intent = activity.intent
        /*
        val intent = Intent(
            Intent.ACTION_CREATE_DOCUMENT
            //Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(invoke, intent, "openSettingsResult")
        */

        /*
        val pickerInitialUri = ""
        val createFileIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            type = "text/plain" // "application/octet-stream" // or "text/plain", etc.
            putExtra(Intent.EXTRA_TITLE, "test.txt")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }

        startActivityForResult(invoke, createFileIntent, REQUEST_CREATE_FILE)
        */

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, "Documents/UniMe Backups")
        }
        startActivityForResult(invoke, intent, "openDocumentTreeResult")
    }

    @ActivityCallback
    private fun openDocumentTreeResult(invoke: Invoke, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_CANCELED) {
            invoke.reject(
                "The system canceled action_open_document_tree",
                "systemCancel"
            )
            return
        }

        Logger.warn(result.data.toString())

        val res = JSObject()
        res.put("value", "foo_openDocumentTreeResult")
        invoke.resolve(res)
    }

    @Command
    fun ping(invoke: Invoke) {
        val args = invoke.parseArgs(PingArgs::class.java)

        val ret = JSObject()
        ret.put("value", "pong from android")
        invoke.resolve(ret)
    }

    @Command
    fun write(invoke: Invoke) {
        val ret = JSObject()
        ret.put("value", "android_write")
        invoke.resolve(ret)
    }

    @Command
    fun exists(invoke: Invoke) {
        val ret = JSObject()
        ret.put("provider", "Google Drive")
        ret.put("size", 0)
        ret.put("modificationDate", "1970-01-01T00:00:00Z")
        invoke.resolve(ret)
    }
}
