package com.jiban.howlong

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.data.StrengthBirthSum
import com.jiban.howlong.data.StrengthSum
import com.jiban.howlong.databinding.FragmentAllCheckBinding
import com.jiban.howlong.korean.separationKorean
import com.jiban.howlong.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCheckFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentAllCheckBinding

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

    private var progressBar: ProgressBar? = null
    private var i = 0
    private var txtView: TextView? = null
    private val handler = Handler()


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
        //starting
        binding = FragmentAllCheckBinding.inflate(layoutInflater)

        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataBirthShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataBirthShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        //get user email address
        val currentUser = Firebase.auth.currentUser
        var userEmail: String? = null
        userEmail = currentUser !!.email

        // connect to db
        val db = Firebase.firestore
        val docRef1 = db.collection("users").document(userEmail !!)
        docRef1.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    myName = document.data !!.getValue("name") as String
                    myYear = document.data !!.getValue("birthYear") as String
                    myMonth = document.data !!.getValue("birthMonth") as String
                    myDay = document.data !!.getValue("birthDay") as String
                    myTime = document.data !!.getValue("birthTime") as String

                    analyze(myName !!, myYear !!, myMonth !!, myDay !!, myTime !!, 0)

                } else {
                    Log.d("JJS DB", "No such document")
                }
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

                    analyze(yourName !!, yourYear !!, yourMonth !!, yourDay !!, yourTime !!, 1)

                } else {
                    Log.d("JJS DB", "No such document")
                }
            }
        return binding.root
    }

    private fun analyze(
        name: String,
        year: String,
        month: String,
        day: String,
        time: String,
        switchFrag: Int
    ) {
        // binding.allBt.setOnClickListener {
        //name analysis
        strengthSum = StrengthSum()
        myStrengthSum = 0
        yourStrengthSum = 0

        activity?.let {
            name.forEach {
                with(it) {
                    if (it.separationKorean().first != null) {
                        search(it.separationKorean().first.toString(), switchFrag)
                    }
                    if (it.separationKorean().second != null) {
                        search(it.separationKorean().second.toString(), switchFrag)
                    }
                    if (it.separationKorean().third != null) {
                        search(it.separationKorean().third.toString(), switchFrag)
                    }
                }
            }

            //birth analysis
            strengthBirthSum = StrengthBirthSum()
            myStrengthBirthSum = 0
            yourStrengthBirthSum = 0
            var numberMonth: Int = 0

            year.filter { it -> it.isDigit() }.forEach {
                with(it) {
                    searchBirth(it.toString(), 0)
                }
            }

            when (month) {
                "Jan" -> numberMonth = 1
                "Feb" -> numberMonth = 2
                "Mar" -> numberMonth = 3
                "Apr" -> numberMonth = 4
                "May" -> numberMonth = 5
                "Jun" -> numberMonth = 6
                "Jul" -> numberMonth = 7
                "Aug" -> numberMonth = 8
                "Sep" -> numberMonth = 9
                "Oct" -> numberMonth = 10
                "Nov" -> numberMonth = 11
                "Dec" -> numberMonth = 12
            }
            numberMonth.toString().filter { it -> it.isDigit() }.forEach {
                with(it) {
                    searchBirth(it.toString(), 0)
                }
            }
            day.filter { it -> it.isDigit() }.forEach {
                with(it) {
                    searchBirth(it.toString(), 0)
                }
            }
            time.filter { it -> it.isDigit() }.forEach {
                with(it) {
                    searchBirth(it.toString(), 0)
                }
            }

        }
    }


    private fun search(query: String, switchFrag: Int) {
        characterViewModel.getMyCharacter(query).observe(viewLifecycleOwner, Observer {
            if (switchFrag == 0) {
                myStrengthSum += it.strength.toInt()
                strengthSum.mySum = myStrengthSum
            } else if (switchFrag == 1) {
                yourStrengthSum += it.strength.toInt()
                strengthSum.yourSum = yourStrengthSum
            }

            strengthSum.totalSum = myStrengthSum + yourStrengthSum

            saveSum()
        })
    }

    private fun searchBirth(query: String, switchFrag: Int) {
        numberViewModel.getMyNumber(query).observe(viewLifecycleOwner, Observer {
            if (switchFrag == 0) {
                myStrengthBirthSum += it.strength.toInt()
                strengthBirthSum.myBirthSum = myStrengthBirthSum
            } else if (switchFrag == 1) {
                yourStrengthBirthSum += it.strength.toInt()
                strengthBirthSum.yourBirthSum = yourStrengthBirthSum
            }

            strengthBirthSum.totalBirthSum = myStrengthBirthSum + yourStrengthBirthSum

            saveBirthSum()
        })
    }

    private fun saveSum() {
        dataShareViewModel.data.value = strengthSum

        val fragment: Fragment = com.jiban.howlong.AllCheckResultFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.allResultFl, fragment, "allCheckResultFragment")
            ?.commit()

    }

    private fun saveBirthSum() {
        dataBirthShareViewModel.data.value = strengthBirthSum

        val fragment: Fragment = com.jiban.howlong.AllCheckResultFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.allResultFl, fragment, "allCheckResultFragment")
            ?.commit()
    }
}
