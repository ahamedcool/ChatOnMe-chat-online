package com.example.chatonme.views.start


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatonme.R
import com.example.chatonme.di.AppModule.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //init DI
        startKoin {
            androidLogger()
            androidContext(this@StartActivity)
            modules(appModule)
        }
    }


}
