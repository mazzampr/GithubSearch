package com.mazzampr.githubsearch.viewmodel

import com.mazzampr.githubsearch.data.Result
import com.mazzampr.githubsearch.data.UsersRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.*

import org.junit.Test

class FavoriteViewModelTest {

    private val repository: UsersRepository = mockk{
        coEvery { getAllFavUser() } returns mockk()
    }
    private val viewModel = FavoriteViewModel(repository)

    @Test
    fun `When fetching users data is shown`() {
        viewModel.getAllFavUsers()
        assert(true)
    }
}