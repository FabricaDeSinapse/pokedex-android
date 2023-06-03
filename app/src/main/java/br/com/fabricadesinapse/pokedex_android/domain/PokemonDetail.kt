package br.com.fabricadesinapse.pokedex_android.domain

data class PokemonDetail(
    val weight: String,
    val height: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val SpAtk: Int,
    val SpDef: Int,
    val Speed: Int
) {
    val formattedWeight = weight.capitalize()
    val formattedHeight = height.capitalize()
}
