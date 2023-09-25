package com.mazzampr.githubsearch.util

import android.app.Activity
import android.widget.Toast
import org.junit.Assert.*

import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun Activity.toast() {
        val text = "Halo test"
        Toast.makeText(this, text, Toast.LENGTH_SHORT)
        assertEquals("Halo Test", toast("Halo Test"))
    }

    @Test
    fun testToast() {
    }

    @Test
    fun show() {
    }

    @Test
    fun hide() {
    }
}