package com.example.demoapp.components

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created on 2020-01-18.
 */

interface CoroutineModule {

    fun launch(work: suspend(() -> Unit)) = CoroutineScope(Dispatchers.Main).launch {
        work()
    }
}