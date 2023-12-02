package com.example.swipingfunction;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class CardStackCallBack extends DiffUtil.Callback {

    private List <StudentModel> oldList;
    private List <StudentModel> newList;

    public CardStackCallBack(List<StudentModel> oldList, List<StudentModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getImage() == newList.get(newItemPosition).getImage();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
