package com.northshore

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        client.get("/test").apply {
            assertEquals(HttpStatusCode.OK, status)
        }


        client.get("/dashboard").apply {
            assertEquals(HttpStatusCode.Found, status)
        }
    }

}
