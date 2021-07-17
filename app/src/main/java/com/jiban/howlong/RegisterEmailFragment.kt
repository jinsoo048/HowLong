package com.jiban.howlong

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.databinding.FragmentRegisterEmailBinding
import java.util.*


class RegisterEmailFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegisterEmailBinding? = null
    private val binding get() = _binding !!

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
    ): View? {
        _binding = FragmentRegisterEmailBinding.inflate(inflater, container, false)

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
        //auth
        auth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {

            val myEmail = binding.emailEt.text.toString()
            val myPassword = binding.passwordEt.text.toString()
            val myName = binding.nameEt.text.toString()
            val myPhone = binding.phoneEt.text.toString()

            if (myEmail == "" || myPassword == "") {
                Toast.makeText(
                    context,
                    "email address와 password는 인증을 위해서 필수사항입니다.입력부탁드립니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Toast.makeText(
                        context,
                        "Registration is successful!!!!!!!!!!!!!!!",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()

            }

            // register profile
            val db = Firebase.firestore
            // Create a new user with a first and last name

            if (myName == "" || myGend == "" || myYear == "" || myMonth == "" || myDay == "" || myTime == "") {
                Toast.makeText(
                    context,
                    "이름과 출생일정보는 음양오행 분석을 위해서 필수사항입니다.입력부탁드립니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            val user = hashMapOf(
                "email" to myEmail,
                "name" to myName,
                "phoneNumber" to myPhone,
                "gender" to myGend,
                "birthYear" to myYear,
                "birthMonth" to myMonth,
                "birthDay" to myDay,
                "birthTime" to myTime
            )
// Add a new document with a generated ID
            db.collection("user")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("JJS Register", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("JJS Register", "Error adding document", e)
                }
        }

        //fun goToLogin(view: View)
        binding.loginTv.setOnClickListener {
            val fragment = LoginEmailFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentCv, fragment, "fragmnetId")
                ?.commit()
        }
        val view = binding.root
        return view
    }

    private fun updateUI(user: FirebaseUser?) {
        // No-op
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


