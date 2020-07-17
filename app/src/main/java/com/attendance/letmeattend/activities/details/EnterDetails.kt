package com.attendance.letmeattend.activities.details

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.DetailsActivityBinding


class EnterDetails : AppCompatActivity() {
    private val TAG = "EnterDetails"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : DetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.details_activity)
        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        supportActionBar?.setShowHideAnimationEnabled(false)
        val viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        val fragmentManager = supportFragmentManager
        viewModel.fragmentDisplayer.observe(this, Observer {
            Log.d(TAG, "Got fragment request")
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit).replace(binding.frame.id, it).addToBackStack(null).commit()
        })


    }

    override fun onResume() {
        super.onResume()
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        supportActionBar?.setShowHideAnimationEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            onBackPressed()
            Toast.makeText(this, "OnBAckPressed Works", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}