package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoItemsDataBaseImpl implements TodoItemsDataBase {

  List<TodoItem> items;

  /**
   * Initialize an empty items holder
   */
  TodoItemsDataBaseImpl(){
    items = new ArrayList<>();
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
    items.add(new TodoItem(description));
  }

  @Override
  public void markItemDone(TodoItem item) {
    item.setDone();
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    item.setInProgress();
  }

  @Override
  public void deleteItem(TodoItem item) {
    items.remove(item);
  }

  @Override
  public void changeStatus(TodoItem item){
    if (item.getStatus() == STATUS.DONE){
      markItemInProgress(item);
    }
    else {
      markItemDone(item);
    }
  }

}
