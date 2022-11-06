package com.android.mfcolak.photosharingapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mfcolak.photosharingapp.model.Post
import com.android.mfcolak.photosharingapp.R
import com.android.mfcolak.photosharingapp.adapter.HomeRecyclerAdapter
import com.android.mfcolak.photosharingapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var recyclerAdapter: HomeRecyclerAdapter
    private lateinit var recyclerView: RecyclerView

    var postList = ArrayList<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        navController = Navigation.findNavController(view)

        checkData()

        var layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = layoutManager
       recyclerAdapter = HomeRecyclerAdapter(postList)
        recyclerView.adapter = recyclerAdapter

    }

    @SuppressLint("NotifyDataSetChanged")
    fun checkData(){

        database.collection("Post").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null){
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if (snapshot != null){
                    if (!snapshot.isEmpty){
                       val documents = snapshot.documents
                        postList.clear()

                        for (document in documents){
                            val userEmail = document.get("userEmail") as String
                            val userComment = document.get("userComment") as String
                            val imgUrl = document.get("imageUrl") as String

                            val post = Post(userEmail, userComment, imgUrl)
                            postList.add(post)
                        }
                        recyclerAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.photo_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.share_photo){
            navController.navigate(R.id.action_homeFragment_to_sharePhotoFragment)
        }else if (item.itemId == R.id.logout){
            auth.signOut()
            navController.navigate(R.id.action_homeFragment_to_signInFragment)
        }
        return super.onOptionsItemSelected(item)
    }

}