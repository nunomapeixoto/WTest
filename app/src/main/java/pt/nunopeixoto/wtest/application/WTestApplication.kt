package pt.nunopeixoto.wtest.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import pt.nunopeixoto.wtest.di.appModule
import pt.nunopeixoto.wtest.di.viewModelModule

class WTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WTestApplication)
            modules(listOf(appModule, viewModelModule))
        }
    }
}