package com.zeiris.cherryweather.data.remote.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val key: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url.newBuilder().addQueryParameter("appid", key).build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}