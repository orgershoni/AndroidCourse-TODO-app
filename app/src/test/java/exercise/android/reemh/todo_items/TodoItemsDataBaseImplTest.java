package exercise.android.reemh.todo_items;

import org.junit.Assert;
import org.junit.Test;

import static exercise.android.reemh.todo_items.STATUS.DONE;
import static exercise.android.reemh.todo_items.STATUS.IN_PROGRESS;

public class TodoItemsDataBaseImplTest {
  @Test
  public void when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
  }


  @Test
  public void when_deletingItem_then_ShouldBeDeleted(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());

    // delete
    holderUnderTest.deleteItem(holderUnderTest.getCurrentItems().get(0));

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_deletingItemNotInHolder_then_ShouldBeDeleted(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());

    TodoItem firstItem =holderUnderTest.getCurrentItems().get(0);

    // delete once
    holderUnderTest.deleteItem(firstItem);
    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // delete twice - nothing should happen
    holderUnderTest.deleteItem(firstItem);

    // verify
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void when_settingItemToDone_then_ItemTypeIsDone(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());

    TodoItem itemToMarkDone = holderUnderTest.getCurrentItems().get(0);
    // delete
    holderUnderTest.markItemDone(itemToMarkDone);

    // verify
    Assert.assertEquals(DONE, itemToMarkDone.getStatus());
  }

  @Test
  public void when_settingItemToInProgress_then_ItemTypeIsInProgress(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());

    TodoItem itemToMarkInProgress = holderUnderTest.getCurrentItems().get(0);
    // delete
    holderUnderTest.markItemInProgress(itemToMarkInProgress);

    // verify
    Assert.assertEquals(IN_PROGRESS, itemToMarkInProgress.getStatus());
  }

  @Test
  public void when_changingItemStatus_then_ItemTypeIsChanged(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());

    TodoItem itemToChangeStatus = holderUnderTest.getCurrentItems().get(0);
    // delete
    holderUnderTest.markItemDone(itemToChangeStatus);

    // verify
    Assert.assertEquals(DONE, itemToChangeStatus.getStatus());

    holderUnderTest.changeStatus(itemToChangeStatus);

    Assert.assertEquals(IN_PROGRESS, itemToChangeStatus.getStatus());
  }

  @Test
  public void when_ItemIsAdded_then_OnTopOfList(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("second-item");
    holderUnderTest.addNewInProgressItem("top-item");

    Assert.assertEquals(holderUnderTest.getCurrentItems().get(0).getDescription(), "top-item");

  }

  @Test
  public void when_ItemIsDone_then_ListOrderIsChanged(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test

    for (int i = 1; i <= 5; i++)
    {
      holderUnderTest.addNewInProgressItem(String.format("item %d", i));
    }

    Assert.assertEquals(holderUnderTest.getCurrentItems().get(0).getDescription(), "item 5");
    holderUnderTest.markItemDone(holderUnderTest.getCurrentItems().get(0));

    for (int i = 0; i < 4; i++)
    {
      Assert.assertEquals(holderUnderTest.getCurrentItems().get(i).getStatus(), IN_PROGRESS);
    }

    Assert.assertEquals(holderUnderTest.getCurrentItems().get(4).getStatus(), DONE);
    Assert.assertEquals(holderUnderTest.getCurrentItems().get(4).getDescription(), "item 5");

  }

  @Test
  public void when_ItemIsDoneAndThenBackToProgress_then_ListOrderReturnsToOrigin(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    for (int i = 1; i <= 5; i++)
    {
      holderUnderTest.addNewInProgressItem(String.format("item %d", i));
    }

    TodoItem firstItem = holderUnderTest.getCurrentItems().get(0);
    Assert.assertEquals(firstItem.getDescription(), "item 5");
    holderUnderTest.markItemDone(firstItem);
    holderUnderTest.changeStatus(firstItem);

    for (int i = 5; i >= 1; i--)
    {
      int j = 5 - i;
      Assert.assertEquals(holderUnderTest.getCurrentItems().get(j).getDescription(),
              String.format("item %d", i));
    }
  }

  @Test
  public void when_SettingInProgressItemToBeInProgress_then_NothingHappens(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    for (int i = 1; i <= 5; i++)
    {
      holderUnderTest.addNewInProgressItem(String.format("item %d", i));
    }

    TodoItem firstItem = holderUnderTest.getCurrentItems().get(0);

    holderUnderTest.markItemInProgress(firstItem);
    STATUS statusAfterFirstTry = firstItem.getStatus();

    holderUnderTest.markItemInProgress(firstItem);
    STATUS statusAfterSecondTry = firstItem.getStatus();

    Assert.assertEquals(statusAfterFirstTry, statusAfterSecondTry);



  }

  @Test
  public void DoneItemsAreOrderedFirstDeleted_FirstInList(){

    // setup
    TodoItemsDataBaseImpl holderUnderTest = new TodoItemsDataBaseImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    for (int i = 1; i <= 5; i++)
    {
      holderUnderTest.addNewInProgressItem(String.format("item %d", i));
    }

    TodoItem firstItem = holderUnderTest.getCurrentItems().get(0);
    TodoItem secondItem = holderUnderTest.getCurrentItems().get(1);
    Assert.assertEquals(firstItem.getDescription(), "item 5");

    holderUnderTest.markItemDone(firstItem);  // should be in position 4

    holderUnderTest.markItemDone(secondItem); // Now firstItem should be is position 3,
    // and secondItem in position 4

    Assert.assertEquals(holderUnderTest.getCurrentItems().get(3).getDescription(), "item 5");
    Assert.assertEquals(holderUnderTest.getCurrentItems().get(4).getDescription(), "item 4");

  }
}