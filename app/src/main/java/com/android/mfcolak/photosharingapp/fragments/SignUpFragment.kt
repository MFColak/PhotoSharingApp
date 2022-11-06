package com.android.mfcolak.photosharingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.mfcolak.photosharingapp.R
import com.android.mfcolak.photosharingapp.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

     private fun registerEvents() {

        binding.authTextView.setOnClickListener{
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.signUpBtn.setOnClickListener{
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passEditText.text.toString().trim()


            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()){

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful){

                                Toast.makeText(context, "Registered Successfully, Welcome $name", Toast.LENGTH_SHORT).show()
                                navController.navigate(R.id.action_signUpFragment_to_homeFragment)
                            }
                            else{
                                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        })


            }else{
                Toast.makeText(context, "These fields cannot be left blank.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

}