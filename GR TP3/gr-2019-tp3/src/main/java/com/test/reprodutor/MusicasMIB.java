package com.test.reprodutor;
import org.snmp4j.smi.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.agent.*;
import org.snmp4j.agent.mo.*;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.mo.snmp.smi.*;
import org.snmp4j.log.LogFactory;
import org.snmp4j.log.LogAdapter;





public class MusicasMIB implements MOGroup {

  private static final LogAdapter LOGGER = LogFactory.getLogger(MusicasMIB.class);
  private MOFactory moFactory = DefaultMOFactory.getInstance();

  
  private static final String TC_MODULE_SNMPV2_TC = "SNMPv2-TC";
  private static final String TC_DISPLAYSTRING = "DisplayString";
  private static final String TC_TRUTHVALUE = "TruthValue";


  
  public static final String tcModuleSNMPv2Tc = "SNMPv2-TC";
  public static final String tcDefDisplayString = "DisplayString";
    




  public static final OID oidMusicasMIBEntry = new OID(new int[] { 1,3,6,1,3,2020,1,1 });

  
  public static final OID oidMusicasMIBID =new OID(new int[] { 1,3,6,1,3,2020,1,1,1 });
  public static final int musicasMIBIndex = 1;
  public static final int musicasMIBTipo = 2;
  public static final int musicasMIBArtista = 3;
  public static final int musicasMIBAlbum = 4;
  public static final int musicasMIBParte = 5;
  public static final int musicasMIBMusica = 6;
  public static final int musicasMIBFormato = 7;
  public static final int musicasMIBDataMod = 8;
  public static final int musicasMIBOrdem = 9;
  public static final int musicasMIBPath = 10;
  public static final OID oidContainerID =new OID(new int[] { 1,3,6,1,3,2020,1,1,1 });
  
   
  public static final int colmusicasMIBIndex = 1;
  public static final int colmusicasMIBTipo = 2;
  public static final int colmusicasMIBArtista = 3;
  public static final int colmusicasMIBAlbum = 4;
  public static final int colmusicasMIBParte = 5 ;
  public static final int colmusicasMIBMusica = 6;
  public static final int colmusicasMIBFormato =7 ;
  public static final int colmusicasMIBDataMod =8 ;
  public static final int colmusicasMIBOrdem =9 ;
  public static final int colmusicasMIBPath =10 ;

  public static final int idxmusicasMIBIndex = 0;
  public static final int idxmusicasMIBTipo = 1;
  public static final int idxmusicasMIBArtista = 2;
  public static final int idxmusicasMIBAlbum = 3;
  public static final int idxmusicasMIBParte = 4;
  public static final int idxmusicasMIBMusica = 5;
  public static final int idxmusicasMIBFormato = 6;
  public static final int idxmusicasMIBDataMod = 7;
  public static final int idxmusicasMIBOrdem = 8;
  public static final int idxmusicasMIBPath = 9;

  private MOTableSubIndex[] musicasMIBEntryIndexes;
  private MOTableIndex musicasMIBEntryIndex;
  
  private MOTable      musicasMIBEntry;
  private MOTableModel musicasMIBEntryModel;
  protected MusicasMIB(){}
  public MusicasMIB(MOFactory moFactory) {
    createMO(moFactory);
  }

  protected void createMO(MOFactory moFactory) {
    addTCsToFactory(moFactory);
    createmusicasMIBEntry(moFactory);
  }

  public MOTable getmusicasMIBEntry() {
    return musicasMIBEntry;
  }
  private void createmusicasMIBEntry(MOFactory moFactory) { 
    musicasMIBEntryIndexes = new MOTableSubIndex[] {moFactory.createSubIndex(oidContainerID,SMIConstants.SYNTAX_INTEGER, 1, 1)};
    musicasMIBEntryIndex = moFactory.createIndex(musicasMIBEntryIndexes,false,new MOTableIndexValidator() {
      public boolean isValidIndex(OID index) {
        boolean isValidIndex = true;
        return isValidIndex;
      }
    });

    
    MOColumn[] musicasMIBEntryColumns = new MOColumn[10];

    musicasMIBEntryColumns[idxmusicasMIBIndex] =new MOMutableColumn(musicasMIBIndex, SMIConstants.SYNTAX_INTEGER32, moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(Integer32)null);
    ValueConstraint containerIDVC = new ConstraintsImpl();
    ((ConstraintsImpl)containerIDVC).add(new Constraint(0L, 1024L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBIndex]).addMOValueValidationListener(new ValueConstraintValidator(containerIDVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBIndex]).addMOValueValidationListener(new ContainerIDValidator());

    musicasMIBEntryColumns[idxmusicasMIBTipo] = new DisplayString(colmusicasMIBTipo,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint tipoVC = new ConstraintsImpl();
    ((ConstraintsImpl)tipoVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBTipo]).addMOValueValidationListener(new ValueConstraintValidator(tipoVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBTipo]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBArtista] = new DisplayString(colmusicasMIBArtista,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint artistaVC = new ConstraintsImpl();
    ((ConstraintsImpl)artistaVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBArtista]).addMOValueValidationListener(new ValueConstraintValidator(artistaVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBArtista]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBAlbum] = new DisplayString(colmusicasMIBAlbum,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint albumVC = new ConstraintsImpl();
    ((ConstraintsImpl)albumVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBAlbum]).addMOValueValidationListener(new ValueConstraintValidator(albumVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBAlbum]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBParte] = new DisplayString(colmusicasMIBParte,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint parteVC = new ConstraintsImpl();
    ((ConstraintsImpl)parteVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBParte]).addMOValueValidationListener(new ValueConstraintValidator(parteVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBParte]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBMusica] = new DisplayString(colmusicasMIBMusica,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint musicaVC = new ConstraintsImpl();
    ((ConstraintsImpl)musicaVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBMusica]).addMOValueValidationListener(new ValueConstraintValidator(musicaVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBMusica]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBFormato] = new DisplayString(colmusicasMIBFormato,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint formatoVC = new ConstraintsImpl();
    ((ConstraintsImpl)formatoVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBFormato]).addMOValueValidationListener(new ValueConstraintValidator(formatoVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBFormato]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBDataMod] = new DisplayString(colmusicasMIBDataMod,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint dataVC = new ConstraintsImpl();
    ((ConstraintsImpl)dataVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBDataMod]).addMOValueValidationListener(new ValueConstraintValidator(dataVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBDataMod]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryColumns[idxmusicasMIBOrdem] = new Enumerated(colmusicasMIBOrdem,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(Integer32)null);
    ValueConstraint ordemVC = new ConstraintsImpl();
    ((ConstraintsImpl)ordemVC).add(new Constraint(0L, 1024L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBOrdem]).addMOValueValidationListener(new ValueConstraintValidator(ordemVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBOrdem]).addMOValueValidationListener(new OrdemValidator());


    musicasMIBEntryColumns[idxmusicasMIBPath] = new DisplayString(colmusicasMIBPath,moFactory.createAccess(MOAccessImpl.ACCESSIBLE_FOR_READ_WRITE),(OctetString)null);
    ValueConstraint pathVC = new ConstraintsImpl();
    ((ConstraintsImpl)pathVC).add(new Constraint(0L, 255L));
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBPath]).addMOValueValidationListener(new ValueConstraintValidator(pathVC));                                  
    ((MOMutableColumn)musicasMIBEntryColumns[idxmusicasMIBPath]).addMOValueValidationListener(new NameValidator());

    musicasMIBEntryModel=moFactory.createTableModel(oidMusicasMIBEntry, musicasMIBEntryIndex, musicasMIBEntryColumns);
    ((MOMutableTableModel)musicasMIBEntryModel).setRowFactory(new musicasMIBEntryRowFactory());

    musicasMIBEntry=moFactory.createTable(oidMusicasMIBEntry,musicasMIBEntryIndex,musicasMIBEntryColumns,musicasMIBEntryModel);
  }



  public void registerMOs(MOServer server, OctetString context) throws DuplicateRegistrationException {    
    server.register(this.musicasMIBEntry, context);
  }
  public void unregisterMOs(MOServer server, OctetString context) {
    server.unregister(this.musicasMIBEntry, context);
  }
static class ContainerIDValidator implements MOValueValidationListener {  
  public void validate(MOValueValidationEvent validationEvent) {
    Variable newValue = validationEvent.getNewValue();
    long v = ((Integer32)newValue).getValue();
    if (!(((v >= 0L) && (v <= 1024L)))) {
      validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_VALUE);
      return;
    } 
  }
}

static class NameValidator implements MOValueValidationListener {    
  public void validate(MOValueValidationEvent validationEvent) {
    Variable newValue = validationEvent.getNewValue();
    OctetString os = (OctetString)newValue;
    if (!(((os.length() >= 0) && (os.length() <= 255)))) {
      validationEvent.setValidationStatus(SnmpConstants.SNMP_ERROR_WRONG_LENGTH);
      return;
    } 
  }
}

static class OrdemValidator implements MOValueValidationListener {    
  public void validate(MOValueValidationEvent validationEvent) {
    Variable newValue = validationEvent.getNewValue(); 
  }
}
public class musicasMIBEntryRow extends DefaultMOMutableRow2PC {
  public musicasMIBEntryRow(OID index, Variable[] values) {
    super(index, values);     
  }  
  public Integer32 getmusicasMIBIndex() {   
    return (Integer32) super.getValue(idxmusicasMIBIndex);
  }    
  public void setmusicasMIBIndex(Integer32 newValue) {   
    super.setValue(idxmusicasMIBIndex, newValue);
  }

  public OctetString getmusicasMIBTipo() {   
    return (OctetString) super.getValue(idxmusicasMIBTipo);
  } 
  public void setmusicasMIBTipo(OctetString newValue){
    super.setValue(idxmusicasMIBTipo,newValue);
  }   
  public OctetString getmusicasMIBArtista() {   
    return (OctetString) super.getValue(idxmusicasMIBArtista);
  } 
  public void setmusicasMIBArtista(OctetString newValue){
    super.setValue(idxmusicasMIBArtista,newValue);
  }   
  public OctetString getmusicasMIBAlbum() {   
    return (OctetString) super.getValue(idxmusicasMIBAlbum);
  } 
  public void setmusicasMIBAlbum(OctetString newValue){
    super.setValue(idxmusicasMIBAlbum,newValue);
  }   
  public OctetString getmusicasMIBParte() {   
    return (OctetString) super.getValue(idxmusicasMIBParte);
  } 
  public void setmusicasMIBParte(OctetString newValue){
    super.setValue(idxmusicasMIBParte,newValue);
  }
  public OctetString getmusicasMIBMusica() {   
    return (OctetString) super.getValue(idxmusicasMIBMusica);
  }
  public void setmusicasMIBMusica(OctetString newValue){
    super.setValue(idxmusicasMIBMusica,newValue);
  }   
  public OctetString getmusicasMIBFormato() {   
    return (OctetString) super.getValue(idxmusicasMIBFormato);
  } 
  public void setmusicasMIBFormato(OctetString newValue){
    super.setValue(idxmusicasMIBFormato,newValue);
  } 
  public OctetString getmusicasMIBDataMod() {   
    return (OctetString) super.getValue(idxmusicasMIBDataMod);
  } 
  public void setmusicasMIBDataMod(OctetString newValue){
    super.setValue(idxmusicasMIBDataMod,newValue);
  }
  public Integer32 getmusicasMIBOrdem() {   
    return (Integer32) super.getValue(idxmusicasMIBOrdem);
  }    
  public void setmusicasMIBOrdem(Integer32 newValue) {   
    super.setValue(idxmusicasMIBOrdem, newValue);
  }
  public OctetString getmusicasMIBPath() {   
    return (OctetString) super.getValue(idxmusicasMIBPath);
  } 
  public void setmusicasMIBPath(OctetString newValue){
    super.setValue(idxmusicasMIBPath,newValue);
  }   
  public Variable getValue(int column) { 
    switch(column) {
      case idxmusicasMIBIndex: 
        return getmusicasMIBIndex();
      case idxmusicasMIBTipo: 
        return getmusicasMIBTipo();
      case idxmusicasMIBArtista: 
        return getmusicasMIBArtista();
      case idxmusicasMIBAlbum: 
        return getmusicasMIBAlbum();
      case idxmusicasMIBParte: 
        return getmusicasMIBParte();
      case idxmusicasMIBMusica: 
        return getmusicasMIBMusica();
      case idxmusicasMIBFormato: 
        return getmusicasMIBFormato();
      case idxmusicasMIBDataMod: 
        return getmusicasMIBDataMod();
      case idxmusicasMIBOrdem: 
        return getmusicasMIBOrdem();
      case idxmusicasMIBPath: 
        return getmusicasMIBPath();
      default:
        return super.getValue(column);
    }
  }  
  public void setValue(int column, Variable value) {   
    switch(column) {
      case idxmusicasMIBIndex: 
        setmusicasMIBIndex((Integer32)value);
      case idxmusicasMIBTipo: 
        setmusicasMIBTipo((OctetString)value);
      case idxmusicasMIBArtista: 
        setmusicasMIBArtista((OctetString)value);
      case idxmusicasMIBAlbum: 
        setmusicasMIBAlbum((OctetString)value);
      case idxmusicasMIBParte: 
        setmusicasMIBParte((OctetString)value);
      case idxmusicasMIBMusica: 
        setmusicasMIBMusica((OctetString)value);
      case idxmusicasMIBFormato: 
        setmusicasMIBFormato((OctetString)value);
      case idxmusicasMIBDataMod: 
        setmusicasMIBDataMod((OctetString)value);
        break;
      case idxmusicasMIBOrdem: 
        setmusicasMIBOrdem((Integer32)value);
        break;
      case idxmusicasMIBPath: 
        setmusicasMIBPath((OctetString)value);
        break;
      default:
        super.setValue(column, value);
    }
  }
   
}
  
class musicasMIBEntryRowFactory extends DefaultMOMutableRow2PCFactory{
  public synchronized MOTableRow createRow(OID index, Variable[] values) throws UnsupportedOperationException {
    musicasMIBEntryRow row =new musicasMIBEntryRow(index, values);     
    return row;
  }  
  public synchronized void freeRow(MOTableRow row) {}     
}
  protected void addTCsToFactory(MOFactory moFactory) {}  
  public void addImportedTCsToFactory(MOFactory moFactory) {}
}