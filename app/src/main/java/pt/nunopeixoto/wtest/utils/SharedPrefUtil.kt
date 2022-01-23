package pt.nunopeixoto.wtest.utils

import android.app.Activity
import android.content.Context

object SharedPrefUtil {

    const val DOWNLOAD_NOT_COMPLETED = 0
    const val DOWNLOAD_COMPLETE = 1
    const val DOWNLOAD_RUNNING = 2

    private const val CURRENT_DOWNLOAD_STATE_KEY = "downloadState"

    /**
     * @return o estado do download inicial
     */
    fun getCurrentDownloadState(activity: Activity): Int {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getInt(CURRENT_DOWNLOAD_STATE_KEY, DOWNLOAD_NOT_COMPLETED)
    }

    /**
     * @param state guarda o estado do download incial
     */
    fun setCurrentDownloadState(activity: Activity, state: Int) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putInt(CURRENT_DOWNLOAD_STATE_KEY, state).apply()
    }

}