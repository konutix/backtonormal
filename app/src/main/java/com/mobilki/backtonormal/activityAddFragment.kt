package com.mobilki.backtonormal

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import kotlin.collections.ArrayList

class activityAddFragment : Fragment() {

    var navc: NavController?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navc = Navigation.findNavController(view)

        var db = DatabaseHelper(requireContext())
        val allActivities = db.getAllActivities()

        val listView = view.findViewById<ListView>(R.id.listViewActivities)
        val adapter = ListRowAdapter(requireContext(), allActivities, android.R.layout.simple_list_item_1, navc!!, R.id.action_activityAddFragment_to_activitiesFragment)

        listView.adapter = adapter

        val inputView = view.findViewById<TextInputEditText>(R.id.addActivitySearchText)

        inputView.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.searchText(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        val list = listOf("All") + db.getActivityCategories()

        val categoryFilter = view.findViewById<Spinner>(R.id.categoryFilter)
        categoryFilter.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, list)

        categoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
                adapter.searchCategory(p0.getItemAtPosition(p2).toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

//
//        val linearLayout = view.findViewById<LinearLayout>(R.id.addActivityLinearLayout)
//        val inflater = LayoutInflater.from(requireContext())
//
//        allActivities.forEach {
//
//            var view = inflater.inflate(R.layout.template_activity_add_list_row, linearLayout, false)
//
//            var activityNameButton = view.findViewById<Button>(R.id.activityNameButton)
//            activityNameButton.text = it.name
//
//            val id = it.id
//            activityNameButton.setOnClickListener {
//                val bundle = bundleOf("activityId" to id)
//                navc?.navigate(R.id.action_activityAddFragment_to_activitiesFragment, bundle)
//            }
//
//            var categoryText = view.findViewById<TextView>(R.id.categoryText)
//            categoryText.text = it.cat
//
//            linearLayout.addView(view)
//        }

    }


}

class ListRowAdapter(context: Context, private val arrayList: ArrayList<ActivityInfo>, private val layoutResource : Int, private val navc : NavController, private val navigateId : Int) :
    ArrayAdapter<ActivityInfo>(context, layoutResource, arrayList),
    Filterable {
    lateinit var activityNameButton : Button
    lateinit var categoryText : TextView

    private var searchedText : String = ""
    private var searchedCategory : String = "All"

    var activityList : ArrayList<ActivityInfo> = arrayList

    override fun getCount() : Int{
        return activityList.size
    }

    override fun getItem(position: Int): ActivityInfo? {
        return activityList[position]
    }

    override fun getItemId(position: Int): Long {
        return activityList[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.template_activity_add_list_row, parent, false)

        var activityNameButton = convertView.findViewById<Button>(R.id.activityNameButton)
        activityNameButton.text = activityList[position].name

        val id = activityList[position].id
        activityNameButton.setOnClickListener {
            val bundle = bundleOf("activityId" to id)
            navc.navigate(navigateId, bundle)
        }

        var categoryText = convertView.findViewById<TextView>(R.id.categoryText)
        categoryText.text = activityList[position].cat

        return convertView
    }

    fun searchText(p0 : String) {
        searchedText = p0
        this.filter.filter(searchedText)
    }

    fun searchCategory(p0 : String) {
        searchedCategory = p0
        this.filter.filter(searchedText)
    }

    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.lowercase(Locale.getDefault())

                val categorisedList = arrayList.filter {
                    it.cat.contains(searchedCategory) || searchedCategory == "All"
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = if ((queryString==null || queryString.isEmpty()))
                    categorisedList
                else
                    categorisedList.filter {
                        it.name.lowercase(Locale.getDefault()).contains(queryString)
                    }

                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                activityList = filterResults.values as ArrayList<ActivityInfo>
                notifyDataSetChanged()
            }

        }
    }
}