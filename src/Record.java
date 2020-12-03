import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Record {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int employeeId;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Record(String employeeId, String projectId, String dateFrom, String dateTo) {
        this.setEmployeeId(employeeId);
        this.setProjectId(projectId);
        this.setDateFrom(dateFrom);
        this.setDateTo(dateTo);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID is null");
        }
        this.employeeId = Integer.parseInt(employeeId);
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID is null");
        }
        this.projectId = Integer.parseInt(projectId);
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = LocalDate.parse(dateFrom, DATE_TIME_FORMATTER);
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        if (dateTo.equals("NULL")) {
            this.dateTo = LocalDate.now();
            return;
        }
        this.dateTo = LocalDate.parse(dateTo, DATE_TIME_FORMATTER);
    }

    public long commonDays(Record record) {
        if (this.dateFrom.isBefore(record.getDateFrom()) && this.dateTo.isAfter(record.getDateTo())) {
            return ChronoUnit.DAYS.between(record.getDateFrom(), record.getDateTo());
        }
        if (this.dateFrom.isAfter(record.getDateFrom()) && this.dateFrom.isBefore(record.getDateTo()) && this.dateTo.isAfter(record.getDateTo())) {
            return ChronoUnit.DAYS.between(this.dateFrom, record.getDateTo());
        }
        if (this.dateFrom.isBefore(record.getDateFrom()) && this.dateTo.isAfter(record.getDateFrom()) && this.dateTo.isBefore(record.getDateTo())) {
            return ChronoUnit.DAYS.between(record.getDateFrom(), this.dateTo);
        }
        if ((this.dateFrom.equals(record.getDateFrom()) && this.dateTo.equals(record.getDateTo())) ||
                (this.dateFrom.isAfter(record.getDateFrom()) && this.dateTo.isBefore(record.getDateTo()))) {
            return ChronoUnit.DAYS.between(this.dateFrom, this.dateTo);
        }
        return -1;
    }
}