package com.jiban.howlong

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentMainBinding

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
        //auth
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser.toString()
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
        binding = FragmentMainBinding.inflate(layoutInflater)

        //get user email address
        val currentUser = Firebase.auth.currentUser
        var userEmail: String? = null
        userEmail = currentUser !!.email

        val db = Firebase.firestore

        db.collection("users")
            .whereEqualTo("users", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("JJS DB", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("JJS DB", "Error getting documents: ", exception)
            }

        val docRef = db.collection("users").document(userEmail !!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("JJS DB", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("JJS DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("JJS DB", "get failed with ", exception)
            }


        val docRef1 = db.collection("users").document(userEmail)
        docRef1.get()
            .addOnSuccessListener { document ->
                if (document != null) {
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
                    Log.d("JJS DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("JJS DB", "get failed with ", exception)
            }
        val docRef2 = db.collection("lover").document(userEmail)
        docRef2.get()
            .addOnSuccessListener { document ->
                if (document != null) {
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
                    Log.d("JJS DB", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("JJS DB", "get failed with ", exception)
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allBt.setOnClickListener {
            val fragment: Fragment = AllCheckFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentCv, fragment, "allCheckFragment")
                ?.commit()
        }

    }
}