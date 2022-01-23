package pt.nunopeixoto.wtest.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pt.nunopeixoto.wtest.db.WTestDatabase
import pt.nunopeixoto.wtest.repository.DataRepository
import pt.nunopeixoto.wtest.repository.DataRepositoryImpl

val appModule = module {
    single<DataRepository> { DataRepositoryImpl(get()) }

    single { WTestDatabase.create(androidContext()) }

    single { get<WTestDatabase>().postalCodeDao() }
}