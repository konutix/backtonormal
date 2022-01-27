package com.mobilki.backtonormal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import java.io.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var navc: NavController?= null
//    var navc: NavController?= null
//    var statsInfo : StatsInfo ?= null
//    var db : DatabaseHelper ?= null
//    var test: List<TextView> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        val myText: TextView
        super.onCreate(savedInstanceState)
//        val id = arguments?.getInt("statsId")
//
//        db = DatabaseHelper(requireContext())
//        statsInfo = db!!.getTask(id!!)
        arguments?.let {

            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {val view: View = inflater.inflate(R.layout.fragment_stats, container, false)




        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var db = DatabaseHelper(requireContext())
        val allTrackedStats = db.getAllTrackedTasks()
        val linearLayout = view.findViewById<LinearLayout>(R.id.addTask)
        val inflater = LayoutInflater.from(requireContext())


        allTrackedStats.forEach{
            var view = inflater.inflate(R.layout.template_task_add_list, linearLayout, false)

            var taskName = view.findViewById<TextView>(R.id.taskName0)
            taskName.text = it.taskName
            val id = it.id


            var taskDescription = view.findViewById<TextView>(R.id.textView1)
            taskDescription.text = it.taskDescription
            var taskProgressBar = view.findViewById<ProgressBar>(R.id.progressBar1)
            taskProgressBar.progress = it.progress
            var editTextNumber: EditText = view.findViewById(R.id.editTextNumber1)
            view.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    if(editTextNumber.text.toString() != ""){
                        taskProgressBar.progress = editTextNumber.text.toString().toInt()
                        it.progress = taskProgressBar.progress
                        db.saveTaskToDataBase(it)

                    }
                }
            })
            taskName.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?){
                    navc = Navigation.findNavController(v!!)
                    val bundle = bundleOf("activityId" to id)
                    navc?.navigate(R.id.action_statsFragment_to_activitiesFragment, bundle)
                }
            })



            println(it.progress)
            linearLayout.addView(view)
        }





//        for (i in 1..4){
//            val idView1: Int = resources.getIdentifier("editTextNumber$i","id", requireContext().packageName)
//            val idView2: Int = resources.getIdentifier("progressBar$i","id", requireContext().packageName)
//            val eventView: View = view.findViewById(idView1)
//            val editTextNumber: EditText = eventView.findViewById(idView1)
//            val progressBar: ProgressBar = view.findViewById(idView2)
//
//            eventView.setOnClickListener(object: View.OnClickListener{
//                override fun onClick(v: View?) {
//                    if(editTextNumber.text.toString() != "")
//                        progressBar.progress = editTextNumber.text.toString().toInt()
//
//                }
//
//            })
//        }
    }


//    private fun ReadFromFile(): MutableList<String> {
//
//        var lineList = mutableListOf<String>()
//        var inputStream: InputStream? = null
//        try {
//            inputStream = requireActivity().assets.open("test.txt")
//            val bufferedReader = inputStream!!.bufferedReader()
//            bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }
//        } catch (e: IOException){
//            e.printStackTrace()
//        } finally {
//            inputStream?.close()
//        }
//        return lineList
//    }
//    private fun writeFile(){
//        val outFile = File(Environment.getExternalStorageDirectory(), "test.txt")
//        val out = FileOutputStream(outFile, false)
//        val contents: ByteArray = byteArrayOf(data.toByte())
//        out.write(contents)
//        out.flush()
//        out.close()
//    }



}

