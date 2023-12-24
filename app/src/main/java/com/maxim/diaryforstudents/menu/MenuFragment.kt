package com.maxim.diaryforstudents.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxim.diaryforstudents.core.BaseFragment
import com.maxim.diaryforstudents.databinding.FragmentMenuBinding

class MenuFragment: BaseFragment<FragmentMenuBinding, MenuViewModel>() {
    override val viewModelClass: Class<MenuViewModel>
        get() = MenuViewModel::class.java

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.diaryButton.setOnClickListener {
            viewModel.diary()
        }
        binding.performanceButton.setOnClickListener {
            viewModel.performance()
        }
        binding.profileButton.setOnClickListener {
            viewModel.profile()
        }
        binding.newsButton.setOnClickListener {
            viewModel.news()
        }
    }
}