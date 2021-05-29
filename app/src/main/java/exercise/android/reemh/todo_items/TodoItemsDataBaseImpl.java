package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static exercise.android.reemh.todo_items.TodoItem.TodoItemFromString;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase {

  List<TodoItem> items = new ArrayList<>();
  Context context;
  SharedPreferences sp;
  private LiveData<List<TodoItem>> liveData;
  private MutableLiveData<List<TodoItem>> mutableLiveData;


  /**
   * Initialize an empty items holder
   */
  TodoItemsDataBaseImpl(Context context) {
    this.context = context;
    this.sp = context.getSharedPreferences("todo_items_db", Context.MODE_PRIVATE);

    this.mutableLiveData = new MutableLiveData<>();
    this.liveData = this.mutableLiveData;

    initializeFromSP();

  }

  public LiveData<List<TodoItem>> getLiveData() {
    return liveData;
  }

  private void initializeFromSP()
  {
    if (this.sp != null)
    {
      for (String todoDesc : this.sp.getAll().keySet())
      {
        TodoItem item = TodoItemFromString(this.sp.getString(todoDesc, ""));
        if (item != null)
        {
          items.add(item);
        }
      }
    }
  }

  /**
   * Return the current items sorted by required order
   * @return
   */
  @Override
  public List<TodoItem> getCurrentItems() {
    Collections.sort(items);
    return items;
  }

  /**
   * Add a new item with given description
   * @param description
   */
  @Override
  public void addNewInProgressItem(String description) {
    TodoItem toAdd = new TodoItem(description);
    items.add(toAdd);

    updateSP(null, toAdd);
    mutableLiveData.setValue(items);
  }

  @Override
  public void markItemDone(TodoItem item) {

    TodoItem oldItem = new TodoItem(item);
    item.setDone();

    updateSP(oldItem, item);
    mutableLiveData.setValue(items);
  }

  @Override
  public void markItemInProgress(TodoItem item) {

    TodoItem oldItem = new TodoItem(item);
    item.setInProgress();

    updateSP(oldItem, item);
    mutableLiveData.setValue(items);
  }

  @Override
  public void deleteItem(TodoItem item) {
    items.remove(item);
    updateSP(item, null);
    mutableLiveData.setValue(items);
  }

  @Override
  public void changeStatus(TodoItem item){
    TodoItem oldItem = new TodoItem(item);
    if (item.getStatus() == STATUS.DONE){
      markItemInProgress(item);
    }
    else {
      markItemDone(item);
    }

    updateSP(oldItem, item);
    mutableLiveData.setValue(items);
  }

  private void updateSP(TodoItem oldItem, TodoItem newItem)
  {
    SharedPreferences.Editor editor = sp.edit();
    if (newItem == null)
    {
      editor.remove(oldItem.getTimeCreated().toString());
      return;
    }

    if (oldItem != null)
    {
      editor.remove(oldItem.getTimeCreated().toString());
    }
    editor.putString(newItem.getTimeCreated().toString(), newItem.toString());
    editor.apply();

  }

  public void setDescription(TodoItem item, String newDescription) {
    TodoItem oldItem = new TodoItem(item);
    int idx = items.indexOf(item);
    if (idx >= 0)
    {
      items.get(idx).setDescription(newDescription);
    }
    updateSP(oldItem, item);
  }


}
