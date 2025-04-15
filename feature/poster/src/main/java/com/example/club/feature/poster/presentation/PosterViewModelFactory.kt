package com.example.club.feature.poster.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.event.domain.usecase.GetEventPosterUseCase


class PosterViewModelFactory(private val getEventPosterUseCase: GetEventPosterUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == PosterViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return PosterViewModel(getEventPosterUseCase) as T
    }
}
