package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static exercise.android.reemh.todo_items.TodoItem.ParseFromString;

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

  /**
   * LiveData getter
   */
  public LiveData<List<TodoItem>> getLiveData() {
    return liveData;
  }

  /**
   * similar implementation to what we saw in TA - fill in itemsMap with data from SP
   */
  private void initializeFromSP()
  {
    if (this.sp != null)
    {
      for (String todoDesc : this.sp.getAll().keySet())
      {
        TodoItem item = ParseFromString(this.sp.getString(todoDesc, ""));
        if (item != null)
        {
          itemsMap.put(item.getId(), item);
        }
      }
    }
  }

  @Override
  public List<TodoItem> getCurrentItems() {
    List<TodoItem> items = new ArrayList<>(itemsMap.values());
    Collections.sort(items);
    return items;
  }

  @Override
  public void addNewInProgressItem(String description) {
    TodoItem toAdd = new TodoItem(description);
    itemsMap.put(toAdd.getId(), toAdd);

    // update SP and notify LiveData
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

    // update SP and notify LiveData
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

    // update SP and notify LiveData
    updateSP(oldItem, item);
    mutableLiveData.setValue(getCurrentItems());
  }

  @Override
  public void deleteItem(TodoItem item) {
    itemsMap.remove(item.getId());

    // update SP and notify LiveData
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

    // update SP and notify LiveData
    updateSP(oldItem, itemInDB);
    mutableLiveData.setValue(getCurrentItems());
  }

  public void setDescription(TodoItem item, String newDescription) {
    TodoItem oldItem = new TodoItem(item);
    TodoItem itemInDB = itemsMap.get(item.getId());
    if (itemInDB != null)
    {
      itemInDB.setDescription(newDescription);
    }

    // update SP and notify LiveData
    updateSP(oldItem, itemInDB);
    mutableLiveData.setValue(getCurrentItems());
  }

  /**
   * Remove old object from SP and inserting a new one instead
   */
  private void updateSP(TodoItem oldItem, TodoItem newItem)
  {
    SharedPreferences.Editor editor = sp.edit();
    if (newItem == null)  // if new item is null it means the item was deleted
    {
      editor.remove(oldItem.getTimeCreated().toString());
      return;
    }

    if (oldItem != null)  // if an old item is null it means newItem is a new item in the DB
    {
      editor.remove(oldItem.getTimeCreated().toString());
    }

    editor.putString(newItem.getTimeCreated().toString(), newItem.toString());
    editor.apply();

  }
}
