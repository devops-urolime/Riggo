package io.riggo.data.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "fiscal_period")
public class FiscalPeriod implements Serializable {
    private static final long serialVersionUID = 7156526077883281623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_dim_id")
    private Integer dateDimId;

    @Column(name = "date_actual")
    private LocalDate dateActual;

    @Column(name = "epoch")
    private Long epoch;

    @Column(name = "day_suffix")
    private String daySuffix;

    @Column(name = "day_name")
    private String dayName;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "day_of_month")
    private Integer dayOfMonth;

    @Column(name = "day_of_quarter")
    private Integer dayOfQuarter;

    @Column(name = "day_of_year")
    private Integer dayOfYear;

    @Column(name = "week_of_month")
    private Integer weekOfMonth;

    @Column(name = "week_of_year")
    private Integer weekOfYear;

    @Column(name = "week_of_year_iso")
    private String weekOfYearIso;

    @Column(name = "month_actual")
    private Integer monthActual;

    @Column(name = "month_name")
    private String monthName;

    @Column(name = "month_name_abbreviated")
    private String monthNameAbbreviated;

    @Column(name = "quarter_actual")
    private Integer quarterActual;

    @Column(name = "quarter_name")
    private String quarterName;

    @Column(name = "year_actual")
    private Integer year_actual;

    @Column(name = "first_day_of_week")
    private LocalDate firstDayOfWeek;

    @Column(name = "last_day_of_week")
    private LocalDate lastDayOfWeek;

    @Column(name = "first_day_of_month")
    private LocalDate firstDayOfMonth;

    @Column(name = "last_day_of_month")
    private LocalDate lastDayOfMonth;

    @Column(name = "first_day_of_quarter")
    private LocalDate firstDayOfQuarter;

    @Column(name = "last_day_of_quarter")
    private LocalDate lastDayOfQuarter;

    @Column(name = "first_day_of_year")
    private LocalDate firstDayOfYear;

    @Column(name = "last_day_of_year")
    private LocalDate lastDayOfYear;

    @Column(name = "mmyyyy")
    private String mmyyyy;

    @Column(name = "mmddyyyy")
    private String mmddyyyy;

    @Column(name = "weekend_indr")
    private Boolean weekendIndr;

    public Integer getDateDimId() {
        return dateDimId;
    }

    public void setDateDimId(Integer dateDimId) {
        this.dateDimId = dateDimId;
    }

    public LocalDate getDateActual() {
        return dateActual;
    }

    public void setDateActual(LocalDate dateActual) {
        this.dateActual = dateActual;
    }

    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }

    public String getDaySuffix() {
        return daySuffix;
    }

    public void setDaySuffix(String daySuffix) {
        this.daySuffix = daySuffix;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer getDayOfQuarter() {
        return dayOfQuarter;
    }

    public void setDayOfQuarter(Integer dayOfQuarter) {
        this.dayOfQuarter = dayOfQuarter;
    }

    public Integer getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(Integer dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public Integer getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(Integer weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public String getWeekOfYearIso() {
        return weekOfYearIso;
    }

    public void setWeekOfYearIso(String weekOfYearIso) {
        this.weekOfYearIso = weekOfYearIso;
    }

    public Integer getMonthActual() {
        return monthActual;
    }

    public void setMonthActual(Integer monthActual) {
        this.monthActual = monthActual;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthNameAbbreviated() {
        return monthNameAbbreviated;
    }

    public void setMonthNameAbbreviated(String monthNameAbbreviated) {
        this.monthNameAbbreviated = monthNameAbbreviated;
    }

    public Integer getQuarterActual() {
        return quarterActual;
    }

    public void setQuarterActual(Integer quarterActual) {
        this.quarterActual = quarterActual;
    }

    public String getQuarterName() {
        return quarterName;
    }

    public void setQuarterName(String quarterName) {
        this.quarterName = quarterName;
    }

    public Integer getYear_actual() {
        return year_actual;
    }

    public void setYear_actual(Integer year_actual) {
        this.year_actual = year_actual;
    }

    public LocalDate getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(LocalDate firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public LocalDate getLastDayOfWeek() {
        return lastDayOfWeek;
    }

    public void setLastDayOfWeek(LocalDate lastDayOfWeek) {
        this.lastDayOfWeek = lastDayOfWeek;
    }

    public LocalDate getFirstDayOfMonth() {
        return firstDayOfMonth;
    }

    public void setFirstDayOfMonth(LocalDate firstDayOfMonth) {
        this.firstDayOfMonth = firstDayOfMonth;
    }

    public LocalDate getLastDayOfMonth() {
        return lastDayOfMonth;
    }

    public void setLastDayOfMonth(LocalDate lastDayOfMonth) {
        this.lastDayOfMonth = lastDayOfMonth;
    }

    public LocalDate getFirstDayOfQuarter() {
        return firstDayOfQuarter;
    }

    public void setFirstDayOfQuarter(LocalDate firstDayOfQuarter) {
        this.firstDayOfQuarter = firstDayOfQuarter;
    }

    public LocalDate getLastDayOfQuarter() {
        return lastDayOfQuarter;
    }

    public void setLastDayOfQuarter(LocalDate lastDayOfQuarter) {
        this.lastDayOfQuarter = lastDayOfQuarter;
    }

    public LocalDate getFirstDayOfYear() {
        return firstDayOfYear;
    }

    public void setFirstDayOfYear(LocalDate firstDayOfYear) {
        this.firstDayOfYear = firstDayOfYear;
    }

    public LocalDate getLastDayOfYear() {
        return lastDayOfYear;
    }

    public void setLastDayOfYear(LocalDate lastDayOfYear) {
        this.lastDayOfYear = lastDayOfYear;
    }

    public String getMmyyyy() {
        return mmyyyy;
    }

    public void setMmyyyy(String mmyyyy) {
        this.mmyyyy = mmyyyy;
    }

    public String getMmddyyyy() {
        return mmddyyyy;
    }

    public void setMmddyyyy(String mmddyyyy) {
        this.mmddyyyy = mmddyyyy;
    }

    public Boolean getWeekendIndr() {
        return weekendIndr;
    }

    public void setWeekendIndr(Boolean weekendIndr) {
        this.weekendIndr = weekendIndr;
    }
}
