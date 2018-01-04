package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class LoginUnitTest {
    val mFirebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }


    @Test
    fun givenMailAndPasswordLoginTest() {
        var result = -1
        val signal = CountDownLatch(1)

        mFirebaseAuth.createUserWithEmailAndPassword("hyelim0716@naver.com", "1111111")
                .addOnCompleteListener {
                    println("Completed!")
                    result = 0
                    signal.countDown()
                }.addOnFailureListener {
                    println("Failed. error: ${it.message}")
                    result = 1
                }.addOnSuccessListener {
                    println("Login Success")
                    signal.countDown()
                    result = 2
                }

        signal.await()

        assertEquals(result, 2)

    }
}