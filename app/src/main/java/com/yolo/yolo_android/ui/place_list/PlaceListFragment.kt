package com.yolo.yolo_android.ui.place_list

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentPlaceListBinding
import com.yolo.yolo_android.model.Document
import dagger.hilt.android.AndroidEntryPoint

interface SelectedPlaceCallback {
    fun setResult(document: Document)
}

@AndroidEntryPoint
class PlaceListFragment: BindingFragment<FragmentPlaceListBinding>(R.layout.fragment_place_list),
    SelectedPlaceCallback {

    companion object {
        const val REQ_SELECTED_PLACE = "REQ_SELECTED_PLACE"
        fun newInstance() = PlaceListFragment()
    }

    private val viewModel: PlaceListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        val placeListAdapter = PlaceListAdapter(this)
        binding.rvPlaceList.apply {
            adapter = placeListAdapter
            setHasFixedSize(true)
        }
        binding.searchVPlace.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchKeyword(newText.toString())
                return true
            }
        })
        viewModel.placeListFlow.observe(viewLifecycleOwner, Observer {
            placeListAdapter.submitList(it)
        })

    }

    override fun setResult(document: Document) {
        // 키보드 내리기
        val inputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchVPlace.windowToken, 0)
        setFragmentResult(REQ_SELECTED_PLACE, bundleOf("data" to document))
        findNavController().popBackStack()
    }
}