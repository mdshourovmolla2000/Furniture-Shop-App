package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.OnBoardingModel
import com.shourov.furnitureshop.utils.DemoData

class OnBoardingRepository {
    fun getOnBoardingData(): List<OnBoardingModel> = DemoData().onBoardingData()
}