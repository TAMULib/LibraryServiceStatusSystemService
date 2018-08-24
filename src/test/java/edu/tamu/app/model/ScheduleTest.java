package edu.tamu.app.model;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ScheduleTest {

    @Test
    public void testNewSchedule() {
        Calendar calendar = Calendar.getInstance();
        Long now = calendar.getTimeInMillis();

        Map<String, String> scheduleData = new HashMap<String, String>();
        scheduleData.put("test", "This is only a test!");

        User author = new User("123456789");
        Note note = new Note("Test", author);

        Schedule schedule = new Schedule();

        schedule.setScheduledPostingStart(now);
        schedule.setScheduledPostingEnd(now + 36000);
        schedule.setScheduleData(scheduleData);
        schedule.setScheduler(note);

        assertEquals("Schedule posting start was not as expected!", now, schedule.getScheduledPostingStart());
        assertEquals("Schedule posting end was not as expected!", now + 36000, schedule.getScheduledPostingEnd(), 0);
        assertEquals("Schedule date was not as expected!", scheduleData, schedule.getScheduleData());
        assertEquals("Schedule scheduler was not as expected!", note, schedule.getScheduler());

    }

}
