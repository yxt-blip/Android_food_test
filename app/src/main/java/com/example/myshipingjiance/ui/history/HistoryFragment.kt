package com.example.myshipingjiance.ui.history

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshipingjiance.R
import com.example.myshipingjiance.databinding.FragmentHistoryBinding
import com.example.myshipingjiance.databinding.ItemHistoryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val records = loadHistoryRecords(requireContext())
        binding.todayRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HistoryAdapter(records)
        }
        // 隐藏昨天分组
        binding.yesterdayRecyclerView.visibility = View.GONE
    }

    private fun loadHistoryRecords(context: Context): List<HistoryRecord> {
        val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
        val gson = Gson()
        val listType = object : TypeToken<List<HistoryRecord>>() {}.type
        val json = prefs.getString("records", "[]")
        return gson.fromJson(json, listType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class HistoryRecord(
    val status: String,
    val time: String,
    val percentage: String,
    val backgroundColor: String
)

class HistoryAdapter(private val items: List<HistoryRecord>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryRecord) {
            binding.apply {
                statusText.text = item.status
                timeText.text = item.time
                percentageText.text = item.percentage
                contentLayout.setBackgroundColor(Color.parseColor(item.backgroundColor))
            }
        }
    }
} 