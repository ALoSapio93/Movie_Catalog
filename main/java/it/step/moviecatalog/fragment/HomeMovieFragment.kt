package it.step.moviecatalog.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentHomeMovieBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class HomeMovieFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var moviesList: List<Movie> = emptyList()
    private lateinit var bindingHomeMovies: FragmentHomeMovieBinding
    private lateinit var view: View
    private lateinit var mainActivity: Activity
    private var isButtonGroupVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = requireActivity() as MainActivity

        movieViewModel.initMovieList()

        // Inflate the layout for this fragment
        bindingHomeMovies = FragmentHomeMovieBinding.inflate(layoutInflater)
        view = bindingHomeMovies.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
            bindingHomeMovies.hmfMessage.text = getString(R.string.no_connection)
            bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)
        } else {
            bindingHomeMovies.hmfMessage.text = getString(R.string.empty_string)
        }

        bindingHomeMovies.hmfSwipeRefreshLayout.setOnRefreshListener {
            bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)

            if ((mainActivity as MainActivity).isNetworkConnected(requireContext())) {
                bindingHomeMovies.hmfRecyclerLayout.visibility = View.VISIBLE
                bindingHomeMovies.hmfErrorLayout.visibility = View.GONE
                bindingHomeMovies.hmfMessage.text = ""
                movieViewModel.initMovieList()
            } else {
                bindingHomeMovies.hmfErrorLayout.visibility = View.VISIBLE
                bindingHomeMovies.hmfRecyclerLayout.visibility = View.GONE
                bindingHomeMovies.hmfMessage.text = getString(R.string.no_connection)
            }
            bindingHomeMovies.hmfSwipeRefreshLayout.isRefreshing = false
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                bindingHomeMovies.hmfProgressBar.visibility =
                    View.VISIBLE // Mostra la ProgressBar
            } else {
                bindingHomeMovies.hmfProgressBar.visibility =
                    View.GONE // Nasconde la ProgressBar
                if (moviesList.isEmpty()){
                    if (!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
                        bindingHomeMovies.hmfMessage.text = getString(R.string.no_connection)
                        bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)
                    }else{
                        bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_list)
                    }
                } else {
                    bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)
                }
            }
        }

        bindingHomeMovies.mhfToggleButton?.addOnButtonCheckedListener { group, checkedId, isChecked ->
            try {
                if (isChecked) {
                    when (checkedId) {
                        R.id.mhf_sortAZButton -> {
                            movieAdapter.sortMoviesAlphabeticallyAZ()
                        }

                        R.id.mhf_sortZAButton -> {
                            movieAdapter.sortMoviesAlphabeticallyZA()
                        }
                    }
                } else {
                    if (group.checkedButtonId == View.NO_ID) {
                        movieViewModel.initMovieList()
                    }
                }
            } catch (e: Exception){

            }

        }

        val recyclerView: RecyclerView = view.findViewById(R.id.hmf_all_movies_recycler)

        // Create the observer which updates the UI.
        val movieListObserver = Observer<List<Movie>?> { newMovieList ->
            // Update the UI

            if (newMovieList!=null && newMovieList.isEmpty()){
                if (!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
                    bindingHomeMovies.hmfMessage.text = getString(R.string.no_connection)
                    bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)
                }else{
                    bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_list)
                }
            } else {
                bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)
            }

            if (newMovieList != null) {
                moviesList = newMovieList
                movieAdapter = MovieAdapter(moviesList) { movie ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                        movie.imdbID
                    )
                    findNavController().navigate(action)
                }
                val layoutManager = LinearLayoutManager(requireContext())
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = movieAdapter
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        // Qui puoi gestire lo stato di scorrimento, ad esempio quando inizia o finisce lo scorrimento.
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        // Qui puoi gestire lo scorrimento effettivo e decidere se mostrare o nascondere il Button Group.

                        if (dy > 100) {
                            // Scorrimento verso il basso, nascondi il Button Group solo se non è già nascosto.
                            if (isButtonGroupVisible) {
                                bindingHomeMovies.mhfToggleButton.visibility = View.GONE
                                isButtonGroupVisible = false
                            }
                        } else if (dy < 0) {
                            // Scorrimento verso l'alto, mostra il Button Group solo se è nascosto.
                            if (!isButtonGroupVisible) {
                                bindingHomeMovies.mhfToggleButton.visibility = View.VISIBLE
                                isButtonGroupVisible = true
                            }
                        }
                    }
                })

            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        movieViewModel.moviesList.observe(viewLifecycleOwner, movieListObserver)

    }

    override fun onResume() {
        super.onResume()
        if (!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
            bindingHomeMovies.hmfMessage.text = getString(R.string.no_connection)
            bindingHomeMovies.hmfVoidListMessage.text = getString(R.string.empty_string)
        } else {
            bindingHomeMovies.hmfMessage.text = getString(R.string.empty_string)
            movieViewModel.initMovieList()
        }


    }


}

