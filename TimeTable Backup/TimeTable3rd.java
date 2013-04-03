
package timetable;


import java.io.*;

interface Constants{
    public static final int TMAX = 10;
    public static final int SMAX = 8;
    public static final int CMAX = 2;
}

class Classes{
    
    Integer Id;
    String Dept = new String();
    String Sem = new String();
    String Div = new String();
    Integer Alt[][] = new Integer[5][7];
    
    public Classes(Integer Id, String Dept, String Sem, String Div){
        this.Id = Id;
        this.Dept = Dept;
        this.Sem = Sem;
        this.Div = Div;
    }
    
    public void SepAssign()
    {
       if(Dept.equals("CS"))
           Alt[0][6]=100;
       if(Dept.equals("IT"))
           Alt[1][6]=100;
       if(Dept.equals("EEE"))
           Alt[2][6]=100;
       if(Dept.equals("AEI"))
           Alt[3][6]=100;
       if(Dept.equals("EC"))
           Alt[4][6]=100;
                          
    }
    
    public boolean isDayFull(int Day){
        boolean Flag = true;
        for(int Period=0;Period<7;Period++){
            if(Alt[Day][Period]==null)
                Flag=false;
         
        }
        
        return Flag;
            
    }
 
    public void Display(){
        
       
        
        
                
    }
    
    
    
}


class Teachers{
    
    Integer Id;
    String Name = new String();
    Integer SCId[][] = new Integer[5][3];
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




class TimeTableGen{
    
    public static Classes Class[] = new Classes[Constants.CMAX];
    public static Teachers Teacher[] = new Teachers[Constants.TMAX];
    public static Subjects Subject[] = new Subjects[Constants.SMAX];
    public static Integer Max;
    
    public static void Create(){
        
       
        
        Class[0] = new Classes(0,"CS","S6","A");
        Class[1] = new Classes(1,"CS","S6","B");
        
             
        Teacher[0] = new Teachers(0,"Mathew",0,0,1);
        Teacher[1] = new Teachers(1,"MPS",2,0,null);
        Teacher[2] = new Teachers(2,"Sminu",1,0,1);
        Teacher[3] = new Teachers(3,"Anita",2,1,null);
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
    }
    
    
    public static void Allotment(){
        
        Integer TCounter,SCounter,CCounter,CurrentSubId,SubHrs,AltHrs,Day,Rnd,CurrentClassId,MaxTry;
        Teachers CurrentTeacher = new Teachers();
        
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){//To traverse through Teacher Objects
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            
            //System.out.println("Zero");
            
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){//To traverse through the Subjects of the Teacher
                
                if(Subject[CurrentTeacher.SCId[SCounter][0]].Duration==3)
                    continue;
                        
                CurrentSubId=CurrentTeacher.SCId[SCounter][0];
                SubHrs=Subject[CurrentSubId].HrsPerWeek;
                //System.out.println(SubHrs);
                
                for(CCounter=1;CCounter<=2;CCounter++){//To traverse throught the Classes of the Teacher
                    
                    if(CurrentTeacher.SCId[SCounter][CCounter]==null)
                        continue;
                            
                    AltHrs=0;
                    CurrentClassId=CurrentTeacher.SCId[SCounter][CCounter];
                    
                    //System.out.println(CurrentClassId);
                    
                    MaxTry = 0;
                    
                    while((AltHrs<SubHrs)&&(MaxTry<500)){
                        
                                              
                        //System.out.println("One");
                                                   
                                                
                        Rnd=(int)(Math.random()*7);
                        
                        for(Day=0;Day<5;Day++){
                            
                            //System.out.println("Two");
                            
                            while(Class[CurrentClassId].isDayFull(Day)==false){

                                if((Class[CurrentClassId].Alt[Day][Rnd]==null)&&(CurrentTeacher.ClsAlt[Day][Rnd]==null)){
                                    Class[CurrentClassId].Alt[Day][Rnd]=CurrentSubId;
                                    CurrentTeacher.ClsAlt[Day][Rnd]=CurrentClassId;
                                    AltHrs++;
                                    Rnd=(int)(Math.random()*7);
                                    
                                    //System.out.println("Three");
                                    
                                    break;
                                }
                                else{
                                    Rnd=(int)(Math.random()*7);
                                }

                            }
                        
                            
                        }
                        
                        MaxTry++;
                       
                    }
                    
                    Max=MaxTry; 
                    
                }
            }
        }
            
            
        
    } 
    
    
    public static void Display(){
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
        
        System.out.println("Max Tries=" + Max);
            
        
    }
        

}

public class TimeTable{
    public static void main(String args[]){
        
        TimeTableGen.Create();
        TimeTableGen.Allotment();
        TimeTableGen.Display();
        
        
    }       
}


