package com.jiban.howlong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jiban.howlong.data.StrengthSum
import com.jiban.howlong.databinding.FragmentNameCheckBinding
import com.jiban.howlong.korean.separationKorean
import com.jiban.howlong.viewmodels.CharacterViewModel
import com.jiban.howlong.viewmodels.DataShareViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameCheckFragment : Fragment() {

    private var myName: String? = null
    private var yourName: String? = null
    private var myStrengthSum: Int = 0
    private var yourStrengthSum: Int = 0
    private var switchFrag: Int = 0

    private val characterViewModel: CharacterViewModel by viewModels()

    private lateinit var dataShareViewModel: DataShareViewModel
    private lateinit var strengthSum: StrengthSum

    private var _binding: FragmentNameCheckBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNameCheckBinding.inflate(inflater, container, false)

        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        strengthSum = StrengthSum()

        binding.howlongBtn.setOnClickListener {
            myName = binding.myNameTe.text.toString()
            yourName = binding.yourNameTe.text.toString()
            myStrengthSum = 0
            yourStrengthSum = 0

            myName !!.forEach {
                with(it) {
                    if (it.separationKorean().first != null) {
                        search(it.separationKorean().first.toString(), 0)
                    }
                    if (it.separationKorean().second != null) {
                        search(it.separationKorean().second.toString(), 0)
                    }
                    if (it.separationKorean().third != null) {
                        search(it.separationKorean().third.toString(), 0)
                    }
                }
            }

            yourName !!.forEach {
                with(it) {
                    if (it.separationKorean().first != null) {
                        switchFrag = 1
                        search(it.separationKorean().first.toString(), 1)
                    }
                    if (it.separationKorean().second != null) {
                        switchFrag = 1
                        search(it.separationKorean().second.toString(), 1)
                    }
                    if (it.separationKorean().third != null) {
                        switchFrag = 1
                        search(it.separationKorean().third.toString(), 1)
                    }
                }
            }
        }
        return binding.root
    }

    private fun search(query: String, switchFrag: Int) {
        characterViewModel.getMyCharacter(query).observe(viewLifecycleOwner, Observer {
            if (switchFrag == 0) {
                myStrengthSum += it.strength.toInt()
            } else if (switchFrag == 1) {
                yourStrengthSum += it.strength.toInt()
            }
            strengthSum.mySum = myStrengthSum
            strengthSum.yourSum = yourStrengthSum
            strengthSum.totalSum = myStrengthSum + yourStrengthSum

            saveSum()
        })
    }

    private fun saveSum() {
        dataShareViewModel.data.value = strengthSum

        val fragment: Fragment = ResultFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.sumFl, fragment, "fragmnetId")
            ?.commit()
    }
}


