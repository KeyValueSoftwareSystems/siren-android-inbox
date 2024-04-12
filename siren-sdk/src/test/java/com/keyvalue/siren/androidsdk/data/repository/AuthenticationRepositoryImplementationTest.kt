package com.keyvalue.siren.androidsdk.data.repository

import com.keyvalue.siren.androidsdk.data.model.AuthenticationResponse
import com.keyvalue.siren.androidsdk.data.model.Data
import com.keyvalue.siren.androidsdk.data.service.AuthenticationApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class AuthenticationRepositoryImplementationTest {
    @Mock
    lateinit var authenticationApiService: AuthenticationApiService

    private lateinit var authenticationRepository: AuthenticationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        authenticationRepository = AuthenticationRepositoryImplementation("https://example.com")
    }

    @Test
    fun verifyToken_Success() {
        // Given
        val userToken = "userToken"
        val recipientId = "recipientId"
        val responseData = Data("success")
        val response = AuthenticationResponse(responseData, null)
        val responseBody = Response.success(response)
        runBlocking {
            `when`(authenticationApiService.verifyToken(recipientId, userToken)).thenReturn(responseBody)
        }
    }

    @Test
    fun verifyToken_Error() {
        // Given
        val userToken = "userToken"
        val recipientId = "recipientId"
        val errorBody = "{\"error\":{\"errorCode\":404,\"message\":\"Not found\"}}"
        val mockJsonObject = mock(JSONObject::class.java)
        `when`(mockJsonObject.put(anyString(), anyString())).thenReturn(mockJsonObject)

        val responseBody =
            Response.error<AuthenticationResponse>(
                404,
                errorBody.toResponseBody(null),
            )
        runBlocking {
            `when`(authenticationApiService.verifyToken(recipientId, userToken)).thenReturn(responseBody)
        }
    }
}
