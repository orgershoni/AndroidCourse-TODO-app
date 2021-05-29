package exercise.android.reemh.todo_items;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    TodoItemsDataBase dataBase = null;
    String taskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_item_edit);

        dataBase = TodoItemsApp.getInstance().getDataBase();

        EditText itemDescriptionView = findViewById(R.id.edit_desc);
        SwitchCompat inProgressSwitch = findViewById(R.id.in_progress_switch);
        TextView dateCreated = findViewById(R.id.date_created);
        TextView lastModified = findViewById(R.id.last_modified);

        TodoItem item = (TodoItem) getIntent().getSerializableExtra("to_edit");

        itemDescriptionView.setText(item.getDescription());
        inProgressSwitch.setChecked(item.getStatus() == STATUS.DONE);
        dateCreated.setText(new Date(item.getTimeCreated()).toString());
        lastModified.setText(new Date(item.getLastModified()).toString());

        inProgressSwitch.setOnClickListener(v ->
        {
            dataBase.changeStatus(item);
        });

        // set listener on the input written by the keyboard to the edit-text
        itemDescriptionView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void afterTextChanged(Editable s) {
                // text did change
                String newText = itemDescriptionView.getText().toString();
                dataBase.setDescription(item, newText);
                taskDescription = newText;
            }
        });

    }
}
