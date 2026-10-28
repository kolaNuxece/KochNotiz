package com.example.rezepteessen

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rezepteessen.databinding.MainLayoutBinding
import com.example.rezepteessen.ui.theme.RezepteEssenTheme

class MainActivity : ComponentActivity() {


    private lateinit var binding: MainLayoutBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    val dbHelper = DatabaseHelper(this)

    private val viewModel: MainViewModel by viewModels(){
        MainViewModelFactory(Repository(dbHelper))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = ItemAdapter(mutableListOf(),
            onDeleteClick = { item -> showDeleteItem(item) },
            onCookAgainClick = { item -> showEditDialogForCookAgain(item) }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.items.observe(this) { list ->
            adapter.updateList(list)
        }

        viewModel.loadItems()



        binding.buttonAdd.setOnClickListener {
            val nameS = binding.editTextName.text.toString()
            val ingredients = binding.editTextIngredients.text.toString()
            val note = binding.editTextNote.text.toString()
            if(nameS.isNotBlank() && ingredients.isNotBlank()){
                val newItem = Item(
                    essenName = nameS,
                    rezepte = ingredients,
                    noteZuEnde = note
                )
                viewModel.addItem(newItem)

                // очищення полів
                binding.editTextName.text.clear()
                binding.editTextIngredients.text.clear()
                binding.editTextNote.text.clear()
            }

        }

    }
    private fun showEditDialogForCookAgain(item: Item) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_item, null)
        val etName = dialogView.findViewById<EditText>(R.id.dialogEditName)
        val etIngredients = dialogView.findViewById<EditText>(R.id.dialogEditIngredients)
        val etNote = dialogView.findViewById<EditText>(R.id.dialogEditNote)

        // заповнюємо поля існуючими даними
        etName.setText(item.essenName)
        etIngredients.setText(item.rezepte)
        etNote.setText(item.noteZuEnde)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Rezept bearbeiten")
            .setView(dialogView)
            .setNegativeButton("Abbrechen", null)
            .setNeutralButton("Kopie erstellen") { _, _ ->
                // створюємо новий запис з полями з діалогу
                val newItem = Item(
                    id = 0,
                    essenName = etName.text.toString().trim(),
                    rezepte = etIngredients.text.toString().trim(),
                    noteZuEnde = etNote.text.toString().trim()
                )
                viewModel.addItem(newItem) // або viewModel.cookAgainAsCopy(newItem)
            }
            .setPositiveButton("Original aktualisieren") { _, _ ->
                // оновлюємо існуючий елемент з новими полями
                val updated = Item(
                    id = item.id,
                    essenName = etName.text.toString().trim(),
                    rezepte = etIngredients.text.toString().trim(),
                    noteZuEnde = etNote.text.toString().trim()
                )
                viewModel.updateItem(updated)
            }
            .create()

        dialog.show()
    }
    private fun showDeleteItem(item: Item){
        val dialog = AlertDialog.Builder(this)
            .setTitle("Löschung bestätigen")
            .setMessage("Möchten Sie das wirklich löschen?")
            .setPositiveButton("Ja") { _, _ ->
                // Якщо користувач натискає "Так", видаляємо користувача
                viewModel.deleteItem(item)
                viewModel.loadItems()  // Оновлюємо список після видалення
                Toast.makeText(this, "Rezept gelöscht", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Nein", null)  // Якщо користувач натискає "Ні", нічого не робимо
            .create()

        dialog.show()
    }
}