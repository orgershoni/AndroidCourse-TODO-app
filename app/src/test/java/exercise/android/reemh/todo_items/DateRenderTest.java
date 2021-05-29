package exercise.android.reemh.todo_items;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateRenderTest {

  static Long dateToLong(LocalDateTime time){

      return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  @Test
  public void lessThen1HourAgo(){
    // setup

    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime before50Mins = currentTime.minusMinutes(50L);

    String returnFromRender = DateUtils.renderDate(dateToLong(before50Mins));
    Assert.assertEquals("50 minutes ago", returnFromRender);

  }

  @Test
  public void MoreThenOneHourAgo(){
        // setup

      LocalDateTime currentTime = LocalDateTime.now();
      LocalDateTime before50Mins = currentTime.minusHours(5L);

      String returnFromRender = DateUtils.renderDate(dateToLong(before50Mins));
      System.out.println(returnFromRender);
  }


    @Test
    public void MoreThenOneDayAgo(){
        // setup

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime before50Mins = currentTime.minusDays(2L);

        String returnFromRender = DateUtils.renderDate(dateToLong(before50Mins));
        System.out.println(returnFromRender);
    }

}
