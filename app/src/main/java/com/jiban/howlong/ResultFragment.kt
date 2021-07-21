package com.jiban.howlong

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jiban.howlong.databinding.FragmentResultBinding
import com.jiban.howlong.viewmodels.BothViewModel
import com.jiban.howlong.viewmodels.DataShareViewModel
import com.jiban.howlong.viewmodels.FemaleViewModel
import com.jiban.howlong.viewmodels.MaleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentResultBinding
    private val maleViewModel: MaleViewModel by viewModels()
    private val femaleViewModel: FemaleViewModel by viewModels()
    private val bothViewModel: BothViewModel by viewModels()

    //private val dataShareViewModel:DataShareViewModel by viewModels()
    private lateinit var dataShareViewModel: DataShareViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(layoutInflater)
        var myScore: Int = 0
        var yourScore: Int = 0
        var totalScore: Int = 0

        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataShareViewModel.data.observe(viewLifecycleOwner, Observer {
            //variable
            myScore = dataShareViewModel.data.value?.mySum !!.toInt()
            yourScore = dataShareViewModel.data.value?.yourSum !!.toInt()
            totalScore = dataShareViewModel.data.value?.totalSum !!.toInt()

            //rating bar
            binding.mySumTv.text = (myScore * 100 / 10).toString()
            binding.mySumRb.rating = (myScore.toFloat() + 10) / 2

            binding.yourSumTv.text = (yourScore * 100 / 10).toString()
            binding.yourSumRb.rating = (yourScore.toFloat() + 10) / 2

            binding.totalSumTv.text = (totalScore * 100 / 10).toString()
            binding.totalSumRb.rating = (totalScore.toFloat() + 10) / 2
        })

        //advice

        myScore = dataShareViewModel.data.value?.mySum !!.toInt()
        yourScore = dataShareViewModel.data.value?.yourSum !!.toInt()
        totalScore = dataShareViewModel.data.value?.totalSum !!.toInt()

        maleViewModel.getMale(myScore).observe(viewLifecycleOwner, Observer {
            binding.myWeakTv.text = it.weak
            binding.myStrongTv.text = it.strong
        })
        femaleViewModel.getFemale(yourScore).observe(viewLifecycleOwner, Observer {
            binding.yourWeakTv.text = it.weak
            binding.yourStrongTv.text = it.strong
        })
        bothViewModel.getBoth(totalScore).observe(viewLifecycleOwner, Observer {
            binding.bothWeakTv.text = it.weak
            binding.bothStrongTv.text = it.strong
        })

        return binding.root
    }

}