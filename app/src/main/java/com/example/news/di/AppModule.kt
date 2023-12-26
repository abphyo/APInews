package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.news.common.Constants
import com.example.news.data.local.NewsDB
import com.example.news.data.local.dao.NewDao
import com.example.news.data.remote.NewsApi
import com.example.news.data.remote.api.HeaderInterceptor
import com.example.news.data.remote.api.LoggingInterceptor
import com.example.news.data.repository.DatabaseRepoImpl
import com.example.news.data.repository.RemoteRepoImpl
import com.example.news.domain.use_case.DeleteFromDatabase
import com.example.news.domain.use_case.GetFromDatabase
import com.example.news.domain.use_case.GetHeadlines
import com.example.news.domain.use_case.GetByHeadlinesSearch
import com.example.news.domain.use_case.GetPublishers
import com.example.news.domain.use_case.SaveToDatabase
import com.example.news.presentation.utils.ConnectivityObserver
import com.example.news.presentation.utils.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext appContext: Context): ChuckerInterceptor {
        val chuckerCollector = ChuckerCollector(
            context = appContext,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
        return ChuckerInterceptor.Builder(appContext)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .createShortcut(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideClient(chucker: ChuckerInterceptor): OkHttpClient {
        val loggingInterceptor = LoggingInterceptor()
        val headerInterceptor = HeaderInterceptor(Constants.API_KEY)
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chucker)
            .build()
    }
    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): NewsApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideRemoteRepoImpl(api: NewsApi): RemoteRepoImpl {
        return RemoteRepoImpl(api)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepoImpl(dao: NewDao): DatabaseRepoImpl {
        return DatabaseRepoImpl(dao)
    }

//    @Provides
//    @Singleton
//    fun provideGetByCategory(remoteRepo: RemoteRepoImpl): GetHeadlines {
//        return GetHeadlines(remoteRepo)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGetBySearch(remoteRepo: RemoteRepoImpl): GetByHeadlinesSearch {
//        return GetByHeadlinesSearch(remoteRepo)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGetPublishers(remoteRepo: RemoteRepoImpl): GetPublishers {
//        return GetPublishers(remoteRepo)
//    }
//
//    @Provides
//    @Singleton
//    fun provideGetByDatabase(databaseRepo: DatabaseRepoImpl): GetFromDatabase {
//        return GetFromDatabase(databaseRepo)
//    }
//
//    @Provides
//    @Singleton
//    fun provideSaveToDatabase(databaseRepo: DatabaseRepoImpl): SaveToDatabase {
//        return SaveToDatabase(databaseRepo)
//    }
//
//    @Provides
//    @Singleton
//    fun provideDeleteFromDatabase(databaseRepo: DatabaseRepoImpl): DeleteFromDatabase {
//        return DeleteFromDatabase(databaseRepo)
//    }

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): NewsDB {
        return Room.databaseBuilder(
            appContext,
            NewsDB::class.java,
            "OKDatabase"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNewDao(appDatabase: NewsDB): NewDao {
        return appDatabase.newDao()
    }

}

@Module
@InstallIn(SingletonComponent::class)
object SystemServiceModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext appContext: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(appContext)
    }

}