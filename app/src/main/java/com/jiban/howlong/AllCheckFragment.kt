package com.jiban.howlong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiban.howlong.databinding.FragmentAllCheckBinding

class AllCheckFragment : Fragment() {

    private lateinit var binding: FragmentAllCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCheckBinding.inflate(layoutInflater)

        return binding.root
    }
}