package br.com.fabricadesinapse.pokedex_android.view

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fabricadesinapse.pokedex_android.R
import br.com.fabricadesinapse.pokedex_android.domain.Pokemon
import br.com.fabricadesinapse.pokedex_android.viewmodel.PokemonViewModel
import br.com.fabricadesinapse.pokedex_android.viewmodel.PokemonViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.rvPokemons)
    }

    private val buttonVoltar by lazy {
        findViewById<Button>(R.id.btnVoltarFavorites)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, PokemonViewModelFactory())
            .get(PokemonViewModel::class.java)
    }

    private fun loadSavedPokemonIds(): List<Int> {
        val sharedPreferences = getSharedPreferences("FavoriteActivity", Context.MODE_PRIVATE)
        val savedPokemonIdsSet = sharedPreferences.getStringSet("savedPokemonIds", emptySet())
        return savedPokemonIdsSet?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val savedPokemonIds = loadSavedPokemonIds()

        buttonVoltar.setOnClickListener {
            finish()
        }

        viewModel.pokemons.observe(this, Observer { allPokemons ->
            val filteredPokemons = allPokemons.filter { pokemon ->
                savedPokemonIds.contains(pokemon?.number)
            }
            loadRecyclerView(filteredPokemons)
        })
    }

    private fun loadRecyclerView(pokemons: List<Pokemon?>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PokemonAdapter(pokemons)
    }
}
