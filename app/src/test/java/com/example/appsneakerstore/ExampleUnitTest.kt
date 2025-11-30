package com.example.appsneakerstore

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests básicos de la aplicación.
 * Tests principales en:
 * - ProductViewModelTest.kt (19 tests)
 * - ProductRepositoryTest.kt (8 tests)
 * - ProductModelTest.kt (13 tests)
 * - SneakerMapperTest.kt (13 tests)
 */
class ExampleUnitTest {
    @Test
    fun `app package is correct`() {
        val expectedPackage = "com.example.appsneakerstore"
        assertEquals(expectedPackage, this.javaClass.packageName)
    }
}