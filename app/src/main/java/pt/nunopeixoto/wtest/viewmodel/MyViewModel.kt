package pt.nunopeixoto.wtest.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pt.nunopeixoto.wtest.DownloadStatus
import pt.nunopeixoto.wtest.DownloadStatusListener
import pt.nunopeixoto.wtest.db.entity.PostalCode
import pt.nunopeixoto.wtest.repository.DataRepository

class MyViewModel(
    application: Application,
    private val dataRepository: DataRepository
): AndroidViewModel(application) {

    private val _downloadStatusObservable: MutableLiveData<DownloadStatus> = MutableLiveData()
    val downloadStatusObservable: LiveData<DownloadStatus> = _downloadStatusObservable

    fun searchDatabaseObservable(searchQuery: String): LiveData<List<PostalCode>> {
        return dataRepository.searchDatabase(searchQuery).asLiveData()
    }

    fun startDownload() {
        viewModelScope.launch {
            dataRepository.startDownload(getApplication(), object : DownloadStatusListener {

                override fun onStatusChanged(downloadStatus: DownloadStatus) {
                    _downloadStatusObservable.value = downloadStatus
                }

            })
        }
    }
}