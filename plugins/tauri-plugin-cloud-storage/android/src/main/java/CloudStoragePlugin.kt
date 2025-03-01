package app.tauri.cloud_storage

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.provider.DocumentsContract
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.credentials.CredentialManager
import androidx.documentfile.provider.DocumentFile
import app.tauri.Logger
import app.tauri.annotation.ActivityCallback
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.Invoke
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException

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
        // Tauri's @ActivityCallback matches by function name,
        // whereas "startIntentSenderForResult" request code needs to be an Int
        val REQUEST_AUTHORIZE = "requestAuthorize".hashCode() and 0xFFFF
    }

    @Command
    override fun checkPermissions(invoke: Invoke) {
        Logger.warn("##########", "Checking permissions")

        val credentialManager = CredentialManager.create(activity)

        foo(invoke, activity)

        // val credentials = getCredentials()

//        if (credentials != null) {
//            Logger.warn("##########", credentials.authenticationType)
//        } else {
//            Logger.warn("########## credentials are null")
//        }

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

    /**
     * https://developer.android.com/identity/authorization#request_permissions_required_by_user_actions
     */
    private fun foo(invoke: Invoke, activity: Activity) {
        Logger.warn("########## Starting foo() ...")
        val requestedScopes: List<Scope> = listOf(Scope(DriveScopes.DRIVE_APPDATA))
        val authorizationRequest: AuthorizationRequest =
            AuthorizationRequest.builder().setRequestedScopes(requestedScopes).build()
        Identity.getAuthorizationClient(activity)
            .authorize(authorizationRequest)
            .addOnSuccessListener { authorizationResult ->
                Logger.info("########## authorizationResult",
                    authorizationResult.hasResolution().toString()
                )
                if (authorizationResult.hasResolution()) {
                    // Access needs to be granted by the user
                    val pendingIntent: PendingIntent = authorizationResult.pendingIntent!!
                    try {
//                        startActivityForResult(invoke, activity.intent, "requestAuthorize")
                        startIntentSenderForResult(
                            activity,
                            pendingIntent.intentSender, REQUEST_AUTHORIZE, null, 0, 0, 0, null
                        )
                    } catch (e: SendIntentException) {
                        Logger.warn("Couldn't start Authorization UI: " + e.localizedMessage)
                    }
                } else {
                    // Access already granted, continue with user action
                    doSomething(authorizationResult)
                }
            }
            .addOnFailureListener { e -> Logger.warn("Failed to authorize", e.localizedMessage) }
    }

    private fun doSomething(authorizationResult: AuthorizationResult) {
        Logger.info("########## Do something ...")
    }

    @Throws(IOException::class)
    private fun getCredentials(): GoogleCredentials? {
        var credentials: GoogleCredentials? = null

        try {
            credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(listOf(DriveScopes.DRIVE_APPDATA))
        } catch (e: IOException) {
            // e.printStackTrace()
            Logger.warn("##########", e.printStackTrace().toString())
        }

        return credentials
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

    @ActivityCallback
    private fun requestAuthorize(invoke: Invoke, result: ActivityResult) {
        Logger.info("########## requestAuthorize", result.toString())
        // TODO: why is the result cancelled?
        if (result.resultCode == Activity.RESULT_CANCELED) {
            Logger.info("########## requestAuthorize, RESULT_CANCELED")
            invoke.reject(
                "The system canceled action_request_authorize",
                "systemCancel"
            )
            return
        }
        Logger.info("########## requestAuthorize, getAuthorizationResultFromIntent ...")
        val authorizationResult = Identity.getAuthorizationClient(
            activity
        ).getAuthorizationResultFromIntent(result.data)
        Logger.info("########## requestAuthorize, authorizationResult", authorizationResult.toString())
        doSomething(authorizationResult)
    }

    /*@Override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if (requestCode == MainActivity.REQUEST_AUTHORIZE) {
            val authorizationResult = Identity.getAuthorizationClient(
                this
            ).getAuthorizationResultFromIntent(data)
            saveToDriveAppFolder(authorizationResult)
        }
    }*/

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
        // startActivityForResult(invoke, intent, "openDocumentTreeResult")
        val ret = JSObject()
        ret.put("path", "none")
        invoke.resolve(ret)
    }
}
