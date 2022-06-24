package edu.tamu.app.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
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

        assertEquals(now, schedule.getScheduledPostingStart(), "Schedule posting start was not as expected!");
        assertEquals(now + 36000, schedule.getScheduledPostingEnd(), 0, "Schedule posting end was not as expected!");
        assertEquals(scheduleData, schedule.getScheduleData(), "Schedule date was not as expected!");
        assertEquals(note, schedule.getScheduler(), "Schedule scheduler was not as expected!");

    }

}
