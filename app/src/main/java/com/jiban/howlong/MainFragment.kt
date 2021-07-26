package com.jiban.howlong

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.data.StrengthBirthSum
import com.jiban.howlong.data.StrengthSum
import com.jiban.howlong.databinding.FragmentMainBinding
import com.jiban.howlong.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding !!

    private lateinit var auth: FirebaseAuth
    private var currentUser: String? = null

    private var myEmail: String? = null
    private var myName: String? = null
    private var yourName: String? = null

    //Birth Information
    private var myGend: String? = null
    private var myYear: String? = null
    private var myMonth: String? = null
    private var myDay: String? = null
    private var myTime: String? = null
    private var yourGend: String? = null
    private var yourYear: String? = null
    private var yourMonth: String? = null
    private var yourDay: String? = null
    private var yourTime: String? = null

    private var myStrengthSum: Int = 0
    private var yourStrengthSum: Int = 0
    private lateinit var strengthSum: StrengthSum

    private var myStrengthBirthSum: Int = 0
    private var yourStrengthBirthSum: Int = 0
    private lateinit var strengthBirthSum: StrengthBirthSum

    private var switchFrag: Int = 0

    private val characterViewModel: CharacterViewModel by viewModels()
    private val numberViewModel: NumberViewModel by viewModels()
    private val maleViewModel: MaleViewModel by viewModels()
    private val femaleViewModel: FemaleViewModel by viewModels()
    private val bothViewModel: BothViewModel by viewModels()
    private lateinit var dataShareViewModel: DataShareViewModel
    private lateinit var dataBirthShareViewModel: DataBirthShareViewModel


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
        //binding = FragmentMainBinding.inflate(layoutInflater)
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        //get user email address
        auth = FirebaseAuth.getInstance()
        val currentUser = Firebase.auth.currentUser


        //already login go to the mainFragment
        //already login go to the mainFragment
        if (currentUser != null) {
            //getting the email
            var userEmail: String? = null
            userEmail = currentUser.email

            //db connection
            val db = Firebase.firestore

            val docRef1 = db.collection("users").document(userEmail !!)
            docRef1.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        myName = document.data !!.getValue("name") as String
                        myYear = document.data !!.getValue("birthYear") as String
                        myMonth = document.data !!.getValue("birthMonth") as String
                        myDay = document.data !!.getValue("birthDay") as String
                        myTime = document.data !!.getValue("birthTime") as String

                        binding.myNameTv.text = myName
                        binding.myYearTv.text = myYear
                        binding.myMonthTv.text = myMonth
                        binding.myDayTv.text = myDay
                        binding.myTimeTv.text = myTime
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
                        yourName = document.data !!.getValue("loverName") as String
                        yourYear = document.data !!.getValue("birthYear") as String
                        yourMonth = document.data !!.getValue("birthMonth") as String
                        yourDay = document.data !!.getValue("birthDay") as String
                        yourTime = document.data !!.getValue("birthTime") as String

                        binding.yourNameTv.text = yourName
                        binding.yourYearTv.text = yourYear
                        binding.yourMonthTv.text = yourMonth
                        binding.yourDayTv.text = yourDay
                        binding.yourTimeTv.text = yourTime
                    } else {
                        Log.d("JJS", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("JJS", "Error getting documents: ", exception)
                }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.resignFb.setOnClickListener {
            Toast.makeText(context, "Resign OR Change", Toast.LENGTH_SHORT).show()

            val fragment = ResignChangeFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentCv, fragment, "resignOrChangeFragment")
                ?.commit()

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