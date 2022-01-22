package com.mobilki.backtonormal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [activityList.newInstance] factory method to
 * create an instance of this fragment.
 */
class activityList : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var navc: NavController ?= null
    var fab: FloatingActionButton ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //requireContext().deleteDatabase("ActivityDB");
        navc = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton)?.setOnClickListener(this)

        val db = DatabaseHelper(requireContext())
        val allActivities = db.getPreferredActivities()

        val linearLayout = view.findViewById<LinearLayout>(R.id.activityListLinearLayout)
        val inflater = LayoutInflater.from(requireContext())

        allActivities.forEach {

            val view = inflater.inflate(R.layout.template_activity_preferred_list_row, linearLayout, false)

            val activityNameButton = view.findViewById<Button>(R.id.activityButton1)
            activityNameButton.text = it.name

            val id = it.id
            activityNameButton.setOnClickListener {
                val bundle = bundleOf("activityId" to id)
                navc?.navigate(R.id.action_activityList_to_activitiesFragment, bundle)
            }

            linearLayout.addView(view)
        }
    }

    override fun onClick(p0: View?) {
        navc?.navigate(R.id.action_activityList_to_activityAddFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment activityList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activityList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}