package com.jiban.howlong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jiban.howlong.databinding.FragmentAllCheckResultBinding
import com.jiban.howlong.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCheckResultFragment : Fragment() {

    private lateinit var binding: FragmentAllCheckResultBinding
    private lateinit var dataShareViewModel: DataShareViewModel
    private lateinit var dataBirthShareViewModel: DataBirthShareViewModel

    private val characterViewModel: CharacterViewModel by viewModels()
    private val maleViewModel: MaleViewModel by viewModels()
    private val femaleViewModel: FemaleViewModel by viewModels()
    private val bothViewModel: BothViewModel by viewModels()

    private var myScore: Int = 0
    private var yourScore: Int = 0
    private var totalScore: Int = 0
    private var myBirthScore: Int = 0
    private var yourBirthScore: Int = 0
    private var totalBirthScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCheckResultBinding.inflate(layoutInflater)

        ////Name
        // result sum
        dataShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataBirthShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataBirthShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataShareViewModel.data.observe(viewLifecycleOwner, Observer {

            myScore = dataShareViewModel.data.value?.mySum !!.toInt()
            yourScore = dataShareViewModel.data.value?.yourSum !!.toInt()
            totalScore = dataShareViewModel.data.value?.totalSum !!.toInt()

            //name score
            binding.mySumTv.text = (myScore * 100 / 10).toString()
            binding.yourSumTv.text = (yourScore * 100 / 10).toString()
            binding.totalSumTv.text = (totalScore * 100 / 10).toString()

            //advice
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

            //bar
            barCalc()
        })

        //// Birth
        dataBirthShareViewModel = activity?.run {
            ViewModelProviders.of(this).get(DataBirthShareViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dataBirthShareViewModel.data.observe(viewLifecycleOwner, Observer {
            //variable
            myBirthScore = dataBirthShareViewModel.data.value?.myBirthSum !!.toInt()
            yourBirthScore = dataBirthShareViewModel.data.value?.yourBirthSum !!.toInt()
            totalBirthScore = dataBirthShareViewModel.data.value?.totalBirthSum !!.toInt()

            //birth score
            binding.myBirthSumTv.text = (myBirthScore * 100 / 10).toString()
            binding.yourBirthSumTv.text = (yourBirthScore * 100 / 10).toString()
            binding.totalBirthSumTv.text = (totalBirthScore * 100 / 10).toString()

            //advice
            maleViewModel.getMale(myBirthScore).observe(viewLifecycleOwner, Observer {
                binding.myBirthWeakTv.text = it.weak
                binding.myBirthStrongTv.text = it.strong
            })
            femaleViewModel.getFemale(yourBirthScore).observe(viewLifecycleOwner, Observer {
                binding.yourBirthWeakTv.text = it.weak
                binding.yourBirthStrongTv.text = it.strong
            })
            bothViewModel.getBoth(totalBirthScore).observe(viewLifecycleOwner, Observer {
                binding.bothBirthWeakTv.text = it.weak
                binding.bothBirthStrongTv.text = it.strong
            })

            //bar
            barCalc()
        })

        return binding.root
    }

    fun barCalc() {
        //rating bar score final calculation
        binding.mySumRb.rating =
            (((myScore.toFloat() + 10) / 2) + ((myBirthScore.toFloat() + 10) / 2)) / 2
        binding.yourSumRb.rating =
            (((yourScore.toFloat() + 10) / 2) + ((yourBirthScore.toFloat() + 10) / 2)) / 2
        binding.bothSumRb.rating =
            (((totalScore.toFloat() + 10) / 2) + ((totalBirthScore.toFloat() + 10) / 2)) / 2

    }
}