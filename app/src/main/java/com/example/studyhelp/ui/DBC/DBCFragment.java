package com.example.studyhelp.ui.DBC;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.studyhelp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class DBCFragment extends Fragment {
    private Button plus;
    private EditText taskET;
    private ExpandableListView taskslist;

    private List<String> tasks;
    private HashMap<String, List<String>> goals;
    private ExpandableListAdapter listAdapter;

    public DBCFragment(){}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dbc, container, false);

        taskslist = root.findViewById(R.id.tasks_list);
        plus = root.findViewById(R.id.button);
        taskET = root.findViewById(R.id.task_edit_text);
        tasks = FileHelper.readData(getActivity());
        goals = new HashMap<>();

        listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
        taskslist.setAdapter(listAdapter);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> goalslist = new ArrayList<>();
                if (v.getId() == R.id.button) {
                    for (int i = 0; i <= tasks.size(); ++i) {
                        if (i == tasks.size()) {
                            goalslist = new ArrayList<>();
                            String taskEntered = "Task: " + taskET.getText().toString();
                            if(taskEntered.length() < 7){
                                Toast.makeText(getActivity(), "Please enter a task", LENGTH_SHORT).show();
                                break;
                            }
                            tasks.add(taskEntered);
                            taskET.setText("");
                            taskET.setHint("Add Task");
                            listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                            taskslist.setAdapter(listAdapter);
                            FileHelper.writeData(tasks, getActivity());
                            Toast.makeText(getActivity(), "Task Added", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else if (taskslist.isGroupExpanded(i)) {
                            goalslist = goals.get(tasks.get(i));
                            String goalEntered = "Goal: " + taskET.getText().toString();
                            if(goalEntered.length() < 7){
                                Toast.makeText(getActivity(), "Please enter a goal", LENGTH_SHORT).show();
                                break;
                            }
                            goalslist.add(goalEntered);
                            goals.put(tasks.get(i), goalslist);
                            taskET.setText("");
                            taskET.setHint("Add Task");
                            listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                            taskslist.setAdapter(listAdapter);
                            taskslist.expandGroup(i);
                            FileHelper.writeData(tasks, getActivity());
                            Toast.makeText(getActivity(), "Goal Added", Toast.LENGTH_SHORT).show();
                            break;
                        }
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
                listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                taskslist.setAdapter(listAdapter);
                taskET.setHint("Add Task");
                taskslist.expandGroup(groupPosition);
                FileHelper.writeData(tasks, getActivity());
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        taskslist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                taskET.setHint("Add Goal");
            }
        });

        taskslist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (goals.get(tasks.get(groupPosition)) == null) {
                    List<String> goalslist = new ArrayList<>();
                    goals.put(tasks.get(groupPosition), goalslist);
                    taskslist.expandGroup(groupPosition);
                    FileHelper.writeData(tasks, getActivity());
                    return true;
                }
                else if (goals.get(tasks.get(groupPosition)).size() == 0) {
                    tasks.remove(groupPosition);
                    taskET.setHint("Add Task");
                    listAdapter = new MyExListAdapter(getActivity(), tasks, goals);
                    taskslist.setAdapter(listAdapter);
                    FileHelper.writeData(tasks, getActivity());
                    Toast.makeText(getActivity(), "Delete", LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        taskslist.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                taskET.setHint("Add Task");
            }
        });
        return root;
    }

}

