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
import it.step.moviecatalog.databinding.FragmentHomeGameBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class HomeGameFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var gamesList: List<Movie> = emptyList()
    private lateinit var bindingHomeGame: FragmentHomeGameBinding
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

        movieViewModel.initGamesList()

        // Inflate the layout for this fragment
        bindingHomeGame = FragmentHomeGameBinding.inflate(layoutInflater)
        view = bindingHomeGame.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!(mainActivity as MainActivity).isNetworkConnected(requireContext()))
            bindingHomeGame.hgfMessage.text = getString(R.string.no_connection)
        else bindingHomeGame.hgfMessage.text = getString(R.string.empty_string)

        bindingHomeGame.hgfSwipeRefreshLayout.setOnRefreshListener {
            bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_string)

            if((mainActivity as MainActivity).isNetworkConnected(requireContext())){
                bindingHomeGame.hgfRecyclerLayout.visibility = View.VISIBLE
                bindingHomeGame.hgfErrorLayout.visibility = View.GONE
                bindingHomeGame.hgfMessage.text = ""
                movieViewModel.initGamesList()
            } else {
                bindingHomeGame.hgfErrorLayout.visibility = View.VISIBLE
                bindingHomeGame.hgfRecyclerLayout.visibility = View.GONE
                bindingHomeGame.hgfMessage.text = getString(R.string.no_connection)
            }
            bindingHomeGame.hgfSwipeRefreshLayout.isRefreshing = false
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                bindingHomeGame.hgfProgressBar.visibility =
                    View.VISIBLE // Mostra la ProgressBar
            } else {
                bindingHomeGame.hgfProgressBar.visibility =
                    View.GONE // Nasconde la ProgressBar
                if (gamesList.isEmpty()){
                    if(!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
                        bindingHomeGame.hgfMessage.text = getString(R.string.no_connection)
                        bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_string)
                    }
                    else{
                        bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_list)
                    }
                } else {
                    bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_string)
                }
            }
        }

        bindingHomeGame.mgfToggleButton?.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.mgf_sortAZButton -> {
                        movieAdapter.sortMoviesAlphabeticallyAZ()
                    }

                    R.id.mgf_sortZAButton -> {
                        movieAdapter.sortMoviesAlphabeticallyZA()
                    }
                }
            } else {
                if (group.checkedButtonId == View.NO_ID) {
                    movieViewModel.initGamesList()
                }
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.hgf_all_games_recycler)

        // Create the observer which updates the UI.
        val gameListObserver = Observer<List<Movie>?> { newGameList ->

            if (newGameList!=null && newGameList.isEmpty()){
                if(!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
                    bindingHomeGame.hgfMessage.text = getString(R.string.no_connection)
                    bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_string)
                }
                else{
                    bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_list)
                }
            } else {
                bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_string)
            }

            // Update the UI
            if (newGameList != null) {
                gamesList = newGameList
                movieAdapter = MovieAdapter(gamesList) { game ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                        game.imdbID
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
                                bindingHomeGame.mgfToggleButton.visibility = View.GONE
                                isButtonGroupVisible = false
                            }
                        } else if (dy < 0) {
                            // Scorrimento verso l'alto, mostra il Button Group solo se è nascosto.
                            if (!isButtonGroupVisible) {
                                bindingHomeGame.mgfToggleButton.visibility = View.VISIBLE
                                isButtonGroupVisible = true
                            }
                        }
                    }
                })

            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        movieViewModel.gamesList.observe(viewLifecycleOwner, gameListObserver)

    }

    override fun onResume() {
        super.onResume()
        if(!(mainActivity as MainActivity).isNetworkConnected(requireContext())) {
            bindingHomeGame.hgfMessage.text = getString(R.string.no_connection)
            bindingHomeGame.hgfVoidListMessage.text = getString(R.string.empty_string)
        }
        else{
            bindingHomeGame.hgfMessage.text = getString(R.string.empty_string)
            movieViewModel.initGamesList()
        }



    }

}