package com.example.aston_courseproject_rickmorty.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aston_courseproject_rickmorty.App
import com.example.aston_courseproject_rickmorty.MainViewModel
import com.example.aston_courseproject_rickmorty.MainViewModelFactory
import com.example.aston_courseproject_rickmorty.R
import com.example.aston_courseproject_rickmorty.view.dialogs.EpisodeFilterDialog
import com.example.aston_courseproject_rickmorty.view.dialogs.Filter
import com.example.aston_courseproject_rickmorty.model.dto.EpisodeForListDto
import com.example.aston_courseproject_rickmorty.view.recycler_view.EpisodePaginationRecyclerAdapter
import com.example.aston_courseproject_rickmorty.view.recycler_view.MyLoaderStateAdapter
import com.example.aston_courseproject_rickmorty.model.repository.EpisodeRepository
import com.example.aston_courseproject_rickmorty.utils.RecyclerDecorator
import com.example.aston_courseproject_rickmorty.viewmodel.EpisodeViewModel
import com.example.aston_courseproject_rickmorty.viewmodel.factory.EpisodeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [EpisodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class EpisodeFragment : Fragment(),
    EpisodePaginationRecyclerAdapter.EpisodeViewHolder.ItemClickListener,
    EpisodeFilterDialog.ApplyClickListener {

    @Inject
    lateinit var vmMainFactory: MainViewModelFactory
    @Inject
    lateinit var repository: EpisodeRepository
    @Inject
    lateinit var mAdapter: EpisodePaginationRecyclerAdapter
    @Inject
    lateinit var dialogProcessor: EpisodeFilterDialog
    private lateinit var viewModel: EpisodeViewModel
    private lateinit var recyclerEpisodeList: RecyclerView
    private var filterList = mutableListOf(Filter(), Filter())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
        val episodeComponent =
            (requireActivity().applicationContext as App).appComponent.getEpisodeComponentBuilder()
                .fragmentContext(requireContext())
                .episodeItemClickListener(this)
                .applyItemClickListener(this)
                .build()
        episodeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerEpisodeList = view.findViewById(R.id.recyclerView_episodes)

        createViewModelUpdateAdapter()

        initRecyclerView()

        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            this.viewModelStore.clear()

            filterList = mutableListOf(Filter(), Filter())
            createViewModelUpdateAdapter()

            if (editTextName.text.toString() != "") editTextName.setText("")

            swipeRefreshLayout.isRefreshing = false
        }

        val filterButton = view.findViewById<Button>(R.id.button_filter)
        filterButton.setOnClickListener {
            dialogProcessor.showDialog(filterList[1])
        }

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                editTextName.isCursorVisible = true
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isNotEmpty()) {
                    filterList[0].stringToFilter = s.toString()
                    filterList[0].isApplied = true
                } else {
                    filterList[0].stringToFilter = ""
                    filterList[0].isApplied = false
                }
                clearView()
                createViewModelUpdateAdapter()
            }
        })
    }

    fun clearView() {
        this.viewModelStore.clear()
    }

    private fun initRecyclerView() {
        recyclerEpisodeList.adapter = mAdapter.withLoadStateFooter(MyLoaderStateAdapter())
        recyclerEpisodeList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(RecyclerDecorator())
        }
    }

    private fun createViewModelUpdateAdapter() {
        val dataSource = repository.getEpisodesFromMediator(
            mutableListOf(
                filterList[0].stringToFilter,
                filterList[1].stringToFilter
            )
        )
        viewModel = ViewModelProvider(
            this,
            EpisodeViewModelFactory(dataSource)
        )[EpisodeViewModel::class.java]
        lifecycleScope.launchWhenCreated {
            viewModel.episodes.collectLatest {
                mAdapter.submitData(it)
            }
        }
        mAdapter.addLoadStateListener { state: CombinedLoadStates ->
            recyclerEpisodeList.visibility =
                if (state.refresh != LoadState.Loading) View.VISIBLE else View.GONE
            val pbView = view?.findViewById<ProgressBar>(R.id.progress)
            pbView?.visibility = if (state.refresh == LoadState.Loading) View.VISIBLE else View.GONE
            val errorText = view?.findViewById<TextView>(R.id.errorText)
            val errorTextTitle = view?.findViewById<TextView>(R.id.errorTextTitle)
            when (state.refresh.toString()) {
                "Error(endOfPaginationReached=false, error=java.io.IOException: Wrong Query)" -> {
                    errorText?.visibility = View.VISIBLE
                    errorTextTitle?.visibility = View.VISIBLE
                    errorTextTitle?.text = getString(R.string.error_empty_list_episode_title)
                    errorText?.text = getString(R.string.error_empty_list_episode)
                }
                "Error(endOfPaginationReached=false, error=java.io.IOException: Empty Database)" -> {
                    errorText?.visibility = View.VISIBLE
                    errorTextTitle?.visibility = View.VISIBLE
                    errorTextTitle?.text = getString(R.string.error_empty_database_title)
                    errorText?.text = getString(R.string.error_empty_database)
                }
                else -> {
                    errorText?.visibility = View.GONE
                    errorTextTitle?.visibility = View.GONE
                }
            }
        }

        viewModel.episodeCodeFilter.observe(viewLifecycleOwner) {
            if (it.stringToFilter != "" && it.isApplied) {
                filterList[1].stringToFilter = it.stringToFilter
                filterList[1].isApplied = it.isApplied
            } else {
                filterList[1].stringToFilter = ""
                filterList[1].isApplied = false
            }
            this.viewModelStore.clear()
            createViewModelUpdateAdapter()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment EpisodeFragment.
         */
        @JvmStatic
        fun newInstance() =
            EpisodeFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onItemClick(episode: EpisodeForListDto?) {
        val fragment: Fragment = EpisodeDetailsFragment.newInstance(episode?.id!!)
        val mainViewModel =
            ViewModelProvider(requireActivity(), vmMainFactory)[MainViewModel::class.java]
        mainViewModel.changeCurrentDetailsFragment(fragment)
    }

    override fun onApplyClick(dialog: Dialog) {
        viewModel.onApplyClick(dialog)
    }
}