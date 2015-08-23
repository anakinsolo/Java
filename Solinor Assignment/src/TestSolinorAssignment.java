/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import javax.swing.JLabel;

/**
 *
 * @author AnhTuan
 */
public class TestSolinorAssignment {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //try {
        // TODO code application logic here
        
        //create an object and execute the function to calculate monthly wage
        CalculateMonthlyWage monthlyWage = new CalculateMonthlyWage();
        monthlyWage.CalculateDailyWorkingDurationFromCSV();
        
        //round the result to 2 decimal
        BigDecimal result1 = round(monthlyWage.getTotalMonthID1(),2);
        BigDecimal result2 = round(monthlyWage.getTotalMonthID2(),2);
        BigDecimal result3 = round(monthlyWage.getTotalMonthID3(),2);
        System.out.println("1, Janet Java, $"+result1);
        //create Java swing window
        Frame f = new Frame();
        JLabel mLabel = new JLabel();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        mLabel.setText(convertToMultiline("Monthly Wage 3/2014"
                +"\n1, Janet Java, $"+result1.toString()
                +"\n2, Scott Scala, $"+result2.toString()
                +"\n3, Larry Lolcode, $"+result3.toString()));
        f.add(mLabel);
        f.setSize(500, 500);
        f.setVisible(true);
    }    
    public static String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>");
    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }
}
