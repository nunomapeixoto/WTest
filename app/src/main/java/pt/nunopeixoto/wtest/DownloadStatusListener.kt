package pt.nunopeixoto.wtest

interface DownloadStatusListener {
    fun onStatusChanged(downloadStatus: DownloadStatus)
}