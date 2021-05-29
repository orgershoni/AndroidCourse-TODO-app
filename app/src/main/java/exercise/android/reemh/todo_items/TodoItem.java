package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum STATUS implements Serializable {
    DONE,
    IN_PROGRESS
}
public class TodoItem implements Serializable, Comparable<TodoItem> {

    private STATUS status;
    private String description;
    private final Long timeCreated;
    private Long lastModified;
    private Long doneTime;
    static int addedItemsCount = 0;
    static int doneItemsCount = 0;

    /**
     * Item is initialized with given description and immediately set to IN PROGRESS
     * @param desc - item's description
     */
    TodoItem(String desc){

       setDescription(desc);
       setInProgress();
       this.doneTime = -1L;
       this.timeCreated = System.currentTimeMillis();
       this.lastModified = timeCreated;
    }

    TodoItem(STATUS status, Long timeCreated, Long doneTime, Long lastModified, String desc){

        setDescription(desc);
        this.status = status;
        this.timeCreated = timeCreated;
        this.doneTime = doneTime;
        this.lastModified = lastModified;
    }

    public TodoItem(TodoItem other) {

        this.status = other.getStatus();
        this.timeCreated = other.timeCreated;
        this.doneTime = other.doneTime;
        this.description = other.getDescription();
        this.lastModified = other.lastModified;
    }

    void setDescription(String desc){
        description = desc;
    }

    String getDescription(){
        return description;
    }

    STATUS getStatus(){
        return status;
    }

    void setInProgress(){
        status = STATUS.IN_PROGRESS;
        doneTime = -1L;  // of item is in progress - set it's doneTime to invalid value (-1)
    }

    void setDone(){
        status = STATUS.DONE;
        this.doneTime = System.currentTimeMillis();
    }

    /**
     * Compare two items by unique value of creation time
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof TodoItem))
        {
            return false;
        }
        TodoItem other = (TodoItem) obj;
        return timeCreated.equals(other.timeCreated);
    }

    /**
     * Compare method to implement order settings of todoitems
     * @param other
     * @return
     */
    @Override
    public int compareTo(TodoItem other){

        if (this.status != other.status)            // If 2 instances dont share the same status
                                                    // - in progress is "smaller"
        {
            if (this.status == STATUS.IN_PROGRESS)
            {
                return -1;
            }
            return 1;
        }
        else if (this.status == STATUS.IN_PROGRESS) {   // Else if both in progress compare by creation time
            return (int) (other.timeCreated - this.timeCreated);
        }
        else {          // Else, both items are done - compare by reverse "done" time
            return (int) (this.doneTime - other.doneTime);
        }
    }

    public String toString(){

        return status.name() + "#"
                + timeCreated + "#"
                + doneTime + "#"
                + lastModified + "#"
                + description;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public static TodoItem TodoItemFromString(String todoSerialized){

        Pattern todoPattern = Pattern.compile("(\\w+)#(\\d+)#(-?\\d+)#(\\d+)#(.*)");
        Matcher matcher = todoPattern.matcher(todoSerialized);
        if (matcher.find())
        {
            try{
                STATUS status = STATUS.valueOf(matcher.group(0));
                Long timeCreated = Long.parseLong(matcher.group(1));
                Long timeDone = Long.parseLong(matcher.group(2));
                Long lastModified = Long.parseLong(matcher.group(3));
                String description = matcher.group(4);

                return new TodoItem(status, timeCreated, timeDone, lastModified, description);
            }
            catch (Exception e)
            {
                System.out.println("While parsing" + todoSerialized + "raised exception" + e.getMessage());
                return null;
            }
        }

        return null;
    }
}
