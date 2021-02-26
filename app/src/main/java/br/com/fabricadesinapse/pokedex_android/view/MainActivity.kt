package br.com.fabricadesinapse.pokedex_android.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.fabricadesinapse.pokedex_android.R
import br.com.fabricadesinapse.pokedex_android.api.PokemonRepository
import br.com.fabricadesinapse.pokedex_android.domain.Pokemon

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvPokemons)

//        val charmander = Pokemon(
//            "https://assets.pokemon.com/assets/cms2/img/pokedex/detail/004.png",
//            4,
//            "Charmander",
//            listOf(PokemonType("Fire"))
//        )
//
//        val pokemons = listOf(charmander, charmander, charmander, charmander, charmander)

        Thread(Runnable {
            loadPokemons()
        }).start()
    }

    private fun loadPokemons() {
        val pokemonsApiResult = PokemonRepository.listPokemons()

        pokemonsApiResult?.results?.let {
            val pokemons: List<Pokemon?> = it.map { pokemonResult ->
                val number = pokemonResult.url
                    .replace("https://pokeapi.co/api/v2/pokemon/", "")
                    .replace("/", "").toInt()

                val pokemonApiResult = PokemonRepository.getPokemon(number)

                pokemonApiResult?.let {
                    Pokemon(
                        pokemonApiResult.id,
                        pokemonApiResult.name,
                        pokemonApiResult.types.map { type ->
                            type.type
                        }
                    )
                }
            }

            val layoutManager = LinearLayoutManager(this)

            recyclerView.post {
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = PokemonAdapter(pokemons)
            }
        }
    }
}
