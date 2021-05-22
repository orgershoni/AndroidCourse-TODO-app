package exercise.android.reemh.todo_items;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  public TodoItemsDataBase holder = null;

  // keys for bundle
  private static final String DESCRIPTION  = "description";
  private static final String HOLDER  = "holder";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    // Find views
    TextView taskDescView = findViewById(R.id.editTextInsertTask);
    View fabToAddTask = findViewById(R.id.buttonCreateTodoItem);
    RecyclerView recyclerView = findViewById(R.id.recyclerTodoItemsList);


    // Load bundle and set text accordingly
    if (savedInstanceState != null)
    {
      taskDescView.setText(savedInstanceState.getString(DESCRIPTION));
      holder = (TodoItemsDataBase) savedInstanceState.getSerializable(HOLDER);
    }
    else  // if savedInstanceState == null then app has just launched and text should be empty
    {
      taskDescView.setText("");
    }

    // Load holder
    if (holder == null) {
      holder = new TodoItemsDataBaseImpl();
    }

    /// Install an Adapter  ///
    TodoItemAdapter adapter = new TodoItemAdapter();
    // set callbacks
    adapter.onCheckClickCallback = (TodoItem todoItem)->holder.changeStatus(todoItem);
    adapter.onDeleteClickCallback = (TodoItem todoItem)-> holder.deleteItem(todoItem);
    adapter.callbackFromAdapter = () -> adapter.setItems(holder.getCurrentItems());

    // feed adapter
    adapter.setItems(holder.getCurrentItems());


    // Bind recycler view with adapter and layout manager
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    fabToAddTask.setOnClickListener((View v)-> {
        if (!(taskDescView.getText().toString().isEmpty()))
        {
          holder.addNewInProgressItem(taskDescView.getText().toString());
          adapter.setItems(holder.getCurrentItems());
          taskDescView.setText("");
        }
    });

  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    TextView taskDescView = findViewById(R.id.editTextInsertTask);
    outState.putString(DESCRIPTION, taskDescView.getText().toString());
    outState.putSerializable(HOLDER, holder);
  }
}
