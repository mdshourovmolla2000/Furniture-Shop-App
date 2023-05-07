package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.SpecialOfferData
import com.shourov.furnitureshop.utils.DemoData

class HomeRepository {
    fun getSpecialOfferData(): ArrayList<SpecialOfferData> = DemoData().specialOfferData()
}