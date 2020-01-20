/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicWeb;

/**
 *
 * @author jose
 */

import data.Dao;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import logic.Accord;

public class Scheduled {

    @Schedules({
        @Schedule(dayOfMonth = "*")
        ,
   @Schedule(dayOfWeek = "Fri", minute = "00", hour = "12", persistent = false)
    })
    //@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false) 
// 
    public void atSchedule() throws Exception {
        Calendar limit = Calendar.getInstance();
        limit.set(Calendar.DAY_OF_MONTH, limit.get(Calendar.DAY_OF_MONTH) - 8);
        Date lmt = limit.getTime();
        Date today = Calendar.getInstance().getTime();
        List<Accord> list = Dao.getDao().emailInfo(today, lmt);
        String message = " ";
        for (Accord a : list) {
            message = message + "\n" + a.toString();
        }
        String message1 = "Los acuerdos notificados son los siguientes \n" + message;
        //SendEmail email=new SendEmail("Acuerdos Notificados esta semana", message1, "djenane114@gmail.com");
        //  email.send();
        System.out.println("hola");
    }

    // @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    // public void atSchedule() throws InterruptedException {
    //System.out.println("DeclarativeScheduler:: In atSchedule()");
//}
}
