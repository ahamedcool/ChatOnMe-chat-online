package com.example.chatonme.views.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatonme.models.AppViewModel
import com.example.chatonme.adapters.UserListAdapter
import com.example.chatonme.databinding.FragmentChatBinding
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.inject


class ChatFragment : Fragment() {

    private val appViewModel: AppViewModel by inject()
    private val userListAdapter: UserListAdapter by inject()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater)


        appViewModel.getUsers(firebaseDatabase)

        binding.recyclerUserList.adapter = userListAdapter
        binding.recyclerUserList.layoutManager = LinearLayoutManager(this.context)

        appViewModel.userLists.observe(this, Observer {users ->
            users?.let {
                userListAdapter.setUsers(it)
            }
        })



        return binding.root
    }


}
