package com.jiban.howlong

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.databinding.FragmentResignChangeBinding


class ResignChangeFragment : Fragment() {

    private var _binding: FragmentResignChangeBinding? = null
    private val binding get() = _binding !!
    private lateinit var auth: FirebaseAuth

    //private var currentUser: String? = null
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

    //private lateinit var db: Firebase

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload()
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        } else {
            Toast.makeText(context, "로그인을 하세요", Toast.LENGTH_SHORT).show()

            val fragment = LoginEmailFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentCv, fragment, "loginEmailFragment")
                ?.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResignChangeBinding.inflate(inflater, container, false)

        //get user email address
        auth = FirebaseAuth.getInstance()
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        var userEmail: String? = null
        if (currentUser != null) {
            userEmail = currentUser.email
        } else {
            Toast.makeText(context, "로그인을 하세요", Toast.LENGTH_SHORT).show()
        }

        //DB Connection
        val db = Firebase.firestore

        db.collection("users")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("JJS DB", "${document.id} => ${document.data}")
                    val docRef1 = db.collection("users").document(userEmail.toString())
                    docRef1.get().addOnSuccessListener { document ->
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
                            Log.d("JJS DB", "No such document")
                        }
                    }.addOnFailureListener { exception ->
                        Log.d("JJS DB", "get failed with ", exception)
                    }
                    val docRef2 = db.collection("lover").document(userEmail.toString())
                    docRef2.get().addOnSuccessListener { document ->
                        if (document.exists()) {
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
                    }.addOnFailureListener { exception ->
                        Log.d("JJS DB", "get failed with ", exception)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("JJS DB", "Error getting documents: ", exception)
            }

        binding.resignBt.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("탈퇴절차")
            builder.setMessage("정말로 모든 정보의 삭제와 탈퇴를 원하시나요?")

            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                Toast.makeText(context, android.R.string.yes, Toast.LENGTH_SHORT).show()

                //delete message
                whereQueryDelete("messages", "email", userEmail.toString())

                //delete lover
                db.collection("lover").document(userEmail !!)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("JJS DB", "DocumentSnapshot successfully deleted!")
                        //return@addOnSuccessListener
                    }
                    .addOnFailureListener { e ->
                        Log.w("JJS DB", "Error deleting document", e)
                        return@addOnFailureListener
                    }
                //delete user
                db.collection("users").document(userEmail)
                    .delete()
                    .addOnSuccessListener {
                        //delete auth
                        val resignUser = FirebaseAuth.getInstance().currentUser
                        if (resignUser != null) {
                            resignUser.delete().addOnCompleteListener { task: Task<Void?> ->
                                if (task.isSuccessful) {
                                    Log.d("JJS DB", "Deletion Success")
                                    return@addOnCompleteListener
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "아직 로그인 전입니다.", LENGTH_SHORT
                            ).show()
                            return@addOnSuccessListener
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("JJS DB", "Error deleting document", e)
                        return@addOnFailureListener
                    }

                //go to main fragment
                val fragment = LoginEmailFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentCv, fragment, "loginEmailFragment")
                    ?.commit()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(
                    context,
                    android.R.string.no, Toast.LENGTH_SHORT
                ).show()
                return@setNegativeButton
            }
            builder.show()
        }

        binding.goodbyeBt.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("삭제절차")
            builder.setMessage("정말로 파트너의 삭제를 원하시나요?")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                Toast.makeText(context, android.R.string.yes, Toast.LENGTH_SHORT).show()

                db.collection("lover").document(userEmail !!)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("JJS DB", "DocumentSnapshot successfully deleted!")
                        return@addOnSuccessListener
                    }
                    .addOnFailureListener { e ->
                        Log.w("JJS DB", "Error deleting document", e)
                        return@addOnFailureListener
                    }
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(
                    context,
                    android.R.string.no, Toast.LENGTH_SHORT
                ).show()
                return@setNegativeButton
            }
            builder.show()
        }

        binding.logoutFb.setOnClickListener {
            Toast.makeText(context, "로그아웃 하겠습니다.", Toast.LENGTH_LONG).show()
            //auth sign out
            Firebase.auth.signOut()
            //go to main fragment
            val fragment = LoginEmailFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentCv, fragment, "loginEmailFragment")
                ?.commit()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            _binding = null
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun whereQueryDelete(collection: String?, field: String?, value: String?) {
        val db = Firebase.firestore
        db.collection(collection.toString())
            .whereEqualTo(field.toString(), value)
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result !!) {
                        Log.d("JJS DB", document.id + " => " + document.data)
                        val idDelete = document.id
                        db.collection(collection.toString()).document(idDelete)
                            .delete()
                            .addOnSuccessListener(OnSuccessListener<Void?> {
                                Log.d("JJS DB", "DocumentSnapshot successfully deleted!")
                            })
                            .addOnFailureListener(OnFailureListener { e ->
                                Log.w("JJS DB", "Error deleting document", e)
                            })
                    }
                } else {
                    Log.d("JJS DB", "Error getting documents: ", task.exception)
                }
            })
    }
}