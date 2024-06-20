package com.c241ps319.patera.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.databinding.FragmentHistoryBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.main.MainViewModel

class HistoryFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentHistoryBinding? = null

    //    Use MainViewModel
    private var session: UserModel? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // get MainViewModel using ViewModelProvider
        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireContext())
        )[MainViewModel::class.java]

        // Initialize RecyclerView
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        // get Session
        mainViewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (session != null) {
                this.session = session

                // get Histories
                mainViewModel.getHistories(session.token).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Error -> {
                            showToast(result.error.toString())
                            showLoading(false)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            //  setup recycler view and set data
                            val histories = result.data.data
                            if (histories?.size != 0) {
                                binding.tvHistoryNull.visibility = View.GONE
                                val adapter = HistoryAdapter(histories!!)
                                binding.rvHistory.adapter = adapter
                            } else {
                                binding.tvHistoryNull.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}