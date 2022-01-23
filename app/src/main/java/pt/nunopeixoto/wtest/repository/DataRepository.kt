package pt.nunopeixoto.wtest.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import pt.nunopeixoto.wtest.DownloadStatusListener
import pt.nunopeixoto.wtest.db.entity.PostalCode

interface DataRepository {
    suspend fun startDownload(context: Context, downloadStatusListener: DownloadStatusListener)
    suspend fun parseCsv(fileName: String, context: Context, downloadStatusListener: DownloadStatusListener)
    fun searchDatabase(searchQuery: String): Flow<List<PostalCode>>
}