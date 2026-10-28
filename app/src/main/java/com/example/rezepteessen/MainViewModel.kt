package com.example.rezepteessen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> = _items

    fun loadItems() {
        _items.value = repository.getItems()
    }

    fun deleteItem(item: Item) {
        repository.deleteItem(item)
        loadItems()
    }

    fun addItem(item: Item) {
        repository.addItem(item)
        loadItems()
    }
    fun updateItem(item: Item){
        repository.updateItem(item)
        loadItems()
    }
}