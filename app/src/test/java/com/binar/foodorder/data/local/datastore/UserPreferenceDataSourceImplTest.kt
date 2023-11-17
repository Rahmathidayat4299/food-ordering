package com.binar.foodorder.data.local.datastore

import app.cash.turbine.test
import com.binar.foodorder.util.PreferenceDataStoreHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserPreferenceDataSourceImplTest {

    @MockK
    lateinit var preferenceDataStoreHelper: PreferenceDataStoreHelper
    private lateinit var userPreferenceDataSourceImpl: UserPreferenceDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userPreferenceDataSourceImpl = UserPreferenceDataSourceImpl(preferenceDataStoreHelper)
    }

    @Test
    fun `getUserLayoutPrefFlow`() {
        runTest {
            coEvery { preferenceDataStoreHelper.getPreference(any(), true) } returns flow { emit(false) }
            userPreferenceDataSourceImpl.getUserLayoutPrefFlow().test {
                val itemResult = awaitItem()
                Assert.assertEquals(false, itemResult)
                awaitComplete()
            }
        }
    }

    @Test
    fun setUserLayoutPref() {
        runTest {
            coEvery { preferenceDataStoreHelper.putPreference(any(), true) } returns Unit
            val result = userPreferenceDataSourceImpl.setUserLayoutPref(true)
            coVerify { preferenceDataStoreHelper.putPreference(any(), true) }
            TestCase.assertEquals(result, Unit)
        }
    }

    @Test
    fun getUserLayoutPref() {
        runTest {
            coEvery { preferenceDataStoreHelper.getFirstPreference(any(), true) } returns false
            val result = userPreferenceDataSourceImpl.getUserLayoutPref()
            coVerify { preferenceDataStoreHelper.getFirstPreference(any(), true) }
            TestCase.assertEquals(result, false)
        }
    }
}
