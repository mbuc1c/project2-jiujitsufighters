package com.bucic.project2_jiujitsufighters.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bucic.project2_jiujitsufighters.R
import com.bucic.project2_jiujitsufighters.databinding.FragmentFighterDetailsBinding
import com.bucic.project2_jiujitsufighters.model.BJJFighterEntity
import com.bucic.project2_jiujitsufighters.viewmodel.FighterDetailsViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@AndroidEntryPoint
class FighterDetailsFragment : Fragment() {

    private val args: FighterDetailsFragmentArgs by navArgs()

    @Inject lateinit var factory: FighterDetailsViewModel.Factory
    private val viewModel: FighterDetailsViewModel by viewModels {
        FighterDetailsViewModel.providesFactory(
            assistedFactory = factory,
            gameId = args.fighterId)
    }
    private var _binding: FragmentFighterDetailsBinding? = null
    private val binding get() = _binding!!

    private var picturePath: String? = null
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFighterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        takePicture()
        viewModel.fighter.observe(viewLifecycleOwner) {
            assignFighterData()
        }
        activateButtons()
    }

    private fun activateButtons() {
        with(binding) {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            datePicker.init(year, month, day, DatePicker.OnDateChangedListener { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date here
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)

                selectedDate = selectedDateCalendar.time
            })

            btnInsert.setOnClickListener {
                if (args.fighterId != -1) viewModel.insertFighter(inputExistingFighter())
                else viewModel.insertFighter(inputNewFighter())
                findNavController().navigateUp()
            }

            btnDelete.setOnClickListener {
                viewModel.deleteFighter()
                findNavController().navigateUp()
            }

            btnTakePicture.setOnClickListener {
                checkPermission()
            }
        }
    }

    private fun checkPermission() = when {
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
            takePicture()
        }

        else -> {
            val cameraPermission = Manifest.permission.CAMERA
            val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

            val hasCameraPermission = ContextCompat.checkSelfPermission(requireContext(), cameraPermission) == PackageManager.PERMISSION_GRANTED
            val hasStoragePermission = ContextCompat.checkSelfPermission(requireContext(), storagePermission) == PackageManager.PERMISSION_GRANTED

            val permissionsToRequest = mutableListOf<String>()
            if (!hasCameraPermission) {
                permissionsToRequest.add(cameraPermission)
            }
            if (!hasStoragePermission) {
                permissionsToRequest.add(storagePermission)
            }
            requestPermissions(permissionsToRequest.toTypedArray(),2)
        }
    }

    private fun assignFighterData() {
        val fighter = viewModel.fighter.value
        if (fighter != null) {
            with(binding) {
                etName.setText(fighter.name)
                etSurname.setText(fighter.surname)
                when (fighter.beltColor) {
                    "White" -> rgBeltColor.check(R.id.rb_white)
                    "Blue" -> rgBeltColor.check(R.id.rb_blue)
                    "Purple" -> rgBeltColor.check(R.id.rb_purple)
                    "Brown" -> rgBeltColor.check(R.id.rb_brown)
                    "Black" -> rgBeltColor.check(R.id.rb_black)
                }
                Glide
                    .with(root)
                    .load(fighter.profilePicture)
                    .into(pictureTaken)

                val calendar = Calendar.getInstance()

                if (viewModel.fighter.value?.dob != null) {
                    selectedDate = viewModel.fighter.value?.dob!!
                    calendar.time = selectedDate!!
                } else selectedDate = calendar.time
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                datePicker.init(year, month, day, DatePicker.OnDateChangedListener { _, selectedYear, selectedMonth, selectedDay ->
                    // Handle the selected date here
                    val selectedDateCalendar = Calendar.getInstance()
                    selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)

                    selectedDate = selectedDateCalendar.time
                })
            }

        }
    }

    private fun inputNewFighter(): BJJFighterEntity = BJJFighterEntity(
        name = binding.etName.text.toString(),
        surname = binding.etSurname.text.toString(),
        profilePicture = picturePath ?: "",
        dob = selectedDate,
        beltColor = view?.findViewById<RadioButton>(binding.rgBeltColor.checkedRadioButtonId)?.text.toString()
    )

    private fun inputExistingFighter(): BJJFighterEntity = BJJFighterEntity(
        id = viewModel.fighter.value!!.id,
        name = binding.etName.text.toString(),
        surname = binding.etSurname.text.toString(),
        profilePicture = picturePath ?: viewModel.fighter.value!!.profilePicture,
        dob = selectedDate,
        beltColor = view?.findViewById<RadioButton>(binding.rgBeltColor.checkedRadioButtonId)?.text.toString()
    )

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity?.packageManager ?: return) == null) {
            return
        }

        picturePath = createPictureFile()

        if (picturePath == null) {
            return
        }

        val pictureFile = File(picturePath)
        val pictureUri: Uri = FileProvider.getUriForFile(
            requireActivity(),
            "bucic.project2_jiujitsufighters.provider",
            pictureFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        startActivityForResult(intent, 1)
    }

    private fun createPictureFile(): String? {
        val name = SimpleDateFormat("yyyyMMddHHmmss").format(Date()) + "_fighter"
        val dir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(name, ".jpg", dir)
        return file.path
    }

    private fun loadTakenImage(imageUri: String) = with(binding) {
        Glide
            .with(root)
            .load(imageUri)
            .into(pictureTaken)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == RESULT_OK) {
                    picturePath?.let { loadTakenImage(it) }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> {
                val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }

                if (allPermissionsGranted) {
                    // All permissions are granted. Continue the action or workflow in your app.
                    takePicture()
                } else {
                    // Permissions denied. Explain to the user that the feature is unavailable.
                    // You can show a custom dialog here with an explanation and a button
                    // to open the app settings.
                    showPermissionDeniedDialog()
                }
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
    private fun showPermissionDeniedDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Permission Required")
            setMessage("This feature requires camera and storage permissions.")
            setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}