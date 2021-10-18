package com.kdn.presentation.view.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.kdn.presentation.R
import com.kdn.presentation.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {
    private lateinit var binding : FragmentLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_location,container,false)
        binding.fragment = this
        return binding.root
    }

    //뒤로가기 클릭
    fun backBtn(view: View){
        view.findNavController().popBackStack()
    }
}