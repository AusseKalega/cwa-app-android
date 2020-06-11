package de.rki.coronawarnapp.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import de.rki.coronawarnapp.exception.ExceptionCategory
import de.rki.coronawarnapp.exception.reporting.report

/**
 * Helper object for external actions
 *
 */
object ExternalActionHelper {
    private val TAG: String? = ExternalActionHelper::class.simpleName

    /**
     * Opens the share default overlay to provide the Corona-Warn-App installation link
     *
     * @param fragment
     * @param text
     * @param title
     */
    fun shareText(fragment: Fragment, text: String, title: String?) {
        fragment.startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }, title))
    }

    /**
     * Opens the client default phone app and inserts a given number
     *
     * @param fragment
     * @param uri
     */
    fun call(fragment: Fragment, uri: String) {
        try {
            fragment.startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$uri")
                )
            )
        } catch (exception: Exception) {
            // catch generic exception on call
            // possibly due to bad number format
            exception.report(
                ExceptionCategory.UI,
                TAG,
                null
            )
        }
    }

    /**
     * Opens a given url in the client default browser
     *
     * @param fragment
     * @param url
     */
    fun openUrl(fragment: Fragment, url: String) {
        try {
            fragment.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
            )
        } catch (exception: Exception) {
            // catch generic exception on url navigation
            // most likely due to bad url format
            // or less likely no browser installed
            exception.report(
                ExceptionCategory.UI,
                TAG,
                null
            )
        }
    }

    /**
     * Navigate the user to the os connection settings.
     *
     * @param context
     */
    fun toConnections(context: Context) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        context.startActivity(intent)
    }

    /**
     * Navigate the user to the os notification settings.
     *
     * @param context
     */
    // todo has to be tested on API23 on a device
    fun toNotifications(context: Context) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(
                Settings.EXTRA_APP_PACKAGE,
                context.packageName
            )
        } else {
            intent.putExtra(
                "app_package",
                context.packageName
            )
            intent.putExtra("app_uid", context.applicationInfo.uid)
        }
        context.startActivity(intent)
    }
    // todo navigate storage settings
}
