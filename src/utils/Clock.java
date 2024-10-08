/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author admin
 */
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Clock extends Thread {

    private javax.swing.JLabel lbl;

    // Constructor để nhận tham số truyền vào
    public Clock(javax.swing.JLabel lbl) {
        this.lbl = lbl;
    }

    @Override
    public void run() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime currentTime = LocalTime.now();
            String formattedTime = currentTime.format(formatter);
            
            lbl.setText(formattedTime);
    }
}
