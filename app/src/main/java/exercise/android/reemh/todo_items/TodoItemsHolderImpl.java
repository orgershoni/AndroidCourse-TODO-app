package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TodoItemsHolderImpl implements TodoItemsHolder {

  HashSet<TodoItem> items;
  Integer counter;

  TodoItemsHolderImpl(){
    items = new HashSet<>();
    counter = 0;
  }

  @Override
  public List<TodoItem> getCurrentItems() {
    List<TodoItem> asList = new ArrayList<>(items);
    Collections.sort(asList);
    return asList;
  }

  @Override
  public void addNewInProgressItem(String description) {
    items.add(new TodoItem(description, counter));
    counter++;
  }

  @Override
  public void markItemDone(TodoItem item) {
    item.setStatus(STATUS.DONE);
  }

  @Override
  public void markItemInProgress(TodoItem item) {
    item.setStatus(STATUS.IN_PROGRESS);
  }

  @Override
  public void deleteItem(TodoItem item) {
    items.remove(item);
  }

  public void changeStatus(TodoItem item){
    item.changeStatus();
  }

}
