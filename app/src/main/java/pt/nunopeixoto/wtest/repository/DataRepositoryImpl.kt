package pt.nunopeixoto.wtest.repository


import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.sqlite.db.SimpleSQLiteQuery
import com.opencsv.CSVReaderHeaderAware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.nunopeixoto.wtest.DownloadStatus
import pt.nunopeixoto.wtest.DownloadStatusListener
import pt.nunopeixoto.wtest.db.dao.PostalCodeDao
import pt.nunopeixoto.wtest.db.entity.PostalCode
import pt.nunopeixoto.wtest.utils.removeNonSpacingMarks
import java.io.File
import java.io.FileReader
import java.util.*


class DataRepositoryImpl(private val postalCodeDao: PostalCodeDao): DataRepository {

    /**
     * @param downloadStatusListener usado para devolver o estado do download.
     */
    override suspend fun startDownload(context: Context, downloadStatusListener: DownloadStatusListener) {
        withContext(IO) {
            val url = "https://raw.githubusercontent.com/centraldedados/codigos_postais/master/data/codigos_postais.csv"
            val fileName = "csv.csv"
            val request = DownloadManager.Request(Uri.parse(url))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDescription("A descarregar")
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
            val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val downloadID = downloadManager.enqueue(request)

            var finishDownload = false
            while (!finishDownload) {
                val cursor: Cursor =
                    downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
                if (cursor.moveToFirst()) {
                    val status: Int =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when (status) {
                        DownloadManager.STATUS_FAILED -> {
                            finishDownload = true
                            withContext(Main) { downloadStatusListener.onStatusChanged(DownloadStatus.DOWNLOAD_FAILED) }
                        }
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            finishDownload = true
                            parseCsv(fileName, context, downloadStatusListener)
                            withContext(Main) { downloadStatusListener.onStatusChanged(DownloadStatus.DOWNLOAD_SUCCESS) }

                        }
                    }
                }
            }
        }

    }

    /**
     * @param searchQuery string que contem as palavras a usar na query da BD.
     * Separa cada palavra da searchQuery para criar a clausula WHERE da query, de modo a poder
     * comparar cada palavra da String com cada coluna da tabela.
     * @return devolve um Flow da listagem obtida com o query de modo a poder ser observado.
     */
    override fun searchDatabase(searchQuery: String): Flow<List<PostalCode>> {
        val queryScanner = Scanner(searchQuery.replace("-", " "))
        val listQuery = mutableListOf<String>()
        while (queryScanner.hasNext()) {
            listQuery.add(queryScanner.next())
        }
        var whereClause = ""
        for (word in listQuery) {
            whereClause += if (whereClause.isEmpty())
                "(name LIKE '%$word%' OR code LIKE '%$word%' OR extCode LIKE '%$word%' OR normalizedName LIKE '%$word%')"
            else
                " AND (name LIKE '%$word%' OR code LIKE '%$word%' OR extCode LIKE '%$word%' OR normalizedName LIKE '%$word%')"
        }

        val rawQuery = SimpleSQLiteQuery("SELECT * FROM postalcode WHERE $whereClause")
        return postalCodeDao.getSearchQueryPostalCodes(rawQuery)
    }


    /**
     * @param downloadStatusListener usado para devolver o estado da atualização da BD.
     * Faz o parsing do ficheiro csv para depois inserir a lista de [PostalCode] na BD.
     */
     override suspend fun parseCsv(fileName: String, context: Context, downloadStatusListener: DownloadStatusListener) {

         CoroutineScope(Default).launch {
             kotlin.runCatching {
                 val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

                 val reader = CSVReaderHeaderAware(FileReader(file))
                 val list = mutableListOf<PostalCode>()
                 var line = reader.readNext()
                 while (line != null) {
                     list.add(
                         PostalCode(
                             line[3],
                             line[3].removeNonSpacingMarks(),
                             line[14],
                             line[15]
                         )
                     )
                     line = reader.readNext()
                 }
                 postalCodeDao.insertPostalCodeList(list)
             }.onFailure {
                 it.printStackTrace()
                 withContext(Main) { downloadStatusListener.onStatusChanged(DownloadStatus.DB_FAILED) }
             }.onSuccess {
                 withContext(Main) { downloadStatusListener.onStatusChanged(DownloadStatus.DB_SUCCESS) }
             }
        }
    }

}