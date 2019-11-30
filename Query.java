import com.barra.cp.bdtbeans.*;
import java.io.*;
import java.util.*; 

public class Query {
    
    static String user, pass, client, job_id, job_name, export;
    static GregorianCalendar cal;
    
    static {
        user   = "DXiu1";
        pass   = "barra2017";
        client = "ttnpg85s8i";
        
        export   = "FactorReport";
        job_name = "ByJava";
    }
    
    static void submit()
    {
        System.out.println("Submit export request ...");
        
        SubmitExportJobWithScheduleRequestDocument doc =
            SubmitExportJobWithScheduleRequestDocument.Factory.newInstance();
        
        SubmitExportJobWithScheduleRequestDocument.SubmitExportJobWithScheduleRequest job =
            doc.addNewSubmitExportJobWithScheduleRequest();
        
        job.setUser(user);
        job.setClient(client);
        job.setPassword(pass);
        job.setExportSetName(export);
        
        ScheduleExportJob sche = job.addNewScheduleExportJob();
        ScheduleOnce once = sche.addNewScheduleOnce();
        once.setJobName("ByJava");
        once.setAnalysisDate(cal);
        once.setOnlyUseUpdatedPortfolios(false);
        once.setProcessAllAssets(true);
        once.setSeed(-1);
        
        try
        {
            BDTServiceStub stub = new BDTServiceStub(); 
            doc.setSubmitExportJobWithScheduleRequest(job);
            SubmitExportJobWithScheduleResponseDocument resp =
                stub.submitExportJobWithSchedule(doc);
            job_id = resp.getSubmitExportJobWithScheduleResponse().getJobID();
            System.out.println("-> Job ID: " + job_id);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e);
            System.out.println("Error in submitting BDTService request.");
        }
    }
    
    static boolean wait_for_success()
    {   
        System.out.println("Wait for job to be completed ... ");
        
        GetExportJobStatusRequestDocument doc =
            GetExportJobStatusRequestDocument.Factory.newInstance();
        
        GetExportJobStatusRequestDocument.GetExportJobStatusRequest job = 
            doc.addNewGetExportJobStatusRequest();
        
        job.setUser(user);
        job.setClient(client);
        job.setPassword(pass);
        job.setJobID(job_id);
        
        int result = -1;
        
        try
        {
            BDTServiceStub stub = new BDTServiceStub();
            
            while(true)
            {
                GetExportJobStatusResponseDocument resp = stub.getExportJobStatus(doc);
                result = resp.getGetExportJobStatusResponse().getStatusValue();
                
                if(result>0)
                {
                    if (result>10) result = 10;
                    System.out.println("-> Wait for " + result + " more seconds ...");
                    Thread.sleep((result) * 1000);
                }
                else break;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e);
            System.out.println("Error in waiting for job status update.");
        }

        return (result == 0);
    }
    
    static void download()
    {
        System.out.println("Download exported results ... " + job_id);
        
        GetExportJobRequestDocument doc = GetExportJobRequestDocument.Factory.newInstance();
        GetExportJobRequestDocument.GetExportJobRequest job = doc.addNewGetExportJobRequest();
        
        job.setUser(user);
        job.setClient(client);
        job.setPassword(pass);
        job.setJobID(job_id);
        
        try
        {
            BDTServiceStub stub = new BDTServiceStub();
            GetExportJobResponseDocument.GetExportJobResponse resp = stub.getExportJob(doc).getGetExportJobResponse();
            
            AttachmentType obj = resp.getAttachmentExportJob();
            Base64Binary data = (Base64Binary)obj.getBinaryData();
            byte[] b = data.getByteArrayValue();
            
            File oFile = new File("./output/" + obj.getFileName());
            FileOutputStream oStream = new FileOutputStream(oFile);
            oStream.write(b);
            oStream.flush();
            oStream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e);
            System.out.println("Error in downloading BDTService results.");
        }
        
    }
    
    static void clean()
    {
        
    }
    
	public static void main(String[] args) 
    {
        int y = Integer.parseInt(args[0].substring(0,4));
        int m = Integer.parseInt(args[0].substring(4,6));
        int d = Integer.parseInt(args[0].substring(6,8));
        System.out.println("Job Date: " + y + "-" + m + "-" + d);
        cal = new GregorianCalendar(y, m-1, d);
        
        submit();
        if (wait_for_success()) 
        {
            download();
            clean();
        }
        else System.out.println("Job failed.");
	}
}
