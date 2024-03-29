package com.example.chatonme.di

import com.example.chatonme.adapters.FriendsList
import com.example.chatonme.models.UsersListViewModel
import com.example.chatonme.adapters.UserListAdapter
import com.example.chatonme.di.components.CustomDialog
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.models.UserProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module{
        single { Messaging(androidContext()) }
        single { ImageProcessing(androidContext()) }
        single { CustomDialog() }
        single { FriendsList(androidContext()) }
        single { UserListAdapter(androidContext(), ImageProcessing(androidContext()), CustomDialog()) }
        viewModel { UsersListViewModel() }
        viewModel { UserProfileViewModel(Messaging(androidContext())) }
    }
}