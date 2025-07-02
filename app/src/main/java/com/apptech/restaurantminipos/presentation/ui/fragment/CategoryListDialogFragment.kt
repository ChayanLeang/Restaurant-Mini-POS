package com.apptech.restaurantminipos.presentation.ui.fragment

import android.content.Context
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apptech.restaurantminipos.databinding.FragmentCategoryListDialogBinding
import com.apptech.restaurantminipos.domain.model.Category
import com.apptech.restaurantminipos.presentation.adapter.CategoryListAdapter
import com.apptech.restaurantminipos.presentation.viewmodel.CategoryViewModel
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.OnItemSelectListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListDialogFragment : DialogFragment() {
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var categoryListAdapter: CategoryListAdapter
    private var onItemSelectListener: OnItemSelectListener ?= null
    private lateinit var binding: FragmentCategoryListDialogBinding
    private val categoryViewModel : CategoryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryListDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDependencies()
        initView()
        setupLoadCategories()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onItemSelectListener = context as? OnItemSelectListener
    }

    private fun setupDependencies(){
        loadingDialogUtil = LoadingDialogUtil(requireContext())
        categoryListAdapter = CategoryListAdapter(requireContext()){ code,name ->
            onItemSelectListener?.onItemSelected(code,name)
            dismiss()
        }
    }

    private fun initView(){
        binding.cancelMBt.setOnClickListener { dismiss() }
    }

    private fun setupLoadCategories(){
        categoryViewModel.loadAll()
        ObserveResource.setup(
            this,
            categoryViewModel.loadedAll,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { categories -> setupOnSuccess(categories) },
            onFailure = { _ -> setupOnLoadCategoriesFailure() }
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.apply {
            noItemIV.visibility = View.GONE
            noItemTV.visibility = View.GONE
            rv.visibility = View.GONE
        }
    }

    private fun setupOnSuccess(categories: List<Category>){
        if(categories.isEmpty()){
            binding.apply {
                noItemIV.visibility = View.VISIBLE
                noItemTV.visibility = View.VISIBLE
            }
        }
        else{
            categoryListAdapter.setCategories(categories)
            binding.rv.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = categoryListAdapter
            }
        }
    }

    private fun setupOnLoadCategoriesFailure(){
        binding.apply {
            noItemIV.visibility = View.VISIBLE
            noItemTV.visibility = View.VISIBLE
        }
    }
}