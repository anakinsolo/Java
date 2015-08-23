/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.Integer.parseInt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import com.opencsv.CSVReader;

/**
 *
 * @author AnhTuan
 */
public class CalculateMonthlyWage {
    private static final String csvFileName = "HourList201403.csv";
    private static final float HOURLY_WAGE = 3.75f;
    private static final float[] OVERTIME_MULTIPLIERS = {1.25f, 1.5f, 2};
    private static final float EVENING_COMPENSATION = 1.15f;
    final static SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    
    private String name;
    private int id;
    private String date;
    private float start;
    private float end;
    private float workingDuration;
    private float totalMonthId1;
    private float totalMonthId2;
    private float totalMonthId3;
    
    
    //get and set function for each attribute
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setId (int id) {
        this.id = id;
    }
    public int getId () {
        return id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }
    public void setStart(float start) {
        this.start = start;
    }
    public float getStart() {
        return start;
    }
    public void setEnd(float end) {
        this.end = end;
    }
    public float getEnd() {
        return end;
    }
    public float getWorkingDuration() {
        return workingDuration;
    }
    public void setWorkingDuration(float workingDuration) {
        this.workingDuration = workingDuration;
    }
    public void setTotalMonthID1(float totalMonth) {
        this.totalMonthId1 = totalMonth;
    }
    public float getTotalMonthID1() {
        return totalMonthId1;
    }
    public void setTotalMonthID2(float totalMonth) {
        this.totalMonthId1 = totalMonth;
    }
    public float getTotalMonthID2() {
        return totalMonthId2;
    }
    public void setTotalMonthID3(float totalMonth) {
        this.totalMonthId1 = totalMonth;
    }
    public float getTotalMonthID3() {
        return totalMonthId3;
    }
    
    //Calculate and print out total monthly wage
    public void CalculateDailyWorkingDurationFromCSV() {
        
        float regularDailyWage, totalDaily,workingM,workingH,eveningCompensation,overTimeCompensation;
        CalculateMonthlyWage monthlyWage;

        try {
            CSVReader csvReader = new CSVReader(new FileReader(csvFileName));
            String[] allLine = csvReader.readNext();
            while((allLine = csvReader.readNext())!= null) {
                monthlyWage = new CalculateMonthlyWage();
                monthlyWage.setId(parseInt(allLine[1]));
                
                DateTime dt1 = new DateTime(format.parse(allLine[3]));
                DateTime dt2 = new DateTime(format.parse(allLine[4]));
                
                if(dt2.getHourOfDay() < dt1.getHourOfDay()) {
                    dt2 = dt2.plusHours(24);
                }
                workingH = Hours.hoursBetween(dt1, dt2).getHours()%24;
                workingM = Minutes.minutesBetween(dt1, dt2).getMinutes()%60;
 
                float workDuration = workingH +workingM/60;
                
                regularDailyWage = RegularDailyWage(workDuration);
                eveningCompensation = EveningCompensation(CheckIfEveningWork(dt1,dt2));
                overTimeCompensation = OverTimeCompensation(CheckIfOverTime(workDuration));
                totalDaily = TotalDailyPay(regularDailyWage,eveningCompensation,overTimeCompensation);

                if (monthlyWage.getId() == 1) {                    
                    totalMonthId1+=totalDaily;
                } else if (monthlyWage.getId() == 2) {                    
                    totalMonthId2+=totalDaily;
                } else if (monthlyWage.getId() == 3) {                    
                    totalMonthId3+=totalDaily;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CalculateMonthlyWage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CalculateMonthlyWage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CalculateMonthlyWage.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Monthly Wage 3/2014");
        System.out.println("1, Janet Java, $"+totalMonthId1);
        System.out.println("2, Scott Scala, $"+totalMonthId2);
        System.out.println("3, Larry Lolcode, $"+totalMonthId3);

    }
    
    public static float RegularDailyWage(float workingDuration) {
        return workingDuration * HOURLY_WAGE;
    }
    
    public static float EveningCompensation(float eveningWorkDuration) {
        float eveningPay = eveningWorkDuration*EVENING_COMPENSATION;
        return eveningPay;
    }
    
    public static float OverTimeCompensation(float overTimeDuration) {
        float overTimeCompensation = 0;
        float temp;
        if (overTimeDuration != 0) {
            if (overTimeDuration <= 2) {
                overTimeCompensation = overTimeDuration*OVERTIME_MULTIPLIERS[0]*HOURLY_WAGE;
            }
            if ((overTimeDuration > 2) && (overTimeDuration <= 4)) {
                temp = overTimeDuration - 2;
                overTimeCompensation = 2*OVERTIME_MULTIPLIERS[0]*HOURLY_WAGE 
                        + temp*OVERTIME_MULTIPLIERS[1]*HOURLY_WAGE;
            }
            if (overTimeDuration > 4) {
                temp = overTimeDuration - 4;
                overTimeCompensation = 2*OVERTIME_MULTIPLIERS[0]*HOURLY_WAGE 
                        + 2*OVERTIME_MULTIPLIERS[1]*HOURLY_WAGE
                        + temp*OVERTIME_MULTIPLIERS[2]*HOURLY_WAGE;
            }
        }
        return overTimeCompensation;
    }
    
    public static float CheckIfEveningWork(DateTime begin, DateTime end) {
        
        float eveningWorkDuration = 0;        
        float a = begin.getHourOfDay();
        float b = end.getHourOfDay(); 

        if ( a > b ) {
            b = b+24;
            
            for (float i=a; i<=b;i++) {
                if ((i>=18) || (i<=6)) {
                    eveningWorkDuration++;
                }
            }
        } else {
            for (float i=a; i<=b;i++) {
                if ((i>=18) || (i<=6)) {
                    eveningWorkDuration++;
                }
            }
        }
        if (eveningWorkDuration != 0) {
            eveningWorkDuration = eveningWorkDuration-1;
        }
        return eveningWorkDuration;
    }
    
    public static float CheckIfOverTime(float workingDuration) {
        float overTimeDuration = 0;
        if (workingDuration > 8) {
            overTimeDuration = workingDuration-8;
        }
        return overTimeDuration;
    }
    
    public static float TotalDailyPay(float regularDaily, float eveningPay, float overTimePay) {
        float totalDailyPay = regularDaily+eveningPay+overTimePay;
        return totalDailyPay;
    }

    
}
