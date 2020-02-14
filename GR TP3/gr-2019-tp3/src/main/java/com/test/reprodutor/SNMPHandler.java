package com.test.reprodutor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
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
    private String ip="localhost";
    private int port=6666;
    private String community = "public";
    private int snmpVersion = SnmpConstants.version2c;
    private CommunityTarget comtarget;
    private TransportMapping transport;

    public SNMPHandler() throws IOException {
        transport = new DefaultUdpTransportMapping();
        transport.listen();
        // Create Target Address object
        comtarget = new CommunityTarget();
        comtarget.setCommunity(new OctetString(community));
        comtarget.setVersion(snmpVersion);
        comtarget.setAddress(new UdpAddress(ip + "/" + port));
        comtarget.setRetries(2);
        comtarget.setTimeout(1000);

    }
    public HashMap<Integer,Musica> doWalk(String index,String tipo, String artista, String album, String parte, String titulo,String formato, String data, String ordem, String path) throws IOException{
        Map<String,String> indexes=snmpWalk(index,this.comtarget);
        Map<String,String> tipos=snmpWalk(tipo,this.comtarget);
        Map<String,String> artistas=snmpWalk(artista,this.comtarget);
        Map<String,String> albuns=snmpWalk(album,this.comtarget);
        Map<String,String> partes=snmpWalk(parte,this.comtarget);
        Map<String,String> titulos=snmpWalk(titulo,this.comtarget);
        Map<String,String> formatos=snmpWalk(formato,this.comtarget);
        Map<String,String> datas=snmpWalk(data,this.comtarget);
        Map<String,String> ordens=snmpWalk(ordem,this.comtarget);
        Map<String,String> paths=snmpWalk(path,this.comtarget);
        HashMap<Integer,Musica> musicas=new HashMap<>();

        for(String s: indexes.keySet()){
            int i=Integer.parseInt(indexes.get(s));
            String type=tipos.get(s).toString();
            String artist=artistas.get(s).toString();
            String al=albuns.get(s).toString();
            String part=partes.get(s).toString();
            String title=titulos.get(s).toString();
            String format=formatos.get(s).toString();
            String d=datas.get(s).toString();
            //String order=ordens.get(s).toString();
            int order=Integer.parseInt(ordens.get(s));
            String p=paths.get(s).toString();
            Musica m=new Musica(i,type,artist,al,part,title,format,d,order,p);
            musicas.put(i,m);
        }
        return musicas;
    }
    public Map<String, String> snmpWalk(String tableOid, CommunityTarget target) throws IOException {
        Map<String, String> result = new TreeMap<>();
        TransportMapping transport = new DefaultUdpTransportMapping();
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
                System.out.println("Vazio no "+ tableOid);
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
    public void snmpSet(String strOID, Object Value) {
        try {
            Snmp snmp = new Snmp(this.transport);
            PDU pdu = new PDU();
            //Depending on the MIB attribute type, appropriate casting can be done here
            if(Value instanceof Integer) {
                pdu.add(new VariableBinding(new OID(strOID), new Integer32((int)Value)));
                pdu.setRequestID(new Integer32());
            }
            if(Value instanceof String){
                pdu.add(new VariableBinding(new OID(strOID), new OctetString((String)Value)));
                pdu.setRequestID(new Integer32());
            } 
            pdu.setType(PDU.SET);

            ResponseEvent event=snmp.set(pdu,this.comtarget);
            if(event!=null){
                System.out.println("Response: "+event.getRequest().getVariableBindings());
                PDU responsePDU=event.getResponse();
                if(responsePDU!=null){
                    int errorStatus=responsePDU.getErrorStatus();
                    System.out.println("ResponsePDU: "+responsePDU);
                    if(errorStatus==PDU.noError){
                        System.out.println("Snmp Set Response: "+responsePDU.getVariableBindings());
                    }
                }
            }
            
            snmp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}