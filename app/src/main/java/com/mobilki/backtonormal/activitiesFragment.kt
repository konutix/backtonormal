package com.mobilki.backtonormal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [activitiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class activitiesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var activityInfo : ActivityInfo ?= null
    var taskInfo : StatsInfo ?= null
    var db : DatabaseHelper ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = arguments?.getInt("activityId")
        db = DatabaseHelper(requireContext())
        activityInfo = db!!.getActivity(id!!)
        taskInfo = db!!.getTask(id!!)

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
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView =  view.findViewById<TextView>(R.id.textViewActivityName)
        val catTextView =  view.findViewById<TextView>(R.id.textViewActivityType)

        nameTextView.text = activityInfo!!.name
        catTextView.text = activityInfo!!.cat


        val switchPrefer = view.findViewById<Switch>(R.id.switchPrefer)
        val switchTask = view.findViewById<Switch>(R.id.switchTask)
        switchPrefer.isChecked = db!!.isPreferred(activityInfo!!.id)
        switchTask.isChecked = db!!.isTracked(taskInfo!!.taskId)
        switchPrefer.setOnCheckedChangeListener { _, isChecked ->
            val err = db!!.preferActivity(activityInfo!!.id, isChecked)
            db!!.test()
        }
        switchTask.setOnCheckedChangeListener { _, isChecked ->
            taskInfo!!.progress = 0
            db!!.saveTaskToDataBase(taskInfo!!)
            val errTask = db!!.preferTask(taskInfo!!.taskId, isChecked)

            db!!.test()
        }

        val image = view.findViewById<ImageView>(R.id.imageActivity)
        val imageId = resources.getIdentifier("@drawable/" + activityInfo!!.name.lowercase().replace(' ', '_'), null, requireContext().packageName)
        image.setImageResource(
            if (imageId == 0)
                R.drawable.generic_sport_image
            else
                imageId
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment activitiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activitiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}