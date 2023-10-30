package it.step.moviecatalog.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentSearchBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class SearchFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var searchList: List<Movie> = emptyList()
    private lateinit var bindingSearch: FragmentSearchBinding
    private lateinit var view: View
    private lateinit var mainActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = requireActivity() as MainActivity

        movieViewModel.initSearchList()

        // Inflate the layout for this fragment
        bindingSearch = FragmentSearchBinding.inflate(layoutInflater)
        view = bindingSearch.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!(mainActivity as MainActivity).isNetworkConnected(requireContext()))
            bindingSearch.sfMessage.text = getString(R.string.no_connection)
        else bindingSearch.sfMessage.text = getString(R.string.empty_string)

        bindingSearch.sfSwipeRefreshLayout.setOnRefreshListener {
            if((mainActivity as MainActivity).isNetworkConnected(requireContext())){
                bindingSearch.sfRecyclerLayout.visibility = View.VISIBLE
                bindingSearch.sfErrorLayout.visibility = View.GONE
                bindingSearch.sfMessage.text = ""
                movieViewModel.initSearchList()
            } else {
                bindingSearch.sfErrorLayout.visibility = View.VISIBLE
                bindingSearch.sfRecyclerLayout.visibility = View.GONE
                bindingSearch.sfMessage.text = getString(R.string.no_connection)
            }
            bindingSearch.sfSwipeRefreshLayout.isRefreshing = false
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                bindingSearch.sfProgressBar.visibility =
                    View.VISIBLE // Mostra la ProgressBar
            } else {
                bindingSearch.sfProgressBar.visibility =
                    View.GONE // Nasconde la ProgressBar
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.sf_searched_recycler)

        // Create the observer which updates the UI.
        val searchListObserver = Observer<List<Movie>?> { newSearchList ->
            // Update the UI
            if (newSearchList != null) {
                searchList = newSearchList
                movieAdapter = MovieAdapter(searchList) { movie ->
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movie.imdbID)
                    findNavController().navigate(action)
                }
                val layoutManager = LinearLayoutManager(requireContext())
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = movieAdapter

                if (newSearchList.isEmpty()) bindingSearch.sfMessage.text =
                    getString(R.string.empty_list)
                else bindingSearch.sfMessage.text = getString(R.string.empty_string)
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        movieViewModel.searchedMovies.observe(viewLifecycleOwner, searchListObserver)

        bindingSearch.sfSearchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                movieAdapter?.getFilter()?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                try {
                    movieAdapter?.getFilter()?.filter(newText);
                }catch (e: Exception){

                }

                return true
            }

        })


    }

    override fun onResume() {
        super.onResume()

        if(!(mainActivity as MainActivity).isNetworkConnected(requireContext()))
            bindingSearch.sfMessage.text = getString(R.string.no_connection)
        else{
            bindingSearch.sfMessage.text = getString(R.string.empty_string)
            movieViewModel.initSearchList()
        }
    }

}