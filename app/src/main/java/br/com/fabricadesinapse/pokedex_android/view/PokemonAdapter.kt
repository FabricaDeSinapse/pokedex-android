package br.com.fabricadesinapse.pokedex_android.view


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.fabricadesinapse.pokedex_android.R
import br.com.fabricadesinapse.pokedex_android.domain.Pokemon
import com.squareup.picasso.Picasso


class PokemonAdapter(
    private val items: List<Pokemon?>
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
        holder.itemView.tag = item // Define o item do Pokémon como tag do itemView
    }

    fun getPositionOfPokemon(pokemonName: String): Int {
        for (i in items.indices) {
            val pokemon = items[i]
            if (pokemon != null) {
                if (pokemon.getName().equals(pokemonName, ignoreCase = true)) {
                    return i
                }
            }
        }
        return -1 // Pokemon não encontrado na lista
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //tenho que consertar isso ainda!
        init {
            val buttonDetail = itemView.findViewById<Button>(R.id.btnDetail)
            buttonDetail.setOnClickListener {
                val item = itemView.tag as Pokemon?
                val context = itemView.context

                if (item != null) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("pokemonNumber", item.obtainNumber())
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Selecione um Pokemon primeiro", Toast.LENGTH_SHORT).show()
                }
            }
        }
        fun bindView(item: Pokemon?) = with(itemView) {//with() é um bloco de contexto!
            val ivPokemon = findViewById<ImageView>(R.id.ivPokemon)
            val tvNumber = findViewById<TextView>(R.id.tvNumber)
            val tvName = findViewById<TextView>(R.id.tvName)
            val tvType1 = findViewById<TextView>(R.id.tvType1Detail)
            val tvType2 = findViewById<TextView>(R.id.tvType2Detail)

            item?.let {
                //Glide.with(itemView.context).load(it.imageUrl).into(ivPokemon)
                Picasso.get().load(it.imageUrl).into(ivPokemon)
                tvNumber.text = "Nº ${item.formattedNumber}"
                tvName.text = item.formattedName
                tvType1.text = item.types[0].name.capitalize()

                //caso o pokemon tenha mais de 1 tipo
                if (item.types.size > 1) {
                    tvType2.visibility = View.VISIBLE
                    tvType2.text = item.types[1].name.capitalize()
                } else {
                    tvType2.visibility = View.GONE
                }
            }
        }
    }
}
