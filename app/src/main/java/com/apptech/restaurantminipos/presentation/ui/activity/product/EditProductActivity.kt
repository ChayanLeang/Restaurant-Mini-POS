package com.apptech.restaurantminipos.presentation.ui.activity.product

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apptech.restaurantminipos.data.dto.ProductDto
import com.apptech.restaurantminipos.databinding.ActivityEditProductBinding
import com.apptech.restaurantminipos.domain.model.Product
import com.apptech.restaurantminipos.presentation.viewmodel.ProductViewModel
import com.apptech.restaurantminipos.util.DialogUtil
import com.apptech.restaurantminipos.util.ImageDownloadUtil
import com.apptech.restaurantminipos.util.ImagePickerUtil
import com.apptech.restaurantminipos.util.LoadingDialogUtil
import com.apptech.restaurantminipos.util.MultiPartUtil
import com.apptech.restaurantminipos.util.ObserveResource
import com.apptech.restaurantminipos.util.OnItemSelectListener
import com.apptech.restaurantminipos.util.TextWatcherUtil
import com.apptech.restaurantminipos.util.ToastUtil
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProductActivity : AppCompatActivity() , OnItemSelectListener {
    private var selectedUri: Uri ?= null
    private var imageDownload: File ?= null
    private var selectedCategoryCode: Int ?= null
    private var selectedCategoryName: String ?= null
    private lateinit var binding: ActivityEditProductBinding
    private lateinit var loadingDialogUtil: LoadingDialogUtil
    private lateinit var launcher : ActivityResultLauncher<String>
    private val productViewModel : ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependency()
        setupLoadProductByCode()
    }

    override fun onItemSelected(code: Int, name: String) {
        selectedCategoryCode = code
        selectedCategoryName = name
        binding.productCategoryTIET.setText(selectedCategoryName)
    }

    private fun setupDependency(){
        loadingDialogUtil = LoadingDialogUtil(this)
    }

    private fun setupLoadProductByCode(){
        val code = intent.getIntExtra("code",0)
        productViewModel.loadByCode(code)
        ObserveResource.setup(
            this,
            productViewModel.loadedByCode,
            onVisibility = { setupOnVisibility() },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { product -> suspend { setupOnLoadProductSuccess(product) } },
            onFailure = { message -> setupOnFailure(message)}
        )
    }

    private fun setupOnVisibility(){
        loadingDialogUtil.show(false)
        binding.sv.visibility = View.GONE
    }

    private suspend fun setupOnLoadProductSuccess(product: Product){
        selectedCategoryCode = product.category.code
        imageDownload = ImageDownloadUtil.download(this,product.imageUrl)
        selectedUri?.let {
            Picasso.get().load(selectedUri).into(binding.chosenIV)
        } ?: run {
            Picasso.get().load(product.imageUrl).into(binding.chosenIV)
        }
        binding.apply {
            sv.visibility = View.VISIBLE
            chosenIV.scaleType = ImageView.ScaleType.CENTER_CROP
            productCodeTIET.setText(product.code)
            productNameTIET.setText(product.name)
            productCategoryTIET.setText(product.category.name)
            productSellPriceTIET.setText(String.format("%s",product.sellPrice))
        }
        initView(product.code)
    }

    private fun initView(code: Int){
        launcher = ImagePickerUtil.setup(this) { uri -> selectedUri = uri }
        setupValidation()
        binding.apply {
            mtb.setNavigationOnClickListener { finish() }
            chooseImageMBt.setOnClickListener { launcher.launch("image/*") }
            saveMBt.setOnClickListener { setupEditProduct(code) }
        }
    }

    private fun setupValidation(){
        val textWatcher = TextWatcherUtil.setup {
            val productCode = binding.productCodeTIET.text.toString()
            val productName = binding.productNameTIET.text.toString()
            val productSellPrice = binding.productSellPriceTIET.text.toString()
            val enable = !TextUtils.isEmpty(productCode) && !TextUtils.isEmpty(productName) &&
                         !TextUtils.isEmpty(productSellPrice)
            binding.apply {
                saveMBt.isEnabled = enable
                saveMBt.alpha = if(enable) 1f else 0.5f
            }
        }
        binding.apply {
            productCodeTIET.addTextChangedListener(textWatcher)
            productNameTIET.addTextChangedListener(textWatcher)
            productSellPriceTIET.addTextChangedListener(textWatcher)
        }
    }

    private fun setupEditProduct(code: Int){
        val image = selectedUri?.let { uri ->
            MultiPartUtil.createImagePartFromUri(this,uri)
        } ?: run { MultiPartUtil.createImagePartFromImageDownload(imageDownload) }
        val productName = binding.productNameTIET.text.toString()
        val productSellPrice = binding.productSellPriceTIET.text.toString()
        productViewModel.edit(code,
            ProductDto(image,null, MultiPartUtil.createStringPart(productName),
                MultiPartUtil.createStringPart(selectedCategoryCode.toString()),
                MultiPartUtil.createStringPart(productSellPrice)))
        setupObserveEditProduct()
    }

    private fun setupObserveEditProduct(){
        val loadingDialogUtil = LoadingDialogUtil(this)
        ObserveResource.setup(
            this,
            productViewModel.edited,
            onVisibility = { loadingDialogUtil.show(false) },
            onLoading = { loadingDialogUtil.show(true) },
            onSuccess = { message -> setupOnEditProductSuccess(message) },
            onFailure = { message -> setupOnFailure(message) }
        )
    }

    private fun setupOnEditProductSuccess(message: String){
        ToastUtil.show(this,message)
        setResult(RESULT_OK)
        finish()
    }

    private fun setupOnFailure(message: String){
        val dialogUtil = DialogUtil(this)
        dialogUtil.showErrorDialog(message)
    }
}