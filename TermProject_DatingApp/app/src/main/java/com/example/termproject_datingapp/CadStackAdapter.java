package com.example.swipingfunction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CadStackAdapter extends RecyclerView.Adapter<CadStackAdapter.ViewHolder> {
    private List<StudentModel> studentList;

    public CadStackAdapter(List<StudentModel> student){
        this.studentList = student;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.student_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(studentList.get(position));

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name ;
        TextView city;
        TextView age;
        TextView major;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.student_image);
            name = itemView.findViewById(R.id.item_name);
            age = itemView.findViewById(R.id.item_age);
            city = itemView.findViewById(R.id.item_city);
            major = itemView.findViewById(R.id.item_major);

        }
        public void setData(StudentModel data) {
            Picasso.get().load(data.getImage()).fit().centerCrop().into(image);
            name.setText(data.getStudentName());
            age.setText(data.getStudentMajor());
            city.setText(data.getStudentCity());
            major.setText(data.getStudentAge());
        }
    }

    public List<StudentModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentModel> studentList) {
        this.studentList = studentList;
    }
}
