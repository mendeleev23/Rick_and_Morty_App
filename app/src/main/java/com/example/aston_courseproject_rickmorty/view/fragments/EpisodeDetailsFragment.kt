package com.example.aston_courseproject_rickmorty.view.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.App
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.model.dto.CharacterForListDto
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeDto
import com.example.aston_courseproject_rickmorty.view.recycler_view.CharacterRecyclerAdapter
import com.example.aston_courseproject_rickmorty.model.retrofit.Status
import com.example.aston_courseproject_rickmorty.utils.CharacterDiffUtilCallback
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeDetailsViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.EpisodeDetailsViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ARG_EPISODE_ID = "episodeId"

/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@ExperimentalPagingApi
class EpisodeDetailsFragment : Fragment(),
    CharacterRecyclerAdapter.CharacterViewHolder.ItemClickListener {
    private var episodeId: Int? = null

    @Inject
    lateinit var vmMainFactory: MainViewModelFactory
    @Inject
    lateinit var vmFactory: EpisodeDetailsViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModel: EpisodeDetailsViewModel
    private var listForRecycler: MutableList<CharacterForListDto> = mutableListOf()
    private lateinit var recyclerCharacterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episodeId = it.getInt(ARG_EPISODE_ID)
        }
        val episodeDetailsComponent = (requireActivity().applicationContext as App).appComponent.getEpisodeDetailsComponentBuilder()
            .episodeId(episodeId!!)
            .characterItemClickListener(this)
            .build()
        episodeDetailsComponent.inject(this)

        mainViewModel = ViewModelProvider(requireActivity(), vmMainFactory)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initView()

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {

            this.viewModelStore.clear()

            initView()

            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initView() {
        viewModel = ViewModelProvider(this, vmFactory)[EpisodeDetailsViewModel::class.java]

        val detailsLayout = view?.findViewById<ConstraintLayout>(R.id.episode_detailsLayout)
        detailsLayout?.visibility = View.INVISIBLE
        val pbView = view?.findViewById<ProgressBar>(R.id.progress)
        pbView?.visibility = View.VISIBLE
        val pbViewRecycler = view?.findViewById<ProgressBar>(R.id.progressRecycler)
        pbViewRecycler?.visibility = View.VISIBLE

        lifecycleScope.launch {
            viewModel.episode.collect {

                when (it.status) {
                    Status.LOADING -> {
                        detailsLayout?.visibility = View.INVISIBLE
                        pbView?.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        detailsLayout?.visibility = View.VISIBLE
                        pbView?.visibility = View.GONE
                        it.data?.let { episode ->
                            updateView(episode)
                        }
                    }
                    Status.ERROR -> {
                        pbView?.visibility = View.GONE
                        Log.e("AAA", "${it.message}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.characters.collect {

                when (it.status) {
                    Status.LOADING -> {
                        pbViewRecycler?.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        pbViewRecycler?.visibility = View.GONE
                        it.data?.let { characters ->
                            val oldList = listForRecycler.map { character -> character.copy() }
                            val errorTextTitle = view?.findViewById<TextView>(R.id.errorTextTitle)
                            val errorText = view?.findViewById<TextView>(R.id.errorText)
                            if (characters.isEmpty()) {
                                errorTextTitle?.visibility = View.VISIBLE
                                errorText?.visibility = View.VISIBLE
                            } else {
                                errorTextTitle?.visibility = View.GONE
                                errorText?.visibility = View.GONE
                            }
                            listForRecycler.clear()
                            listForRecycler.addAll(characters)
                            notifyWithDiffUtil(oldList.toMutableList())
                        }
                    }
                    Status.ERROR -> {
                        pbView?.visibility = View.GONE
                        Log.e("AAAList", "${it.message}")
                    }
                }
            }
        }

    }

    private fun updateView(currentEpisode: EpisodeDto) {
        val textViewName = view?.findViewById<TextView>(R.id.textView_name)
        val textViewAirDate = view?.findViewById<TextView>(R.id.textView_airdate)
        val textViewEpisode = view?.findViewById<TextView>(R.id.textView_episode)

        textViewName?.text = currentEpisode.name
        textViewAirDate?.text = currentEpisode.episode
        textViewEpisode?.text = currentEpisode.air_date
    }

    private fun initRecyclerView() {
        val mAdapter = CharacterRecyclerAdapter(
            listForRecycler, this
        )
        recyclerCharacterList = requireView().findViewById(R.id.recycler_characters)
        recyclerCharacterList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator())
            adapter = mAdapter
        }
    }

    private fun notifyWithDiffUtil(oldCharacters: MutableList<CharacterForListDto>) {
        val characterDiffUtilCallback = CharacterDiffUtilCallback(oldCharacters, listForRecycler)
        val characterDiffResult = DiffUtil.calculateDiff(characterDiffUtilCallback)
        recyclerCharacterList.adapter?.let { characterDiffResult.dispatchUpdatesTo(it) }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param episodeId Parameter 1.
         * @return A new instance of fragment EpisodeDetailsFragment.
         */
        @JvmStatic
        fun newInstance(episodeId: Int) =
            EpisodeDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_EPISODE_ID, episodeId)
                }
            }
    }

    override fun onItemClick(character: CharacterForListDto?) {
        if (character?.name != "") {
            val fragment: Fragment = CharacterDetailsFragment.newInstance(character?.id!!)
            mainViewModel.changeCurrentDetailsFragment(fragment)
        }
    }
}