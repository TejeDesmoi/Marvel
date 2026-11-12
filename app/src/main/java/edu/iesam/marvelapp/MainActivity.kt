package edu.iesam.marvelapp

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.iesam.marvelapp.features.superheroes.data.SuperHeroDataRepository
import edu.iesam.marvelapp.features.superheroes.data.local.xml.SuperHeroXmlLocalDataSource
import edu.iesam.marvelapp.features.superheroes.domain.GetSuperHeroUseCase
import edu.iesam.marvelapp.features.superheroes.presentation.SuperHeroAdapter
import edu.iesam.marvelapp.features.superheroes.presentation.SuperHeroListViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: SuperHeroListViewModel by viewModels()
    private val superHeroAdapter = SuperHeroAdapter()

    /**
     * Ciclo de vida: Paso 1 OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("MainActivity","OnCreate")

        setupRecyclerView()
        initObserver()

        viewModel.loadSuperHeroes()

    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.superhero_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = superHeroAdapter
    }

    private fun initObserver() {
        viewModel.uiState.observe(this){ uiState ->
            Log.d("MainActivity", uiState.isLoading.toString())
            bind(uiState)
        }
    }

    private fun bind(uiState: SuperHeroListViewModel.UiState){
        val spinner = findViewById<ProgressBar>(R.id.action_bar_spinner)
        val errorLayout = findViewById<View>(R.id.layout_error)
        val recyclerView = findViewById<RecyclerView>(R.id.superhero_list)

        /** Comprobar estado de la vista **/
        if (uiState.isLoading){
            spinner.visibility = View.VISIBLE
        } else {
            spinner.visibility = View.GONE
        }

        if (uiState.hasError){
            errorLayout.visibility = View.VISIBLE
        } else {
            errorLayout.visibility = View.GONE
        }

        uiState.superHeroes?.let{ superHeroes ->
            if (superHeroes.isNotEmpty()){
                recyclerView.visibility = View.VISIBLE
                superHeroAdapter.updateList(superHeroes)
            } else {
                //muestra la pantalla una lista vacia de superheroes
            }
        }
    }

}