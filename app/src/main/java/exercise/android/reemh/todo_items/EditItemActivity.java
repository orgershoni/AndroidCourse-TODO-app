package exercise.android.reemh.todo_items;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Edit item activity
 */
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
        dataBase = TodoItemsApp.getInstance().getDataBase(); // access shared DB

        // Find views & get item from intent
        itemDescriptionView = findViewById(R.id.edit_desc);
        inProgressSwitch = findViewById(R.id.in_progress_switch);
        dateCreated = findViewById(R.id.date_created);
        lastModified = findViewById(R.id.last_modified);
        item = (TodoItem) getIntent().getSerializableExtra("to_edit");


        // restore from bundle if necessary
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

        // Render view according to item's information
        inProgressSwitch.setChecked(item.getStatus() == STATUS.DONE);
        renderDates();
        dateCreated.setEnabled(false);
        lastModified.setEnabled(false);

        inProgressSwitch.setOnClickListener(v -> dataBase.changeStatus(item));
    }

    /**
     * Go to previous screen and update item's description if activity's description is not empty.
     * Else - raise a informative toast.
     */
    @Override
    public void onBackPressed() {
        String newText = itemDescriptionView.getEditText().getText().toString();
        if (newText.isEmpty())
        {
            String toastText = "Todo item description can't be empty";
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
        }
        else
        {
            super.onBackPressed();
            // save new description to DB
            dataBase.setDescription(item, newText);
        }
    }

    /**
     * Save item data + editable description data to DB
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        taskDescription = itemDescriptionView.getEditText().getText().toString();
        outState.putString("task_description", taskDescription);
        outState.putSerializable("item", item);
    }

    /**
     * Render creation date + last modified dates
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void renderDates()
    {
        dateCreated.getEditText().setText(DateUtils.renderDate(item.getTimeCreated()));
        lastModified.getEditText().setText(DateUtils.renderDate(item.getLastModified()));
    }



}

class DateUtils {
    /**
     * Given a date represented as long - return a string that describes the date
     * @param dateAsLong
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String renderDate(Long dateAsLong) {

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime dateToProcess = millisToLocalDateTime(dateAsLong);

        long minutesPassed = dateToProcess.until(currentTime, ChronoUnit.MINUTES);
        if (minutesPassed < 60L) {
            return minutesPassed + " minutes ago";

        } else if (currentTime.getDayOfYear() == dateToProcess.getDayOfYear() &&
                   currentTime.getYear() == dateToProcess.getYear()) {
            return "Today at " + getLocalTimeFormatted(dateToProcess);

        } else {
            return getLocalDateFormatted(dateToProcess) + " at " + getLocalTimeFormatted(dateToProcess);
        }
    }

    /**
     * Return given date's time in a formatted string
     * @param dateTime
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    static String getLocalTimeFormatted(LocalDateTime dateTime)
    {
        return dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * Return given date's date (not time) in a formatted string
     * @param dateTime
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    static String getLocalDateFormatted(LocalDateTime dateTime)
    {
        return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Convert milliseconds to LocalDateTime object
     * @param millis
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDateTime millisToLocalDateTime(Long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
