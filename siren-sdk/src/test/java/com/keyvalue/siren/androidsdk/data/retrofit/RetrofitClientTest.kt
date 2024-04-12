package com.keyvalue.siren.androidsdk.data.retrofit

import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class RetrofitClientTest {
    @Mock
    private lateinit var mockOkHttpClient: OkHttpClient

    private lateinit var retrofitClient: RetrofitClient

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        retrofitClient = RetrofitClient()
    }

    @Test
    fun testGetRetrofitInstance() {
        // Given
        val baseUrl = "https://example.com"

        // Mock OkHttpClient.Builder
        val mockBuilder = Mockito.mock(OkHttpClient.Builder::class.java)
        `when`(mockBuilder.readTimeout(30, TimeUnit.SECONDS)).thenReturn(mockBuilder)
        `when`(mockBuilder.connectTimeout(30, TimeUnit.SECONDS)).thenReturn(mockBuilder)
        `when`(mockBuilder.build()).thenReturn(mockOkHttpClient)

        // When
        val actualRetrofit = RetrofitClient.getRetrofitInstance(baseUrl)

        // Then
        assertNotNull(actualRetrofit)
    }
}
