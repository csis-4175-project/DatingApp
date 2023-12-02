package com.example.termproject_datingapp;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class MatchingFragment extends Fragment {

    private CardStackLayoutManager manager;
    private CadStackAdapter adapter;

    private static final String TAG = "MatchingFragment";

    public MatchingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matching, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardStackView cardStackView  = view.findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(requireContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if(direction == Direction.Right){
                    Toast.makeText(requireContext(), "Direction Right", Toast.LENGTH_LONG).show();
                }

                if(direction == Direction.Top){
                    Toast.makeText(requireContext(), "Direction top", Toast.LENGTH_LONG).show();
                }

                if(direction == Direction.Left){
                    Toast.makeText(requireContext(), "Direction Left", Toast.LENGTH_LONG).show();
                }

                if(direction == Direction.Bottom){
                    Toast.makeText(requireContext(), "Direction Bottom", Toast.LENGTH_LONG).show();
                }

                if(manager.getTopPosition() == adapter.getItemCount() - 3){
                    paginate();
                }
            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ",name " + tv.getText());

            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ",name " + tv.getText());
            }

        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        adapter = new CadStackAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<StudentModel> addList() {
        List<StudentModel> student = new ArrayList<>();
        //stores user information
        //profile picture, name, age, city, and major, etc...
        student.add(new StudentModel(R.raw.man2, "Daniel", "Computer Science", "22", "Surrey"));
        student.add(new StudentModel(R.raw.womans, "Mary", "Computer Science", "21", "Richmond"));
        student.add(new StudentModel(R.raw.woman2, "Lauren", "Computer Science", "19", "Burnaby"));
        student.add(new StudentModel(R.raw.woman, "Emily", "Accounting", "23", "Newton"));
        student.add(new StudentModel(R.raw.male, "Kate", "Human Resource", "19", "North Vancouver"));
        student.add(new StudentModel(R.raw.istockphoto, "Dave", "Computer Science", "25", "Burnaby"));
        student.add(new StudentModel(R.raw.istockphotos, "Lauren", "Child and Youth Care", "22", "Surrey"));
        student.add(new StudentModel(R.raw.fashion, "Niel", "Business", "18", "Burnaby"));
        return student;
    }

    private void paginate() {
        List<StudentModel> oldList = adapter.getStudentList();
        List<StudentModel> newList = new ArrayList<>(addList());
        CardStackCallBack callBack = new CardStackCallBack(oldList, newList);
        DiffUtil.DiffResult hasList = DiffUtil.calculateDiff(callBack);
        adapter.setStudentList(newList);
        hasList.dispatchUpdatesTo(adapter);
    }
}
