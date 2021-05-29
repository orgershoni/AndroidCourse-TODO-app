package exercise.android.reemh.todo_items;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {

    TodoItemsDataBase dataBase = null;
    String taskDescription;
    TodoItem item;

    TextInputLayout itemDescriptionView;
    SwitchCompat inProgressSwitch;
    TextInputLayout dateCreated;
    TextInputLayout lastModified;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.todo_item_edit);
        dataBase = TodoItemsApp.getInstance().getDataBase();

        itemDescriptionView = findViewById(R.id.edit_desc);
        inProgressSwitch = findViewById(R.id.in_progress_switch);
        dateCreated = findViewById(R.id.date_created);
        lastModified = findViewById(R.id.last_modified);
        item = (TodoItem) getIntent().getSerializableExtra("to_edit");


        if (savedInstanceState != null)
        {
            item = (TodoItem) savedInstanceState.getSerializable("item");
            taskDescription = savedInstanceState.getString("task_description");
            itemDescriptionView.getEditText().setText(taskDescription);
        }
        else
        {
            itemDescriptionView.getEditText().setText(item.getDescription());
        }

        inProgressSwitch.setChecked(item.getStatus() == STATUS.DONE);
        renderDates();
        dateCreated.setEnabled(false);
        lastModified.setEnabled(false);

        inProgressSwitch.setOnClickListener(v -> dataBase.changeStatus(item));


        // set listener on the input written by the keyboard to the edit-text
        itemDescriptionView.getEditText().addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            public void afterTextChanged(Editable s) {
                // text did change
                String newText = itemDescriptionView.getEditText().getText().toString();
                dataBase.setDescription(item, newText);
                taskDescription = newText;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String newText = itemDescriptionView.getEditText().getText().toString();
        dataBase.setDescription(item, newText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        taskDescription = itemDescriptionView.getEditText().getText().toString();
        outState.putString("task_description", taskDescription);
        outState.putSerializable("item", item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String renderDate(Long dateAsLong){
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime date = millisToLocalDateTime(dateAsLong);
        long minutesPassed = date.until(currentTime, ChronoUnit.MINUTES);
        if (minutesPassed < 60L)
        {
            if (minutesPassed == 0L) {minutesPassed++;}
            return  minutesPassed + " minutes ago";
        }
        else if (currentTime.getDayOfYear() == date.getDayOfYear() && currentTime.getYear() == date.getYear())
        {
            return "Today at " + date.toLocalTime().toString();
        }
        else
        {
            return date.toLocalDate().toString() + " at " + date.toLocalTime().toString();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderDates()
    {
        dateCreated.getEditText().setText(renderDate(item.getTimeCreated()));
        lastModified.getEditText().setText(renderDate(item.getLastModified()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime millisToLocalDateTime(Long millis){
        Instant instant = Instant.ofEpochMilli(millis);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}
