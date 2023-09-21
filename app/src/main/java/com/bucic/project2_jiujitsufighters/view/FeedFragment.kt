package com.bucic.project2_jiujitsufighters.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bucic.project2_jiujitsufighters.R
import com.bucic.project2_jiujitsufighters.databinding.FragmentFeedBinding
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import com.bucic.project2_jiujitsufighters.viewmodel.FeedViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fighters()
        viewModel.fighters.observe(viewLifecycleOwner) {
            populateListView()
        }
        activateComponents()
    }

    private fun activateComponents() {
        binding.newFighterFab.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToFighterDetailsFragment()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun populateListView() {
        binding.linearLayout.removeAllViews()
        val roomEntities = viewModel.fighters

        // Binding
        for (entity in roomEntities.value!!) {
            val viewHolder = LayoutInflater.from(this.requireContext()).inflate(R.layout.fighter_list_item, null)

            val tvName = viewHolder.findViewById<TextView>(R.id.tv_name)
            tvName.text = entity.name + " " + entity.surname

            val tvDob = viewHolder.findViewById<TextView>(R.id.tv_dob)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = entity.dob?.let {
                dateFormat.format(it)
            }
            tvDob.text = formattedDate

            val tvBelt = viewHolder.findViewById<TextView>(R.id.tv_belt)
            tvBelt.text = entity.beltColor

            setPicture(entity.profilePicture, viewHolder.findViewById(R.id.profile_picture))

            viewHolder.setOnClickListener {
                val action = FeedFragmentDirections.actionFeedFragmentToFighterDetailsFragment(entity.id)
                findNavController().navigate(action)
            }
            // Add the ViewHolder to the LinearLayout
            binding.linearLayout.addView(viewHolder)
        }
    }

    private fun setPicture(picture: String?, imageView: ImageView) {
        Glide.with(imageView)
            .load(picture)
            .override(imageView.width, imageView.width)
            .into(imageView)
    }
}