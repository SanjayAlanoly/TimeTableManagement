
package timetable;



import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import java.util.*;
import java.sql.*;


interface Constants{
    public static final int TMAX = 42; //26
    public static final int SMAX = 44;  //45
    public static final int CMAX = 8;   //8
    public static final int LMAX = 8;   //8
    public static final int UnallottedMax = 100;
}

class Classes{
    
    Integer Id;
    String Sem = new String();
    String Dept = new String();    
    String Div = new String();
    Integer Alt[][][] = new Integer[5][7][3];
    Integer LabAssigned[] = new Integer[5];
    Integer Unallotted[][] = new Integer [50][3];
    Integer UnallottedCount = 0;
    
    
    
    public Classes(Integer Id, String Sem, String Dept, String Div){
        this.Id = Id;
        this.Sem = Sem;
        this.Dept = Dept;
        this.Div = Div;
    }
    
       
    public boolean isDayFull(int Day){
        boolean Flag = true;
        for(int Period=0;Period<7;Period++){
            if(Alt[Day][Period][0]==null)
                Flag=false;
         
        }
        
        return Flag;
            
    }
    
    public boolean ConsecutiveCheck(Integer Day, Integer Period, Integer CurrentSubId){
        
        if(Period!=1&&Period!=6){
            
            if(CurrentSubId==Alt[Day][Period+1][0])
                return false;
                   
        }
        
        if(Period!=0&&Period!=2){
            
            if(CurrentSubId==Alt[Day][Period-1][0])
                return false;
              
        }
        
        return true;
     
    }
    
    public boolean FirstPeriodCheck(Integer SubId){
        
        Integer Day;
        Integer Count=0;
        for(Day=0;Day<5;Day++){
            if(Alt[Day][0][0]==SubId)
                Count++;
        }
        
        if(Count>=2)
            return false;
        else
            return true;
        
    }
    
    public boolean FirstPeriodViolation(Integer SubId){
        
        Integer Day;
        Integer Count=0;
        for(Day=0;Day<5;Day++){
            if(Alt[Day][0][0]==SubId)
                Count++;
        }
        
        if(Count>=3)
            return false;
        else
            return true;
        
    }
    
    
    public boolean MaxTwoPeriodCheck(Integer Day,Integer SubId){
        Integer Period;
        Integer Count=0;
        
        for(Period=0;Period<7;Period++){
            if(Alt[Day][Period][0]==SubId){
                Count++;
            }
            
        }
        
        if(Count>=2)
            return false;
        else
            return true;
    }
    
    public boolean TwoPeriodViolation(Integer Day,Integer SubId){
        Integer Period;
        Integer Count=0;
        
        for(Period=0;Period<7;Period++){
            if(Alt[Day][Period][0]==SubId){
                Count++;
            }
            
        }
        
        if(Count>=3)
            return false;
        else
            return true;
    }
    
    
    public boolean MaxOnePeriodCheck(Integer Day,Integer SubId){
        Integer Period;
        Integer Count=0;
        
        for(Period=0;Period<7;Period++){
            if(Alt[Day][Period][0]==SubId){
                Count++;
            }
            
        }
        
        if(Count>=1)
            return false;
        else
            return true;
    }
    
  
    
    public boolean MaxPeriodCheck(Integer Day,Integer SubId){
        Integer Period,CDay,DCount;
        Integer Count=0;
        Boolean Flag = false;
        
        for(Period=0;Period<7;Period++){
            if(Alt[Day][Period][0]==SubId){
                Count++;
            }
            
        }
        
        for(CDay=0;CDay<5;CDay++){            
            
            DCount = 0;
            
            for(Period=0;Period<7;Period++){
                
                if(Alt[CDay][Period][0]==SubId){
                    DCount++;
                }
            }
            
            if(DCount == 2){
                Flag=true;
                break;
            }
            
        }
        
        if(Count==0)
            return true;
        else if(Count==1 && Flag==false)
            return true;
        else return false;
    }
    

   
     
    
}


class Teachers{
    
    Integer Id;
    String Name = new String();
    String TCode = new String();
    Integer SCId[][] = new Integer[5][5];
    Integer Pref[][] = new Integer[5][7];
    Integer ClsAlt[][][] = new Integer[5][7][3];
    
    
    public Teachers(Integer Id, String Name, String TCode, Integer SCId[][]){
        
        Integer Row,Column;
        
        this.Id = Id;
        this.Name = Name;
        this.TCode = TCode;
        
        for(Row=0;Row<5;Row++){
            for(Column=0;Column<5;Column++){
                this.SCId[Row][Column] = SCId[Row][Column];
            }
        }
        
        
    }
    
    public Teachers(){
        
        this.Id = null;
        this.Name = null;
             
        
    }        
    
    
    public boolean ConsecutiveCheck(Integer Day, Integer Period, Integer CurrentClassId){
        
        if(Period!=1&&Period!=6){
            
            if(ClsAlt[Day][Period+1][0]!=null)
                return false;
                   
        }
        
        if(Period!=0&&Period!=2){
            
            if(ClsAlt[Day][Period-1][0]!=null)
                return false;   
              
        }
        
        return true;
     
    }
        
        
    public boolean MaxWorkloadPerDayForTheory(Integer Day){
        
        Integer Period;
        Integer TheoryCount = 0;
        Integer LabCount = 0;
        
        for(Period=0;Period<7;Period++){
            
            if(ClsAlt[Day][Period][0]!=null){
                
                //System.out.println(this.Name + "   " + Day);
                //System.out.println(ClsAlt[Day][Period][0]);
                
                //System.out.println(Name + "    " + Day + "    " + Period + "   " + ClsAlt[Day][Period][0]);
                //System.out.println(TimeTableGen.Class[ClsAlt[Day][Period][0]].Alt[Day][Period][0]);
                if(TimeTableGen.Subject[TimeTableGen.Class[ClsAlt[Day][Period][0]].Alt[Day][Period][0]].Duration==3)
                    LabCount++;
                if(TimeTableGen.Subject[TimeTableGen.Class[ClsAlt[Day][Period][0]].Alt[Day][Period][0]].Duration==1)
                    TheoryCount++;
                                
            }
        }
        
        if(( (TheoryCount<=1) && (LabCount==3) )  ||  ( (TheoryCount<=2) && (LabCount==0) ))
            return true;
        else{
            //System.out.println("Violation: " + Name + ", " + Day);
            return false;  
        }
        
    }
     
        
        public boolean MaxWorkloadViolation(Integer Day){
        
        Integer Period;
        Integer TheoryCount = 0;
        Integer LabCount = 0;
        
        for(Period=0;Period<7;Period++){
            
            if(ClsAlt[Day][Period][0]!=null){
                
                //System.out.println(this.Name + "   " + Day);
                //System.out.println(ClsAlt[Day][Period][0]);
                
                //System.out.println(Name + "    " + Day + "    " + Period + "   " + ClsAlt[Day][Period][0]);
                //System.out.println(TimeTableGen.Class[ClsAlt[Day][Period][0]].Alt[Day][Period][0]);
                if(TimeTableGen.Subject[ClsAlt[Day][Period][1]].Duration==3)
                    LabCount++;
                if(TimeTableGen.Subject[ClsAlt[Day][Period][1]].Duration==1)
                    TheoryCount++;
                                
            }
        }
        
        if(( (TheoryCount<=2) && (LabCount==3) )  ||  ( (TheoryCount<=3) && (LabCount==0) ))
            return true;
        else{
            //System.out.println("Violation: " + Name + ", " + Day);
            return false;  
        }
            
        
    }
    

}

class Subjects{   
       
    Integer Id;
    String Name = new String();
    String ShortName = new String();
    Integer HrsPerWeek;
    Integer Duration;
    Integer ElectiveNo;
    
    public Subjects(Integer Id, String Name, String ShortName, Integer HrsPerWeek, Integer Duration, Integer ElectiveNo){
        
        this.Id = Id;
        this.Name = Name;
        this.ShortName = ShortName;
        this.HrsPerWeek = HrsPerWeek;
        this.Duration = Duration;
        this.ElectiveNo = ElectiveNo;
        
    }
    
}


class Labs{
    
    Integer Id;
    String Name = new String();
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


class Elective{
            
            Integer SubjectId;
            Integer TeacherClassId[][] = new Integer[5][2];
            Integer Counter = 0;
        }



class TimeTableGen{
    
    public static Classes Class[] = new Classes[Constants.CMAX];
    public static Teachers Teacher[] = new Teachers[Constants.TMAX];
    public static Subjects Subject[] = new Subjects[Constants.SMAX];
    public static Labs Lab[] = new Labs[Constants.LMAX];
    
    public static Elective CS[] = new Elective[6];
    public static Elective IT[] = new Elective[6];
    public static Elective EC[] = new Elective[6];
    public static Elective AEI[] = new Elective[6];
    public static Elective EEE[] = new Elective[6];
    
    
    public static Integer TotalUnallotted = 0;
    public static Integer UnallottedBreak = 0 ;
    public static Integer CallCount=0;
    
    /*public static void Create(){
        
       
        
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
    }*/
    
   public static void ExcelInput(String Input){
       
       try{
          
        ClassDBInput();        
        SubjectsInput(Input);
        }
       catch(Exception E){
           GUI.SetLabelText("Input File Not Selected");
           System.out.println("Error in Excel Input Classes: " + E);
       }
       
       
       
        try{
        LabDBInput();
        TeacherDBInput();
        }
        catch(Exception E){
            System.out.println("Error in Excel Input Labs Teachers: " + E);
        }
        
       
        
       try{
            ClassFixedInput(Input);
       }
       catch(Exception E){
           System.out.println("Error in Excel Input Fixed: " + E);
       }
           
        
    }
   
    public static void test(){
        
        int i;
        for(i=0;i<Constants.CMAX;i++){
            System.out.println(Class[i].Sem + Class[i].Dept);
        }
        
    }
    
    public static void ClassesInput(String Input) throws IOException, BiffException {
        Integer Id;
        String Sem;
        String Dept;    
        String Div;
                         
        Integer Row;
        Workbook CWorkbook = Workbook.getWorkbook(new File(Input));
        Sheet CSheet = CWorkbook.getSheet(2); 
        
        for(Row=0;Row<Constants.CMAX;Row++){
            
            Cell CCell;
            
            CCell = CSheet.getCell(0,Row+1);
            if(!"".equals(CCell.getContents()))
                Id = Integer.parseInt(CCell.getContents());
            else
                Id = null;
            
            CCell = CSheet.getCell(1,Row+1);
            if(!"".equals(CCell.getContents()))
                Sem = CCell.getContents();
            else
                Sem = null;
            
            CCell = CSheet.getCell(2,Row+1);
            if(!"".equals(CCell.getContents()))
                Dept = CCell.getContents();
            else
                Dept = null;
            
            CCell = CSheet.getCell(3,Row+1);
            if(!"".equals(CCell.getContents()))
                Div = CCell.getContents();
            else
                Div = "";
            
            Class[Row] = new Classes(Id,Sem,Dept,Div);
            
            
        }
        
        CWorkbook.close();      
        
        
    }
    
    public static void ClassDBInput(){
        
        Integer Id=null;
        String Sem=null;
        String Dept=null;    
        String Div=null;
                         
        Integer Row=0;
        
        
        try{
            String dbURL = "jdbc:sqlserver://localhost\\sqlexpress;databaseName=timetable;user=sa;password=timetable";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
            }
            else
                System.out.println("Not Connected");

            Statement st = conn.createStatement();
            String sql = "Select * from class";
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            
            while(rs.next()){
                
                Id = rs.getInt(1);
                Sem = rs.getString(2);
                Dept = rs.getString(3);
                Div = rs.getString(4);
            
                Class[Row] = new Classes(Id,Sem,Dept,Div);
                Row++;

            }
            rs.close();
            st.close();
            conn.close();
        
        }catch(Exception e){
            System.out.println(e);
        }
        
        
        
        
    }
    
    public static void SubjectsInput(String Input) throws IOException, BiffException {
        Integer Id;
        String Name;
        String ShortName;
        Integer HrsPerWeek;
        Integer Duration;
        Integer ElectiveNo;
              
        Integer Row;
        Workbook CWorkbook = Workbook.getWorkbook(new File(Input));
        Sheet CSheet = CWorkbook.getSheet(1); 
        
        for(Row=0;Row<Constants.SMAX;Row++){
            
            Cell CCell;
            
            CCell = CSheet.getCell(0,Row+1);
            if(!"".equals(CCell.getContents()))
                Id = Integer.parseInt(CCell.getContents());
            else
                Id = null;
            
            CCell = CSheet.getCell(1,Row+1);
            if(!"".equals(CCell.getContents()))
                Name = CCell.getContents();
            else
                Name = null;
            
            CCell = CSheet.getCell(2,Row+1);
            if(!"".equals(CCell.getContents()))
                ShortName = CCell.getContents();
            else
                ShortName = null;
            
            
            
            CCell = CSheet.getCell(3,Row+1);
            if(!"".equals(CCell.getContents()))               
                 HrsPerWeek = Integer.parseInt(CCell.getContents());
            else
                HrsPerWeek = null;
            
            CCell = CSheet.getCell(4,Row+1);
            if(!"".equals(CCell.getContents()))
                Duration = Integer.parseInt(CCell.getContents());
            else
                Duration = null;
            
            CCell = CSheet.getCell(5,Row+1);
            if(!"".equals(CCell.getContents()))
                ElectiveNo = Integer.parseInt(CCell.getContents());
            else
                ElectiveNo = null;
            //System.out.println(Id + Name + ShortName + HrsPerWeek + Duration + ElectiveNo);
            Subject[Row] = new Subjects(Id,Name,ShortName,HrsPerWeek,Duration,ElectiveNo);
            
        }
        
        CWorkbook.close();
        
        
        
    }
    
    
    public static void SubjectDBInput(){
        Integer Id=null;
        String Name=null;
        String ShortName=null;
        Integer HrsPerWeek=null;
        Integer Duration=null;
        Integer ElectiveNo=null;
              
        Integer Row=0;
        
        
        
        try{
            String dbURL = "jdbc:sqlserver://localhost\\sqlexpress;databaseName=timetable;user=sa;password=timetable";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
            }
            else
                System.out.println("Not Connected");

            Statement st = conn.createStatement();
            String sql = "Select * from subject";
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            
            while(rs.next()){
                
                Id = rs.getInt(1);
                Name = rs.getString(2);
                ShortName = rs.getString(3);
                HrsPerWeek = rs.getInt(4);
                Duration = rs.getInt(5);
                ElectiveNo = rs.getInt(6);
            
                Subject[Row] = new Subjects(Id,Name,ShortName,HrsPerWeek,Duration,ElectiveNo);
                Row++;

            }
            rs.close();
            st.close();
            conn.close();
        
        }catch(Exception e){
            System.out.println(e);
        }
        
        
        
    }
    
    
    public static void LabsInput(String Input) throws IOException, BiffException {
        Integer Id;
        String Name;
        Integer SId[] = new Integer[5];
                         
        Integer Row, Column;
        Integer Counter;
        
        Workbook CWorkbook = Workbook.getWorkbook(new File(Input));
        Sheet CSheet = CWorkbook.getSheet(3); 
        
        for(Row=0;Row<Constants.LMAX;Row++){
            
            Cell CCell;
            
            CCell = CSheet.getCell(0,Row+1);
            if(!"".equals(CCell.getContents()))
                Id = Integer.parseInt(CCell.getContents());
            else
                Id = null;
            
            CCell = CSheet.getCell(1,Row+1);
            if(!"".equals(CCell.getContents()))
                Name = CCell.getContents();
            else
                Name = null;
            
            Column=2;
            
            for(Counter=0;Counter<5;Counter++){
                CCell = CSheet.getCell(Column,Row+1);
                if(!"".equals(CCell.getContents()))
                    SId[Counter] = getSubjectId(CCell.getContents());
                else
                    SId[Counter] = null;
                
                Column++;
            }
            
            
            Lab[Row] = new Labs(Id,Name,SId[0],SId[1],SId[2],SId[3],SId[4]);
            
        }
        CWorkbook.close();
    }
    
    
    public static void LabDBInput(){
        
        Integer Id=null;
        String Name=null;
        Integer SId[] = new Integer[5];
        
                         
        Integer Row=0;
        
        
        
        try{
            String dbURL = "jdbc:sqlserver://localhost\\sqlexpress;databaseName=timetable;user=sa;password=timetable";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
            }
            else
                System.out.println("Not Connected");

            Statement st = conn.createStatement();
            String sql = "Select * from lab";
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            
            while(rs.next()){
                
                Id = rs.getInt(1);
                Name = rs.getString(2);
                SId[0] = getSubjectId(rs.getString(3));
                SId[1] = getSubjectId(rs.getString(4));
                SId[2] = getSubjectId(rs.getString(5));
                SId[3] = getSubjectId(rs.getString(6));
                SId[4] = getSubjectId(rs.getString(7));
                
                
                Lab[Row] = new Labs(Id,Name,SId[0],SId[1],SId[2],SId[3],SId[4]);
                Row++;

            }
            rs.close();
            st.close();
            conn.close();
        
        }catch(Exception e){
            System.out.println(e);
        }
        
        
    }
    
    
    public static void TeacherDBInput(){
        
        Integer Id = null;
        String Name = null;
        String TCode = null;
        String Cell = null;
        
        Integer SCId[][] = new Integer[5][5];
               
        Integer Row=0,Column;
        Integer ARow,AColumn;
        
        try{
            String dbURL = "jdbc:sqlserver://localhost\\sqlexpress;databaseName=timetable;user=sa;password=timetable";
            Connection conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                System.out.println("Connected");
            }
            else
                System.out.println("Not Connected");

            Statement st = conn.createStatement();
            String sql = "Select * from teacher";
            st.executeQuery(sql);
            ResultSet rs = st.getResultSet();
            
            while(rs.next()){
                
                Id = rs.getInt(1);
                Name = rs.getString(2);
                TCode = rs.getString(3);
                //System.out.println(Id+Name+TCode);
                
                Column=3;
                for(ARow=0;ARow<5;ARow++){
                    for(AColumn=0;AColumn<5;AColumn++){
                        
                        Cell = rs.getString(Column+1);
                        if(!"".equals(rs.getString(4))){
                            
                            if(AColumn==0){
                            
                                SCId[ARow][AColumn] = getSubjectId(Cell);
                                //System.out.println(SCId[ARow][AColumn]);
                            
                            }
                            else{
                               
                                SCId[ARow][AColumn] = getClassId(Cell);
                                //System.out.println(SCId[ARow][AColumn]);

                            } 
                        }
                        else{
                            SCId[ARow][AColumn] = null;                        
                        }
                        Column++;
                    }
                }
                //System.out.println(Id+Name+TCode);
                Teacher[Row] = new Teachers(Id,Name,TCode,SCId);
                Row++;
            }
            rs.close();
            st.close();
            conn.close();
                
       
        }catch(SQLException e){
            System.out.println(e);
        }
        
        
        
    }
    
    
            
    
    
    public static void TeachersInput(String Input) throws IOException, BiffException {
        Integer Id;
        String Name;
        String TCode;
        
        Integer SCId[][] = new Integer[5][5];
               
        Integer Row,Column;
        Integer ARow,AColumn;
        
        //System.out.println(Input);
        Workbook CWorkbook = Workbook.getWorkbook(new File(Input));
        Sheet CSheet = CWorkbook.getSheet(0); 
        
        for(Row=0;Row<Constants.TMAX;Row++){
            
            Cell CCell;
            
            CCell = CSheet.getCell(0,Row+1);
            if(!"".equals(CCell.getContents()))
                Id = Integer.parseInt(CCell.getContents());
            else
                Id = null;
            
            CCell = CSheet.getCell(1,Row+1);
            if(!"".equals(CCell.getContents()))
                Name = CCell.getContents();
            else
                Name = null;
            
            
            CCell = CSheet.getCell(2,Row+1);
            if(!"".equals(CCell.getContents()))
                TCode = CCell.getContents();
            else
                TCode = null;
            
           
            
            Column=3;
                        
            for(ARow=0;ARow<5;ARow++){
                for(AColumn=0;AColumn<5;AColumn++){
                    
                    CCell = CSheet.getCell(Column,Row+1);
                    if(!"".equals(CCell.getContents())){
                        
                        if(AColumn==0){
                            //System.out.println(CCell.getContents());
                            SCId[ARow][AColumn] = getSubjectId(CCell.getContents());
                            
                        }
                        else  {
                            //System.out.println(CCell.getContents());
                            SCId[ARow][AColumn] = getClassId(CCell.getContents());
                             
                        }                      
                            
                        
                    }
                    
                    else
                        SCId[ARow][AColumn] = null;                        
                    
                    Column++;
                }
                 
            }
                      
            Teacher[Row] = new Teachers(Id,Name,TCode,SCId);
            
        }
        CWorkbook.close();
    }
    
    
    
     public static void ClassFixedInput(String Input) throws IOException, BiffException {
        Integer CCounter,ClassId,Middle,End,SubId,Count,Start,TCounter;
        String SubName,CellString;
        String TeacherName[] = new String[6];
        Integer TeacherId[] = new Integer[6];
        
        Integer Row,Column;
                
        Workbook CWorkbook = Workbook.getWorkbook(new File(Input));
        Sheet CSheet = CWorkbook.getSheet(4); 
        
        
        for(CCounter=0;CCounter<Constants.CMAX;CCounter++){
            
            Cell CCell;
            CCell = null;
            ClassId = null;
                                 
            for(Row=0;Row<8;Row++){
                for(Column=0;Column<8;Column++){
                    
                    //System.out.println(CCounter + "    " + Row + "    " + Column ) ;
                    
                    if(!(CCounter==(Constants.CMAX-1)&&Row>5))
                        CCell = CSheet.getCell(Column,Row+(CCounter*8));
                    
                    if(!"".equals(CCell.getContents())){
                        
                        //System.out.println(CCell.getContents());
                        
                        if(Row==0&&Column==0){
                            ClassId = getClassId(CCell.getContents());
                            //System.out.println("Name:" + CCell.getContents() + "Id:" + ClassId);
                        }
                        if(Row!=0 && Row!=6 && Row!=7 && Column!=0){
                            
                            CellString = CCell.getContents();
                            //System.out.println(CellString);
                            Count = 0;
                            SubId = null;
                            
                            if(CellString.indexOf("/")==-1){
                                End = CellString.length();
                                SubName = CellString.substring(0,End);
                                SubId = getSubjectId(SubName);
                                TeacherId[0] = null;
                            }
                            //System.out.println("Blah");
                            if(CellString.indexOf("/")!=-1 && CellString.indexOf(",")==-1){
                                //System.out.println("Test");
                                Middle = CellString.indexOf("/");
                                End = CellString.length();
                                SubName = CellString.substring(0,Middle);
                                TeacherName[0] = CellString.substring(Middle+1,End);
                                 //System.out.println("Blah1");
                                SubId = getSubjectId(SubName);
                                 //System.out.println("Name: " + TeacherName[0]);
                                TeacherId[0] = getTeacherId(TeacherName[0]);  
                               //System.out.println("Id: " + TeacherId[0]);
                            }
                            
                            if(CellString.indexOf("/")!=-1 && CellString.indexOf(",")!=-1){
                                
                                Middle = CellString.indexOf("/");
                                SubName = CellString.substring(0,Middle);
                                SubId = getSubjectId(SubName);
                                Start = Middle + 1;
                                
                                while(Start<CellString.length()){
                                    
                                    //System.out.println("Start:" + Start);
                                    
                                    if(CellString.indexOf(",",Start)!=-1)
                                        End = CellString.indexOf(",",Start);
                                    else
                                        End = CellString.length();
                                    
                                    //System.out.println("End:" + End);
                                    TeacherName[Count] = CellString.substring(Start,End);
                                    TeacherId[Count] = getTeacherId(TeacherName[Count]);
                                    
                                    //System.out.println("String: " + TeacherName[Count]);
                                    
                                    Count++;
                                    Start = End+1;
                                    
                                }
                                
                                
                            }                        
                            
                            Class[ClassId].Alt[Row-1][Column-1][0] = SubId;
                            Class[ClassId].Alt[Row-1][Column-1][1] = TeacherId[0];
                            Class[ClassId].Alt[Row-1][Column-1][2] = 1;
                            
                            //System.out.println("Blah3");
                            
                            for(TCounter=0;TCounter<Count;TCounter++){
                                
                                Teacher[TeacherId[TCounter]].ClsAlt[Row-1][Column-1][0] = ClassId;
                                Teacher[TeacherId[TCounter]].ClsAlt[Row-1][Column-1][1] = SubId;
                                Teacher[TeacherId[TCounter]].ClsAlt[Row-1][Column-1][2] = 1;
                            }
                            
                            
                            
                        }      
                        
                        
                    }
                    //System.out.println("Row:" + Row + "Column:" + Column + "Counter:" + CCounter);
                }
                
            }     
            
        }
        CWorkbook.close();
    }
    
    
    
    
    public static Integer getSubjectId(String SubName){      
        
        Integer Counter,SubId;
        
        //System.out.println("InputName: " + SubName);
        
        SubId = null;
        
        for(Counter=0;Counter<Constants.SMAX;Counter++){
            if(Subject[Counter].ShortName.equals(SubName)){
                SubId = Subject[Counter].Id;
            }
             
        }
        //System.out.println(SubId);
        return SubId;
        
    }
    
    
    public static Integer getClassId(String ClassName){      
        
        Integer Counter,ClassId;
        String CurrentClassName;
        
        //System.out.println("InputName: " + ClassName);
        
        ClassId = null;
        
        for(Counter=0;Counter<Constants.CMAX;Counter++){
            CurrentClassName = Class[Counter].Sem + Class[Counter].Dept + Class[Counter].Div;
            if(CurrentClassName.equals(ClassName)){
                ClassId = Subject[Counter].Id;
            }
             
        }
        //System.out.println(ClassId);
        return ClassId;
        
    }
    
    
    
    public static Integer getTeacherId(String TeacherName){      
        
        Integer Counter,TeacherId;
        String CurrentTeacherName;
        
        //System.out.println(TeacherName);
        //System.out.println(Teacher[0].TCode);
        TeacherId = null;
        
        for(Counter=0;Counter<Constants.TMAX;Counter++){
            //System.out.println(Teacher[Counter].TCode);
            CurrentTeacherName = Teacher[Counter].TCode;
            
            if(CurrentTeacherName.equals(TeacherName)){
                TeacherId = Teacher[Counter].Id;
            }
             
        }
        
        //System.out.println("Output:" + TeacherId);
        
        return TeacherId;
        
    }
    
    
       
    public static void ExcelOutput(){
        try{
            WritableWorkbook OutputWorkbook = Workbook.createWorkbook(new File("Output.xls"));
            ClassTimeTableOuput(OutputWorkbook);
            TeacherTimeTableOuput(OutputWorkbook);
            LabTimeTableOuput(OutputWorkbook);
        }
        catch(Exception E){
            System.out.println("Error in Excel Output:" + E);
        }
        
    }
    
    public static void ExcelOutput2(){
        try{
            WritableWorkbook OutputWorkbook = Workbook.createWorkbook(new File("OutputSwap.xls"));
            ClassTimeTableOuput(OutputWorkbook);
            TeacherTimeTableOuput(OutputWorkbook);
            LabTimeTableOuput(OutputWorkbook);
        }
        catch(Exception E){
            System.out.println("Error in Excel Output:" + E);
        }
        
    }
    
    public static void ExcelOutput3(){
        try{
            WritableWorkbook OutputWorkbook = Workbook.createWorkbook(new File("OutputSwapLevel2.xls"));
            ClassTimeTableOuput(OutputWorkbook);
            TeacherTimeTableOuput(OutputWorkbook);
            LabTimeTableOuput(OutputWorkbook);
        }
        catch(Exception E){
            System.out.println("Error in Excel Output:" + E);
        }
        
    }
    
    


    public static void ClassTimeTableOuput(WritableWorkbook OutputWorkbook) throws IOException, jxl.write.WriteException {
        
        //System.out.println("ClassTTT");
        
        Integer ObCounter;
        Integer Day,Period;
        String Temp = new String();
        Integer Column,Row;
        Row=0;
        
        
        WritableSheet CSheet = OutputWorkbook.createSheet("Classes", 0);
        
        
        Label CLabel;
        
        
        
        for(ObCounter=0;ObCounter<Constants.CMAX;ObCounter++){
            Row++;
            Column=0;
            CLabel = new Label(Column,Row,"Allotment for Class " + Class[ObCounter].Sem + Class[ObCounter].Dept + " " +  " " + Class[ObCounter].Div);
            CSheet.addCell(CLabel);
            Row++;
            for(Day=0;Day<5;Day++)
            {
                Column=0;
                if(Day==0){
                    CLabel = new Label(Column,Row,"Monday:    ");
                    CSheet.addCell(CLabel);
                    Column++;
                }
                
                if(Day==1){
                    CLabel = new Label(Column,Row,"Tuesday:    ");
                    CSheet.addCell(CLabel);                    
                    Column++;
                }
                
                if(Day==2){
                    CLabel = new Label(Column,Row,"Wednesday:    ");
                    CSheet.addCell(CLabel); 
                    Column++;
                }
                
                if(Day==3){
                    CLabel = new Label(Column,Row,"Thursday:    ");
                    CSheet.addCell(CLabel);   
                    Column++;
                }
                
                if(Day==4){
                    CLabel = new Label(Column,Row,"Friday:    ");
                    CSheet.addCell(CLabel);  
                    Column++;
                }
                    
                
                for(Period=0;Period<7;Period++)
                {
                    if(Class[ObCounter].Alt[Day][Period][0]!=null){
                        if(Class[ObCounter].Alt[Day][Period][1]!=null){
                            CLabel = new Label(Column,Row,Subject[Class[ObCounter].Alt[Day][Period][0]].ShortName + "/" + Teacher[Class[ObCounter].Alt[Day][Period][1]].TCode);  
                            CSheet.addCell(CLabel);   
                            Column++;
                        }
                        else{
                            CLabel = new Label(Column,Row,Subject[Class[ObCounter].Alt[Day][Period][0]].ShortName);
                            CSheet.addCell(CLabel);   
                            Column++;
                        } 
                    }
                    else{
                            CLabel = new Label(Column,Row,"N/A" + "     ");
                            CSheet.addCell(CLabel);   
                            Column++;
                    }
                       
                }
            Row++;
            }
            
        }
        
    }
    
    
    public static void TeacherTimeTableOuput(WritableWorkbook OutputWorkbook) throws IOException, jxl.write.WriteException {
        
        
        //System.out.println("TeacherTT");
        
        Integer ObCounter;
        Integer Day,Period;
        String Temp = new String();
        Integer Column,Row;
        Row=0;
        
        
        WritableSheet CSheet = OutputWorkbook.createSheet("Teachers", 1);
        
         
        Label CLabel;
                       
        for(ObCounter=0;ObCounter<Constants.TMAX;ObCounter++){
            Row++;
            Column=0;
            //System.out.println(Teacher[ObCounter].Name);
            CLabel = new Label(Column,Row,"Allotment for Teacher: " + Teacher[ObCounter].Name);
            CSheet.addCell(CLabel);
            Row++;
            for(Day=0;Day<5;Day++)
            {
                Column=0;
                if(Day==0){
                    CLabel = new Label(Column,Row,"Monday:    ");
                    CSheet.addCell(CLabel);
                    Column++;
                }
                
                if(Day==1){
                    CLabel = new Label(Column,Row,"Tuesday:    ");
                    CSheet.addCell(CLabel);                    
                    Column++;
                }
                
                if(Day==2){
                    CLabel = new Label(Column,Row,"Wednesday:    ");
                    CSheet.addCell(CLabel); 
                    Column++;
                }
                
                if(Day==3){
                    CLabel = new Label(Column,Row,"Thursday:    ");
                    CSheet.addCell(CLabel);   
                    Column++;
                }
                
                if(Day==4){
                    CLabel = new Label(Column,Row,"Friday:    ");
                    CSheet.addCell(CLabel);  
                    Column++;
                }
                    
                
                for(Period=0;Period<7;Period++)
                {
                    
                    //System.out.println(Day + "   " + Period + "   " +Teacher[ObCounter].ClsAlt[Day][Period][0] + "   " +Teacher[ObCounter].ClsAlt[Day][Period][1]);
                    
                    if(Teacher[ObCounter].ClsAlt[Day][Period][0]!=null){
                        
                        if(Teacher[ObCounter].ClsAlt[Day][Period][1]!=null){
                            
                            CLabel = new Label(Column,Row,Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Sem +  Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Dept + Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Div + "/" + Subject[Teacher[ObCounter].ClsAlt[Day][Period][1]].ShortName);  
                            CSheet.addCell(CLabel);   
                            Column++;
                            
                        }
                        
                        else
                        {
                            CLabel = new Label(Column,Row,Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Sem +  Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Dept + Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Div);  
                            CSheet.addCell(CLabel);   
                            Column++;
                        }
                            
                    }
                    else{
                            CLabel = new Label(Column,Row,"N/A" + "     ");
                            CSheet.addCell(CLabel);   
                            Column++;
                    }
                       
                }
            Row++;
            }
            
        }
        
    }
    
    
    
    public static void LabTimeTableOuput(WritableWorkbook OutputWorkbook) throws IOException, jxl.write.WriteException {
        
        
        //System.out.println("LabTT");
        
        Integer ObCounter;
        Integer Day,Period;
        String Temp = new String();
        Integer Column,Row;
        Row=0;
        
        
        WritableSheet CSheet = OutputWorkbook.createSheet("Labs", 2);
        
        
        Label CLabel;
                       
        for(ObCounter=0;ObCounter<Constants.LMAX;ObCounter++){
            Row++;
            Column=0;
            CLabel = new Label(Column,Row,"Allotment for Lab: " + Lab[ObCounter].Name);
            CSheet.addCell(CLabel);
            Row++;
            for(Day=0;Day<5;Day++)
            {
                Column=0;
                if(Day==0){
                    CLabel = new Label(Column,Row,"Monday:    ");
                    CSheet.addCell(CLabel);
                    Column++;
                }
                
                if(Day==1){
                    CLabel = new Label(Column,Row,"Tuesday:    ");
                    CSheet.addCell(CLabel);                    
                    Column++;
                }
                
                if(Day==2){
                    CLabel = new Label(Column,Row,"Wednesday:    ");
                    CSheet.addCell(CLabel); 
                    Column++;
                }
                
                if(Day==3){
                    CLabel = new Label(Column,Row,"Thursday:    ");
                    CSheet.addCell(CLabel);   
                    Column++;
                }
                
                if(Day==4){
                    CLabel = new Label(Column,Row,"Friday:    ");
                    CSheet.addCell(CLabel);  
                    Column++;
                }
                    
                
                for(Period=0;Period<7;Period++)
                {
                    if(Lab[ObCounter].Alt[Day][Period]!=null){
                            CLabel = new Label(Column,Row,Class[Lab[ObCounter].Alt[Day][Period]].Sem + Class[Lab[ObCounter].Alt[Day][Period]].Dept +  Class[Lab[ObCounter].Alt[Day][Period]].Div);  
                            CSheet.addCell(CLabel);   
                            Column++;
                    }
                    else{
                            CLabel = new Label(Column,Row,"N/A" + "     ");
                            CSheet.addCell(CLabel);   
                            Column++;
                    }
                       
                }
            Row++;
            }
            
        }
        OutputWorkbook.write();
        OutputWorkbook.close();
    }
    
    
    public static void ClassAllotment(){
        
        
        
        Integer TCounter,SCounter,CCounter,CurrentSubId,SubHrs,AltHrs,Day,DayRnd,PeriodRnd,CurrentClassId,MaxDayTry,MaxClassTry,MaxWeekTry,TempDayRnd;
        Teachers CurrentTeacher;
        
        
        
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){//To traverse through Teacher Objects
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            
            //System.out.println("Zero");
            
           
            
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){//To traverse through the Subjects of the Teacher
                
                if((Subject[CurrentTeacher.SCId[SCounter][0]].Duration==3) || (Subject[CurrentTeacher.SCId[SCounter][0]].ElectiveNo!=null)){//If it is a Lab Session or an elective
                    
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
                    
                    MaxClassTry = 0;
                    
                    while((AltHrs<SubHrs)&&(MaxClassTry<2)){
                        
                                              
                        //System.out.println("One");
                                                   
                                                
                        PeriodRnd=(int)(Math.random()*7);
                        DayRnd = (int)(Math.random()*5);
                        
                        MaxWeekTry=0;
                        
                        while((AltHrs<SubHrs)&&(MaxWeekTry<20)){
                            
                            //System.out.println("Two");
                            MaxDayTry=0;
                            
                            while((Class[CurrentClassId].isDayFull(DayRnd)==false)&&(MaxDayTry<10)){

                                if((Class[CurrentClassId].Alt[DayRnd][PeriodRnd][0]==null)
                                        &&(CurrentTeacher.ClsAlt[DayRnd][PeriodRnd][0]==null)
                                        &&(Class[CurrentClassId].ConsecutiveCheck(DayRnd,PeriodRnd,CurrentSubId))
                                        &&(Class[CurrentClassId].FirstPeriodCheck(CurrentSubId))
                                        &&(Class[CurrentClassId].MaxPeriodCheck(DayRnd,CurrentSubId))
                                        &&(CurrentTeacher.MaxWorkloadPerDayForTheory(DayRnd))
                                        &&(CurrentTeacher.ConsecutiveCheck(DayRnd,PeriodRnd,CurrentClassId))){
                                    
                                    
                                    Class[CurrentClassId].Alt[DayRnd][PeriodRnd][0]=CurrentSubId;
                                    Class[CurrentClassId].Alt[DayRnd][PeriodRnd][1]=CurrentTeacher.Id;
                                    CurrentTeacher.ClsAlt[DayRnd][PeriodRnd][0]=CurrentClassId;
                                    CurrentTeacher.ClsAlt[DayRnd][PeriodRnd][1]=CurrentSubId;
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
                            
                            TempDayRnd = (int)(Math.random()*5);
                            while( TempDayRnd == DayRnd)
                                TempDayRnd = (int)(Math.random()*5);
                                
                            DayRnd = TempDayRnd;
                            
                        }
                        
                        //System.out.println("Max Week Try: " + MaxSubTry);
                        MaxClassTry++;
                        
                        
                       
                    }
                     if(AltHrs<SubHrs){
                
                        
                        Class[CurrentClassId].Unallotted[Class[CurrentClassId].UnallottedCount][0] = CurrentTeacher.Id;
                        Class[CurrentClassId].Unallotted[Class[CurrentClassId].UnallottedCount][1] = CurrentSubId;
                        Class[CurrentClassId].Unallotted[Class[CurrentClassId].UnallottedCount][2] = SubHrs-AltHrs;
                        Class[CurrentClassId].UnallottedCount++;
                        
                        //System.out.println("Class: " + Class[CurrentClassId].Sem + Class[CurrentClassId].Dept + Class[CurrentClassId].Div + "  UnallottedCount: " +   Class[CurrentClassId].UnallottedCount + "  Teacher: " + Teacher[CurrentTeacher.Id].Name + "  CurrentSubId: " + Subject[CurrentSubId].Name + "  Unallotted: " + (SubHrs-AltHrs));
                        TimeTableGen.UnallottedBreak++;
                        
                        if(TimeTableGen.UnallottedBreak > Constants.UnallottedMax)
                            return;
                    }
                    
                    
                }
            }    
            }
        }
        
            
            
        
     
    
    
     
    public static void LabAllotment(){
        
        Integer TCounter,SCounter,CurrentSubId,LCounter;
        boolean Flag;
        Teachers CurrentTeacher;
        Integer LabRnd,MaxLabTry;
        
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){//To traverse through Teacher Objects
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            //System.out.println("One");
                        
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){
                
                //System.out.println("Two");
                CurrentSubId=CurrentTeacher.SCId[SCounter][0];
                //Flag=false;
                
                //System.out.println(SCounter + "  ");
                LabRnd = (int)(Math.random()*Constants.LMAX);
                MaxLabTry = 0;
                
                if(Subject[CurrentTeacher.SCId[SCounter][0]].Duration==3){
                    
                    while(MaxLabTry<10){
                        
                        //System.out.println("LCounter: " + LCounter);
                        Flag=LabAssign(LabRnd,CurrentTeacher.Id,CurrentSubId,CurrentTeacher.SCId[SCounter][1]);
                        MaxLabTry++;
                        LabRnd = (int)(Math.random()*Constants.LMAX);
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
        Integer Try=0;
        //System.out.println("Variables: " + "  " + Lab[LCounter].Name + "  " + Teacher[TeacherId].Name + "  " + Subject[SubId].Name + "  " + ClassId);
        for(SCounter=0;(SCounter<5)&&(Lab[LCounter].SId[SCounter]!=null);SCounter++){
            
            Day=(int)(Math.random()*5);
                        
            if(Lab[LCounter].SId[SCounter]==SubId){
                
                while((Flag==false) && Try<25){
                    
                    //System.out.println("Here");
                    
                    if((Lab[LCounter].Alt[Day][4]==null)&&(Lab[LCounter].Alt[Day][5]==null)&&(Lab[LCounter].Alt[Day][6]==null)&&(Teacher[TeacherId].ClsAlt[Day][4][0]==null)&&(Teacher[TeacherId].ClsAlt[Day][5][0]==null)&&(Teacher[TeacherId].ClsAlt[Day][6][0]==null)&&(Class[ClassId].Alt[Day][4][0]==null)&&(Class[ClassId].Alt[Day][5][0]==null)&&(Class[ClassId].Alt[Day][6][0]==null)&&(Class[ClassId].LabAssigned[Day]==null)){
                        
                        //System.out.println("One");
                        
                        Lab[LCounter].Alt[Day][4]=ClassId;
                        Lab[LCounter].Alt[Day][5]=ClassId;
                        Lab[LCounter].Alt[Day][6]=ClassId;
                        
                        Teacher[TeacherId].ClsAlt[Day][4][0]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][5][0]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][6][0]=ClassId;
                        
                        Teacher[TeacherId].ClsAlt[Day][4][1]=SubId;
                        Teacher[TeacherId].ClsAlt[Day][5][1]=SubId;
                        Teacher[TeacherId].ClsAlt[Day][6][1]=SubId;
                        
                        Class[ClassId].Alt[Day][4][0]=SubId;
                        Class[ClassId].Alt[Day][5][0]=SubId;
                        Class[ClassId].Alt[Day][6][0]=SubId;
                        
                        Class[ClassId].Alt[Day][4][1]=TeacherId;
                        Class[ClassId].Alt[Day][5][1]=TeacherId;
                        Class[ClassId].Alt[Day][6][1]=TeacherId;
                        
                        Class[ClassId].LabAssigned[Day] = 1;
                        
                        Flag=true;
                        
                        
                                
                    }
                    
                    else if((Lab[LCounter].Alt[Day][1]==null)&&(Lab[LCounter].Alt[Day][2]==null)&&(Lab[LCounter].Alt[Day][3]==null)&&(Teacher[TeacherId].ClsAlt[Day][1]==null)&&(Teacher[TeacherId].ClsAlt[Day][2][0]==null)&&(Teacher[TeacherId].ClsAlt[Day][3][0]==null)&&(Class[ClassId].Alt[Day][1][0]==null)&&(Class[ClassId].Alt[Day][2][0]==null)&&(Class[ClassId].Alt[Day][3][0]==null)&&(Class[ClassId].LabAssigned[Day]==null)){
                        
                        //System.out.println("Two");
                        
                        Lab[LCounter].Alt[Day][1]=ClassId;
                        Lab[LCounter].Alt[Day][2]=ClassId;
                        Lab[LCounter].Alt[Day][3]=ClassId;
                        
                        Teacher[TeacherId].ClsAlt[Day][1][0]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][2][0]=ClassId;
                        Teacher[TeacherId].ClsAlt[Day][3][0]=ClassId;
                        
                        Class[ClassId].Alt[Day][1][0]=SubId;
                        Class[ClassId].Alt[Day][2][0]=SubId;
                        Class[ClassId].Alt[Day][3][0]=SubId;
                        
                        Class[ClassId].Alt[Day][1][1]=TeacherId;
                        Class[ClassId].Alt[Day][2][1]=TeacherId;
                        Class[ClassId].Alt[Day][3][1]=TeacherId;
                        
                        Class[ClassId].LabAssigned[Day] = 1;
                        
                        Flag=true;
                        
                        
                                
                    }
                    
                    else
                        Day=(int)(Math.random()*5);
                    
                    
                    Try++;
                }
                
                                                                           
            }
        }
        
        return Flag;
        
    }
    
    
    
    public static void ElectiveAllotment(){
        
        Integer TCounter,SCounter,CCounter,CurrentElective,CurrentSubId,Counter,ObCounter;
        boolean Flag;
        Teachers CurrentTeacher;
        
        for(ObCounter=0;ObCounter<6;ObCounter++){
            CS[ObCounter] = new Elective();
        }
        
        for(ObCounter=0;ObCounter<6;ObCounter++){
            EC[ObCounter] = new Elective();
        }
        
        for(ObCounter=0;ObCounter<6;ObCounter++){
            EEE[ObCounter] = new Elective();
        }
        
        for(ObCounter=0;ObCounter<6;ObCounter++){
            AEI[ObCounter] = new Elective();
        }
        
        for(ObCounter=0;ObCounter<6;ObCounter++){
            IT[ObCounter] = new Elective();
        }
        
          
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){//To traverse through Teacher Objects
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            //System.out.println("One");
                        
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){
                
                //System.out.println("Two");
                CurrentSubId=CurrentTeacher.SCId[SCounter][0];
                Flag=false;
                
                //System.out.println(SCounter + "  ");
                
                if(Subject[CurrentTeacher.SCId[SCounter][0]].ElectiveNo!=null){
                    
                    CurrentElective = Subject[CurrentTeacher.SCId[SCounter][0]].ElectiveNo;
                    
                     for(CCounter=1;(CCounter<5)&&(CurrentTeacher.SCId[SCounter][CCounter]!=null);CCounter++){
                         
                         if(Class[CurrentTeacher.SCId[SCounter][CCounter]].Dept.equals("CS")){
                             
                             //System.out.println("Yaay CS");
                             
                             
                             CS[CurrentElective].SubjectId = CurrentTeacher.SCId[SCounter][0];
                             //System.out.println("Coutner:" +CS[CurrentElective].Counter + "Teacher:" + CurrentTeacher.Name + "Class:" + Class[CurrentTeacher.SCId[SCounter][CCounter]].Id);
                             CS[CurrentElective].TeacherClassId[CS[CurrentElective].Counter][0] = CurrentTeacher.Id;
                             CS[CurrentElective].TeacherClassId[CS[CurrentElective].Counter][1] = CurrentTeacher.SCId[SCounter][CCounter];
                             CS[CurrentElective].Counter++;
                                                          
                         }
                         
                         if(Class[CurrentTeacher.SCId[SCounter][CCounter]].Dept.equals("IT")){
                             
                             
                             IT[CurrentElective].SubjectId = CurrentTeacher.SCId[SCounter][0];
                             IT[CurrentElective].TeacherClassId[IT[CurrentElective].Counter][0] = CurrentTeacher.Id;
                             IT[CurrentElective].TeacherClassId[IT[CurrentElective].Counter][1] = CurrentTeacher.SCId[SCounter][CCounter];
                             IT[CurrentElective].Counter++;
                                                          
                         }
                         
                         if(Class[CurrentTeacher.SCId[SCounter][CCounter]].Dept.equals("EC")){
                             
                             
                             EC[CurrentElective].SubjectId = CurrentTeacher.SCId[SCounter][0];
                             EC[CurrentElective].TeacherClassId[EC[CurrentElective].Counter][0] = CurrentTeacher.Id;
                             EC[CurrentElective].TeacherClassId[EC[CurrentElective].Counter][1] = CurrentTeacher.SCId[SCounter][CCounter];
                             EC[CurrentElective].Counter++;
                                                          
                         }
                         
                         if(Class[CurrentTeacher.SCId[SCounter][CCounter]].Dept.equals("EEE")){
                             
                             
                             EEE[CurrentElective].SubjectId = CurrentTeacher.SCId[SCounter][0];
                             EEE[CurrentElective].TeacherClassId[EEE[CurrentElective].Counter][0] = CurrentTeacher.Id;
                             EEE[CurrentElective].TeacherClassId[EEE[CurrentElective].Counter][1] = CurrentTeacher.SCId[SCounter][CCounter];
                             EEE[CurrentElective].Counter++;
                                                          
                         }
                         
                         if(Class[CurrentTeacher.SCId[SCounter][CCounter]].Dept.equals("AEI")){
                             
                             
                             AEI[CurrentElective].SubjectId = CurrentTeacher.SCId[SCounter][0];
                             AEI[CurrentElective].TeacherClassId[AEI[CurrentElective].Counter][0] = CurrentTeacher.Id;
                             AEI[CurrentElective].TeacherClassId[AEI[CurrentElective].Counter][1] = CurrentTeacher.SCId[SCounter][CCounter];
                             AEI[CurrentElective].Counter++;
                                                          
                         }
                     }
                }
            }
        }
        
       for(Counter=1;Counter<6;Counter++){
           if(CS[Counter].SubjectId!=null)
               ElectiveAssign(CS[Counter]);
           if(IT[Counter].SubjectId!=null)
               ElectiveAssign(IT[Counter]);
           if(EC[Counter].SubjectId!=null)
               ElectiveAssign(EC[Counter]);
           if(EEE[Counter].SubjectId!=null)
               ElectiveAssign(EEE[Counter]);
           if(AEI[Counter].SubjectId!=null)
               ElectiveAssign(AEI[Counter]);
           
       } 
        
    }
    
    
    public static void ElectiveAssign(Elective ElectiveObject){
        
        Integer PeriodRnd,DayRnd,MaxDayTry,MaxWeekTry,MaxClassTry;
        
        Integer MaxHrs,AltHrs;
        
        PeriodRnd=(int)(Math.random()*7);
        DayRnd = (int)(Math.random()*5);
        
        MaxHrs = Subject[ElectiveObject.SubjectId].HrsPerWeek;
        
        AltHrs = 0;
        
        MaxClassTry=0;
        
        
        while((AltHrs<MaxHrs)&&(MaxClassTry<5)){
            
            
        
            MaxWeekTry=0;
            
            while((AltHrs<MaxHrs)&&(MaxWeekTry<20)){

                MaxDayTry=0;

                while((AltHrs<MaxHrs) && (MaxDayTry<10)){

                    if(ElectiveObject.Counter==5){

                        if(   (((Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[2][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[3][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[3][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[4][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[4][0]].ClsAlt[DayRnd][PeriodRnd][0]==null)) 
                            &&(Class[ElectiveObject.TeacherClassId[0][1]].ConsecutiveCheck(DayRnd,PeriodRnd,ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].FirstPeriodCheck(ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].MaxTwoPeriodCheck(DayRnd,ElectiveObject.SubjectId))
                            ){

                            Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[0][1];
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[1][1];
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[2][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[2][1];
                            Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[3][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[3][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[3][1];
                            Teacher[ElectiveObject.TeacherClassId[3][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[4][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[4][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[4][1];
                            Teacher[ElectiveObject.TeacherClassId[4][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            AltHrs++;
                            
                            break;

                        }

                        else{

                            PeriodRnd=(int)(Math.random()*7);


                        }

                    }

                    if(ElectiveObject.Counter==4){

                        if(   (((Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[2][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[3][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[3][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(Class[ElectiveObject.TeacherClassId[0][1]].ConsecutiveCheck(DayRnd,PeriodRnd,ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].FirstPeriodCheck(ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].MaxOnePeriodCheck(DayRnd,ElectiveObject.SubjectId))
                            ){

                            Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[0][1];
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[1][1];
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[2][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[2][1];
                            Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[3][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[3][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[3][1];
                            Teacher[ElectiveObject.TeacherClassId[3][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;
                            
                            AltHrs++;
                            
                            break;

                        }

                        else{

                            PeriodRnd=(int)(Math.random()*7);


                        }

                    }

                    if(ElectiveObject.Counter==3){


                        if(   (((Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[2][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(Class[ElectiveObject.TeacherClassId[0][1]].ConsecutiveCheck(DayRnd,PeriodRnd,ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].FirstPeriodCheck(ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].MaxOnePeriodCheck(DayRnd,ElectiveObject.SubjectId))
                            ){


                            Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[0][1];
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[1][1];
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[2][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[2][1];
                                 Teacher[ElectiveObject.TeacherClassId[2][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;


                            AltHrs++;
                            
                            break;

                        }

                        else{
                            PeriodRnd=(int)(Math.random()*7);
                        }

                    }

                    if(ElectiveObject.Counter==2){
                        
//                        System.out.println("Teacher:" + ElectiveObject.TeacherClassId[0][0]); 
//                        System.out.println("Class:" + ElectiveObject.TeacherClassId[0][1]);
//                        System.out.println("Teacher:" + ElectiveObject.TeacherClassId[1][0]); 
//                        System.out.println("Class:" + ElectiveObject.TeacherClassId[1][1]);
                        
//                        System.out.println("Subject:" + ElectiveObject.SubjectId);
                        
                        if(   (((Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(((Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(Class[ElectiveObject.TeacherClassId[0][1]].ConsecutiveCheck(DayRnd,PeriodRnd,ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].FirstPeriodCheck(ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].MaxOnePeriodCheck(DayRnd,ElectiveObject.SubjectId))
                              ){

                            Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[0][1];
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            Class[ElectiveObject.TeacherClassId[1][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[1][1];
                             Teacher[ElectiveObject.TeacherClassId[1][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            AltHrs++;
                            
                            break;
                            

                        }

                        else{

                            PeriodRnd=(int)(Math.random()*7);
                            


                        }

                    }

                    if(ElectiveObject.Counter==1){

                        if( (((Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0]==null)  )&&(Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0]==null))
                            &&(Class[ElectiveObject.TeacherClassId[0][1]].ConsecutiveCheck(DayRnd,PeriodRnd,ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].FirstPeriodCheck(ElectiveObject.SubjectId))&&(Class[ElectiveObject.TeacherClassId[0][1]].MaxOnePeriodCheck(DayRnd,ElectiveObject.SubjectId))
                            ){

                            Class[ElectiveObject.TeacherClassId[0][1]].Alt[DayRnd][PeriodRnd][0] = ElectiveObject.SubjectId;
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][0] = ElectiveObject.TeacherClassId[0][1];
                            Teacher[ElectiveObject.TeacherClassId[0][0]].ClsAlt[DayRnd][PeriodRnd][1] = ElectiveObject.SubjectId;

                            AltHrs++;
                            
                            break;

                        }

                        else{

                            PeriodRnd=(int)(Math.random()*7);


                        }
                    }

                MaxDayTry++;    
                }

            MaxWeekTry++;
            DayRnd = (int)(Math.random()*5);
            }
        
        MaxClassTry++;
        PeriodRnd=(int)(Math.random()*7);
        DayRnd = (int)(Math.random()*5);    
        }
        
         if(AltHrs<MaxHrs){
                
                        
            Class[ElectiveObject.TeacherClassId[0][1]].Unallotted[Class[ElectiveObject.TeacherClassId[0][1]].UnallottedCount][0] = ElectiveObject.TeacherClassId[0][0];
            Class[ElectiveObject.TeacherClassId[0][1]].Unallotted[Class[ElectiveObject.TeacherClassId[0][1]].UnallottedCount][1] = ElectiveObject.SubjectId;
            Class[ElectiveObject.TeacherClassId[0][1]].Unallotted[Class[ElectiveObject.TeacherClassId[0][1]].UnallottedCount][2] = MaxHrs-AltHrs;
            Class[ElectiveObject.TeacherClassId[0][1]].UnallottedCount++;

            //System.out.println("Class: " + Class[ElectiveObject.TeacherClassId[0][1]].Sem + Class[ElectiveObject.TeacherClassId[0][1]].Dept + Class[ElectiveObject.TeacherClassId[0][1]].Div + "  UnallottedCount: " +   Class[ElectiveObject.TeacherClassId[0][1]].UnallottedCount + "  Teacher: " + Teacher[ElectiveObject.TeacherClassId[0][0]].Name + "  CurrentSubId: " + Subject[ElectiveObject.SubjectId].Name + "  Unallotted: " + (MaxHrs-AltHrs));
            TimeTableGen.UnallottedBreak++;

            
        }
    }
    
    public static void SepAssign(){
        
        Integer CCounter;
        
        
                
        for(CCounter=0;CCounter<Constants.CMAX;CCounter++){
            
            
            if(Class[CCounter].Dept.equals("CS"))
                Class[CCounter].Alt[0][6][0]=16;
            if(Class[CCounter].Dept.equals("IT"))
                Class[CCounter].Alt[1][6][0]=16;
            if(Class[CCounter].Dept.equals("EEE"))
                Class[CCounter].Alt[2][6][0]=16;
            if(Class[CCounter].Dept.equals("AEI"))
                Class[CCounter].Alt[3][6][0]=16;
            if(Class[CCounter].Dept.equals("EC"))
                Class[CCounter].Alt[4][6][0]=16;
            
        } 
    }
    
    
    
    public static void Unallotted(){
        Integer CCounter,NoUnallotted,Day,Period;
        for(CCounter=0;CCounter<Constants.CMAX;CCounter++){
            
            NoUnallotted=0;
            
            for(Day=0;Day<5;Day++){
                
                for(Period=0;Period<7;Period++){
                    
                    if(Class[CCounter].Alt[Day][Period][0]==null)
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
                    if(Class[ObCounter].Alt[Day][Period][2]==null){
                    
                    Class[ObCounter].Alt[Day][Period][0]=null;
                    Class[ObCounter].Alt[Day][Period][1]=null;
                    
                }
                    
                    
                }
            }
            Class[ObCounter].UnallottedCount = 0;
        }
        
        //Clear the Teacher Allotments in Teachers class
        
        for(ObCounter=0;ObCounter<Constants.TMAX;ObCounter++){
            for(Day=0;Day<5;Day++)
            {
                for(Period=0;Period<7;Period++)
                {
                    if(Teacher[ObCounter].ClsAlt[Day][Period][2]==null){
                        
                        Teacher[ObCounter].ClsAlt[Day][Period][0]=null;
                        Teacher[ObCounter].ClsAlt[Day][Period][1]=null;
                        
                    }
                    
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
        
        //Clear all Elective Objects
        /*
        for(ObCounter=0;ObCounter<4;ObCounter++){
            
                CS[ObCounter] = null;
            
        }
        
        for(ObCounter=0;ObCounter<4;ObCounter++){
            
                EC[ObCounter] = null;
            
        }
        
        for(ObCounter=0;ObCounter<4;ObCounter++){
            
                EEE[ObCounter] = null;
            
        }
        
        for(ObCounter=0;ObCounter<4;ObCounter++){
            
                IT[ObCounter] = null;
            
        }
        
        for(ObCounter=0;ObCounter<4;ObCounter++){
            
                AEI[ObCounter] = null;
            
        }
        */
        
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
                    if(Class[ObCounter].Alt[Day][Period][0]!=null){
                        if(Class[ObCounter].Alt[Day][Period][1]!=null)
                            System.out.print(Subject[Class[ObCounter].Alt[Day][Period][0]].ShortName + "/" + Teacher[Class[ObCounter].Alt[Day][Period][1]].TCode + "     ");  
                        else                            
                            System.out.print(Subject[Class[ObCounter].Alt[Day][Period][0]].ShortName + "/" + "N.S" + "     ");
                        
                        
                    }
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
                    if(Teacher[ObCounter].ClsAlt[Day][Period][0]!=null)
                        System.out.print(Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Dept + Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Sem + Class[Teacher[ObCounter].ClsAlt[Day][Period][0]].Div + "     ");
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
    
    
    public static void CountTotalInputHours(){
        
        Integer TCounter,SCounter,CCounter,SubHrs,CurrentSubId,TotalHrs,Day,Period,FixedHrs;
        Teachers CurrentTeacher;
        List ENos = new ArrayList();
        TotalHrs=0;
        FixedHrs=0;
        
        for(TCounter=0;TCounter<Constants.TMAX;TCounter++){
            
            CurrentTeacher = Teacher[TCounter];//Teacher Object being operated on at the present
            
            //System.out.println("Teacher: " + Teacher[TCounter].Name + TCounter);
            //System.out.println("Test");
            for(SCounter=0;CurrentTeacher.SCId[SCounter][0]!=null;SCounter++){//To traverse through the Subjects of the Teacher
                
                      
                CurrentSubId=CurrentTeacher.SCId[SCounter][0];
                SubHrs=Subject[CurrentSubId].HrsPerWeek;
                
                //System.out.println(CurrentSubId);
                                
                for(CCounter=1;CCounter<5;CCounter++){//To traverse throught the Classes of the Teacher
                    
                    if(CurrentTeacher.SCId[SCounter][CCounter]==null)
                        continue;
                    else if(Subject[CurrentSubId].ElectiveNo == null) 
                        TotalHrs = TotalHrs + SubHrs;
                    else if(!ENos.contains(Subject[CurrentSubId].ElectiveNo)){
                        TotalHrs = TotalHrs + SubHrs;
                        ENos.add(Subject[CurrentSubId].ElectiveNo);
                    }
                }
            }  
        }
        
        for(CCounter = 0; CCounter < Constants.CMAX; CCounter++){
            
            for(Day = 0; Day < 5; Day++){
                for(Period = 0; Period < 7; Period++){
                    if(Class[CCounter].Alt[Day][Period][2] != null)
                        FixedHrs++;
                        
                }
            }
        }
        
        
        System.out.println("Total Input Hours: " + TotalHrs);
        System.out.println("Total Required Hours: " + Constants.CMAX*35);
        System.out.println("Total Fixed Hours: " + FixedHrs);
        System.out.println("Deficit:" + ((Constants.CMAX*35)-(TotalHrs+FixedHrs)));
        
    }
    
    public static void UnallottedIdentify(){
        
        Integer Counter,i;
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        
        for(Counter=0;Counter<Constants.CMAX;Counter++){
            
            
            
            for(i = 0; i < Class[Counter].UnallottedCount ; i++){
                
                System.out.println("Class Name: " + Class[Counter].Sem + Class[Counter].Dept + Class[Counter].Div);
                //System.out.println("UnallottedCount: " + Class[Counter].UnallottedCount);
                System.out.println("Teacher: " + Teacher[Class[Counter].Unallotted[i][0]].Name);
                System.out.println("Subject: " + Subject[Class[Counter].Unallotted[i][1]].Name);
                System.out.println("Count: " + Class[Counter].Unallotted[i][2]);
                System.out.println();
                
            }
        }        
              
    }
    
    
    public static void CheckViolation(){
        
        System.out.println();        
        System.out.println("Violations in Fixed Class: ");
        System.out.println();  
        
        for(Integer CCounter = 0; CCounter < Constants.CMAX; CCounter++){
            
                   
            for(Integer Day = 0; Day < 5; Day++){

                for(Integer Period = 0; Period < 7; Period++){
                    
                    
                    
                    if(Class[CCounter].Alt[Day][Period][2] != null){
                                         
                        if((!Class[CCounter].ConsecutiveCheck(Day,Period,Class[CCounter].Alt[Day][Period][0])) && Subject[Class[CCounter].Alt[Day][Period][0]].Duration != 3){
                            System.out.println("Consecutive Period violated in " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div + " for subject " +
                                    Subject[Class[CCounter].Alt[Day][Period][0]].Name + " on Day " + (Day + 1) + ", Period " + (Period + 1));
                        }
                        
                        if( (!Class[CCounter].FirstPeriodViolation(Class[CCounter].Alt[Day][Period][0])) ){
                        
                            System.out.println("First Period violated in " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div 
                                    + " for subject " + Subject[Class[CCounter].Alt[Day][Period][0]].Name + " on Day " + (Day + 1) + ", Period " + (Period + 1));                       
                        }
                        
                        if(Class[CCounter].Alt[Day][Period][1] != null){
                            
                           
                            
                            if( (!Teacher[Class[CCounter].Alt[Day][Period][1]].MaxWorkloadViolation(Day)) && Subject[Class[CCounter].Alt[Day][Period][0]].Duration != 3){
                        
                            System.out.println("Max Workload violated in " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div 
                                    + " for teacher " + Teacher[Class[CCounter].Alt[Day][Period][1]].Name + " for subject " + Subject[Class[CCounter].Alt[Day][Period][0]].Name + " on Day " + (Day + 1) + ", Period " + (Period + 1));                       
                            }
                            
                        }
                        
                        if( (!Class[CCounter].TwoPeriodViolation(Day,Class[CCounter].Alt[Day][Period][0]) ) && Subject[Class[CCounter].Alt[Day][Period][0]].Duration != 3){
                        
                            System.out.println("Max Two Period violated in " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div 
                                    + " for subject " + Subject[Class[CCounter].Alt[Day][Period][0]].Name + " on Day " + (Day + 1) + ", Period " + (Period + 1));                       
                        }
                        
                        /*
                        if(Class[CCounter].Alt[Day][Period][1] != null){
                            
                           
                            
                            if( (!Teacher[Class[CCounter].Alt[Day][Period][1]].ConsecutiveCheck(Day,Period,Class[CCounter].Id)) && Subject[Class[CCounter].Alt[Day][Period][0]].Duration != 3){
                        
                            System.out.println("Teacher Consecutive Period Violated in " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div 
                                    + " for teacher " + Teacher[Class[CCounter].Alt[Day][Period][1]].Name + " for subject " + Subject[Class[CCounter].Alt[Day][Period][0]].Name + " on Day " + (Day + 1) + ", Period " + (Period + 1));                       
                            }
                            
                        }*/
                                
                        
                                            
                        
                    }
                    
                    
                }
            }
       }
        
    }
    
    
    public static void Swap(){
        
        Integer UC,UTeacher,USub,Day,Period,CCounter,UDP,CheckDay,CheckPeriod,SwapTeacher,SwapSub;
        List UDay = new ArrayList();
        List UPeriod = new ArrayList();
        Boolean Flag;
        
        
        
        
        
        for(CCounter = 0; CCounter < Constants.CMAX; CCounter++){
            
            UDay.clear();
            UPeriod.clear();
            
            for(Day = 0; Day < 5; Day++){
                    
                    for(Period =0; Period < 7; Period++){
                           
                        if(Class[CCounter].Alt[Day][Period][0] == null){
                            
                            UDay.add(Day);
                            UPeriod.add(Period);
                            
                           
                        }
                        
                    }
            }
            
            for(UC = 0; UC < Class[CCounter].UnallottedCount; UC++){
                
                Flag=false;
                
                if(Class[CCounter].Unallotted[UC][0]==-1)
                    continue;
                
                UTeacher = Class[CCounter].Unallotted[UC][0];
                USub = Class[CCounter].Unallotted[UC][1];
                
                //System.out.println("CCounter: " + CCounter + " UTeacher: " + Teacher[UTeacher].Name + " USub: " + Subject[USub].Name);
                
                //System.out.println("Class Name: " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div + " Teacher: " + Teacher[UTeacher].Name + " Subject: " + Subject[USub].Name);
                
                for(Day = 0; Day < 5; Day++){
                    
                    for(Period =0; Period < 7; Period++){
                        
                        for(UDP = 0; UDP < UDay.size(); UDP++){
                            
                            CheckDay = (Integer) UDay.get(UDP);
                            CheckPeriod = (Integer) UPeriod.get(UDP);
                            
                            //System.out.println("CheckDay: " + CheckDay + " CheckPeriod: " + CheckPeriod);
                            
                            SwapSub = Class[CCounter].Alt[Day][Period][0];
                            SwapTeacher = Class[CCounter].Alt[Day][Period][1];
                            
                            //System.out.println("SwapTeacher: " + Teacher[SwapTeacher].Name);
                            
                           
                            if((Class[CCounter].ConsecutiveCheck(Day,Period,USub))                                    
                                        &&(Class[CCounter].FirstPeriodCheck(USub))
                                        &&(Class[CCounter].MaxTwoPeriodCheck(Day,USub))
                                        &&(Teacher[UTeacher].MaxWorkloadPerDayForTheory(Day))
                                        &&(Teacher[UTeacher].ConsecutiveCheck(Day,Period,CCounter))
                                    
                                        &&SwapTeacher!=null
                                        &&Subject[SwapSub].Duration!=3
                                        &&Subject[SwapSub].ElectiveNo==null
                                        &&Class[CCounter].Alt[CheckDay][CheckPeriod][2]==null
                                        &&Teacher[SwapTeacher].ClsAlt[CheckDay][CheckPeriod][2]==null
                                        &&(Class[CCounter].ConsecutiveCheck(CheckDay,CheckPeriod,SwapSub))
                                        &&(Class[CCounter].FirstPeriodCheck(SwapSub))
                                        &&(Class[CCounter].MaxPeriodCheck(CheckDay,SwapSub))
                                        &&(Teacher[SwapTeacher].MaxWorkloadPerDayForTheory(CheckDay))
                                        &&(Teacher[SwapTeacher].ConsecutiveCheck(CheckDay,CheckPeriod,CCounter))){
                                
                                Class[CCounter].Alt[Day][Period][0] = USub;
                                Class[CCounter].Alt[Day][Period][1] = UTeacher;
                                Teacher[UTeacher].ClsAlt[Day][Period][0] = CCounter;
                                Teacher[UTeacher].ClsAlt[Day][Period][1] = USub;
                                
                                Class[CCounter].Alt[CheckDay][CheckPeriod][0] = SwapSub;
                                Class[CCounter].Alt[CheckDay][CheckPeriod][1] = SwapTeacher;
                                Teacher[SwapTeacher].ClsAlt[CheckDay][CheckPeriod][0] = CCounter;
                                Teacher[SwapTeacher].ClsAlt[CheckDay][CheckPeriod][1] = SwapSub;
                                
                                Class[CCounter].Unallotted[UC][0] = -1;
                                Class[CCounter].Unallotted[UC][1] = -1;
                                
                                UDay.remove(UDP);
                                UPeriod.remove(UDP);
                                
                                                           
                                System.out.println("From: " + (Day+1) + "  " + (Period+1) + "  " + Subject[USub].Name + " To: " + (CheckDay+1) + "  " + (CheckPeriod+1) + "  " + Subject[SwapSub].Name);
                                
                                        
                                Flag = true;
                                break;
                                
                            }
                        }
                        
                        if(Flag==true) break;
                    }
                    
                    if(Flag==true) break;
                }
            }
        }
        
        
    }
    
    
    
    public static void SwapLevel2(){
        
        Integer UC,UTeacher,USub,Day,Period,CCounter,UDP,CheckDay,CheckPeriod,SwapTeacher,SwapSub,SwapTeacher2,SwapSub2,Day2,Period2;
        List UDay = new ArrayList();
        List UPeriod = new ArrayList();
        Boolean Flag;
        
        
        
        
        
        for(CCounter = 0; CCounter < Constants.CMAX; CCounter++){
            
            UDay.clear();
            UPeriod.clear();
            
            for(Day = 0; Day < 5; Day++){
                    
                    for(Period =0; Period < 7; Period++){
                           
                        if(Class[CCounter].Alt[Day][Period][0] == null){
                            
                            UDay.add(Day);
                            UPeriod.add(Period);
                            
                           
                        }
                        
                    }
            }
            
            for(UC = 0; UC < Class[CCounter].UnallottedCount; UC++){
                
                Flag=false;
                
                if(Class[CCounter].Unallotted[UC][0]==-1)
                    continue;
                
                UTeacher = Class[CCounter].Unallotted[UC][0];
                USub = Class[CCounter].Unallotted[UC][1];
                
                //System.out.println("CCounter: " + CCounter + " UTeacher: " + Teacher[UTeacher].Name + " USub: " + Subject[USub].Name);
                
                //System.out.println("Class Name: " + Class[CCounter].Sem + Class[CCounter].Dept + Class[CCounter].Div + " Teacher: " + Teacher[UTeacher].Name + " Subject: " + Subject[USub].Name);
                
                for(Day = 0; Day < 5; Day++){
                    
                    for(Period = 0; Period < 7; Period++){
                        
                        for(Day2 = 0; Day2 < 5; Day2++){
                            
                            for(Period2 = 0; Period2 <7; Period2++){
                                
                                for(UDP = 0; UDP < UDay.size(); UDP++){

                                    CheckDay = (Integer) UDay.get(UDP);
                                    CheckPeriod = (Integer) UPeriod.get(UDP);

                                    //System.out.println("CheckDay: " + CheckDay + " CheckPeriod: " + CheckPeriod);

                                    SwapSub = Class[CCounter].Alt[Day][Period][0];
                                    SwapTeacher = Class[CCounter].Alt[Day][Period][1];
                                    
                                    SwapSub2 = Class[CCounter].Alt[Day2][Period2][0];
                                    SwapTeacher2 = Class[CCounter].Alt[Day2][Period2][1];

                                    //System.out.println("SwapTeacher: " + Teacher[SwapTeacher].Name);


                                    if((Class[CCounter].ConsecutiveCheck(Day,Period,USub))                                    
                                                &&(Class[CCounter].FirstPeriodCheck(USub))
                                                &&(Class[CCounter].MaxTwoPeriodCheck(Day,USub))
                                                &&(Teacher[UTeacher].MaxWorkloadPerDayForTheory(Day))
                                                &&(Teacher[UTeacher].ConsecutiveCheck(Day,Period,CCounter))
                                                
                                            
                                                &&SwapTeacher!=null
                                                &&Subject[SwapSub].Duration!=3
                                                &&Subject[SwapSub].ElectiveNo==null
                                                &&(Class[CCounter].ConsecutiveCheck(Day2,Period2,SwapSub))                                    
                                                &&(Class[CCounter].FirstPeriodCheck(SwapSub))
                                                &&(Class[CCounter].MaxTwoPeriodCheck(Day2,SwapSub))
                                                &&(Teacher[SwapTeacher].MaxWorkloadPerDayForTheory(Day2))
                                                &&(Teacher[SwapTeacher].ConsecutiveCheck(Day2,Period2,CCounter))
                                                

                                                &&SwapTeacher2!=null
                                                &&Subject[SwapSub2].Duration!=3
                                                &&Subject[SwapSub2].ElectiveNo==null
                                                &&Class[CCounter].Alt[CheckDay][CheckPeriod][2]==null
                                                &&Teacher[SwapTeacher2].ClsAlt[CheckDay][CheckPeriod][2]==null
                                                &&(Class[CCounter].ConsecutiveCheck(CheckDay,CheckPeriod,SwapSub2))
                                                &&(Class[CCounter].FirstPeriodCheck(SwapSub2))
                                                &&(Class[CCounter].MaxPeriodCheck(CheckDay,SwapSub2))
                                                &&(Teacher[SwapTeacher2].MaxWorkloadPerDayForTheory(CheckDay))
                                                &&(Teacher[SwapTeacher2].ConsecutiveCheck(CheckDay,CheckPeriod,CCounter))){

                                        Class[CCounter].Alt[Day][Period][0] = USub;
                                        Class[CCounter].Alt[Day][Period][1] = UTeacher;
                                        Teacher[UTeacher].ClsAlt[Day][Period][0] = CCounter;
                                        Teacher[UTeacher].ClsAlt[Day][Period][1] = USub;
                                        
                                        Class[CCounter].Alt[Day2][Period2][0] = SwapSub;
                                        Class[CCounter].Alt[Day2][Period2][1] = SwapTeacher;
                                        Teacher[SwapTeacher].ClsAlt[Day2][Period2][0] = CCounter;
                                        Teacher[SwapTeacher].ClsAlt[Day2][Period2][1] = SwapSub;

                                        Class[CCounter].Alt[CheckDay][CheckPeriod][0] = SwapSub2;
                                        Class[CCounter].Alt[CheckDay][CheckPeriod][1] = SwapTeacher2;
                                        Teacher[SwapTeacher2].ClsAlt[CheckDay][CheckPeriod][0] = CCounter;
                                        Teacher[SwapTeacher2].ClsAlt[CheckDay][CheckPeriod][1] = SwapSub2;

                                        Class[CCounter].Unallotted[UC][0] = -1;
                                        Class[CCounter].Unallotted[UC][1] = -1;

                                        UDay.remove(UDP);
                                        UPeriod.remove(UDP);

                                                                    
                                        //System.out.println("From: " + (Day+1) + "  " + (Period+1) + "  " + Subject[USub].Name + " To: " + (CheckDay+1) + "  " + (CheckPeriod+1) + "  " + Subject[SwapSub].Name);
                                        System.out.println("Hai");    

                                        Flag = true;
                                        break;

                                    }
                                }
                            }    
                        }        
                        if(Flag==true) break;
                    }
                    
                    if(Flag==true) break;
                }
            }
        }
        
        
    }
        

}




public class TimeTable extends Thread {
    
    public void run(){
        
        GenerationIteration();      
              
    }      
    
    
    
    public static void GenerationIteration(){
        
        String Input;
        Input = "C:\\TimeTableManagement\\TimeTable\\Excel Input Files\\The Input Modified.xls";
        
        Integer Generation=1;
        Integer LowestUnallotted=10000;
        Integer MaxUnallotted=0;
        Integer MaxOccur=0;
        Integer Occurrences=0;
        Integer NumUnallotted = 5;
        Integer i;
        Float c;
        Integer Count[] = new Integer[31];
        Integer TotalCount = 0;
        
        if(!"".equals(GUI.getTextField())){
            NumUnallotted = Integer.parseInt(GUI.getTextField()); 
        }
        
        for(i=1;i<=30;i++)
            Count[i]=0;
           
        //TimeTableGen.Create();
        
        TimeTableGen.ExcelInput(Input);
        //TimeTableGen.ExcelInput(GUI.Input);
        
        TimeTableGen.CountTotalInputHours();  
        
        TimeTableGen.CheckViolation();
        
              
        
        TimeTableGen.ExcelOutput();
        
        
        do{
            
            TimeTableGen.UnallottedBreak = 0;
            TimeTableGen.TotalUnallotted = 0;
            TimeTableGen.ClearTables();
            //TimeTableGen.SepAssign();
            TimeTableGen.LabAllotment();
            TimeTableGen.ElectiveAllotment();
            TimeTableGen.ClassAllotment();
            
             
                
            TimeTableGen.Unallotted();
            
            
            
            if(TimeTableGen.TotalUnallotted<LowestUnallotted){
             LowestUnallotted=TimeTableGen.TotalUnallotted;
             Occurrences=0;
             TimeTableGen.ExcelOutput();
             TimeTableGen.UnallottedIdentify();
             
            }
                                    
            if(TimeTableGen.TotalUnallotted==LowestUnallotted){
                Occurrences++;
            }
                
            
            
            if(TimeTableGen.TotalUnallotted>MaxUnallotted){
             MaxUnallotted=TimeTableGen.TotalUnallotted;
             MaxOccur=0;
            }
            
            if(TimeTableGen.TotalUnallotted==MaxUnallotted)
                MaxOccur++;
            
            
            
            for(i=1;i<=30;i++){
                if(TimeTableGen.TotalUnallotted==i)
                    Count[i]++;
            }
                    
            String Output;
            Output = "Generation: " + Generation + ": " + TimeTableGen.TotalUnallotted + " Min: " + LowestUnallotted + " Occ: " + Occurrences + " Max: " + MaxUnallotted + " Occ: " + MaxOccur;
            //System.out.println(Output);
            GUI.SetLabelText(Output);
                       
            //System.out.println("  1: " + Count[1] + "  2: " + Count[2] + "  3: " + Count[3] + "  4: " + Count[4] + "  5: " + Count[5] + "  6: " + Count[6] + "  7: " + Count[7] + "  8: " + Count[8]);
            
            Generation++;
            
            /*try{
                Thread.sleep(0,1);
            }catch(Exception E){
                System.out.println("Exception in Threads: " + E);
            }*/
            
        }while(TimeTableGen.TotalUnallotted>NumUnallotted);
        
        
        TimeTableGen.ExcelOutput();
        
        TimeTableGen.Swap();
        TimeTableGen.ExcelOutput2();
        
        TimeTableGen.SwapLevel2();
        TimeTableGen.ExcelOutput3();
        
        
        
        
        System.out.println("");
        System.out.println("TimeTable of Generation " + (Generation-1) + " is:");
        TimeTableGen.Display();
        
        
       
        for(i=1;i<=30;i++){
            TotalCount = TotalCount + Count[i];
        }
        
        System.out.println("");
        
        for(i=1;i<=30;i++){
            c = ((float)Count[i]/(float)TotalCount)*100;
            System.out.println(i + ": " + c + "%" + "    " + Count[i]);
        }
    }
}