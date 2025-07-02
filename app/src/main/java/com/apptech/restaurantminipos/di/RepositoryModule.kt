package com.apptech.restaurantminipos.di

import com.apptech.restaurantminipos.data.repository.CartRepositoryImpl
import com.apptech.restaurantminipos.data.repository.CategoryRepositoryImpl
import com.apptech.restaurantminipos.data.repository.CustomerRepositoryImpl
import com.apptech.restaurantminipos.data.repository.ExpenseRepositoryImpl
import com.apptech.restaurantminipos.data.repository.OrderTypeRepositoryImpl
import com.apptech.restaurantminipos.data.repository.PaymentMethodRepositoryImpl
import com.apptech.restaurantminipos.data.repository.ProductRepositoryImpl
import com.apptech.restaurantminipos.data.repository.SaleDetailRepositoryImpl
import com.apptech.restaurantminipos.data.repository.SaleRepositoryImpl
import com.apptech.restaurantminipos.domain.repository.CartRepository
import com.apptech.restaurantminipos.domain.repository.CategoryRepository
import com.apptech.restaurantminipos.domain.repository.CustomerRepository
import com.apptech.restaurantminipos.domain.repository.ExpenseRepository
import com.apptech.restaurantminipos.domain.repository.OrderTypeRepository
import com.apptech.restaurantminipos.domain.repository.PaymentMethodRepository
import com.apptech.restaurantminipos.domain.repository.ProductRepository
import com.apptech.restaurantminipos.domain.repository.SaleDetailRepository
import com.apptech.restaurantminipos.domain.repository.SaleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(customerRepositoryImpl: CustomerRepositoryImpl):
                                                                      CustomerRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(expenseRepositoryImpl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindSaleRepository(saleRepositoryImpl: SaleRepositoryImpl): SaleRepository

    @Binds
    @Singleton
    abstract fun bindSaleDetailRepository(saleDetailRepositoryImpl: SaleDetailRepositoryImpl):
                                                                          SaleDetailRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl):
                                                                     CategoryRepository

    @Binds
    @Singleton
    abstract fun bindPaymentMethodRepository(paymentMethodRepositoryImpl: PaymentMethodRepositoryImpl):
                                                                                PaymentMethodRepository

    @Binds
    @Singleton
    abstract fun bindOrderTypeRepository(orderTypeRepositoryImpl: OrderTypeRepositoryImpl):
                                                                        OrderTypeRepository
}