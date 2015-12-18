/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.progress.progressbar;

import com.project.traceability.staticdata.StaticData;

/**
 *
 * @author shiyam
 */
public class Exection {
    
    public void execute(){
       
       

        // Print some stuff
        System.out.print("hello ");
        System.out.println(5);
        System.out.flush();

        System.err.println("Some error");
        System.err.flush();

        // Restore System.out / System.err
        System.setOut(StaticData.lpOut.underlying);
        System.setErr(StaticData.lpEror.underlying);

        // Print the logged output
        System.out.println("----- Log for System.out: -----\n" + StaticData.lpOut.buf);
        System.out.println("----- Log for System.err: -----\n" + StaticData.lpEror.buf);
    }
}
