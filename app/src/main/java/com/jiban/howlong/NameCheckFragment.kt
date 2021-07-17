package com.jiban.howlong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiban.howlong.databinding.FragmentNameCheckBinding


class NameCheckFragment : Fragment() {
    private lateinit var binding: FragmentNameCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNameCheckBinding.inflate(layoutInflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}