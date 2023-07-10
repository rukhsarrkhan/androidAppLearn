package com.example.secondproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.secondproject.databinding.FragmentSecondBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding : FragmentSecondBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        try {
            binding.button!!.setOnClickListener {
                findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)
            }

            binding.button2!!.setOnClickListener {
                findNavController().navigate(R.id.action_secondFragment_to_youMatterFragment)
            }

            binding.button3!!.setOnClickListener {
                findNavController().navigate(R.id.action_secondFragment_to_fourthFragment)
            }

            binding.button4!!.setOnClickListener {
                Toast.makeText(activity, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            }

            binding.button5!!.setOnClickListener {
                openWhatsApp(this, "https://wa.me/917208867122?text=Hello")
            }

            binding.button6!!.setOnClickListener {
                phoneDialer()
            }

        } catch ( e : Exception ) {
            e.printStackTrace()
        }
        return binding.root
    }

    fun openWhatsApp(context: SecondFragment, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    fun phoneDialer() {
        val u = Uri.parse("tel:" + "917208867122")
        val intent = Intent(Intent.ACTION_DIAL, u)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}