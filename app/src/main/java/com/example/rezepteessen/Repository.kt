package com.example.rezepteessen

class Repository(private val dbHelper: DatabaseHelper) {
    fun getItems(): List<Item> = dbHelper.getAllItems()

    fun addItem(item: Item) {
        dbHelper.createNewEssen(item)
    }
    fun deleteItem(item: Item) {
        dbHelper.writableDatabase.delete(
            "rezepteEssen",
            "id = ?",
            arrayOf(item.id.toString())
        )
    }
    fun updateItem(item: Item){
        dbHelper.updateEssen(item)
    }
}