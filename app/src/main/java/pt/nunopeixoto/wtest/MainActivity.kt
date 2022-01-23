package pt.nunopeixoto.wtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import pt.nunopeixoto.wtest.databinding.ActivityMainBinding
import pt.nunopeixoto.wtest.utils.SharedPrefUtil
import pt.nunopeixoto.wtest.viewmodel.MyViewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding

    private var postalCodeAdapter = PostalCodeAdapter(emptyList())

    private val myViewModel by viewModel<MyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = postalCodeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        //Verifica se o download não completou, caso não tenha completado e não está a correr inicia o download, caso
        //contrário esconde as views de download
        if (SharedPrefUtil.getCurrentDownloadState(this) != SharedPrefUtil.DOWNLOAD_COMPLETE) {
            initObservers()
            if (SharedPrefUtil.getCurrentDownloadState(this) != SharedPrefUtil.DOWNLOAD_RUNNING) {
                startDownload()
            }
        } else {
            hideDownloadViews()
        }
    }

    private fun startDownload() {
        binding.indicatorProgress.show()
        SharedPrefUtil.setCurrentDownloadState(this, SharedPrefUtil.DOWNLOAD_RUNNING)
        myViewModel.startDownload()
    }

    private fun hideDownloadViews() {
        binding.indicatorProgress.hide()
        binding.textViewProgress.visibility = View.GONE
        binding.searchView.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.searchView.setOnQueryTextListener(this)
    }

    /**
     *inicia obervação do downloadStatusObsevable para atualizar a UI de acordo com o
     * estado do download do ficheiro csv
     */
    private fun initObservers() {
        myViewModel.downloadStatusObservable.observe(this, {
            when (it) {
                DownloadStatus.DOWNLOAD_FAILED -> {
                    binding.indicatorProgress.hide()
                }
                DownloadStatus.DOWNLOAD_SUCCESS -> {
                    binding.textViewProgress.text = getString(R.string.text_view_progress_updating_db)
                }
                DownloadStatus.DB_FAILED -> {

                }
                DownloadStatus.DB_SUCCESS -> {
                    SharedPrefUtil.setCurrentDownloadState(this, SharedPrefUtil.DOWNLOAD_COMPLETE)
                    hideDownloadViews()
                }
                else -> {}
            }
        })
    }

    /**
     * @param query query usado na pesquisa da BD
     * e inicia obervação do searchDatabaseObervable para
     */
    private fun searchDatabase(query: String) {

        myViewModel.searchDatabaseObservable(query).observe(this, { list ->
            list.let {
                postalCodeAdapter.setData(it)
            }
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    /**
     * @param query recebido da searchView usado para a pesquisa na BD
     * verifica se é diferente de nulo e se tem pelo menos 2 caracteres
     */
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null && query.length > 1) {
            searchDatabase(query)
        }
        return true
    }

}