package com.mobilki.backtonormal

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

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

        /**
         * Get all activities from database and display them as a list
         * */

        var db = DatabaseHelper(requireContext())
        val allActivities = db.getAllActivities()

        val linearLayout = view.findViewById<LinearLayout>(R.id.addActivityLinearLayout)
        val inflater = LayoutInflater.from(requireContext())

        allActivities.forEach {

            var view = inflater.inflate(R.layout.template_activity_add_list_row, linearLayout, false)

            var activityNameButton = view.findViewById<Button>(R.id.activityNameButton)
            activityNameButton.text = it.name

            val id = it.id
            activityNameButton.setOnClickListener {
                val bundle = bundleOf("activityId" to id)
                navc?.navigate(R.id.action_activityAddFragment_to_activitiesFragment, bundle)
            }

            var categoryText = view.findViewById<TextView>(R.id.categoryText)
            categoryText.text = it.cat

            linearLayout.addView(view)
        }

    }


}

//class ListRowAdapter(private val context: Context, private val arrayList: ArrayList<ActivityInfo>) : BaseAdapter() {
//    lateinit var activityNameButton : Button
//    lateinit var categoryText : TextView
//
//    override fun getCount() : Int{
//        return arrayList.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return position
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
//        var convertView = convertView
//        convertView = LayoutInflater.from(context).inflate(R.layout.template_activity_add_list_row, parent, false)
//
//        activityNameButton = convertView.findViewById(R.id.activityNameButton)
//        activityNameButton.text = arrayList[position].name
//
//        categoryText = convertView.findViewById(R.id.categoryText)
//        categoryText.text = arrayList[position].cat
//
//        return convertView
//    }
//}