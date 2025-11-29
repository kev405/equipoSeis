package com.univalle.inventoryapp


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.univalle.inventoryapp.model.Inventory
import com.univalle.inventoryapp.repository.InventoryRepository
import com.univalle.inventoryapp.viewmodel.InventoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class InventoryViewModelTest {

    // Executes LiveData synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: InventoryRepository

    private lateinit var viewModel: InventoryViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = InventoryViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getListInventory should update listInventory LiveData`() = runTest {
        val sampleList = mutableListOf(
            Inventory(1, "Product A", 10.0, 5),
            Inventory(2, "Product B", 15.0, 3)
        )

        `when`(repository.getListInventory()).thenReturn(sampleList)

        viewModel.getListInventory()

        // Move coroutine forward
        testDispatcher.scheduler.advanceUntilIdle()

        Assert.assertEquals(sampleList, viewModel.listInventory.value)
    }

    @Test
    fun `insertProduct should call repository and refresh list`() = runTest {
        val item = Inventory(1, "New Product", 20.0, 2)

        `when`(repository.getListInventory()).thenReturn(mutableListOf(item))

        viewModel.insertProduct(item)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).insertProduct(item)
        verify(repository, times(1)).getListInventory()
        Assert.assertEquals(1, viewModel.listInventory.value?.size)
    }

    @Test
    fun `deleteInventory should call repository delete`() = runTest {
        val item = Inventory(1, "P", 10.0, 1)

        viewModel.deleteInventory(item)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).delete(item)
    }

    @Test
    fun `updateInventory should call repository update`() = runTest {
        val item = Inventory(1, "P", 10.0, 1)

        viewModel.updateInventory(item)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).update(item)
    }

    @Test
    fun `totalProducto should return price times quantity`() {
        val result = viewModel.totalProducto(10.0, 3)
        Assert.assertEquals(30.0, result, 0.001)
    }

    @Test
    fun `getTotalInventoryValue should return repository result`() = runTest {
        `when`(repository.getTotalInventoryValue()).thenReturn(250.0)

        val result = viewModel.getTotalInventoryValue()

        Assert.assertEquals(250.0, result, 0.001)
    }
}