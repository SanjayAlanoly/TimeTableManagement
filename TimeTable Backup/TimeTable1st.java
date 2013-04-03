

import java.io.*;

class Classes
{
    Integer Id;
    String Sem = new String();
    String Dept = new String();
    String Div = new String();
    Integer Alt[][] = new Integer[5][7];
    Integer TId[] = new Integer[8];
    public void SepAssign()
    {
       if(Dept.equals("CS"))
           Alt[0][6]=5;
       if(Dept.equals("IT"))
           Alt[1][6]=5;
       if(Dept.equals("EEE"))
           Alt[2][6]=5;
       if(Dept.equals("AEI"))
           Alt[3][6]=5;
       if(Dept.equals("EC"))
           Alt[4][6]=5;
       
                     
    }
    
   public void Input()
   {
       Integer i;
       BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
       try
       {
           System.out.println("Class Details Input:");
           System.out.println("Enter the Class Id:");
           Id=Integer.parseInt(br.readLine());
           System.out.println("Enter the Dept:");
           Dept=br.readLine();
           System.out.println("Enter the Sem:");
           Sem=br.readLine();
           System.out.println("Enter the Div:");
           Div=br.readLine();
           for(i=0;i<2;i++)
           {
           System.out.println("Enter the Teacher Ids:");
           TId[i]=Integer.parseInt(br.readLine());
           }
           
       }
       catch(Exception e)
       {
           System.out.println("Exception:"+ e);
           
       }
   }
  
    public void Display()
    {
        Integer p,q;
        System.out.println(" ");
        System.out.println("Allotment for Class " + Dept + " " + Sem + " " + Div);
        System.out.println(" ");
        for(p=0;p<5;p++)
        {
            if(p==0)
                System.out.print("Monday:    ");
            if(p==1)
                System.out.print("Tuesday:   ");
            if(p==2)
                System.out.print("Wednesday: ");
            if(p==3)
                System.out.print("Thursday:  ");
            if(p==4)
                System.out.print("Friday:    ");
            
            
            for(q=0;q<7;q++)
            {
                
                System.out.print(Alt[p][q] + "     ");
            }
        System.out.println(" ");
        }
        
                
    }
    
}


class Teacher
{
    Integer Id;
    String Name = new String();
    String Desig = new String();
    Integer Priority;
    Integer Pref[][] = new Integer[5][7];
    Integer ClsAlt[][] = new Integer[5][7];
     public void Input()
   {
       BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
       try
       {
           System.out.println("Teacher Details Input:");
           System.out.println("Enter the Teacher Id:");
           Id=Integer.parseInt(br.readLine());
           System.out.println("Enter the Name:");
           Name=br.readLine();
           System.out.println("Enter the Desig:");
           Desig=br.readLine();
           System.out.println("Enter the Priority:");
           Priority=Integer.parseInt(br.readLine());
           
       }
       catch(Exception e)
       {
           System.out.println("Exception:"+ e);
           
       }
   }
}

class Subjects
{
   String Id = new String();
   Integer MaxDay;
}
public class TimeTable {

   
    public static void main(String[] args) {
        Integer i;
        Integer p,q;
        Classes Cls[] = new Classes[20];
        Teacher Tr[] = new Teacher[20];
        Subjects Sb[] = new Subjects[20];
         for(i=0;i<2;i++)
        {
            Cls[i] = new Classes();
        }
         for(i=0;i<2;i++)
        {
            Tr[i] = new Teacher();
        }
         for(i=0;i<2;i++)
        {
            for(p=0;p<5;p++)
            {
            for(q=0;q<7;q++)
            {
                Cls[i].Alt[p][q]=9;
                Tr[i].ClsAlt[p][q]=9;
            }
        }
        }
        for(i=0;i<2;i++)
        {
            Cls[i].Input();
        }
        for(i=0;i<2;i++)
        {
            Tr[i].Input();
        }
         for(i=0;i<2;i++)
        {
            Cls[i].SepAssign();
        }
         
        for(i=0;i<2;i++)
        {
            Integer Rnd;
            
               Rnd = (int)(Math.random()*6);
               for(p=0;p<2;p++)
               {
                   Integer Td = Cls[i].TId[p];

                   for(Integer m=0;m<5;m++)
                   {
                       while(true)
                       {
                           if((Cls[i].Alt[m][Rnd]==9)&&(Tr[Td].ClsAlt[m][Rnd]==9))
                           {
                               Cls[i].Alt[m][Rnd]=Td;
                               Tr[Td].ClsAlt[m][Rnd]=1;
                               Rnd = (int)(Math.random()*6);
                               break;
                           }
                           else
                           {
                               Rnd = (int)(Math.random()*6);
                           }
                       }
                   }
               }    
       
        }
        for(i=0;i<2;i++)
        {
            Cls[i].Display();
        }
        
        
    }
}


