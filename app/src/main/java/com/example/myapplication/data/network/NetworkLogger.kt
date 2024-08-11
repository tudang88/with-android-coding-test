package com.example.myapplication.data.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class NetworkLogger: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Timber.d("NetworkLogger - STATUS CODE: ${response.code}")
        return response
    }
}