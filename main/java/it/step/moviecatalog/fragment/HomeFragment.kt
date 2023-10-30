package it.step.moviecatalog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.ViewPagerAdapter
import it.step.moviecatalog.databinding.FragmentHomeBinding
import it.step.moviecatalog.viewmodel.MovieViewModel

class HomeFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var bindingHome: FragmentHomeBinding
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingHome = FragmentHomeBinding.inflate(layoutInflater)
        view = bindingHome.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val fragments = listOf(HomeMovieFragment(), HomeSeriesFragment(), HomeGameFragment()) // Replace with your fragment instances
        val adapter = ViewPagerAdapter(fragments, childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Set tab text or icon here if needed
            tab.text = when(position){
                0 -> getString(R.string.tab_item_movies);
                1 -> getString(R.string.tab_item_series);
                2 -> getString(R.string.tab_item_games);
                else -> ""
            }

        }.attach()
    }


}