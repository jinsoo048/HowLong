package com.jiban.howlong

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jiban.howlong.databinding.FragmentAllCheckResultBinding
import com.jiban.howlong.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import kotlin.math.*

@AndroidEntryPoint
class AllCheckResultFragment : Fragment() {
    private var _binding: FragmentAllCheckResultBinding? = null
    private val binding get() = _binding !!

    private lateinit var auth: FirebaseAuth

    private lateinit var dataShareViewModel: DataShareViewModel
    private lateinit var dataBirthShareViewModel: DataBirthShareViewModel

    //private val characterViewModel: CharacterViewModel by viewModels()
    //private val maleViewModel: MaleViewModel by viewModels()
    //private val femaleViewModel: FemaleViewModel by viewModels()
    private val bothViewModel: BothViewModel by viewModels()
    //private var myName: String? = null
    //private var yourName: String? = null
    //private var myStrengthSum: Int = 0
    //private var yourStrengthSum: Int = 0
    //private var switchFrag: Int = 0
    //private val characterViewModel: CharacterViewModel by viewModels()
    //private lateinit var dataShareViewModel: DataShareViewModel
    //private lateinit var strengthSum: StrengthSum

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCheckResultBinding.inflate(inflater, container, false)

        //get user email address
        auth = FirebaseAuth.getInstance()
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        var userEmail: String? = null
        userEmail = currentUser !!.email

        scoringBar()
        adviceName()
        adviceBirth()

        binding.loveMessageFb.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())
            // dialog title
            builder.setTitle("사랑의 주문을...(20자 미만)")
            // dialog message view
            val constraintLayout = getEditTextLayout(requireContext())
            builder.setView(constraintLayout)
            val textInputLayout =
                constraintLayout.findViewWithTag<TextInputLayout>("textInputLayoutTag")
            val textInputEditText =
                constraintLayout.findViewWithTag<TextInputEditText>("textInputEditTextTag")
            // alert dialog positive button
            builder.setPositiveButton("Submit") { dialog, which ->
                //input message
                val messageEt = textInputEditText.text
                val stringLength = messageEt !!.length
                if (stringLength > 20) {
                    Toast.makeText(context, "사랑의 주문은 20자 미만으로 부탁드립니다.", LENGTH_LONG)
                    return@setPositiveButton
                } else {
                    //textView.text = "Hello, $name"
                    val date = LocalDate.now()
                    //calculation
                    //DB Connection
                    val db = Firebase.firestore
                    if (userEmail != null) {
                        val message = hashMapOf(
                            "email" to userEmail.toString(),
                            "date" to date.toString(),
                            "message" to messageEt.toString(),
                        )
                        // Add a new document with a generated ID
                        db.collection("messages").document()
                            .set(message)
                            .addOnSuccessListener {
                                Log.d(TAG, "DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                        return@setPositiveButton
                    } else {
                        Toast.makeText(context, "로그인을 하세요", Toast.LENGTH_SHORT).show()
                        val fragment = LoginEmailFragment()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.fragmentCv, fragment, "loginEmailFragment")
                            ?.commit()
                    }
                }
            }
            // alert dialog other buttons
            builder.setNegativeButton("No", null)
            builder.setNeutralButton("Cancel", null)
            // set dialog non cancelable
            builder.setCancelable(false)
            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
            // initially disable the positive button
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            // edit text text change listener
            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(
                    p0: CharSequence?, p1: Int,
                    p2: Int, p3: Int
                ) {
                }

                override fun onTextChanged(
                    p0: CharSequence?, p1: Int,
                    p2: Int, p3: Int
                ) {
                    if (p0.isNullOrBlank()) {
                        textInputLayout.error = "사랑의 주문을 입력하세요"
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .isEnabled = false
                    } else {
                        textInputLayout.error = ""
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .isEnabled = true
                    }
                }
            })
            return@setOnClickListener
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner)
        {
            _binding = null
        }
        return binding.root
    }

    private fun scoringBar() {

        var myScore: Int = 0
        var yourScore: Int = 0
        var totalScore: Int = 0
        var myBirthScore = 0
        var yourBirthScore: Int = 0
        var totalBirthScore: Int = 0
        var expectedTime: Int = 0

        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataShareViewModel.data.observe(viewLifecycleOwner, Observer {

            myScore = dataShareViewModel.data.value?.mySum !!.toInt()
            yourScore = dataShareViewModel.data.value?.yourSum !!.toInt()
            totalScore = dataShareViewModel.data.value?.totalSum !!.toInt()

            //name score
            binding.mySumTv.text = (myScore).toString()
            binding.yourSumTv.text = (yourScore).toString()
            binding.totalSumTv.text = (totalScore).toString()

            dataBirthShareViewModel = activity?.run {
                ViewModelProviders.of(this).get(DataBirthShareViewModel::class.java)
            } ?: throw Exception("Invalid Activity")

            dataBirthShareViewModel.data.observe(viewLifecycleOwner, Observer {
                //variable
                myBirthScore = dataBirthShareViewModel.data.value?.myBirthSum !!.toInt()
                yourBirthScore = dataBirthShareViewModel.data.value?.yourBirthSum !!.toInt()
                totalBirthScore = dataBirthShareViewModel.data.value?.totalBirthSum !!.toInt()
                //birth score
                binding.myBirthSumTv.text = (myBirthScore).toString()
                binding.yourBirthSumTv.text = (yourBirthScore).toString()
                binding.totalBirthSumTv.text = (totalBirthScore).toString()
                //bar making
                barCalculator()
            })
        })
    }

    private fun adviceName() {

        var myScore: Int = 0
        var yourScore: Int = 0
        var totalScore: Int = 0
        var myBirthScore = 0
        var yourBirthScore: Int = 0
        var totalBirthScore: Int = 0
        var expectedTime: Int = 0

        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataShareViewModel.data.observe(viewLifecycleOwner, Observer {

            myScore = dataShareViewModel.data.value?.mySum !!.toInt()
            yourScore = dataShareViewModel.data.value?.yourSum !!.toInt()
            totalScore = dataShareViewModel.data.value?.totalSum !!.toInt()

            //advice
            bothViewModel.getBoth(myScore).observe(viewLifecycleOwner, Observer {
                binding.myWeakTv.text = it.nameWeak
                binding.myStrongTv.text = it.nameStrong
            })
            bothViewModel.getBoth(yourScore).observe(viewLifecycleOwner, Observer {
                binding.yourWeakTv.text = it.nameWeak
                binding.yourStrongTv.text = it.nameStrong
            })
        })
    }

    private fun adviceBirth() {
        //var myScore: Int = 0
        //var yourScore: Int = 0
        //var totalScore: Int = 0
        var myBirthScore = 0
        var yourBirthScore: Int = 0
        var totalBirthScore: Int = 0
        //var expectedTime: Int = 0

        //// Birth
        dataBirthShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataBirthShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataBirthShareViewModel.data.observe(viewLifecycleOwner, Observer {

            //variable
            myBirthScore = dataBirthShareViewModel.data.value?.myBirthSum !!.toInt()
            yourBirthScore = dataBirthShareViewModel.data.value?.yourBirthSum !!.toInt()
            totalBirthScore = dataBirthShareViewModel.data.value?.totalBirthSum !!.toInt()

            //advice
            bothViewModel.getBoth(myBirthScore).observe(viewLifecycleOwner, Observer {
                binding.myBirthWeakTv.text = it.birthWeak
                binding.myBirthStrongTv.text = it.birthStrong
            })
            bothViewModel.getBoth(yourBirthScore).observe(viewLifecycleOwner, Observer {
                binding.yourBirthWeakTv.text = it.birthWeak
                binding.yourBirthStrongTv.text = it.birthStrong
            })
            bothViewModel.getBoth(totalBirthScore).observe(viewLifecycleOwner, Observer {
                binding.bothWeakTv.text = it.weak
                binding.bothStrongTv.text = it.strong
                binding.relationShipTv.text = it.relationship
            })
        })
    }

    fun barCalculator() {

        var myScore: Int = 0
        var yourScore: Int = 0
        var totalScore: Int = 0
        var myBirthScore = 0
        var yourBirthScore: Int = 0
        var totalBirthScore: Int = 0
        var expectedTime: String? = null

        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataShareViewModel.data.observe(viewLifecycleOwner, Observer {

            myScore = dataShareViewModel.data.value?.mySum !!.toInt()
            yourScore = dataShareViewModel.data.value?.yourSum !!.toInt()
            totalScore = dataShareViewModel.data.value?.totalSum !!.toInt()

            dataBirthShareViewModel = activity?.run {
                ViewModelProviders.of(this).get(DataBirthShareViewModel::class.java)
            } ?: throw Exception("Invalid Activity")

            dataBirthShareViewModel.data.observe(viewLifecycleOwner, Observer {

                //variable
                myBirthScore = dataBirthShareViewModel.data.value?.myBirthSum !!.toInt()
                yourBirthScore = dataBirthShareViewModel.data.value?.yourBirthSum !!.toInt()
                totalBirthScore = dataBirthShareViewModel.data.value?.totalBirthSum !!.toInt()

                //rating bar score final calculation
                binding.mySumRb.rating =
                    (1 / ((myScore.toFloat()) + (myBirthScore.toFloat())).absoluteValue) * 100 / 10
                binding.yourSumRb.rating =
                    (1 / ((yourScore.toFloat()) + (yourBirthScore.toFloat())).absoluteValue) * 100 / 10
                binding.bothSumRb.rating =
                    (1 / ((totalScore.toFloat()) + (totalBirthScore.toFloat())).absoluteValue) * 100 / 10

                if ((totalScore.toFloat() + totalBirthScore.toFloat()).toInt() != 0) {
                    expectedTime =
                        (((1F / ((((totalScore.toFloat() * 0.002F)) + (totalBirthScore.toFloat() * 0.008F)).absoluteValue)) / 2F).toInt()).toString()
                    if (expectedTime !!.toInt() > 100) {
                        binding.expectedTimeTv.text = "천년만"
                    } else {
                        binding.expectedTimeTv.text = expectedTime.toString()
                    }

                } else {
                    expectedTime = 100.toString()
                    binding.expectedTimeTv.text = expectedTime.toString()
                }
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // get edit text layout
    private fun getEditTextLayout(context: Context): ConstraintLayout {
        val constraintLayout = ConstraintLayout(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constraintLayout.layoutParams = layoutParams
        constraintLayout.id = View.generateViewId()

        val textInputLayout = TextInputLayout(context)
        textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
        layoutParams.setMargins(
            32.toDp(context),
            8.toDp(context),
            32.toDp(context),
            8.toDp(context)
        )
        textInputLayout.layoutParams = layoutParams
        textInputLayout.hint = "마음을 담아"
        textInputLayout.id = View.generateViewId()
        textInputLayout.tag = "textInputLayoutTag"

        val textInputEditText = TextInputEditText(context)
        textInputEditText.id = View.generateViewId()
        textInputEditText.tag = "textInputEditTextTag"

        textInputLayout.addView(textInputEditText)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintLayout.addView(textInputLayout)
        return constraintLayout
    }

    // extension method to convert pixels to dp
    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

}