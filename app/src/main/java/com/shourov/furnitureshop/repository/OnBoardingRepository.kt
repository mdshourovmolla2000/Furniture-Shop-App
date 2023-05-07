package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.OnBoardingData
import com.shourov.furnitureshop.utils.DemoData

class OnBoardingRepository {
    fun getOnBoardingData(): ArrayList<OnBoardingData> = DemoData().onBoardingData()
}