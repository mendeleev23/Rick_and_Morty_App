package com.example.aston_courseproject_rickmorty

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.example.aston_courseproject_rickmorty.dagger.AppComponent
import com.example.aston_courseproject_rickmorty.dagger.AppModule
import com.example.aston_courseproject_rickmorty.dagger.DaggerAppComponent

@ExperimentalPagingApi
class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}