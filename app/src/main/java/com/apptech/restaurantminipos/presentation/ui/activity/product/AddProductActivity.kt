package com.apptech.restaurantminipos.presentation.ui.activity.product

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.R
import com.apptech.restaurantminipos.data.dto.ProductDto
import com.apptech.restaurantminipos.databinding.ActivityAddProductBinding
import com.apptech.restaurantminipos.presentation.ui.fragment.CategoryListDialogFragment
import com.apptech.restaurantminipos.presentation.viewmodel.ProductViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.ImagePickerUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.MultiPartUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.OnItemSelectListener
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() , OnItemSelectListener {
    private var selectedUri: Uri?= null
    private var selectedCategoryCode: Int ?= null
    private var selectedCategoryName: String ?= null
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var launcher : ActivityResultLauncher<String>
    private val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onItemSelected(code: Int, name: String) {
        selectedCategoryCode = code
        selectedCategoryName = name
        binding.productCategoryTIET.setText(selectedCategoryName)
    }

    private fun initView(){
        setupImagePicker()
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            chooseImageMBt.setOnClickListener { launcher.launch("image/*") }
            productCategoryTIET.setOnClickListener { setupLoadCategoryListDialog() }
            saveMBt.setOnClickListener { setupAddProduct() }
        }
    }

    private fun setupImagePicker(){
        launcher = ImagePickerUtil.setup(this) { uri ->
            selectedUri = uri
            selectedUri?.let {
                binding.chosenIV.scaleType = ImageView.ScaleType.CENTER_CROP
                Picasso.get().load(selectedUri).into(binding.chosenIV)
            }
        }
    }

    private fun setupValidation(){
        val textWatcher = TextWatcherUtil.setup {
            val productCode = binding.productCodeTIET.text.toString()
            val productName = binding.productNameTIET.text.toString()
            val productCategory = binding.productCategoryTIET.text.toString()
            val productSellPrice = binding.productSellPriceTIET.text.toString()
            val enable = !TextUtils.isEmpty(productCode) && !TextUtils.isEmpty(productName) &&
                         !TextUtils.isEmpty(productCategory) && !TextUtils.isEmpty(productSellPrice)
            binding.apply {
                saveMBt.isEnabled = enable
                saveMBt.alpha = if(enable) 1f else 0.5f
            }
        }
        binding.apply {
            productCodeTIET.addTextChangedListener(textWatcher)
            productNameTIET.addTextChangedListener(textWatcher)
            productCategoryTIET.addTextChangedListener(textWatcher)
            productSellPriceTIET.addTextChangedListener(textWatcher)
        }
    }

    private fun setupLoadCategoryListDialog(){
        val categoryListDialog = CategoryListDialogFragment()
        categoryListDialog.show(supportFragmentManager,"Product Category Dialog")
    }

    private fun setupAddProduct(){
        selectedUri?.let { uri ->
            println("Add")
            val productCode = binding.productCodeTIET.text.toString()
            val productName = binding.productNameTIET.text.toString()
            val productSellPrice = binding.productSellPriceTIET.text.toString()
            productViewModel.add(
                ProductDto(MultiPartUtil.createImagePartFromUri(this,uri)!!,
                           MultiPartUtil.createStringPart(productCode),
                           MultiPartUtil.createStringPart(productName),
                           MultiPartUtil.createStringPart(selectedCategoryCode.toString()),
                           MultiPartUtil.createStringPart(productSellPrice)))
            setupObserveAddProduct()
        } ?: run {
            val dialogUtil = DialogUtil(this)
            dialogUtil.showWarningDialog(getString(R.string.product_image_required))
        }
    }

    private fun setupObserveAddProduct(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            productViewModel.added,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnSuccess(message)},
            onFailure = { message -> setupOnFailure(message)}
        )
    }

    private fun setupOnSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }
}