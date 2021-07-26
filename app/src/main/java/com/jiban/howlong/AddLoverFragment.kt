package com.jiban.howlong

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.databinding.FragmentAddLoverBinding

class AddLoverFragment : Fragment() {

    private var _binding: FragmentAddLoverBinding? = null
    private val binding get() = _binding !!

    private lateinit var auth: FirebaseAuth

    private var currentUser: String? = null

    private var myEmail: String? = null
    private var loverName: String? = null

    //Birth Information
    private var myGend: String? = null
    private var myYear: String? = null
    private var myMonth: String? = null
    private var myDay: String? = null
    private var myTime: String? = null


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
    ): View {
        //binding = FragmentAddLoverBinding.inflate(layoutInflater)
        _binding = FragmentAddLoverBinding.inflate(inflater, container, false)

        // initialized
        auth = FirebaseAuth.getInstance()

        // load autocomplete list
        val gendList = resources.getStringArray(R.array.gend_menu)
        val yearList = resources.getStringArray(R.array.year_menu)
        val monthList = resources.getStringArray(R.array.month_menu)
        val dayList = resources.getStringArray(R.array.day_menu)
        val timeList = resources.getStringArray(R.array.time_menu)

        val gendAdapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, R.layout.item_common_menu, gendList) }
        binding.genderAc.setAdapter(gendAdapter)
        binding.genderAc.setOnItemClickListener { _, _, position, _ ->
            myGend = (gendAdapter?.getItem(position) ?: "")
            Toast.makeText(context, myGend + "을 선택하셨습니다!", Toast.LENGTH_SHORT).show()
        }
        val yearAdapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, R.layout.item_common_menu, yearList) }
        binding.yearAc.setAdapter(yearAdapter)
        binding.yearAc.setOnItemClickListener { _, _, position, _ ->
            myYear = (yearAdapter?.getItem(position) ?: "")
            Toast.makeText(context, myYear + "을 선택하셨습니다!", Toast.LENGTH_SHORT).show()
        }
        val monthAdapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, R.layout.item_common_menu, monthList) }
        binding.monthAc.setAdapter(monthAdapter)
        binding.monthAc.setOnItemClickListener { _, _, position, _ ->
            myMonth = (monthAdapter?.getItem(position) ?: "")
            Toast.makeText(context, myMonth + "을 선택하셨습니다!", Toast.LENGTH_SHORT).show()
        }
        val dayAdapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, R.layout.item_common_menu, dayList) }
        binding.dayAc.setAdapter(dayAdapter)
        binding.dayAc.setOnItemClickListener { _, _, position, _ ->
            myDay = (dayAdapter?.getItem(position) ?: "")
            Toast.makeText(context, myDay + "을 선택하셨습니다!", Toast.LENGTH_SHORT).show()
        }
        val timeAdapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter(it, R.layout.item_common_menu, timeList) }
        binding.timeAc.setAdapter(timeAdapter)
        binding.timeAc.setOnItemClickListener { _, _, position, _ ->
            myTime = (timeAdapter?.getItem(position) ?: "")
            Toast.makeText(context, myTime + "을 선택하셨습니다!", Toast.LENGTH_SHORT).show()
        }


        binding.addBtn.setOnClickListener {

            //get user email address
            val currentUser = Firebase.auth.currentUser
            var userEmail: String? = null
            userEmail = currentUser !!.email

            // connect to db
            val db = Firebase.firestore
            db.collection("lover").document(userEmail.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        Toast.makeText(
                            context,
                            "당신의 lover는 이미 등록되어 있습니다.혹시 당신은 바람둥이?",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnSuccessListener
                    }
                }

            loverName = binding.nameEt.text.toString()

            if (loverName == "" || myGend == "" || myYear == "" || myMonth == "" || myDay == "" || myTime == "") {
                Toast.makeText(
                    context,
                    "이름과 출생일정보는 음양오행 분석을 위해서 필수사항입니다.입력부탁드립니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val lover = hashMapOf(
                "userEmail" to userEmail,
                "loverName" to loverName,
                "gender" to myGend,
                "birthYear" to myYear,
                "birthMonth" to myMonth,
                "birthDay" to myDay,
                "birthTime" to myTime
            )
// Add a new document with a generated ID
            if (userEmail != null) {
                db.collection("lover").document(userEmail)
                    .set(lover)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                        Toast.makeText(
                            context,
                            "Succeed to add your lover!",
                            Toast.LENGTH_SHORT
                        ).show()
                        //go to check
                        val fragment = com.jiban.howlong.AllCheckFragment()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragmentCv, fragment, "allCheckFragment")
                            ?.commit()
                    }
                    .addOnFailureListener { e ->
                        Log.w("JJS Register", "Error adding document", e)
                    }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}