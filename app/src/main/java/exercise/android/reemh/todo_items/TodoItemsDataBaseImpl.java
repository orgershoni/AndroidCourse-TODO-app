package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static exercise.android.reemh.todo_items.TodoItem.TodoItemFromString;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase {

  Context context;
  SharedPreferences sp;
  private LiveData<List<TodoItem>> liveData;
  private MutableLiveData<List<TodoItem>> mutableLiveData;
  HashMap<String, TodoItem> itemsMap = new HashMap<>();


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
          itemsMap.put(item.getId(), item);
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
    List<TodoItem> items = new ArrayList<>(itemsMap.values());
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
    itemsMap.put(toAdd.getId(), toAdd);

    updateSP(null, toAdd);
    mutableLiveData.setValue(getCurrentItems());
  }

  @Override
  public void markItemDone(TodoItem item) {

    TodoItem oldItem = new TodoItem(item);

    TodoItem itemInDB = itemsMap.get(item.getId());
    if (itemInDB != null)
    {
      itemInDB.setDone();
    }
    updateSP(oldItem, itemInDB);
    mutableLiveData.setValue(getCurrentItems());
  }

  @Override
  public void markItemInProgress(TodoItem item) {

    TodoItem oldItem = new TodoItem(item);
    TodoItem itemInDB = itemsMap.get(item.getId());
    if (itemInDB != null)
    {
      itemInDB.setInProgress();
    }

    updateSP(oldItem, item);
    mutableLiveData.setValue(getCurrentItems());
  }

  @Override
  public void deleteItem(TodoItem item) {
    itemsMap.remove(item.getId());
    updateSP(item, null);
    mutableLiveData.setValue(getCurrentItems());
  }

  @Override
  public void changeStatus(TodoItem item){
    TodoItem oldItem = new TodoItem(item);
    TodoItem itemInDB = itemsMap.get(item.getId());
    if (itemInDB != null)
    {
      if (itemInDB.getStatus() == STATUS.DONE){
        markItemInProgress(itemInDB);
      }
      else {
        markItemDone(itemInDB);
      }
    }

    updateSP(oldItem, itemInDB);
    mutableLiveData.setValue(getCurrentItems());
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
    TodoItem itemInDB = itemsMap.get(item.getId());
    if (itemInDB != null)
    {
      itemInDB.setDescription(newDescription);
    }
    updateSP(oldItem, itemInDB);
    mutableLiveData.setValue(getCurrentItems());
  }


}
