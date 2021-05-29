package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    /**
     * Item is initialized with given description and immediately set to IN PROGRESS
     * @param desc - item's description
     */
    TodoItem(String desc){

       setDescription(desc);
       setInProgress();
       this.doneTime = -1L;
       this.timeCreated = System.currentTimeMillis();
       // lastModified value is being updated in the setter functions
    }

    /**
     * Raw constructor (for deserializing)
     */
    TodoItem(STATUS status, Long timeCreated, Long doneTime, Long lastModified, String desc){

        this.description = desc;
        this.status = status;
        this.timeCreated = timeCreated;
        this.doneTime = doneTime;
        this.lastModified = lastModified;
    }

    /**
     * Copy constructor
     * @param other
     */
    public TodoItem(TodoItem other) {

        this.status = other.getStatus();
        this.timeCreated = other.getTimeCreated();
        this.doneTime = other.getDoneTime();
        this.description = other.getDescription();
        this.lastModified = other.getLastModified();
    }


    /**
     * getter function for item's description
     * @return
     */
    String getDescription(){
        return description;
    }

    /**
     * getter function for item's last modified value
     * @return
     */
    public Long getLastModified() {
        return lastModified;
    }

    /**
     * getter function for item's status
     * @return
     */
    STATUS getStatus(){
        return status;
    }

    /**
     * Item's unique ID can be it's creation time
     * @return
     */
    public String getId()
    {
        return timeCreated.toString();
    }

    /**
     * getter function for item's timeCreated
     */
    public Long getTimeCreated() {
        return timeCreated;
    }

    /**
     * getter function for item's doneTime
     */
    public Long getDoneTime() {
        return doneTime;
    }

    /**
     * Set item to be in status IN PROGRESS
     */
    void setInProgress(){
        if (status != null && !status.equals(STATUS.IN_PROGRESS))
        {
            // checks that status was indeed modified
            updateLastModified();
        }
        status = STATUS.IN_PROGRESS;
        doneTime = -1L;  // of item is in progress - set it's doneTime to invalid value (-1)
        updateLastModified();
    }

    void setDone(){
        if (status != null && !status.equals(STATUS.DONE))
        {
            // checks that status was indeed modified
            updateLastModified();
        }
        status = STATUS.DONE;
        this.doneTime = System.currentTimeMillis();
    }

    /**
     * Set item's description
     * @param desc
     */
    void setDescription(String desc){
        if (description != null && !description.equals(desc))
        {
            // checks that description was indeed modified
            updateLastModified();
        }
        description = desc;
    }

    /**
     * Update item's last modified time
     */
    private void updateLastModified(){
        lastModified = System.currentTimeMillis();
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


    /**
     * Serialize item's data members to a string object, fields are separated by #
     */
    public String toString(){

        return status.name() + "#"
                + timeCreated + "#"
                + doneTime + "#"
                + lastModified + "#"
                + description;
    }

    /**
     * Parse a TodoItem from a string (reverse .toString() method)
     */
    public static TodoItem ParseFromString(String todoSerialized){

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
                System.out.println("While parsing " + todoSerialized + " raised exception: " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
