package com.incwelltechnology.lms.activity

import androidx.fragment.app.Fragment

interface NavigationHostActivity {
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}