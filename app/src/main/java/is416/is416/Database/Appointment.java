package is416.is416.Database;

/**
 * Created by Cheryl on 18/3/2018.
 */

public class Appointment {
    private Integer id;
    private String date;
    private String time;
    private String details;

    public Appointment (String date, String time, String details){
        this(null, date, time, details);
    }

    public Appointment (Integer id, String date, String time, String details){
        this.id = id;
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }


}
