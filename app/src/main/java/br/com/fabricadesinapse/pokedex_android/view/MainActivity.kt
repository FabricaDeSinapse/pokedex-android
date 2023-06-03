package br.com.fabricadesinapse.pokedex_android.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fabricadesinapse.pokedex_android.R
import br.com.fabricadesinapse.pokedex_android.api.PokemonRepository
import br.com.fabricadesinapse.pokedex_android.domain.Pokemon
import br.com.fabricadesinapse.pokedex_android.viewmodel.PokemonViewModel
import br.com.fabricadesinapse.pokedex_android.viewmodel.PokemonViewModelFactory
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.rvPokemons)
    }

    //texto com o pokemon digitado
    private val textInputEditText by lazy {
        findViewById<TextInputEditText>(R.id.inputPokemon)
    }

    //botão que procura
    private val buttonSearch by lazy {
        findViewById<Button>(R.id.btnProcura)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, PokemonViewModelFactory())
            .get(PokemonViewModel::class.java)
    }
    
    private fun scrollToPokemon(position: Int) {
        recyclerView.smoothScrollToPosition(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.pokemons.observe(this, Observer {
            loadRecyclerView(it)
        })

        //botão para procurar pokemon!!
        buttonSearch.setOnClickListener {
            PokemonRepository.searchPokemonByName(textInputEditText.text.toString(),
                onSuccess = { pokemon ->
                    pokemon != null
                    Toast.makeText(this, "Pokémon encontrado!", Toast.LENGTH_SHORT).show()
                    val pokemonName = textInputEditText.text.toString().toLowerCase()
                    val pokemonAdapter = recyclerView.adapter as PokemonAdapter
                    val position =  pokemonAdapter.getPositionOfPokemon(pokemonName)
                    scrollToPokemon(position)
                },
                onFailure = {
                    Toast.makeText(this, "Erro na procura do pokémon", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun loadRecyclerView(pokemons: List<Pokemon?>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PokemonAdapter(pokemons)
    }

}