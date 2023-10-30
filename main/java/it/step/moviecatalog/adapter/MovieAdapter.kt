package it.step.moviecatalog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import it.step.moviecatalog.R
import it.step.moviecatalog.model.Movie
import java.util.Locale

class MovieAdapter(
    private var data: List<Movie>,
    private val onItemClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {


    inner class ViewHolder(
        itemView: View,
        onItemClicked: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var cardTitle: TextView = itemView.findViewById(R.id.card_title)
        var cardDescription: TextView = itemView.findViewById(R.id.card_description)
        var cardImage : ImageView = itemView.findViewById(R.id.card_image)


        fun bind(model: Movie) {
            //bind data with the component
            itemView.setOnClickListener {
                // this will be called only once.
                onItemClick.invoke(model)
            }
        }
    }

    val initialDataList = ArrayList<Movie>().apply {
        data?.let { addAll(it) }
    }

    private val movieFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: ArrayList<Movie> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                initialDataList.let { filteredList.addAll(it) }
            } else {
                val query = constraint.toString().trim().toLowerCase()
                initialDataList.forEach {
                    if (it.title.toLowerCase(Locale.ROOT).contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values is ArrayList<*>) {
                data = ArrayList(results.values as List<Movie>)
                notifyDataSetChanged()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
        return ViewHolder(viewHolder) {
            onItemClick(it)
        }
    }

    override fun getItemCount(): Int = data.size
    fun getFilter(): Filter {
        return movieFilter
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.cardTitle.text = data[position].title
        viewHolder.cardDescription.text = data[position].plot
        viewHolder.cardImage.load(data[position].poster)
        viewHolder.bind(data[position])
    }

//    private fun abbreviateString(string: String, dim : Int) : String{
//        var newString = string
//        if(string.length >= dim){
//            newString = string.substring(0, dim - 3) + "..."
//        }
//        return newString
//    }

    fun sortMoviesAlphabeticallyAZ() {
        data = data.sortedBy { it.title }
        notifyDataSetChanged()
    }

    fun sortMoviesAlphabeticallyZA() {
        data = data.sortedByDescending { it.title }
        notifyDataSetChanged()
    }

}