package com.bucic.project2_jiujitsufighters.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import com.bucic.project2_jiujitsufighters.repository.BJJFighterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: BJJFighterRepository
) : ViewModel() {

    private var _fighters: MutableLiveData<List<BJJFighterEntity>?> = MutableLiveData()
    val fighters: LiveData<List<BJJFighterEntity>?> = _fighters

    init {
        fighters()
    }
    fun fighters() = viewModelScope.launch {
        _fighters.postValue(repository.fighters())
    }
}