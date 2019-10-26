package com.example.chatonme.views.home


import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chatonme.R
import com.example.chatonme.databinding.FragmentProfileBinding
import com.example.chatonme.di.components.ImageProcessing
import com.example.chatonme.di.components.Messaging
import com.example.chatonme.helpers.PICK_IMAGE_REQUEST
import com.example.chatonme.models.User
import com.example.chatonme.views.other.UserProfileInformationFragment
import com.firebase.ui.auth.AuthUI
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_connect_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment() {

    private val messaging: Messaging by  inject()
    private val imageProcessing: ImageProcessing by  inject()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentProfileBinding.inflate(inflater)

        signOutListener(binding.signOutButton)
        imageListener(binding.profileImage)
        editProfileListener(binding.editProfileButton)
        setUserData()


        return binding.root

    }


    private fun editProfileListener(view: View) {
        RxView.clicks(view).map {
            findNavController().navigate(R.id.action_profileFragment_to_userProfileInformationFragment)
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }





    /**
     * Read and set data from firebase
     */
    private fun setUserData() {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(item in snapshot.children){
                    val modelUser: User = item.getValue(User::class.java)!!
                    binding.apply {
                        presentationTextView.text = modelUser.presentation
                        displayAgeTv.text = modelUser.age
                        displayCountryTv.text = modelUser.country
                        displayEmailTv.text = modelUser.email
                    }
                    imageProcessing.setImage(modelUser.image.toString(), profileImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                messaging.showToast("error", error.message) }
        })
    }

    /**
     * Open gallery to pick profile photo
     */
    private fun imageListener(view: View){
        RxView.clicks(view).map {
           Intent().apply {
               type = "image/*"
               action = Intent.ACTION_GET_CONTENT
               startActivityForResult(Intent.createChooser(this, getString(R.string.select_picture)), PICK_IMAGE_REQUEST)
           }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }

    /**
     * Return and set picked image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data?.data != null)
        {
            imageProcessing.setImage(data.data?.toString()!!, profileImage)
        }
    }

    /**
     *Sign out and close the app
     */
    private fun signOutListener(view: View){
        RxView.clicks(view).map{
            AuthUI.getInstance().signOut(this.activity!!).addOnSuccessListener {
                activity?.finish()
            }.addOnFailureListener {
                messaging.showSnackBar(binding.root, getString(R.string.something_went_wrong_try_again))
            }
        }.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe()
    }
}
