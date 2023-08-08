package br.com.fabricadesinapse.pokedex_android.api

import android.annotation.SuppressLint
import br.com.fabricadesinapse.pokedex_android.api.model.PokemonApiResult
import br.com.fabricadesinapse.pokedex_android.api.model.PokemonsApiResult
import br.com.fabricadesinapse.pokedex_android.domain.Pokemon
import br.com.fabricadesinapse.pokedex_android.domain.PokemonType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokemonRepository {
    private val service: PokemonService

    init {
        //RETROFIT --
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(PokemonService::class.java)
    }

    fun listPokemons(limit: Int = 151): PokemonsApiResult? {
        val call = service.listPokemons(limit)

        return call.execute().body()
    }

    fun getPokemon(number: Int): PokemonApiResult? {
        val call = service.getPokemon(number)

        return call.execute().body()
    }

    // Método de extensão para converter PokemonApiResult em Pokemon
    @SuppressLint("DefaultLocale")
    private fun PokemonApiResult?.toPokemon(): Pokemon? {
        if (this != null) {
            val formattedName = name.capitalize()
            val types = types.map { PokemonType(it.type.name) }
            return Pokemon(id, formattedName, types)
        }
        return null
    }

    // Método para procurar Pokémon por nome usando o Retrofit criado!
    fun searchPokemonByName(
        name: String,
        onSuccess: (Pokemon?) -> Unit,
        onFailure: () -> Unit,
        service: PokemonService = this.service // parâmetro opcional
    ) {
        val call = service.searchPokemonByName(name.toLowerCase())

        call.enqueue(object : Callback<PokemonApiResult> {
            override fun onResponse(call: Call<PokemonApiResult>, response: Response<PokemonApiResult>) {
                if (response.isSuccessful) {
                    val pokemonApiResult = response.body()
                    val pokemon = pokemonApiResult?.toPokemon()
                    onSuccess(pokemon)
                } else {
                    onFailure()
                }
            }

            override fun onFailure(call: Call<PokemonApiResult>, t: Throwable) {
                onFailure()
            }
        })
    }
}