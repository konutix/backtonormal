package com.mobilki.backtonormal

import android.R.attr.data
import android.content.res.AssetFileDescriptor
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.collection.arrayMapOf
import androidx.fragment.app.Fragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        val myText: TextView
        super.onCreate(savedInstanceState)

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

        val list: MutableList<String> = ReadFromFile()
        val myText1: TextView = view.findViewById(R.id.textView0)
        val myText2: TextView = view.findViewById(R.id.textView1)
        val myText3: TextView = view.findViewById(R.id.textView2)
        val myText4: TextView = view.findViewById(R.id.textView3)
        val myText5: TextView = view.findViewById(R.id.textView4)
        val myText6: TextView = view.findViewById(R.id.textView5)
        val myText7: TextView = view.findViewById(R.id.textView6)
        val myText8: TextView = view.findViewById(R.id.textView7)
        myText1.setText(list[0])
        Log.d("string", myText1.text.toString())
        myText2.setText(list[1])
        myText3.setText(list[2])
        myText4.setText(list[3])
        myText5.setText(list[4])
        myText6.setText(list[5])
        myText7.setText(list[6])
        myText8.setText(list[7])


        for (i in 1..4){
            val idView1: Int = resources.getIdentifier("editTextNumber$i","id", requireContext().packageName)
            val idView2: Int = resources.getIdentifier("progressBar$i","id", requireContext().packageName)
            val eventView: View = view.findViewById(idView1)
            val editTextNumber: EditText = eventView.findViewById(idView1)
            val progressBar: ProgressBar = view.findViewById(idView2)
            eventView.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    if(editTextNumber.text.toString() != "")
                        progressBar.progress = editTextNumber.text.toString().toInt()

                }

            })
        }
    }


    private fun ReadFromFile(): MutableList<String> {

        var lineList = mutableListOf<String>()
        var inputStream: InputStream? = null
        try {
            inputStream = requireActivity().assets.open("test.txt")
            val bufferedReader = inputStream!!.bufferedReader()
            bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }
        } catch (e: IOException){
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return lineList
    }
//    private fun writeFile(){
//        val outFile = File(Environment.getExternalStorageDirectory(), "test.txt")
//        val out = FileOutputStream(outFile, false)
//        val contents: ByteArray = byteArrayOf(data.toByte())
//        out.write(contents)
//        out.flush()
//        out.close()
//    }



}

