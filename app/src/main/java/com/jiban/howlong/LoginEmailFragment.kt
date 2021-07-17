package com.jiban.howlong

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.databinding.FragmentLoginEmailBinding


class LoginEmailFragment : Fragment() {
    private var _binding: FragmentLoginEmailBinding? = null
    private val binding get() = _binding !!
    private lateinit var auth: FirebaseAuth

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
        _binding = FragmentLoginEmailBinding.inflate(inflater, container, false)

        // context?.let { FirebaseApp.initializeApp(it) }
        auth = FirebaseAuth.getInstance()
        //auth = Firebase.auth
        //auth = FirebaseAuth.getInstance()


        //enter control
        val editTexts: ArrayList<EditText> = ArrayList()

        editTexts.add(binding.editTextEmailAddress)
        editTexts.add(binding.editTextPassword)




        binding.loginBtn.setOnClickListener {
            val myEmail = binding.editTextEmailAddress.text.toString()
            val myPassword = binding.editTextPassword.text.toString()

            if (myEmail == "" || myPassword == "") {
                Toast.makeText(
                    context,
                    "email address와 password는 로그인을 위해서 필수사항입니다.입력부탁드립니다.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login is successful!!!!!!!!!!!", Toast.LENGTH_LONG)
                            .show()
                        val user = auth.currentUser
                        updateUI(user)

                        val intent = Intent(activity, MainActivity::class.java)
                        activity?.startActivity(intent)
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_LONG).show()
                updateUI(null)
            }
        }

        binding.registerTv.setOnClickListener {
            val fragment = RegisterEmailFragment()
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