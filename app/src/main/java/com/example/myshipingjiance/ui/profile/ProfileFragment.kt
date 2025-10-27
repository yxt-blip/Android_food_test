package com.example.myshipingjiance.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myshipingjiance.R
import de.hdodenhof.circleimageview.CircleImageView
import com.example.myshipingjiance.db.UserDao
import java.io.File

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userDao = UserDao(requireContext())
        val avatarImage = view.findViewById<CircleImageView>(R.id.avatarImage)
        val userNameText = view.findViewById<TextView>(R.id.userNameText)
        val userDescriptionText = view.findViewById<TextView>(R.id.userDescriptionText)
        val editButton = view.findViewById<Button>(R.id.editProfileButton)
        val user = userDao.getCurrentUser()
        user?.let {
            userNameText.text = it.username
            userDescriptionText.text = it.description
            it.avatarPath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    avatarImage.setImageURI(Uri.fromFile(file))
                }
            }
        }
        editButton.setOnClickListener {
            startActivity(Intent(requireContext(), com.example.myshipingjiance.EditProfileActivity::class.java))
        }
    }
} 