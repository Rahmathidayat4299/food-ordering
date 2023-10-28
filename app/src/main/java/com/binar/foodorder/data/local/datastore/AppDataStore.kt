package com.binar.foodorder.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
val Context.appDataStore by preferencesDataStore(
    name = "foodOrder"
)
