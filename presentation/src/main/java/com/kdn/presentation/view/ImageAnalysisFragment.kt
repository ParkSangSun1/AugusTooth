package com.kdn.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.kdn.presentation.R
import com.kdn.presentation.databinding.FragmentImageAnalysisBinding

class ImageAnalysisFragment : Fragment() {

    private val args by navArgs<ImageAnalysisFragmentArgs>()
    private lateinit var binding : FragmentImageAnalysisBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_analysis, container, false)

        return binding.root
    }
}