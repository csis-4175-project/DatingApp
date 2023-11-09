package com.example.termproject_datingapp.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.termproject_datingapp.R;

public class ProgramSpinnerAdapter implements AdapterView.OnItemSelectedListener {

    ArrayAdapter adapter;
    String program;

    public ProgramSpinnerAdapter(Context context) {
        adapter = ArrayAdapter.createFromResource(
                context,
                R.array.programs_array,
                android.R.layout.simple_spinner_item
        );
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setProgram(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fillProgramSpinner(Spinner targetSpinner) {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetSpinner.setAdapter(adapter);
        targetSpinner.setOnItemSelectedListener(this);
    }
}
