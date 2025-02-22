package com.lucagiorgetti.surprix.ui.mainfragments.catalog.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucagiorgetti.surprix.R
import com.lucagiorgetti.surprix.listenerInterfaces.CallbackInterface
import com.lucagiorgetti.surprix.model.Set
import com.lucagiorgetti.surprix.model.Surprise
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseListType
import com.lucagiorgetti.surprix.ui.mainfragments.surpriseList.SurpriseRecyclerAdapter
import com.lucagiorgetti.surprix.utility.BaseFragment
import com.lucagiorgetti.surprix.utility.RecyclerItemClickListener
import com.lucagiorgetti.surprix.utility.SystemUtils
import com.lucagiorgetti.surprix.utility.dao.SetDao

class SearchFragment : BaseFragment() {
    private var mode: SearchMode? = null
    private var recyclerView: RecyclerView? = null
    private var searchSurpriseRecyclerAdapter: SurpriseRecyclerAdapter? = null
    private var searchSetRecyclerAdapter: SearchSetRecyclerAdapter? = null
    private var searchView: SearchView? = null
    private var root: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_search, container, false)
        }
        val radioGroup = root!!.findViewById<RadioGroup>(R.id.search_radio_group)
        recyclerView = root!!.findViewById(R.id.search_recycler_view)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, SystemUtils.getColumnsNumber(recyclerView))
        recyclerView!!.layoutManager = layoutManager
        searchSurpriseRecyclerAdapter = SurpriseRecyclerAdapter(SurpriseListType.SEARCH)
        searchSetRecyclerAdapter = SearchSetRecyclerAdapter()
        when (radioGroup.checkedRadioButtonId) {
            R.id.search_radio_surprise -> {
                recyclerView!!.adapter = searchSurpriseRecyclerAdapter
                mode = SearchMode.SURPRISE
            }

            R.id.search_radio_sets -> {
                recyclerView!!.adapter = searchSetRecyclerAdapter
                mode = SearchMode.SET
            }
        }
        radioGroup.setOnCheckedChangeListener { radioG: RadioGroup, _: Int ->
            when (radioG.checkedRadioButtonId) {
                R.id.search_radio_surprise -> changeMode(SearchMode.SURPRISE)
                R.id.search_radio_sets -> changeMode(SearchMode.SET)
            }
        }
        searchViewModel.surprises.observe(viewLifecycleOwner) { surprises: MutableList<Surprise> ->
            searchSurpriseRecyclerAdapter!!.submitList(surprises)
            searchSurpriseRecyclerAdapter!!.setFilterableList(surprises)
            searchSurpriseRecyclerAdapter!!.notifyDataSetChanged()
        }
        searchViewModel.sets.observe(viewLifecycleOwner) { sets: List<Set> ->
            searchSetRecyclerAdapter!!.submitList(sets)
            searchSetRecyclerAdapter!!.setFilterableList(sets)
            searchSetRecyclerAdapter!!.notifyDataSetChanged()
        }
        searchView = root!!.findViewById(R.id.search_field)
        SystemUtils.setSearchViewStyle(searchView)
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                searchSetRecyclerAdapter!!.filter.filter(s)
                searchSurpriseRecyclerAdapter!!.filter.filter(s)
                return false
            }
        })
        searchView!!.queryHint = getString(R.string.search)
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(activity, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                var setId: String? = null
                var setName: String? = null
                when (mode) {
                    SearchMode.SURPRISE -> {
                        val surprise = searchSurpriseRecyclerAdapter!!.getItemAtPosition(position)!!
                        SetDao.getSetById(surprise.set_id, object: CallbackInterface<Set>{
                            override fun onStart() {
                                TODO("Not yet implemented")
                            }

                            override fun onFailure() {
                                TODO("Not yet implemented")
                            }

                            override fun onSuccess(set:Set) {
                                openSet(view!!, set.id!!, set.name!!, set.thanks_to?.split(",")?.toTypedArray())
                            }

                        });
                        setId = surprise.set_id
                        setName = surprise.set_name
                    }

                    SearchMode.SET -> {
                        val set = searchSetRecyclerAdapter!!.getItemAtPosition(position)!!
                        openSet(view!!, set.id!!, set.name!!, set.thanks_to?.split(",")?.toTypedArray())
                    }

                    else -> {}
                }
                searchView!!.setQuery("", false)
                SystemUtils.closeKeyboard(activity)
            }

            override fun onLongItemClick(view: View?, position: Int) {}
        })
        )
        setTitle(getString(R.string.search_title))
        return root
    }

    private fun openSet(view: View, setId: String, setName: String, thanksTo: Array<String>?) {
        val action = SearchFragmentDirections.onSearchedItemClick(setId, setName, thanksTo)
        findNavController(view).navigate(action)
    }

    private fun changeMode(mode: SearchMode) {
        this.mode = mode
        when (mode) {
            SearchMode.SURPRISE -> recyclerView!!.adapter = searchSurpriseRecyclerAdapter
            SearchMode.SET -> recyclerView!!.adapter = searchSetRecyclerAdapter
        }
    }
}
