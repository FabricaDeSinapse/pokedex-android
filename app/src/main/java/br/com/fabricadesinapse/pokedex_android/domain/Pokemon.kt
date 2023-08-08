package br.com.fabricadesinapse.pokedex_android.domain

data class Pokemon(
    val number: Int,
    val name: String,
    val types: List<PokemonType>
) {
    val formattedName = name.capitalize()

    val formattedNumber = String.format("%d", number)

    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$formattedNumber.png"

    @JvmName("getName1")
    fun getName(): String {
        return formattedName
    }

    fun obtainNumber(): Int {
        return number
    }
}
