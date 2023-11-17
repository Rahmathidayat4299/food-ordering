package com.binar.foodorder.data.network.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FirebaseAuthDataSourceImplTest {

    @MockK(relaxed = true)
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dataSource: FirebaseAuthDataSource

    val currentUser = mockk<FirebaseUser>(relaxed = true)

    @Before
    fun setUp() {
        init(this)
        dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Test
    fun `testLogin`() {
        every { firebaseAuth.signInWithEmailAndPassword(any(), any()) } returns mockTasAuthResult()
        runTest {
            val result = dataSource.doLogin("email", "password")
            Assert.assertEquals(result, true)
        }
    }

    @Test
    fun `getCurrentUser should return the current user`() {
        runTest {
            every { firebaseAuth.currentUser } returns currentUser

            val result = dataSource.getCurrentUser()

            Assert.assertEquals(currentUser, result)
        }
    }

    @Test
    fun `isLoggedIn should return true when there is a current user`() {
        runTest {
            every { firebaseAuth.currentUser } returns currentUser
            val result = dataSource.isLoggedIn()
            Assert.assertTrue(result)
        }
    }

    @Test
    fun `test update email`() {
        coEvery { firebaseAuth.currentUser?.updateEmail(any()) } returns mockTaskVoid()
        runTest {
            val result = dataSource.updateEmail("new email")
            Assert.assertEquals(result, true)
        }
    }

    @Test
    fun `test send change password by email`() {
        coEvery { firebaseAuth.currentUser?.email } returns ""
        runTest {
            val result = dataSource.sendChangePasswordRequestByEmail()
            Assert.assertEquals(result, true)
        }
    }

    @Test
    fun `test update password`() {
        coEvery { firebaseAuth.currentUser?.updatePassword(any()) } returns mockTaskVoid()
        runTest {
            val result = dataSource.updatePassword("new password")
            Assert.assertEquals(result, true)
        }
    }

    @Test
    fun `doLogout`() {
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns firebaseAuth
        every { firebaseAuth.signOut() } returns Unit
        val result = dataSource.doLogut()
        Assert.assertEquals(result, true)
    }

    @Test
    fun `testRegister`() {
        runTest {
            mockkConstructor(UserProfileChangeRequest.Builder::class)
            every { anyConstructed<UserProfileChangeRequest.Builder>().build() } returns mockk()
            val mockAuthResult = mockTasAuthResult()
            every { firebaseAuth.createUserWithEmailAndPassword(any(), any()) } returns mockAuthResult
            val mockUser = mockk<FirebaseUser>(relaxed = true)
            every { mockAuthResult.result.user } returns mockUser
            every { mockUser.updateProfile(any()) } returns mockTaskVoid()
            val result = dataSource.doRegister("name", "email", "password")
            Assert.assertEquals(result, true)
        }
    }

    private fun mockTaskVoid(exception: Exception? = null): Task<Void> {
        val task: Task<Void> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedVoid: Void = mockk(relaxed = true)
        every { task.result } returns relaxedVoid
        return task
    }

    private fun mockTasAuthResult(exception: Exception? = null): Task<AuthResult> {
        val task: Task<AuthResult> = mockk(relaxed = true)
        every { task.isComplete } returns true
        every { task.exception } returns exception
        every { task.isCanceled } returns false
        val relaxedResult: AuthResult = mockk(relaxed = true)
        every { task.result } returns relaxedResult
        every { task.result.user } returns mockk(
            relaxed = true
        )
        return task
    }
}
