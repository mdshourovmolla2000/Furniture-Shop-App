package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.database.tables.AddressTable

interface AddressItemClickListener {
    fun onAddressItemClick(currentItem: AddressTable, clickedOn: String)
}