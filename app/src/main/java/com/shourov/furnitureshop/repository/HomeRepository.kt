package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.UserTable
import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.utils.DemoData

class HomeRepository(private val dao: AppDao) {
    fun getSpecialOfferData(): ArrayList<SpecialOfferModel> = DemoData().specialOfferData()

    fun getUserInfo(id: Int?): LiveData<UserTable?> = dao.getUserInfo(id)
}