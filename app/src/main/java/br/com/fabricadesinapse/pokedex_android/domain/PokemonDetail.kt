package br.com.fabricadesinapse.pokedex_android.domain

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val name: String,
    val weight: Double,
    val height: Double,
    val types: List<PokemonTypeSlot>,
    val stats: List<PokemonStat>
)

data class PokemonTypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String
)

data class PokemonStat(
    @SerializedName("base_stat")
    val baseStat: Int
)
