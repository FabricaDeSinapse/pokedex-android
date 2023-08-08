package br.com.fabricadesinapse.pokedex_android.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.fabricadesinapse.pokedex_android.R
import br.com.fabricadesinapse.pokedex_android.domain.PokemonDetail
import com.squareup.picasso.Picasso
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


//PRECISO DESMEMBRAR ESSA CLASSE EM PEDAÇOS PARA FAZER SENTIDO COM O PROJETO!

//Vou colocar comentários abaixo para saber o que deve ser feito. Sinta-se livre para colaborar.
class DetailActivity : AppCompatActivity() {

    private val buttonVoltar by lazy {
        findViewById<Button>(R.id.btnVoltar)
    }

    private val buttonSalvar by lazy {
        findViewById<ImageButton>(R.id.salvarPokemon)
    }

    private val pokemonIdsList = mutableListOf<Int>()

    private fun loadSavedPokemonIds(): List<Int> {
        val sharedPreferences = getSharedPreferences("FavoriteActivity", Context.MODE_PRIVATE)
        val savedPokemonIdsSet = sharedPreferences.getStringSet("savedPokemonIds", emptySet())
        return savedPokemonIdsSet?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }

    private fun savePokemonIds(savedPokemonIds: List<Int>) {
        val sharedPreferences = getSharedPreferences("FavoriteActivity", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("savedPokemonIds", savedPokemonIds.map { it.toString() }.toSet())
        editor.apply()
    }

    //Não era pra isso estar aqui----------------------
    private lateinit var tvNameDetail: TextView
    private lateinit var tvWeightDetail: TextView
    private lateinit var tvHeightDetail: TextView
    private lateinit var tvType1Detail: TextView
    private lateinit var tvType2Detail: TextView
    private lateinit var tvHPDetail: TextView
    private lateinit var tvAttackDetail: TextView
    private lateinit var tvDefenseDetail: TextView
    private lateinit var tvSpAtkDetail: TextView
    private lateinit var tvSpDefDetail: TextView
    private lateinit var tvSpeedDetail: TextView
    private lateinit var ivPokemonDetail: ImageView
    private lateinit var apiService: PokemonApiService
    private lateinit var rootView: View
    //---------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        rootView = findViewById(R.id.main_layout)

        val savedPokemonIds = loadSavedPokemonIds()

        //botão para voltar!!
        buttonVoltar.setOnClickListener {
            finish()
        }

        //Não era pra isso estar aqui--------------------------------------------
        tvNameDetail = findViewById(R.id.tvNameDetail)
        tvWeightDetail = findViewById(R.id.tvWeightDetail)
        tvHeightDetail = findViewById(R.id.tvHeightDetail)
        tvType1Detail = findViewById(R.id.tvType1Detail)
        tvType2Detail = findViewById(R.id.tvType2Detail)
        tvHPDetail = findViewById(R.id.tvHPDetail)
        tvAttackDetail = findViewById(R.id.tvAttackDetail)
        tvDefenseDetail = findViewById(R.id.tvDefenseDetail)
        tvSpAtkDetail = findViewById(R.id.tvSpAtkDetail)
        tvSpDefDetail = findViewById(R.id.tvSpDefDetail)
        tvSpeedDetail = findViewById(R.id.tvSpeedDetail)
        ivPokemonDetail = findViewById(R.id.ivPokemonDetail)
        //-------------------------------------------------------------------------

        // Configurar o Retrofit ? (já temos um retrofit em *PokemonRepository*)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //-------------------------------------------------------------------------


        // Criar a instância do serviço da API ? (temos a classe *PokemonService* justamente para isso)
        apiService = retrofit.create(PokemonApiService::class.java)
        //-------------------------------------------------------------------------



        // Obtém o ID do Pokémon selecionado da intent (talvez em *PokemonAdapter*?)
        val pokemonId = intent.getIntExtra("pokemonNumber", 1)
        //-------------------------------------------------------------------------

        //botão para salvar!
        buttonSalvar.setOnClickListener {
            buttonSalvar.setImageResource(R.drawable.pokeball)
            Toast.makeText(this, "Pokemon Salvo!", Toast.LENGTH_SHORT).show()

            pokemonIdsList.addAll(savedPokemonIds) // Adiciona os IDs existentes à lista
            pokemonIdsList.add(pokemonId) // Adiciona o novo ID do Pokémon

            savePokemonIds(pokemonIdsList) // Salva a lista atualizada
        }



        // Chama o método para obter os detalhes do Pokémon (talvez em *PokemonAdapter*?)
        getPokemonDetails(pokemonId)
        //-------------------------------------------------------------------------
    }


    //Okay, isso definitivamente não era para estar aqui (acho que caberia em PokemonRepository!)
    private fun getPokemonDetails(pokemonId: Int) {
        var endColor: Int = R.color.defaultEndColor
        apiService.getPokemonDetails(pokemonId).enqueue(object : Callback<PokemonDetail> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if (response.isSuccessful) {
                    val pokemonDetails = response.body()
                    if (pokemonDetails != null) {
                        try {
                            val name = pokemonDetails.name
                            val weight = pokemonDetails.weight
                            val weightDividedBy10 = weight / 10
                            val height = pokemonDetails.height
                            val heightDividedBy10 = height / 10
                            val types = pokemonDetails.types
                            val type1Name = types[0].type.name
                            val type2Name = if (types.size > 1) types[1].type.name else null
                            val stats = pokemonDetails.stats
                            val hp = stats[0].baseStat
                            val attack = stats[1].baseStat
                            val defense = stats[2].baseStat
                            val spAtk = stats[3].baseStat
                            val spDef = stats[4].baseStat
                            val speed = stats[5].baseStat
                            val spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonId}.png"


                            // Atribui o valor "endColor" de acordo com o tipo do Pokemon
                            endColor = when (type1Name) {
                                "bug"      -> R.color.bugEndColor
                                "dark"     -> R.color.darkEndColor
                                "dragon"   -> R.color.dragonEndColor
                                "electric" -> R.color.electricEndColor
                                "fighting" -> R.color.fightingEndColor
                                "flying"   -> R.color.flyingEndColor
                                "ghost"    -> R.color.ghostEndColor
                                "grass"    -> R.color.grassEndColor
                                "ground"   -> R.color.groundEndColor
                                "ice"      -> R.color.iceEndColor
                                "normal"   -> R.color.normalEndColor
                                "poison"   -> R.color.poisonEndColor
                                "psychic"  -> R.color.psychicEndColor
                                "rock"     -> R.color.rockEndColor
                                "steel"    -> R.color.steelEndColor
                                "water"    -> R.color.waterEndColor
                                "fairy"    -> R.color.fairyEndColor
                                "fire"     -> R.color.fireEndColor
                                // Adicione outros tipos de Pokémon e suas cores correspondentes aqui
                                else -> R.color.defaultEndColor
                            }

                            // Atualiza os elementos da UI com os dados do Pokémon
                            tvNameDetail.text = name
                            tvWeightDetail.text = "Weight: $weightDividedBy10 kg"
                            tvHeightDetail.text = "Height: $heightDividedBy10 m"
                            tvType1Detail.text = type1Name
                            tvType2Detail.text = type2Name
                            tvHPDetail.text = "HP: $hp"
                            tvAttackDetail.text = "Attack: $attack"
                            tvDefenseDetail.text = "Defense: $defense"
                            tvSpAtkDetail.text = "Sp. Attack: $spAtk"
                            tvSpDefDetail.text = "Sp. Defense: $spDef"
                            tvSpeedDetail.text = "Speed: $speed"

                            // Carrega a imagem do Pokémon usando a biblioteca Picasso
                            Picasso.get().load(spriteUrl).into(ivPokemonDetail)

                            // Altera o gradiente do background da atividade de acordo com o tipo do Pokémon
                            changeBackgroundGradient(endColor)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    //-----------------------------------------------------------------------------------------------


    private fun changeBackgroundGradient(endColor: Int) {
        val startColor = ContextCompat.getColor(this, R.color.azulPokemon) // Cor de fim do gradiente (se necessário)
        val endColorResolved = ContextCompat.getColor(this, endColor)

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(startColor, endColorResolved)
        )

        rootView.background = gradientDrawable
    }

    // Define a interface do serviço da API (Isso é papel de PokemonService)
    interface PokemonApiService {
        @GET("pokemon/{id}/")
        fun getPokemonDetails(@Path("id") id: Int): Call<PokemonDetail>
    }
    //----------------------------------------------------------------------
}