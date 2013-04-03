
package timetable;


import java.io.*;

import java.io.File;

import java.io.IOException;

import java.util.Date;

import jxl.*;

import jxl.read.biff.BiffException;

interface Constants{
    public static final int TMAX = 10;
    public static final int SMAX = 9;
    public static final int CMAX = 2;
    public static final int LMAX = 3;
}

class Classes{
    
    Integer Id;
    String Sem = new String();
    String Dept = new String();    
    String Div = new String();
    Integer Alt[][] = new Integer[5][7];
    Integer LabAssigned[] = new Integer[5];
    
    public Classes(Integer Id, String Dept, String Sem, String Div){
        this.Id = Id;
        this.Dept = Dept;
        this.Sem = Sem;
        this.Div = Div;
    }
    
       
    public boolean isDayFull(int Day){
        boolean Flag = true;
        for(int Period=0;Period<7;Period++){
            if(Alt[Day][Period]==null)
                Flag=false;
         
        }
        
        return Flag;
            
    }
    
    public boolean ConsecutiveCheck(Integer Day, Integer Period, Integer CurrentSubId){
        
        if(Period!=1&&Period!=6){
            
            if(CurrentSubId==Alt[Day][Period+1])
                return false;
                   
        }
        
        if(Period!=0&&Period!=2){
            
            if(CurrentSubId==Alt[Day][Period-1])
                return false;
              
        }
        
        return true;
     
    }
    
    public boolean FirstPeriodCheck(Integer SubId){
        
        Integer Day;
        Integer Count=0;
        for(Day=0;Day<5;Day++){
            if(Alt[Day][0]==SubId)
                Count++;
        }
        
        if(Count>=1)
            return false;
        else
            return true;
        
    }
     
    
}


class Teachers{
    
    Integer Id;
    String Name = new String();
    Integer SCId[][] = new Integer[5][5];
    Integer Pref[][] = new Integer[5][7];
    Integer ClsAlt[][] = new Integer[5][7];
    
    
    public Teachers(Integer Id, String Name, Integer S1,Integer C01, Integer C02){
        
        this.Id = Id;
        this.Name = Name;
        SCId[0][0] = S1;
        SCId[0][1] = C01;
        SCId[0][2] = C02;
        
    }
    
    public Teachers(){
        
        this.Id = null;
        this.Name = null;
        SCId[0][0] = null;
        SCId[0][1] = null;
        SCId[0][2] = null;
        
    }        
    
    public void Input(){
        
    }
    

}

class Subjects{   
       
    Integer Id;
    String Name;
    Integer HrsPerWeek;
    Integer Duration;
    
    public Subjects(Integer Id, String Name, Integer HrsPerWeek, Integer Duration){
        
        this.Id = Id;
        this.Name = Name;
        this.HrsPerWeek = HrsPerWeek;
        this.Duration = Duration;
        
    }
    
}


class Labs{
    
    Integer Id;
    String Name;
    Integer SId[] = new Integer[5];
    Integer Alt[][] = new Integer[5][7];
        
    public Labs(Integer Id, String Name, Integer S0,Integer S1, Integer S2, Integer S3, Integer S4){
        this.Id = Id;
        this.Name = Name;
        this.SId[0] = S0;
        this.SId[1] = S1;
        this.SId[2] = S2;
        this.SId[3] = S3;        
        this.SId[4] = S4;
        
    }
    
}

class TimeTableGen{
    
    public static Classes Class[] = new Classes[Constants.CMAX];
    public static Teachers Teacher[] = new Teachers[Constants.TMAX];
    public static Subjects Subject[] = new Subjects[Constants.SMAX];
    public static Labs Lab[] = new Labs[Constants.LMAX];
    public static Integer TotalUnallotted = 0;
    
    public static void Create(){
        
       
        
        Class[0] = new Classes(0,"CS","S6","A");
        Class[1] = new Classes(1,"CS","S6","B");
        
             
        Teacher[0] = new Teachers(0,"Mathew",0,0,1);
        Teacher[1] = new Teachers(1,"MPS",2,1,null);
        Teacher[2] = new Teachers(2,"Sminu",1,0,1);
        Teacher[3] = new Teachers(3,"Anita",2,0,null);
        Teacher[4] = new Teachers(4,"Vinod",3,0,1);
        Teacher[5] = new Teachers(5,"Mintu",4,1,null);
        Teacher[6] = new Teachers(6,"Anna",4,0,null);
        Teacher[7] = new Teachers(7,"Elizabeth",5,0,1);
        Teacher[8] = new Teachers(8,"Diana",6,0,1);
        Teacher[9] = new Teachers(9,"Abitha",7,0,1);
                
        
        Subject[0] = new Subjects(0,"SE",4,1);
        Subject[1] = new Subjects(1,"AAD",5,1);
        Subject[2] = new Subjects(2,"Mini",1,3);
        Subject[3] = new Subjects(3,"CN",5,1);
        Subject[4] = new Subjects(4,"SS",1,3);
        Subject[5] = new Subjects(5,"PC",5,1);
        Subject[6] = new Subjects(6,"PMQA",4,1);
        Subject[7] = new Subjects(7,"NC",5,1);
        Subject[8] = new Subjects(8,"SEP",1,1);
        
        Lab[0] = new Labs(0,"Photon",2,null,null,null,null);
        Lab[1] = new Labs(1,"Heisenberg",4,null,null,null,null);
        Lab[2] = new Labs(2,"Quantum",2,null,null,null,null);
    }
    
   /* public static void ExcelInput(){
        
        TeachersInput();
        ClassesInput();
        LabsInput();
        SubjectsInput();
        
    }
    
    public static void ClassesInput() throws IOException, BiffException {
        
        Workbook CWorkbook = Workbook.getWorkbook(new File("C:\\Documents and Settings\\Home\\Desktop\\TimeTable\\TimeTable\\Excel Input Files\\Classes.xls"));
        Sheet CSheet = CWorkbook.getSheet(0); 
        
        
        
    }*/
    
    public static void ClassAllotment(){
        
        Integer TCounter,SCounter,CCounter,CurrentSubId,SubHrs,AltHrs,Day,DayRnd,PeriodRnd,CurrentClassId,MaxDayTry,MaxSubTry,MaxWeekTry;
        Teachers CurrentTeacher = new Teachers();
        
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){//To traverse through Teacher Objects
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            
            //System.out.println("Zero");
            
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){//To traverse through the Subjects of the Teacher
                
                if(Subject[CurrentTeacher.SCId[SCounter][0]].Duration==3){//If it is a Lab Session
                    
                    continue;                    
                }                   
                        
                CurrentSubId=CurrentTeacher.SCId[SCounter][0];
                SubHrs=Subject[CurrentSubId].HrsPerWeek;
                //System.out.println(SubHrs);
                
                                
                for(CCounter=1;CCounter<5;CCounter++){//To traverse throught the Classes of the Teacher
                    
                    if(CurrentTeacher.SCId[SCounter][CCounter]==null)
                        continue;
                            
                    AltHrs=0;
                    CurrentClassId=CurrentTeacher.SCId[SCounter][CCounter];
                    
                    //System.out.println(CurrentClassId);
                    
                    MaxSubTry = 0;
                    
                    while((AltHrs<SubHrs)&&(MaxSubTry<3)){
                        
                                              
                        //System.out.println("One");
                                                   
                                                
                        PeriodRnd=(int)(Math.random()*7);
                        DayRnd = (int)(Math.random()*5);
                        
                        MaxWeekTry=0;
                        
                        while((AltHrs<SubHrs)&&(MaxWeekTry<20)){
                            
                            //System.out.println("Two");
                            MaxDayTry=0;
                            
                            while((Class[CurrentClassId].isDayFull(DayRnd)==false)&&(MaxDayTry<30)){

                                if((Class[CurrentClassId].Alt[DayRnd][PeriodRnd]==null)&&(CurrentTeacher.ClsAlt[DayRnd][PeriodRnd]==null)&&(Class[CurrentClassId].ConsecutiveCheck(DayRnd,PeriodRnd,CurrentSubId))&&(Class[CurrentClassId].FirstPeriodCheck(CurrentSubId))){
                                    Class[CurrentClassId].Alt[DayRnd][PeriodRnd]=CurrentSubId;
                                    CurrentTeacher.ClsAlt[DayRnd][PeriodRnd]=CurrentClassId;
                                    AltHrs++;
                                    PeriodRnd=(int)(Math.random()*7);
                                    
                                    //System.out.println("Three");
                                    
                                    break;
                                }
                                else{
                                    
                                    PeriodRnd=(int)(Math.random()*7);
                                    
                                }
                                
                                //System.out.println("Max Day Try: " + MaxDayTry);
                                MaxDayTry++;

                            }
                            
                            //System.out.println("Max Week Try: " + MaxWeekTry);
                            MaxWeekTry++;
                            DayRnd = (int)(Math.random()*5);
                            
                        }
                        
                        //System.out.println("Max Week Try: " + MaxSubTry);
                        MaxSubTry++;
                        
                        
                       
                    }
                    
                     
                    
                }
            }
        }
        
            
            
        
    } 
    
    
     
    public static void LabAllotment(){
        
        Integer TCounter,SCounter,CurrentSubId,LCounter;
        boolean Flag;
        Teachers CurrentTeacher = new Teachers();
        
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){//To traverse through Teacher Objects
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            //System.out.println("One");
                        
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){
                
                //System.out.println("Two");
                CurrentSubId=CurrentTeacher.SCId[SCounter][0];
                Flag=false;
                
                if(Subject[CurrentTeacher.SCId[SCounter][0]].Duration==3){
                    
                    for(LCounter=0;LCounter<Constants.LMAX;LCounter++){
                        
                        //System.out.println("LCounter: " + LCounter);
                        Flag=LabAssign(LCounter,CurrentTeacher.Id,CurrentSubId,CurrentTeacher.SCId[SCounter][1]);
                        //System.out.println("Flag: " +Flag);
                        if(Flag==true)
                            break;
                        
                    }
                    
                    
                }
                
            }
        }
    }
   
    
    public static boolean LabAssign(Integer LCounter,Integer TeacherId,Integer SubId, Integer ClassId){
        
        Integer SCounter,Day;
        boolean Flag=false;
        
        //System.out.println("Variables: " + LCounter + TeacherId + SubId + ClassId);
        for(SCounter=0;(SCounter<5)&&(Lab[LCounter].SId[SCounter]!=null);SCounter++){
            
            Day=(int)(Math.random()*5);
                        
            if(Lab[LCounter].SId[SCounter]==SubId){
                
                while(Flag==false){
                    
                    if((Lab[LCounter].Alt[Day][4]==null)&&(Lab[LCounter].Alt[Day][5]==null)&&(Lab[LCounter].Alt[Day][6]==null)&&(Teacher[TeacherId].ClsAlt[Day][4]==null)&&(Teacher[TeacherId].ClsAlt[Day][5]==null)&&(Teacher[TeacherId].ClsAlt[Day][6]==null)&&(Class[ClassId].Alt[Day][4]==null)&&(Class[ClassId].Alt[Day][5]==null)&&(Class[ClassId].Alt[Day][6]==null)&&(Class[ClassId].LabAssigned[Day]==null)){
                        
                        //System.out.println("One");
                        
                        Lab[LCounter].Alt[Day][4]=ClassId;
                        Lab[LCounter].Alt[Day][5]=ClassId;
                        Lab[LCounter].Alt[Day][6]=ClassId;
                        
                        Teacher[TeacherId].ClsAlt[Day][4]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][5]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][6]=ClassId;
                        
                        Class[ClassId].Alt[Day][4]=SubId;
                        Class[ClassId].Alt[Day][5]=SubId;
                        Class[ClassId].Alt[Day][6]=SubId;
                        
                        Class[ClassId].LabAssigned[Day] = 1;
                        
                        Flag=true;
                        
                        
                                
                    }
                    
                    else if((Lab[LCounter].Alt[Day][1]==null)&&(Lab[LCounter].Alt[Day][2]==null)&&(Lab[LCounter].Alt[Day][3]==null)&&(Teacher[TeacherId].ClsAlt[Day][1]==null)&&(Teacher[TeacherId].ClsAlt[Day][2]==null)&&(Teacher[TeacherId].ClsAlt[Day][3]==null)&&(Class[ClassId].Alt[Day][1]==null)&&(Class[ClassId].Alt[Day][2]==null)&&(Class[ClassId].Alt[Day][3]==null)&&(Class[ClassId].LabAssigned[Day]==null)){
                        
                        //System.out.println("Two");
                        
                        Lab[LCounter].Alt[Day][1]=ClassId;
                        Lab[LCounter].Alt[Day][2]=ClassId;
                        Lab[LCounter].Alt[Day][3]=ClassId;
                        
                        Teacher[TeacherId].ClsAlt[Day][1]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][2]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][3]=ClassId;
                        
                        Class[ClassId].Alt[Day][1]=SubId;
                        Class[ClassId].Alt[Day][2]=SubId;
                        Class[ClassId].Alt[Day][3]=SubId;
                        
                        Class[ClassId].LabAssigned[Day] = 1;
                        
                        Flag=true;
                        
                        
                                
                    }
                    
                    else
                        Day=(int)(Math.random()*5);
                }
                
                                                                           
            }
        }
        
        return Flag;
        
    }
    
    public static void SepAssign(){
        
        Integer CCounter;
        
        for(CCounter=0;CCounter<Constants.CMAX;CCounter++){
                       
            if(Class[CCounter].Dept.equals("CS"))
                Class[CCounter].Alt[0][6]=8;
            if(Class[CCounter].Dept.equals("IT"))
                Class[CCounter].Alt[1][6]=8;
            if(Class[CCounter].Dept.equals("EEE"))
                Class[CCounter].Alt[2][6]=8;
            if(Class[CCounter].Dept.equals("AEI"))
                Class[CCounter].Alt[3][6]=8;
            if(Class[CCounter].Dept.equals("EC"))
                Class[CCounter].Alt[4][6]=8;
                     
            
        } 
    }
    
    
    
    public static void Unallotted(){
        Integer CCounter,NoUnallotted,Day,Period;
        for(CCounter=0;CCounter<Constants.CMAX;CCounter++){
            
            NoUnallotted=0;
            
            for(Day=0;Day<5;Day++){
                
                for(Period=0;Period<7;Period++){
                    
                    if(Class[CCounter].Alt[Day][Period]==null)
                        NoUnallotted++;
                }
            }
            TotalUnallotted += NoUnallotted;
            //System.out.println("No of Unassigned Slots in Class " + Class[CCounter].Dept + Class[CCounter].Sem +Class[CCounter].Div + " is " + NoUnassigned);
        }
    }
    
    public static void Display(){
        
      ClassDisplay();
      //TeacherDisplay();
      //LabDisplay();
      
            
        
    }
    
    public static void ClearTables(){
        
        Integer ObCounter;
        Integer Day,Period;
        
        //Clear the Class Allotments in Classes class
               
        for(ObCounter=0;ObCounter<Constants.CMAX;ObCounter++){
            for(Day=0;Day<5;Day++)
            {
                for(Period=0;Period<7;Period++)
                {
                    Class[ObCounter].Alt[Day][Period]=null;
                }
            }
        }
        
        //Clear the Teacher Allotments in Teachers class
        
        for(ObCounter=0;ObCounter<Constants.TMAX;ObCounter++){
            for(Day=0;Day<5;Day++)
            {
                for(Period=0;Period<7;Period++)
                {
                    Teacher[ObCounter].ClsAlt[Day][Period]=null;
                }
            }
        }
        
        //Clear the Lab Allotments in Labs class
        
        for(ObCounter=0;ObCounter<Constants.LMAX;ObCounter++){
            for(Day=0;Day<5;Day++)
            {
                for(Period=0;Period<7;Period++)
                {
                    Lab[ObCounter].Alt[Day][Period]=null;
                }
            }
        }
        
        //Clear the Lab Assigned Array in Classes class
        
        for(ObCounter=0;ObCounter<Constants.CMAX;ObCounter++){
            for(Day=0;Day<5;Day++)
            {
                Class[ObCounter].LabAssigned[Day] = null;
            }
        }
        
        
    }
    
    public static void ClassDisplay(){
        
        Integer ObCounter;
        Integer Day,Period;
               
        for(ObCounter=0;ObCounter<Constants.CMAX;ObCounter++){
            System.out.println(" ");
            System.out.println("Allotment for Class " + Class[ObCounter].Dept + " " + Class[ObCounter].Sem + " " + Class[ObCounter].Div);
            System.out.println(" ");
            for(Day=0;Day<5;Day++)
            {
                if(Day==0)
                    System.out.print("Monday:    ");
                if(Day==1)
                    System.out.print("Tuesday:   ");
                if(Day==2)
                    System.out.print("Wednesday: ");
                if(Day==3)
                    System.out.print("Thursday:  ");
                if(Day==4)
                    System.out.print("Friday:    ");


                for(Period=0;Period<7;Period++)
                {
                    if(Class[ObCounter].Alt[Day][Period]!=null)
                        System.out.print(Subject[Class[ObCounter].Alt[Day][Period]].Name + "     ");
                    else
                        System.out.print("N/A" + "     ");
                }
            System.out.println(" ");
            }
            
        }
    }
    
    public static void TeacherDisplay(){
        
        Integer ObCounter;
        Integer Day,Period;
               
        for(ObCounter=0;ObCounter<Constants.TMAX;ObCounter++){
            System.out.println(" ");
            System.out.println("Allotment for Teacher: " + Teacher[ObCounter].Name);
            System.out.println(" ");
            for(Day=0;Day<5;Day++)
            {
                if(Day==0)
                    System.out.print("Monday:    ");
                if(Day==1)
                    System.out.print("Tuesday:   ");
                if(Day==2)
                    System.out.print("Wednesday: ");
                if(Day==3)
                    System.out.print("Thursday:  ");
                if(Day==4)
                    System.out.print("Friday:    ");


                for(Period=0;Period<7;Period++)
                {
                    if(Teacher[ObCounter].ClsAlt[Day][Period]!=null)
                        System.out.print(Class[Teacher[ObCounter].ClsAlt[Day][Period]].Dept + Class[Teacher[ObCounter].ClsAlt[Day][Period]].Sem + Class[Teacher[ObCounter].ClsAlt[Day][Period]].Div + "     ");
                    else
                        System.out.print("N/A" + "     ");
                }
            System.out.println(" ");
            }
            
        }
        
    }
    
    public static void LabDisplay(){
        
        Integer ObCounter;
        Integer Day,Period;
               
        for(ObCounter=0;ObCounter<Constants.LMAX;ObCounter++){
            System.out.println(" ");
            System.out.println("Allotment for Lab: " + Lab[ObCounter].Name);
            System.out.println(" ");
            for(Day=0;Day<5;Day++)
            {
                if(Day==0)
                    System.out.print("Monday:    ");
                if(Day==1)
                    System.out.print("Tuesday:   ");
                if(Day==2)
                    System.out.print("Wednesday: ");
                if(Day==3)
                    System.out.print("Thursday:  ");
                if(Day==4)
                    System.out.print("Friday:    ");


                for(Period=0;Period<7;Period++)
                {
                    if(Lab[ObCounter].Alt[Day][Period]!=null)
                        System.out.print(Class[Lab[ObCounter].Alt[Day][Period]].Dept + Class[Lab[ObCounter].Alt[Day][Period]].Sem + Class[Lab[ObCounter].Alt[Day][Period]].Div + "     ");
                    else
                        System.out.print("N/A" + "     ");
                }
            System.out.println(" ");
            }
            
        }
    }
        

}

public class TimeTable{
    public static void main(String args[]){
        
        Integer Generation=1;
        Integer LowestUnallotted=100;
        Integer Occurrences=0;
        TimeTableGen.Create();
        
        
        do{
            TimeTableGen.TotalUnallotted=0;
            TimeTableGen.ClearTables();
            TimeTableGen.SepAssign();
            TimeTableGen.LabAllotment();
            TimeTableGen.ClassAllotment();
            TimeTableGen.Unallotted();
            if(TimeTableGen.TotalUnallotted<LowestUnallotted){
             LowestUnallotted=TimeTableGen.TotalUnallotted;
             Occurrences=0;
            }
            
            if(TimeTableGen.TotalUnallotted==LowestUnallotted)
                Occurrences++;
            System.out.print("No of unallotted periods in Generation " + Generation + ": " + TimeTableGen.TotalUnallotted);
            System.out.print(" Min: " + LowestUnallotted);
            System.out.println(" Occ: " + Occurrences);
            
            Generation++;
        }while(TimeTableGen.TotalUnallotted>2);
        
        System.out.println("");
        System.out.println("TimeTable of Generation " + (Generation-1) + " is:");
        TimeTableGen.Display();
        
        
        
    }       
}


