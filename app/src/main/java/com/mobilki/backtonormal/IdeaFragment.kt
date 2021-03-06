package com.mobilki.backtonormal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import java.text.DateFormat
import java.util.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IdeaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IdeaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var db : DatabaseHelper
    lateinit var navc : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        db = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_idea, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navc = Navigation.findNavController(view)

        val button = view.findViewById<Button>(R.id.buttonIdeaDraw)
        button.setOnClickListener {
            var activities = db.getPreferredActivities()
            if (activities.size < 1) {
                activities = db.getAllActivities()
            }

            val randomActivity = activities[Random.nextInt(activities.size)]

            val id = randomActivity.id
            val bundle = bundleOf("activityId" to id)
            navc.navigate(R.id.action_ideaFragment_to_activitiesFragment, bundle)
        }

        val lastDay = db.getLastLogin()

        val calendar = Calendar.getInstance()
        val day = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.time)

        if(lastDay != day){

            db.drawDaily()
            db.setLastLogin()

        }

        val allDaily = db.getDaily()

        val daily1 = view.findViewById<CheckBox>(R.id.daily1)
        val daily2 = view.findViewById<CheckBox>(R.id.daily2)
        val daily3 = view.findViewById<CheckBox>(R.id.daily3)

        daily1.text = allDaily.get(0).title
        daily1.isChecked = allDaily.get(0).completed == 1
        val daily1Id = allDaily.get(0)

        daily2.text = allDaily.get(1).title
        daily2.isChecked = allDaily.get(1).completed == 1
        val daily2Id = allDaily.get(1)

        daily3.text = allDaily.get(2).title
        daily3.isChecked = allDaily.get(2).completed == 1
        val daily3Id = allDaily.get(2)

        daily1.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked)
                db.changeDailyState(daily1Id,1)
            else
                db.changeDailyState(daily1Id,0)

        }

        daily2.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked)
                db.changeDailyState(daily2Id,1)
            else
                db.changeDailyState(daily2Id,0)

        }

        daily3.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked)
                db.changeDailyState(daily3Id,1)
            else
                db.changeDailyState(daily3Id,0)

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IdeaFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IdeaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}