package com.test.reprodutor;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.BasicConfigurator;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.DefaultMOFactory;
import org.snmp4j.agent.mo.MOFactory;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogLevel;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;

public class Agent extends BaseAgent{
    static{
        LogFactory.setLogFactory(new Log4jLogFactory());
        BasicConfigurator.configure();
        LogFactory.getLogFactory().getRootLogger().setLogLevel(LogLevel.OFF);
    }
    String address;
    public Agent(String str) throws IOException{
        super(new File("conf.agent"),new File("bootCounter.agent"),new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        this.address=str;
    }
    MusicasMIB mib;
    protected void registerManagedObjects(ManagedObject mo){}

    public void registerManagedObject(ManagedObject mo){
        try{
            server.register(mo, null);
        }catch(DuplicateRegistrationException ex){
            throw new RuntimeException(ex);
        }
    }
    public void unregisterManagedObject(MOGroup moGroup){
        moGroup.unregisterMOs(server,getContext(moGroup));
    }
    @Override
    protected void addNotificationTargets(SnmpTargetMIB targetMIB,SnmpNotificationMIB notificationMIB){}
    protected void addViews(VacmMIB vacm){
        vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c,new OctetString("cpublic"),new OctetString("v1v2group"),StorageType.nonVolatile);
        vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"),SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV
            ,MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"), new OctetString("fullWriteView"), new OctetString("fullNotifyView"), StorageType.nonVolatile);
        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"), new OctetString(), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
        vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3"), new OctetString(), VacmMIB.vacmViewIncluded, StorageType.nonVolatile);
    }
    protected void addUsmUser(USM usm){}
    protected void initTransportMappings() throws IOException{
        transportMappings=new TransportMapping[1];
        Address addr=GenericAddress.parse(address);
        TransportMapping tm=TransportMappings.getInstance().createTransportMapping(addr);
        transportMappings[0]=tm;
    }
    public void start() throws IOException{
        init();
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
    }
    protected void unregisterManagedObjects(){

    }
    protected void addCommunities(SnmpCommunityMIB communityMIB){
        Variable[] com2sec=new Variable[]{
            new OctetString("public"),//community name
            new OctetString("cpublic"),//security name
            getAgent().getContextEngineID(),//local engine ID
            new OctetString("public"),//default context name
            new OctetString(),//transport tag
            new Integer32(StorageType.nonVolatile),//storage type
            new Integer32(RowStatus.active)
        };
        MOTableRow row=communityMIB.getSnmpCommunityEntry().createRow(new OctetString("public2public").toSubIndex(true),com2sec);
        communityMIB.getSnmpCommunityEntry().addRow(row);   
    }
    public void setupMIB(){
        MOFactory moFactory=DefaultMOFactory.getInstance();
        this.mib=new MusicasMIB(moFactory);
        try{
            mib.registerMOs(getServer(),new OctetString("public"));
        }catch(DuplicateRegistrationException e){
            e.printStackTrace();
        }
    }
    public void prepMIB(Musica m){    
        fillImg(this.mib.getmusicasMIBEntry(),m.getIndex(),m.getTipo(),m.getArtista(),m.getAlbum(),m.getParte(),m.getMusica(),
        m.getFormato(),m.getDataMod(),m.getOrdem(),m.getPath());
    }
    public void fillImg(MOTable img, int index, String tipo, String artista, String album ,String parte, String musica, String formato, String data, int ordem, String path){
        img.addRow(img.createRow(new OID(Integer.toString(index)), new Variable[]{
            new Integer32(index),new OctetString(tipo),new OctetString(artista),new OctetString(album),new OctetString(parte),
            new OctetString(musica),new OctetString(formato),new OctetString(data),new Integer32(ordem),new OctetString(path)
        }));
    }
    @Override
    protected void registerManagedObjects(){}
}