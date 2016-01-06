/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.openide.util.Exceptions;

/**
 *
 * @author S. Shobiga
 */
public class MultipleClassListHandlingDemo extends Thread {

    private HashSet classList;
    private HashSet attributesList;
    private HashSet methodList;
    
    MultipleClassListHandlingGUI demo1;
    HashMap<String, HashSet> classWithAttribute = new HashMap<String, HashSet>();
    HashMap<String, HashSet> classWithMethod = new HashMap<String, HashSet>();

    MultipleClassListHandlingDemo(HashSet classList, HashSet attributesList, HashSet methodList) {
        //setClass(classList, attributesList, methodList);
        this.classList = classList;
        this.attributesList = attributesList;
        this.methodList = methodList;
        setClass(classList, attributesList, methodList);

    }

    public void setClass(HashSet classList, HashSet attributesList, HashSet methodList) {
        try{
             this.run();
             Thread.sleep(1000);
        }
        catch(Exception e){
            e.printStackTrace();
            //this.getCurrentFrame().dispose();
        }
        
       
    }

    public void run() {

        
/*        if (lock == false) {
            System.out.println("1");
            lock = true;
*/            synchronized (this) {
                System.out.println("2");

                try {
                    System.out.println("4");
                    //Thread.sleep(1000);
                    
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            //Turn off metal's use of bold fonts
                            UIManager.put("swing.boldMetal", Boolean.FALSE);
                            
                            demo1 = new MultipleClassListHandlingGUI(classList, attributesList, methodList);
                            classWithAttribute = demo1.getClassWithAttribute();
                            classWithMethod = demo1.getClassWithMethod();
           
                        }
                    });
                    
                    System.out.println("5");
                    
                    //this.join();
                    //Thread.sleep(1000);
                } catch (Exception ex) {
                    
                    System.out.println("6");
                    Exceptions.printStackTrace(ex);
                }

            }
/*
        }
  */  
    }

    public JFrame getCurrentFrame(){
        return demo1.getFrame();
    }
    public HashMap<String, HashSet> getClassWithAttribute(){
        return classWithAttribute;
    }
    
    public HashMap<String, HashSet> getClassWithMethod(){
        return classWithMethod;
    }

/*    public static void main(String args[]) throws InterruptedException {
        // MultipleClassListHandlingGUI demo = new MultipleClassListHandlingGUI();
        final HashSet classList = new HashSet();
        classList.add("application");
        classList.add("Saving account");
        classList.add("Transaction");
        final HashSet attributes = new HashSet();
        attributes.add("balance");
        attributes.add("current amount");
        attributes.add("pin");
        attributes.add("total amount");

        final HashSet method = new HashSet();
        method.add("access");
        method.add("record");
        method.add("validate");

//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                //Turn off metal's use of bold fonts
//                UIManager.put("swing.boldMetal", Boolean.FALSE);
//                MultipleClassListHandlingDemo demo1 = new MultipleClassListHandlingDemo(classList, attributes, method);
//                MultipleClassListHandlingDemo demo2 = new MultipleClassListHandlingDemo(classList, attributes, method);
//                
//            }
//        });
        
        MultipleClassListHandlingDemo newThr;
        try{
            for(int i=0;i<2;i++){
            System.out.println("11");
            newThr = new MultipleClassListHandlingDemo(classList, attributes, method);
            System.out.println("22");
            
            newThr.start();
            Thread.sleep(1000);
            System.out.println("33");
            
            if(i == 2){
                newThr.getCurrentFrame().dispose();
            }
        }
        }
        catch(Exception e){
            
        }
        
        finally{
            
        }
        System.out.println("44");

        //demo.getClassWithAttributes();
    }
*/
//    @Override
//       public HashMap getClassWithAttribute() {
//        return attributeMap;
//    }
//
//    @Override
//    public HashMap getClassWithMethod() {
//        return methodMap;
//    }
}
