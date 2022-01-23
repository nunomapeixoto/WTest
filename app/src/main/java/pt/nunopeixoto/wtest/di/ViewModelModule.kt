package pt.nunopeixoto.wtest.di


import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pt.nunopeixoto.wtest.viewmodel.MyViewModel

val viewModelModule = module {
    viewModel { MyViewModel(androidApplication(), get()) }
}