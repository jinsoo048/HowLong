package com.jiban.howlong

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.adapter.MessageAdapter
import com.jiban.howlong.data.message.Message
import com.jiban.howlong.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding !!

    private lateinit var auth: FirebaseAuth

    //private var currentUser: String? = null
    private var userEmail: String? = null

    //private var myEmail: String? = null
    private var myName: String? = null
    private var yourName: String? = null

    //Birth Information
    //private var myGend: String? = null
    private var myYear: String? = null
    private var myMonth: String? = null
    private var myDay: String? = null
    private var myTime: String? = null

    //private var yourGend: String? = null
    private var yourYear: String? = null
    private var yourMonth: String? = null
    private var yourDay: String? = null
    private var yourTime: String? = null
    private var messageList: ArrayList<Message> = ArrayList()


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload();
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val currentUser = Firebase.auth.currentUser
        //already login go to the mainFragment
        if (currentUser != null) {
            //getting the email
            userEmail = currentUser.email
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userEmail != null) {
            //db connection
            val db = Firebase.firestore
            val docRef1 = db.collection("users").document(userEmail !!)
            docRef1.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        while (myName == null || myYear == null || myMonth == null || myTime == null) {
                            activity?.let {
                                myName = document.data !!.getValue("name") as String
                                myYear = document.data !!.getValue("birthYear") as String
                                myMonth = document.data !!.getValue("birthMonth") as String
                                myDay = document.data !!.getValue("birthDay") as String
                                myTime = document.data !!.getValue("birthTime") as String
                            }
                        }
                        activity?.let {
                            binding.myNameTv.text = myName
                            binding.myYearTv.text = myYear
                            binding.myMonthTv.text = myMonth
                            binding.myDayTv.text = myDay
                            binding.myTimeTv.text = myTime
                        }

                    } else {
                        Log.d("JJS", "No such document")
                    }
                }.addOnFailureListener { exception ->
                    Log.d("JJS", "get failed with ", exception)
                }
            //check the lover be there or not
            val docRef2 = db.collection("lover").document(userEmail.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("JJS", "${document.id} => ${document.data}")
                        while (yourName == null || yourYear == null || yourMonth == null || yourTime == null) {
                            activity?.let {
                                yourName = document.data !!.getValue("loverName") as String
                                yourYear = document.data !!.getValue("birthYear") as String
                                yourMonth = document.data !!.getValue("birthMonth") as String
                                yourDay = document.data !!.getValue("birthDay") as String
                                yourTime = document.data !!.getValue("birthTime") as String
                            }

                        }
                        activity?.let {
                            binding.yourNameTv.text = yourName
                            binding.yourYearTv.text = yourYear
                            binding.yourMonthTv.text = yourMonth
                            binding.yourDayTv.text = yourDay
                            binding.yourTimeTv.text = yourTime
                        }

                    } else {
                        Log.d("JJS", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("JJS", "Error getting documents: ", exception)
                }
            //check messages
            val docRef3 = db.collection("messages")
                .whereEqualTo("email", userEmail.toString())
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        var messageBuf: Message = Message()
                        messageBuf.date = (document.data["date"] as String?) !!
                        messageBuf.message = (document.data["message"] as String?) !!
                        messageList.add(messageBuf)

                    }
                    //recycler adapter
                    activity?.let {
                        val recyclerview = binding.messageRv
                        recyclerview.layoutManager =
                            StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
                        val adapter = MessageAdapter(context, messageList)
                        recyclerview.adapter = adapter
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

        } else {
            Toast.makeText(context, "로그인하세요", Toast.LENGTH_LONG).show()

            val fragment: Fragment = LoginEmailFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentCv, fragment, "LoginEmailFragment")
                ?.commit()
        }

        binding.allBt.setOnClickListener {
            if (yourName != null) {
                val fragment: Fragment = AllCheckFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentCv, fragment, "allCheckFragment")
                    ?.commit()
            } else {
                val fragment: Fragment = AddLoverFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentCv, fragment, "addLoverFragment")
                    ?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            _binding = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}