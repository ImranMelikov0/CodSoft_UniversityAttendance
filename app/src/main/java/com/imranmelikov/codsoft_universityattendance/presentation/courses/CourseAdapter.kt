package com.imranmelikov.codsoft_universityattendance.presentation.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.codsoft_universityattendance.R
import com.imranmelikov.codsoft_universityattendance.databinding.ItemCoursesLayoutBinding
import com.imranmelikov.codsoft_universityattendance.domain.model.Courses

class CourseAdapter:RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    class CourseViewHolder(val binding:ItemCoursesLayoutBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Courses>(){
        override fun areItemsTheSame(oldItem: Courses, newItem: Courses): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Courses, newItem: Courses): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter for the list of course

    var userName=""
    var courseList:List<Courses>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding=ItemCoursesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CourseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course=courseList[position]
        holder.binding.courseName.text=course.course_name
        holder.binding.studentCount.text=course.student_list.size.toString()
        holder.binding.studentCountTitle.text="STUDENTS"

        holder.itemView.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("user",userName)
            bundle.putString("courseId",course.course_id)
            Navigation.findNavController(it).navigate(R.id.action_coursesFragment_to_coursesDetailFragment,bundle)
        }
    }
}