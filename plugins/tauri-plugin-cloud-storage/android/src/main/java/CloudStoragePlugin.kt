package app.tauri.cloud_storage

import android.app.Activity
import android.content.Intent
import android.provider.DocumentsContract
import androidx.activity.result.ActivityResult
import androidx.documentfile.provider.DocumentFile
import app.tauri.Logger
import app.tauri.annotation.ActivityCallback
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import android.os.ParcelFileDescriptor
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke
import android.net.Uri
// import com.google.api.services.drive.DriveScopes;

@InvokeArg
class WriteArgs {
    var fileUri: String? = null
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

        val ret = JSObject()
        ret.put("value", "prompt")
        invoke.resolve(ret)
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

        val folderUri = result.data?.data

        Logger.warn("##########", folderUri.toString())

        val folderDoc = folderUri?.let { uri ->
            DocumentFile.fromTreeUri(activity, uri)?.uri
        } ?: run {
            invoke.reject("treeUri could not be converted to DocumentFile")
        }

        Logger.warn("##########", folderDoc.toString())

        // val fd = activity.contentResolver.openAssetFileDescriptor(
        //         Uri.parse(folderUri.toString()),
        //         "MODE_READ_WRITE"
        //         //ParcelFileDescriptor.MODE_READ_WRITE
        //     )?.parcelFileDescriptor?.detachFd()

        // Logger.warn("##########", fd)

        val ret = JSObject()
        ret.put("alias", "Android")
        ret.put("path", folderDoc.toString())
        ret.put("path", folderUri.toString())
        // ret.put("path", fd)
        invoke.resolve(ret)
    }

    // @Command
    // fun write(invoke: Invoke) {
    //     val args = invoke.parseArgs(WriteOptions::class.java)

    //     Logger.warn("##### Writing to ...", folderUri.toString())

    //     args.fileUri

    //     val ret = JSObject()
    //     ret.put("value", "android_write")
    //     invoke.resolve(ret)
    // }

    @Command
    fun exists(invoke: Invoke) {
        val ret = JSObject()
        ret.put("provider", "Google Drive")
        ret.put("size", 0)
        ret.put("modificationDate", "1970-01-01T00:00:00Z")
        invoke.resolve(ret)
    }

    @Command
    fun getDir(invoke: Invoke) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            /*
            Key android.provider.extra.INITIAL_URI expected Parcelable but value was a java.lang.String.  The default value <null> was returned.
            Attempt to cast generated internal exception:
            java.lang.ClassCastException: java.lang.String cannot be cast to android.os.Parcelable
            */
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, "Documents/UniMe Backups")
        }
        startActivityForResult(invoke, intent, "openDocumentTreeResult")
    }
}
