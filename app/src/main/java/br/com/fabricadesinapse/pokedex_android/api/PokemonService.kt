package br.com.fabricadesinapse.pokedex_android.api

import br.com.fabricadesinapse.pokedex_android.api.model.PokemonApiResult
import br.com.fabricadesinapse.pokedex_android.api.model.PokemonsApiResult
import br.com.fabricadesinapse.pokedex_android.domain.PokemonDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    fun listPokemons(@Query("limit") limit: Int): Call<PokemonsApiResult>

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") number: Int): Call<PokemonApiResult>

    @GET("pokemon/{name}")
    fun searchPokemonByName(@Path("name") name: String): Call<PokemonApiResult>
}
