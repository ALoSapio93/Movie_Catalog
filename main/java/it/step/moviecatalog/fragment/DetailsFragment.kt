package it.step.moviecatalog.fragment

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import coil.transform.RoundedCornersTransformation
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.databinding.FragmentDetailsBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class DetailsFragment : Fragment() {

    private lateinit var bindingDetails: FragmentDetailsBinding
    private lateinit var view: View
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var mainActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        movieViewModel.getByID(args?.getString("imdbID").toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = requireActivity() as MainActivity
        if ((mainActivity as MainActivity).isNetworkConnected(requireContext())) {
            bindingDetails = FragmentDetailsBinding.inflate(layoutInflater)
            view = bindingDetails.root


            val movieObserver = Observer<Movie?> { newMovie ->
                // Update the UI
                if (newMovie != null) {
                    bindingDetails.imPosterDetails.load(newMovie.poster){
                        transformations(RoundedCornersTransformation(30F))
                    }
                    bindingDetails.titleDetails.text = newMovie.title
                    val ratingValue = newMovie.imdbrating?.toFloatOrNull()
                    if (ratingValue != null) {
                        val color = when {
                            ratingValue >= 4.0 -> R.color.colorStarDetails
                            ratingValue >= 3.0 -> R.color.colorStarDetails
                            ratingValue >= 2.0 -> R.color.colorStarDetails
                            ratingValue >= 1.0 -> R.color.colorStarDetails
                            else -> R.color.colorPrimaryDark
                        }
                        bindingDetails.ratingBar.rating = ratingValue
                        bindingDetails.ratingBar.setProgressTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), color)))

                    }

                    bindingDetails.plotDetails.text = newMovie.plot
                    bindingDetails.listActorsDetails.text = newMovie.actors
                    bindingDetails.directorDetails.text = newMovie.director
                }
            }

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            movieViewModel.movie.observe(viewLifecycleOwner, movieObserver)
        } else {
            view = inflater.inflate(R.layout.no_connection_layout, container, false)
        }

        return view
    }

}