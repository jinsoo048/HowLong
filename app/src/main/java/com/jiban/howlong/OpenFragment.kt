package com.jiban.howlong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiban.howlong.databinding.FragmentOpenBinding

class OpenFragment : Fragment() {
    private var _binding: FragmentOpenBinding? = null
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_open, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
