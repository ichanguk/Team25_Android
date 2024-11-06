package com.kakaotech.team25.ui.reservation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaotech.team25.R
import com.kakaotech.team25.databinding.FragmentReservationStep9Binding
import com.kakaotech.team25.ui.reservation.adapters.ManagerRecyclerViewAdapter
import com.kakaotech.team25.ui.reservation.interfaces.OnManagerClickListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReservationStep9Fragment : Fragment() {
    private var _binding: FragmentReservationStep9Binding? = null
    private val binding get() = _binding!!
    private val viewModel: ManagerDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReservationStep9Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setManagerRecyclerView()
        collectManagerData()
        setManagerSearchListener()
        navigateToPrevious()
    }

    private fun collectManagerData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.managers.collectLatest {
                    (binding.managerRecyclerView.adapter as? ManagerRecyclerViewAdapter)?.submitList(it)
                }
            }
        }
    }

    private fun setManagerRecyclerView() {
        val managerClickListener = object : OnManagerClickListener {
            override fun onManagerClicked() {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, ReservationStep10Fragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        val adapter = ManagerRecyclerViewAdapter(managerClickListener)

        binding.managerRecyclerView.adapter = adapter
        binding.managerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setManagerSearchListener() {
        binding.searchManagerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateManagersByName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}