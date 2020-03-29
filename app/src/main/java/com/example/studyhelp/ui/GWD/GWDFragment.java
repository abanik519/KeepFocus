package com.example.studyhelp.ui.GWD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.studyhelp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class GWDFragment extends Fragment {


    private Button plus;
    private EditText taskET;
    private ExpandableListView taskslist;

    private List<String> tasks;
    private HashMap<String, List<String>> goals;
    private ExpandableListAdapter listAdapter;

    public GWDFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gwd, container, false);

        taskslist = root.findViewById(R.id.tasks_list);
        plus = root.findViewById(R.id.button);
        taskET = root.findViewById(R.id.task_edit_text);
        tasks = new ArrayList<>();
        tasks.add("Short Term Tasks");
        tasks.add("Long Term Tasks");
        goals = FileHelper2.readData(getActivity());

        listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
        taskslist.setAdapter(listAdapter);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> goalslist = new ArrayList<>();
                if (v.getId() == R.id.button) {
                    if (taskslist.isGroupExpanded(0)) {
                        goalslist = goals.get(tasks.get(0));
                        String goalEntered = "Goal: " + taskET.getText().toString();
                        if(goalEntered.length() < 7){
                            Toast.makeText(getActivity(), "Please enter a goal", LENGTH_SHORT).show();
                            return;
                        }
                        goalslist.add(goalEntered);
                        goals.put(tasks.get(0), goalslist);
                        taskET.setText("");
                        taskET.setHint("Add Goal");
                        listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                        taskslist.setAdapter(listAdapter);
                        taskslist.expandGroup(0);
                        FileHelper2.writeData(goals, getActivity());
                        Toast.makeText(getActivity(), "Goal Added", Toast.LENGTH_SHORT).show();
                    }
                    else if (taskslist.isGroupExpanded(1)) {
                        goalslist = goals.get(tasks.get(1));
                        String goalEntered = "Goal: " + taskET.getText().toString();
                        if(goalEntered.length() < 7){
                            Toast.makeText(getActivity(), "Please enter a goal", LENGTH_SHORT).show();
                            return;
                        }
                        goalslist.add(goalEntered);
                        goals.put(tasks.get(1), goalslist);
                        taskET.setText("");
                        taskET.setHint("Add Goal");
                        listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                        taskslist.setAdapter(listAdapter);
                        taskslist.expandGroup(1);
                        FileHelper2.writeData(goals, getActivity());
                        Toast.makeText(getActivity(), "Goal Added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        taskslist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                List<String> goalslist = goals.get(tasks.get(groupPosition));
                assert goalslist != null;
                goalslist.remove(childPosition);
                goals.put(tasks.get(groupPosition), goalslist);
                taskET.setHint("Add Goal");
                listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                taskslist.setAdapter(listAdapter);
                taskslist.expandGroup(groupPosition);
                FileHelper2.writeData(goals, getActivity());
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        taskslist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition == 0) {
                    taskET.setHint("Add Short Term Goal");
                } else if (groupPosition == 1) {
                    taskET.setHint("Add Long Term Goal");
                }
            }
        });

        taskslist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (goals.get(tasks.get(groupPosition)) == null) {
                    List<String> goalslist = new ArrayList<>();
                    goals.put(tasks.get(groupPosition), goalslist);
                    taskslist.expandGroup(groupPosition);
                    FileHelper2.writeData(goals, getActivity());
                    return true;
                }
                return false;
            }
        });
        return root;
    }

}
