package com.android.mfcolak.photosharingapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.mfcolak.photosharingapp.R
import com.android.mfcolak.photosharingapp.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if (firebaseAuth.currentUser != null){
                navController.navigate(R.id.action_splashFragment_to_homeFragment)
            }else{
                navController.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 2000)

    }

}