

package com.test.jetty;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * BlockingServlet
 */
public class BlockingServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //https://stackoverflow.com/questions/3831680/httpservletrequest-get-json-post-data
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip=request.getParameter("adressInput");
        String porta=request.getParameter("portaInput");
        String sec=request.getParameter("secInput");
        int segundos=Integer.parseInt(sec);
        int i=0;
        int d=0;
        SNMPHandler snmpHandler=new SNMPHandler(ip, porta);
        String resp="";
        if(segundos<50){
            d=1;
        }else{
            if(segundos<200){
                d=5;
            }else{
                d=10;
            }
        }
        while(i<segundos){
            snmpHandler.setSec(i);
            resp =resp+snmpHandler.doWalk();
            i+=d;
            try{
                Thread.sleep(d*1000);
            }catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }

        }    
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("['Segundo','Processo','hrSWRunPerfMem','hrSWRunPerfCPU']"+resp);
    }   
}