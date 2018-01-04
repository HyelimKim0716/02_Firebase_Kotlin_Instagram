package com.example.myapplication

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.action.ViewActions.*

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.myapplication.view.login.LoginActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch

/**
 * Created by owner on 2018-01-03.
 *
 * Test
 * User Interface
 *  input : input email and password, click button
 *  output : show toast, MainActivity
 *
 *  * ArgumentCater<List<String>> captor : 특정 메서드가 실행될 때 인자값이 잘 넘어가는 지 확인
 *  verify(mockedList).addAll(captor.capture())
 *
 */
@RunWith(AndroidJUnit4::class)
//@RunWith(MockitoJUnitRunner::class)
class LoginActivityTest {

    @get:Rule
    val mActivityRule: ActivityTestRule<LoginActivity> by lazy { ActivityTestRule<LoginActivity>(LoginActivity::class.java) }


//    val mActivity: LoginActivity by lazy { mActivityRule.activity }

    val signal = CountDownLatch(1)      // Latch to wait countdown 1
    @Before
    fun setUp() {

    }

    @Test
    fun givenMailAndPassword_whenClickLoginBtn_LoginSuccess_thenMainActivity() {


        val mActivity = spy(mActivityRule.activity)
        val spyPreesnter = spy(mActivity.mPresenter)

        // Given
        onView(withId(R.id.et_email)).perform(typeText("hyelim0716@naver.com"))
        onView(withId(R.id.et_password))
                .perform(typeText("qwer6538"))
                .perform(ViewActions.closeSoftKeyboard())

        // When
        assertEquals(mActivity.getUserPassword().length > 6, true)
        assertEquals(mActivity.getUserMail().contains("@"), true)
        assertEquals(mActivity.checkEmailPasswordValidity(), true)

        println(mActivity.getUserMail())

        onView(withId(R.id.btn_email_sign_in))
                .perform(click())

        // Stubbing
        // sign in -> server 통신?
        // Then next activity : MainActivity

        Mockito.verify(spyPreesnter).registerEmailAndSignIn()

    }

    fun givenMailAndPassword_whenClickLoginBtn_LoginFail_thenMainActivity() {
        // Given
        onView(withId(R.id.et_email)).perform(typeText("hyelim0716@naver.com"))
        onView(withId(R.id.et_password))
                .perform(typeText("111111"))
                .perform(ViewActions.closeSoftKeyboard())

        // When
        // Stubbing
        // sign in -> server 통신?
//        Mockito.`when`(mActivity.mPresenter.registerEmailAndSignIn())
//        onView(withId(R.id.btn_google_sign_in))
//                .perform(click())
//
//        // Then next activity : MainActivity
//        Mockito.verify(mActivity.mPresenter).registerEmailAndSignIn()
//
//        Mockito.doReturn(false).`when`(mActivity.mPresenter).registerEmailAndSignIn()


//        Mockito.doAnswer {
//            println("registerEmailAndSignIn() called, false")
//        }.`when`(mActivity.mPresenter).registerEmailAndSignIn()

    }

    fun givenGoogleAuth_whenClickGoogleLoginBtn_LoginSuccess_thenMainActivity() {


    }


}