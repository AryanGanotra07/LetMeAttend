package com.attendance.letmeattend.activities

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.R
import com.attendance.letmeattend.adapters.TabLayoutAdapter
import com.attendance.letmeattend.databinding.EnterDetailsActivityBinding
import com.attendance.letmeattend.fragments.AttendanceCriteriaFragment
import com.attendance.letmeattend.details.Monday
import com.attendance.letmeattend.listeners.AddSubjectListener
import com.attendance.letmeattend.listeners.OnLectureClickListener
import com.attendance.letmeattend.listeners.SaveClickListener
import com.attendance.letmeattend.fragments.timetable.*
import com.attendance.letmeattend.helpers.LectureDeserializer
import com.attendance.letmeattend.helpers.NotificationAlertStatus
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.attendance.letmeattend.views.NotificationAlert
import com.google.android.material.tabs.TabLayout

import java.util.*


class EnterDetailsActivity: AppCompatActivity(),
    SaveClickListener, AddSubjectListener, OnLectureClickListener {

    val fragmentManager:FragmentManager=supportFragmentManager
    val attendanceCriteriaFragment: AttendanceCriteriaFragment =
        AttendanceCriteriaFragment()

    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var alertView : View
    private lateinit var dialogHelper : SubjectDialogHelper
    private var id : Int = 0


    private lateinit var attendanceLiveData : MediatorLiveData<Attendance>

    private val TAG  = "EnterDetailsActivity"

    val tabLayoutAdapter: TabLayoutAdapter =
        TabLayoutAdapter(fragmentManager, 6)


    override fun onSave(attendance: Int) {
      //  add_btn.visibility = View.VISIBLE

        viewModel.setAttendance(Attendance(attendance = attendance))
        attendanceLiveData.removeObserver(attendanceObserver)

        val fragment : Fragment? = fragmentManager.findFragmentByTag("attendance");
        fragment?.let { fragmentManager.beginTransaction().remove(it).commit() }

        setUpViewPagerWithAdapter()



    }

   val broadcastReceiver : BroadcastReceiver =  object : BroadcastReceiver() {
       override fun onReceive(context: Context?, intent: Intent?) {

           val notificationAlert = NotificationAlert(context!!)
           val lecture = LectureDeserializer.getLecture(intent!!)
           val inte = intent.getParcelableExtra<Intent>("intent")
           //context?.stopService(inte)
if (NotificationAlertStatus.getLecture().id != lecture!!.id) {

    val aL = notificationAlert.executeDialog(lecture, inte)
    NotificationAlertStatus.setAbsentMarked(lecture!!, aL)
           NotificationAlertStatus.setLecture(lecture!!)
           NotificationAlertStatus.setRunning(true)
           NotificationAlertStatus.setAlertView(aL)
}

       }
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(broadcastReceiver, IntentFilter("NEW_NOTIFICATION"))

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val intent = Intent()
//            val packageName = packageName
//            val pm =
//                getSystemService(Context.POWER_SERVICE) as PowerManager
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//                intent.data = Uri.parse("package:$packageName")
//                startActivity(intent)
//            }
//        }

        val binding : EnterDetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.enter_details_activity)

        viewModel = ViewModelProviders.of(this).get(EnterDetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
        alertView = LayoutInflater.from(this).inflate(R.layout.alert_save_subject,null,false)
        dialogHelper = SubjectDialogHelper(this@EnterDetailsActivity,alertView,viewModel)
//
//        viewModel.getSubjects().observe(this, Observer {
//            if (it!=null)
//            {
//
//            }
//        })

//        attendanceLiveData = viewModel.getAttendance()
//
//        attendanceLiveData.observe(this, attendanceObserver)





//        val lecture : User.Day.Lecture = User.Day.Lecture("Lecture",User.Day.Lecture.CTime("start","end"), 75, User.Day.Lecture.Attendance
//        (23,45))
//
//
//        val lectures : ArrayList<User.Day.Lecture> = ArrayList()
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//
//
//        val mon : User.Day = User.Day(lectures)
//        val tues : User.Day = User.Day(lectures)
//        val wed : User.Day = User.Day(lectures)
//        val thurs : User.Day = User.Day(lectures)
//        val fri : User.Day = User.Day(lectures)
//        val sat : User.Day = User.Day(lectures)
//
//        val days = ArrayList<User.Day>()
//        days.add(mon)
//        days.add(tues)
//        days.add(wed)
//        days.add(thurs)
//        days.add(fri)
//        days.add(sat)
//
//        var user : com.attendance.letmeattend.Model.User = com.attendance.letmeattend.Model.User("1001",
//            75,
//            User.CollegeLocation(123,123.4),
//            days
//            )
//
//
//        FirebaseDatabase.getInstance().reference.child("User").child("11001").setValue(user)


//



//        viewPager.addOnPageChangeListener(onPageChangeListener)
//        viewPager.post(Runnable {
//            onPageChangeListener.onPageSelected(viewPager.currentItem)
//        })

//
//        val tabLayoutAdapter:TabLayoutAdapter= TabLayoutAdapter(supportFragmentManager,7)
//        val view_pager:ViewPager=findViewById(R.id.view_pager) as ViewPager
//        view_pager.adapter=tabLayoutAdapter
//        tab_layout.setupWithViewPager(view_pager)
//

//
    }

    val attendanceObserver = Observer<Attendance> {

        //Log.d(TAG, (it as Attendance).attendance.toString());
        if (it != null) {
            val attendanceobj = (it as Attendance)
            if (attendanceobj != null) {
                val attendance = attendanceobj.attendance
                if (attendance == null || attendance == 0) {
                   askForAttendance()

                } else {
                    setUpViewPagerWithAdapter()
                }
            }
        }
        else {
            askForAttendance()
        }
    }

    private fun askForAttendance () {
        val fragmentTransaction: FragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.add(
            R.id.frame_layout,
            attendanceCriteriaFragment,
            "attendance"
        )
        fragmentTransaction.addToBackStack("attendance")
        fragmentTransaction.commit()
    }


    private fun setUpViewPagerWithAdapter() {
        viewPager.adapter=tabLayoutAdapter
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)
        attendanceLiveData.removeObserver(attendanceObserver)

    }



    val onPageChangeListener : ViewPager.OnPageChangeListener = object  : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
           // this@EnterDetailsActivity.toast(positionOffsetPixels.toString())

        }

        override fun onPageSelected(position: Int) {
            id = position

        }

    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is AttendanceCriteriaFragment){
            fragment.setAttendanceListener(this)

        }
       else if (fragment is Monday){
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Tuesday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Wednesday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Thursday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Friday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Saturday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
    }

    override fun onAddSubject(day: Int) {
        //applicationContext.toast("Called")
       dialogHelper.addSubject(day)

    }

    override fun onAddSubject(day: Int, subject: Subject) {
        dialogHelper.addSubject(day, subject)
    }

    fun getFragmentId(): Int {
       return viewPager.currentItem


    }

    override fun onLectureClick(lecture: Lecture) {
           dialogHelper.updateSubject(lecture)
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        var position = -1
        var adapter = viewModel.getMonLectureRecyclerAdapter()
        when(item.groupId)
        {
            Calendar.MONDAY-> adapter = viewModel.getMonLectureRecyclerAdapter()
            Calendar.TUESDAY -> adapter = viewModel.getTueLectureRecyclerAdapter()
            Calendar.WEDNESDAY-> adapter = viewModel.getWedLectureRecyclerAdapter()
            Calendar.THURSDAY->adapter = viewModel.getThurLectureRecyclerAdapter()
            Calendar.FRIDAY->adapter = viewModel.getFriLectureRecyclerAdapter()
            Calendar.SATURDAY->adapter = viewModel.getsatLectureRecyclerAdapter()
        }

        position = try {
            adapter.getPosition()
        } catch (e: Exception) {
            return super.onContextItemSelected(item)
        }
        val lecture = adapter.getLecture(position)
        when (item.getItemId()) {
            R.id.edit_lecture -> dialogHelper.updateSubject(lecture)
            R.id.delete_lecture -> viewModel.deleteLecture(lecture)
        }
        return super.onContextItemSelected(item)
    }



}