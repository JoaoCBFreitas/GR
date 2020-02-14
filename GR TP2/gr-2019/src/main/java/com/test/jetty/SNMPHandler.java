package com.test.jetty;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.snmp4j.CommunityTarget;

import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;

import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;


/**
 * SNMPHandler
 */
public class SNMPHandler {
    private String ip;
    private int port;
    private String name = ".1.3.6.1.2.1.25.4.2.1.2";//hrSWRunName
    private String mem=".1.3.6.1.2.1.25.5.1.1.2.";//hrSWRunPerfMem
    private String cpu=".1.3.6.1.2.1.25.5.1.1.1.";//hrSWRunPerfCPU
    private String community = "public";
    private int snmpVersion = SnmpConstants.version2c;
    private CommunityTarget comtarget;
    private TransportMapping<?extends Address> transport;
    int segundos;

    public SNMPHandler(String endereco,String porta) throws IOException {
        this.ip=endereco;
        this.port=Integer.parseInt(porta);
        transport = new DefaultUdpTransportMapping();
        transport.listen();

        // Create Target Address object
        comtarget = new CommunityTarget<>();
        comtarget.setCommunity(new OctetString(community));
        comtarget.setVersion(snmpVersion);
        comtarget.setAddress(new UdpAddress(ip + "/" + port));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);

    }
    public String doWalk(){
        String res="";
        String s="";
        try{
            Map<String,String> hashname=snmpWalk(name,comtarget);
            Map<String,String> hashmem=snmpWalk(mem,comtarget);
            Map<String,String> hashcpu=snmpWalk(cpu,comtarget);
            Map<String,String> hashres=new TreeMap<>();
            for(String v: hashname.keySet()){
                hashres.put(v,hashname.get(v));
            }
            for(String v: hashmem.keySet()){
                if(hashres.containsKey(v)){
                    s=hashres.get(v);
                    s=s+","+hashmem.get(v);
                }else{
                    s=hashmem.get(v);
                }
                hashres.put(v,s);
            }
            for(String v: hashcpu.keySet()){
                if(hashres.containsKey(v)){
                    s=hashres.get(v);
                    s=s+","+hashcpu.get(v);
                }else{
                    s=hashcpu.get(v);
                }
                hashres.put(v,s);
            }
            for(String v: hashres.keySet()){
                String d=hashres.get(v);
                String sub[]=d.split(",");
                res=res+",\n['"+this.segundos+"','"+v+"-"+sub[0]+"',"+Integer.parseInt(sub[1])+","+Integer.parseInt(sub[2])+"]\n";
            }
        }catch(IOException e){}
        return res;
    }
    public Map<String, String> snmpWalk(String tableOid, CommunityTarget target) throws IOException {
        Map<String, String> result = new TreeMap<>();
        TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
 
        TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
        List<TreeEvent> events = treeUtils.getSubtree(target, new OID(tableOid));
        if (events == null || events.size() == 0) {
            System.out.println("Error: Unable to read table...");
            return result;
        }
 
        for (TreeEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.isError()) {
                System.out.println("Error: table OID [" + tableOid + "] " + event.getErrorMessage());
                continue;
            }
 
            VariableBinding[] varBindings = event.getVariableBindings();
            if (varBindings == null || varBindings.length == 0) {
                continue;
            }
            for (VariableBinding varBinding : varBindings) {
                if (varBinding == null) {
                    continue;
                }
                String v=varBinding.getOid().toString();
                String[] sub=v.split("\\.");
                String s=sub[sub.length-1];
                result.put("." + s, varBinding.getVariable().toString());
            }
 
        }
        snmp.close();
 
        return result;
    }
    public void setSec(int s){
        this.segundos=s;
    }
}