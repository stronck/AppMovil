package com.stronck.appmovil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/** Pantalla principal: permite agregar tareas y marcarlas como completadas. */
public class MainActivity extends Activity {
    private final List<Task> tasks = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private EditText taskInput;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.taskInput);
        Button addButton = findViewById(R.id.addButton);
        ListView taskList = findViewById(R.id.taskList);
        emptyMessage = findViewById(R.id.emptyMessage);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        taskList.setAdapter(adapter);
        taskList.setEmptyView(emptyMessage);

        addButton.setOnClickListener(v -> addTask());
        taskList.setOnItemClickListener((parent, view, position, id) -> toggleTask(position));
        taskList.setOnItemLongClickListener((parent, view, position, id) -> {
            tasks.remove(position);
            refreshTasks();
            Toast.makeText(this, R.string.task_deleted, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void addTask() {
        String title = taskInput.getText().toString();
        if (!TaskValidator.isValidTitle(title)) {
            taskInput.setError(getString(R.string.title_required));
            return;
        }
        tasks.add(new Task(title));
        taskInput.setText("");
        refreshTasks();
    }

    private void toggleTask(int position) {
        Task task = tasks.get(position);
        task.setCompleted(!task.isCompleted());
        refreshTasks();
    }

    private void refreshTasks() {
        List<String> labels = new ArrayList<>();
        for (Task task : tasks) {
            labels.add((task.isCompleted() ? "✓ " : "○ ") + task.getTitle());
        }
        adapter.clear();
        adapter.addAll(labels);
        adapter.notifyDataSetChanged();
        emptyMessage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
