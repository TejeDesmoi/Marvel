package edu.iesam.marvelapp.features.superheroes.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.iesam.marvelapp.R
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero

class SuperHeroAdapter(
    private var superHeroes: List<SuperHero> = emptyList()
): RecyclerView.Adapter<SuperHeroViewHolder>() {
    fun updateList(newList: List<SuperHero>){
        superHeroes = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperHeroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.superhero_card, parent, false)
        return SuperHeroViewHolder(view)
    }

    override fun getItemCount(): Int = superHeroes.size

    override fun onBindViewHolder(holder: SuperHeroViewHolder, position: Int) {
        holder.bind(superHeroes[position])
    }
}

class SuperHeroViewHolder(view: View): RecyclerView.ViewHolder(view){
    private val nameTextView: TextView = view.findViewById(R.id.superhero_name)
    private val slugTextView: TextView = view.findViewById(R.id.superhero_slug)
    private val imageView: ImageView = view.findViewById(R.id.superhero_image)

    fun bind(superHero: SuperHero) {
        nameTextView.text = superHero.name
        slugTextView.text = superHero.slug
    }
}