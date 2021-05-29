package exercise.android.reemh.todo_items;

import androidx.appcompat.app.AppCompatActivity;

public class EditItemActivity extends AppCompatActivity {

    TodoItem item = (TodoItem) getIntent().getSerializableExtra("to_edit");

}
