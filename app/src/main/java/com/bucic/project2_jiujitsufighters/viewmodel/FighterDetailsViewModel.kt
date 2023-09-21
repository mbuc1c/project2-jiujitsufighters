package com.bucic.project2_jiujitsufighters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import com.bucic.project2_jiujitsufighters.repository.BJJFighterRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class FighterDetailsViewModel @AssistedInject constructor(
    @Assisted private val fighterId: Int,
    private val repository: BJJFighterRepository
) : ViewModel() {

    private var _fighter: MutableLiveData<BJJFighterEntity?> = MutableLiveData()
    val fighter: LiveData<BJJFighterEntity?> = _fighter

    init {
        getFighter(fighterId)
    }


    private fun getFighter(id: Int) = viewModelScope.launch {
        if (id != -1) {
            _fighter.postValue(repository.getFighter(id))
        }
    }

    fun insertFighter(fighter: BJJFighterEntity) = viewModelScope.launch {
        repository.insertFighter(fighter)
    }

    fun deleteFighter() = viewModelScope.launch {
        _fighter.value?.let { repository.deleteFighter(it) }
    }


    @AssistedFactory
    interface Factory {
        fun create(gameId: Int): FighterDetailsViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun providesFactory(
            assistedFactory: Factory,
            gameId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(gameId) as T
            }
        }
    }
}