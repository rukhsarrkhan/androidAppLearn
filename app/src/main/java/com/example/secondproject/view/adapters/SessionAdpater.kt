package com.example.secondproject.view.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.secondproject.R
import com.example.secondproject.model.data.Session
import com.example.secondproject.databinding.ItemSessionBinding
import com.example.secondproject.databinding.ItemSessionTwoBinding
import com.example.secondproject.view.YouMatterFragment
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

class SessionAdpater(val context: YouMatterFragment, private val listener: SessionAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sessionList: MutableList<Session> = mutableListOf()
    var viewChoice = 2;

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewChoice == VIEW_TYPE_ONE) {
            return SessionView1Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_session,parent,false))
        }
        return SessionView2Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_session_two,parent,false))
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    override fun getItemViewType(position: Int): Int {
        return sessionList[position].viewType
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = sessionList[position];
        Log.e("page","item--->${item}")


        try {
            val current = LocalDateTime.now()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            val date1: Date = sdf.parse(current.toString())
            val date2: Date = sdf.parse(item.date)

            Log.e("page","current--->${date1}")
            Log.e("page","time--->${date2}")

           if (date1.compareTo(date2) < 0) {
               viewChoice = 2
                Log.i("page", "Date1 is before Date2");
                (holder as SessionView2Holder)
                val dateFormated = formatDate(item.date,"yyyy-MM-dd'T'HH:mm:ss","dd MMM") + ", " + formatDate(item.time,"HH:mm:ss","h a")
                holder.date.text = dateFormated;
                if ( item.name == "Live Yoga Session") {
                    holder.title.text = "Yoga Session";
                    holder.image.setImageResource(R.drawable.yoga_session);
                } else if ( item.name == "Live Workout Session") {
                    holder.title.text = "Meditation Session";
                    holder.image.setImageResource(R.drawable.meditation_session);
                }


           }
           else
               if (date1.compareTo(date2) == 0 || date1.compareTo(date2) > 0) {
                   viewChoice = 1
                Log.i("page", "Date1 is equal to Date2");
                (holder as SessionView1Holder)
                   holder.date.text = "On Going";
                   Log.i("page", "item.link--->${item.link}");
                   holder.button.setOnClickListener {
                       Log.i("page", "item.link--->${item.link}");
                       listener.joinNowClick(item)
                   }
                   if ( item.name == "Live Yoga Session") {
                    holder.title.text = "Yoga Session";
                    holder.image.setImageResource(R.drawable.yoga_session);
                } else if ( item.name == "Live Workout Session") {
                    holder.title.text = "Meditation Session";
                    holder.image.setImageResource(R.drawable.meditation_session);
                }

               }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateList(inputList : MutableList<Session>) {
        sessionList.clear()
        // sessionList.add(Session("61a3179eaac4953fb7d7a64e","Live Yoga Session","7:00:00","2023-07-19T02:30:42.838+00:00", "https://us02web.zoom.us/j/84627074500?pwd=RDVXQ1V4Z2JGR3ZqK3BxQ1c4VzJSdz09", R.drawable.yoga_session, 1))
        sessionList.addAll(inputList)
        notifyDataSetChanged()
    }

    inner class SessionView1Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSessionBinding.bind(itemView)
        var layoutSession = binding.layoutLiveSession1;
        var date = binding.txtLiveSessionDate;
        var title = binding.txtLiveSessionName;
        var image = binding.imgLiveSession;
        var button = binding.joinButton;
    }

    inner class SessionView2Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemSessionTwoBinding.bind(itemView)
        var layoutSession = binding.layoutLiveSession2;
        var date = binding.txtLiveSessionDate;
        var title = binding.txtLiveSessionName;
        var image = binding.imgLiveSession;
    }

    interface SessionAdapterListener {
        fun joinNowClick(sessionModal : Session)
    }

    fun formatDate(date: String?, sourceFormat: String, destinationFormat: String): String {
        val srcFormat = SimpleDateFormat(sourceFormat, Locale.ENGLISH)
        val destFormat = SimpleDateFormat(destinationFormat, Locale.ENGLISH)
        return destFormat.format(srcFormat.parse(date))
    }

}