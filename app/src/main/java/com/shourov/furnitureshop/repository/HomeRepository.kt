package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.SpecialOfferModel
import com.shourov.furnitureshop.utils.DemoData

class HomeRepository {
    fun getSpecialOfferData(): ArrayList<SpecialOfferModel> = DemoData().specialOfferData()
}