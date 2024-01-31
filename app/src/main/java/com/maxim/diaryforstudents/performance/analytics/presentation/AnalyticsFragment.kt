package com.maxim.diaryforstudents.performance.analytics.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.diaryforstudents.core.presentation.BaseFragment
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.databinding.FragmentAnalyticsBinding

class AnalyticsFragment : BaseFragment<FragmentAnalyticsBinding, AnalyticsViewModel>() {
    private var isDependent = arguments?.getBoolean(INDEPENDENT_KEY) ?: true

    override lateinit var viewModelClass: Class<out AnalyticsViewModel>

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAnalyticsBinding.inflate(inflater, container, false)


    override var setOnBackPressedCallback = isDependent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        isDependent = arguments?.getBoolean(INDEPENDENT_KEY) ?: true
        viewModelClass = if (isDependent) AnalyticsNotInnerViewModel::class.java else
            AnalyticsInnerViewModel::class.java
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            viewModel.restore(BundleWrapper.Base(it))
        }

        val adapter = AnalyticsAdapter(object : AnalyticsAdapter.Listener {
            override fun changeQuarter(value: Int) {
                viewModel.changeQuarter(value)
            }

            override fun changeInterval(value: Int) {
                viewModel.changeInterval(value)
            }
        })
        binding.lessonsRecyclerView.adapter = adapter
        binding.lessonsRecyclerView.itemAnimator = null

        binding.retryButton.setOnClickListener {
            viewModel.reload()
        }

        viewModel.observe(this) {
            it.show(
                binding.titleTextView,
                adapter,
                binding.progressBar,
                binding.errorTextView,
                binding.retryButton
            )
        }

        viewModel.init(savedInstanceState == null, isDependent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    companion object {
        private const val INDEPENDENT_KEY = "independent_key"

        fun newInstance(isIndependent: Boolean): AnalyticsFragment {
            return AnalyticsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(INDEPENDENT_KEY, isIndependent)
                }
            }
        }
    }
}