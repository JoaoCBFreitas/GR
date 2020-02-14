package com.test.reprodutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.beans.value.WritableLongValue;
import javafx.embed.swing.JFXPanel;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ReprodutorMusical{
	static String musicasMIB="1.3.6.1.3.2020";
	static String musicasMIBTable=musicasMIB+".1";
	static String musicasMIBEntry=musicasMIBTable+".1";
	static String musicasMIBIndex=musicasMIBEntry+".1";
	static String musicasMIBTipo=musicasMIBEntry+".2";
	static String musicasMIBArtista=musicasMIBEntry+".3";
	static String musicasMIBAlbum=musicasMIBEntry+".4";
	static String musicasMIBParte=musicasMIBEntry+".5";
	static String musicasMIBMusica=musicasMIBEntry+".6";
	static String musicasMIBFormato=musicasMIBEntry+".7";
	static String musicasMIBDataMod=musicasMIBEntry+".8";
	static String musicasMIBOrdem=musicasMIBEntry+".9";
	static String musicasMIBPath=musicasMIBEntry+".10";

    public void printMenu(){
		System.out.println();
		System.out.println("     +-------- MENU --------+");
		System.out.println("     |     1. Procurar      |");
		System.out.println("     |     2. Play          |");
		System.out.println("     |     0. Sair          |");
		System.out.println("     +----------------------+");
		System.out.println();
		System.out.print("     Opção: ");
	}
	public void printIni(){
		System.out.println();
		System.out.println("     +-------- MENU --------+");
		System.out.println("     |     1. Ler da cache  |");
		System.out.println("     |     2. Ler de inicio |");
		System.out.println("     |     0. Sair          |");
		System.out.println("     +----------------------+");
		System.out.println();
		System.out.print("     Opção: ");
	}
	public void printMusica(){
		System.out.println();
		System.out.println("     +------- Player--------+");
		System.out.println("     |     1. Pause         |");
		System.out.println("     |     2. Play          |");
		System.out.println("     |     3. Stop          |");
		System.out.println("     |     0. Skip          |");
		System.out.println("     +----------------------+");
		System.out.println();
		System.out.print("     Opção: ");
	}
	public void printProcura(){
		System.out.println();
		System.out.println("     +--------- Procura ---------+");
		System.out.println("     |     1. Todas as músicas   |");
		System.out.println("     |     2. Procura por titulo |");
		System.out.println("     |     3. Procura por artista|");
		System.out.println("     |     4. Procura por album  |");
		System.out.println("     |     5. Procura por tipo   |");
		System.out.println("     |     0. Voltar             |");
		System.out.println("     +---------------------------+");
		System.out.println();
		System.out.print("     Opção: ");
	}
	public void printPlaylist(){
		System.out.println();
		System.out.println("     +------------ Playlist ------------+");
		System.out.println("     |     1. Tocar uma música          |");
		System.out.println("     |     2. Criar playlist por indice |");
		System.out.println("     |     3. Criar playlist por artista|");
		System.out.println("     |     4. Criar playlist por album  |");
		System.out.println("     |     0. Sair                      |");
		System.out.println("     +----------------------------------+");
		System.out.println();
		System.out.print("     Opção: ");
	}
	public HashMap<String,File> listFilesForFolder(final File folder,HashMap<String,File> ficheiros) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry,ficheiros);
			} else {
				ficheiros.put(fileEntry.getName(),fileEntry);
			}
		}
		return ficheiros;
	}
	public void start(String path) throws IOException,InterruptedException{
		File folder=new File(path);
		HashMap<String,File> ficheiros=new HashMap<String,File>();
		ficheiros=listFilesForFolder(folder,ficheiros);
		int i=1;
		Agent agent=new Agent("0.0.0.0/6666");
		agent.start();
		agent.setupMIB();
		for(File fich:ficheiros.values()){
			Musica m=new Musica(fich,i,this);
			agent.prepMIB(m);
			i++;
		}
	}

	public void writeLogs(String path, String logs,FileHandler fh){
		Logger logger = Logger.getLogger("MyLogs");
	 	logger.setUseParentHandlers(false);
	 	try {
			// This block configure the logger with handler and formatter
			logger.addHandler(fh); 
			SimpleFormatter formatter = new SimpleFormatter(); 
			fh.setFormatter(formatter);
	 
			 // the following statement is used to log any messages
					logger.info(logs);

	 	} catch (SecurityException e) {
		  e.printStackTrace(); 
		} 
	}
	 

	public void writeCache(String path, HashMap<Integer, Musica> musicasMap) {
		try {
			// write object to file
			FileOutputStream fos = new FileOutputStream(path +File.separator+"cache.ser"+File.separator);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(musicasMap);
			oos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, Musica> readCache(String path) {
		HashMap<Integer, Musica> musicasMap = new HashMap<>();
		try {
			// read object from file
			FileInputStream fis = new FileInputStream(path + File.separator+"cache.ser"+File.separator);
			ObjectInputStream ois = new ObjectInputStream(fis);
			musicasMap = (HashMap<Integer, Musica>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return musicasMap;
	}

    public static void main(String[] args) throws IOException,InterruptedException{
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        ReprodutorMusical rpm=new ReprodutorMusical();
        SNMPHandler snmpHandler=new SNMPHandler();
		final JFXPanel fxPanel = new JFXPanel();
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString();
		String musicpath=path+File.separator+"Music"+File.separator;
		String cachepath=path;
		HashMap<Integer,Musica> musicasMap= new HashMap<>();
		String logs="";
		FileHandler fh = new FileHandler(path+File.separator+"logs"+File.separator,true);
		String op="";
        while(op!="-1"){       // Ler e escrever
			rpm.printIni();
			op = teclado.readLine();
			switch(op){
				case "0": 
					logs="O utilizador saiu da aplicaçao";
					rpm.writeLogs(path,logs,fh);
					System.exit(0);
				case "1": // Ler da cache
					logs="O utilizador leu da cache";
					rpm.writeLogs(path,logs,fh);
					musicasMap=rpm.readCache(cachepath);
					op="-1";
					break;
				case "2": // Ler de inicio
					logs="O utilizador leu pelo snmp";
					rpm.writeLogs(path,logs,fh);
					rpm.start(musicpath);
					musicasMap=snmpHandler.doWalk(musicasMIBIndex,musicasMIBTipo,musicasMIBArtista,musicasMIBAlbum,musicasMIBParte,musicasMIBMusica,musicasMIBFormato,musicasMIBDataMod,musicasMIBOrdem,musicasMIBPath);
					rpm.writeCache(cachepath,musicasMap);
					op="-1";
					break;
				default:
					System.out.println("     Comando desconhecido!");
			}
		}
		op="";
		while(op!="-1"){
			if(musicasMap.size()==0){
				logs="Não foram encontradas músicas";
				rpm.writeLogs(path,logs,fh);
				System.out.println("Não foram encontradas músicas");
				op="-1";
				break;
			}
			rpm.printMenu();
			op = teclado.readLine();
			switch(op){
				case "0": 
					logs="O utilizador saiu da aplicaçao";
					rpm.writeLogs(path,logs,fh);
					op="-1";
					break;
				case "1": 
					String proc="";
					while(proc!="-1"){
						rpm.printProcura();
						proc=teclado.readLine();
						String procura="";
						switch(proc){
							case "0":
								proc="-1";
								break;
							case "1":
								for(Musica m:musicasMap.values()){
									System.out.println("Index: "+m.getIndex()+"->"+m.getMusica()+"->"+m.getArtista()+"->"+m.getAlbum()+"->"+m.getTipo());
									System.out.println("---------------------------------------------------------------------");
								}
								break;
							case "2":
								System.out.println("Indique o título");
								procura=teclado.readLine();
								for(Musica m:musicasMap.values()){
									if(m.getMusica().toLowerCase().contains(procura.toLowerCase())){
										System.out.println("Index: "+m.getIndex()+"->"+m.getMusica());
										System.out.println("---------------------------------------------------------------------");
									}
								}
								break;
							case "3":
								System.out.println("Indique o artista");
								procura=teclado.readLine();
								for(Musica m:musicasMap.values()){
									if(m.getArtista().toLowerCase().contains(procura.toLowerCase())){
										System.out.println("Index: "+m.getIndex()+"->"+m.getMusica()+"->"+m.getArtista());
										System.out.println("---------------------------------------------------------------------");
									}
								}
								break;
							case "4":
								System.out.println("Indique o álbum");
								procura=teclado.readLine();
								for(Musica m:musicasMap.values()){
									if(m.getAlbum().toLowerCase().contains(procura.toLowerCase())){
										System.out.println("Index: "+m.getIndex()+"->"+m.getMusica()+"->"+m.getArtista()+"->"+m.getAlbum());
										System.out.println("---------------------------------------------------------------------");
									}
								}
								break;
							case "5":
								System.out.println("Indique o tipo");
								procura=teclado.readLine();
								for(Musica m:musicasMap.values()){
									if(m.getTipo().toLowerCase().contains(procura.toLowerCase())){
										System.out.println("Index: "+m.getIndex()+"->"+m.getMusica()+"->"+m.getArtista()+"->"+m.getTipo());
										System.out.println("---------------------------------------------------------------------");
									}
								}
								break;
							default:
								System.out.println("     Comando desconhecido!");
						}
					}
					break;
				case "2":
					String pl="";
					ArrayList<Musica> listaMusicas=new ArrayList<>();
					while(pl!="-1"){
						rpm.printPlaylist();
						pl=teclado.readLine();
						String play="";
						switch(pl){
							case "0":
								pl="-1";
								break;
							case "1":
								System.out.println("Insira o índice da música");
								play=teclado.readLine();
								if(musicasMap.containsKey(Integer.parseInt(play))){
									System.out.println("Playing "+musicasMap.get(Integer.parseInt(play)).getMusica());
									musicasMap.get(Integer.parseInt(play)).playMedia(musicasMap.get(Integer.parseInt(play)).getPath(),rpm,musicasMap.get(Integer.parseInt(play)).getMusica());
								}else{
									System.out.println("Índice inválido");
								}
								break;
							case "2":
								System.out.println("Insira o nº de índices");
								listaMusicas.clear();
								int n=Integer.parseInt(teclado.readLine());
								System.out.println("Insira os índices das músicas a adicionar á playlist");
								int lista[]=new int[n];
								for(int i=0;i<n;i++){
									lista[i]=Integer.parseInt(teclado.readLine());
								}
								for(int i=0;i<n;i++){
									listaMusicas.add(musicasMap.get(lista[i]));
								}
								for(Musica m:listaMusicas){
									System.out.println(listaMusicas.indexOf(m)+"->"+m.getMusica());
								}
								for(Musica m:listaMusicas){
									System.out.println("Playing "+m.getMusica());
									m.playMedia(m.getPath(), rpm,m.getMusica());
								}
								break;
							case "3":
								System.out.println("Indique o artista");
								listaMusicas.clear();
								play=teclado.readLine();
								for(Musica m:musicasMap.values()){
									if(m.getArtista().toLowerCase().contains(play.toLowerCase())){
										listaMusicas.add(m);
									}
								}
								if(listaMusicas.size()==0){
									System.out.println("Nenhum artista com esse nome encontrado");
								}else{
									for(Musica m:listaMusicas){
										System.out.println(listaMusicas.indexOf(m)+"->"+m.getMusica());
									}
									for(Musica m:listaMusicas){
										System.out.println("Playing "+m.getMusica());
										m.playMedia(m.getPath(), rpm,m.getMusica());
									}
								}
								break;
							case "4":
								System.out.println("Indique o album");
								listaMusicas.clear();
								play=teclado.readLine();
								for(Musica m:musicasMap.values()){
									if(m.getAlbum().toLowerCase().contains(play.toLowerCase())){
										listaMusicas.add(m);
									}
								}
								if(listaMusicas.size()==0){
									System.out.println("Nenhum album com esse nome encontrado");
								}else{
									for(Musica m:listaMusicas){
										System.out.println(listaMusicas.indexOf(m)+"->"+m.getMusica());
									}
									for(Musica m:listaMusicas){
										System.out.println("Playing "+m.getMusica());
										m.playMedia(m.getPath(), rpm,m.getMusica());
									}
								}
								break;
							default:
								System.out.println("     Comando desconhecido!");
						}
					}
					break;
				default:
					System.out.println("     Comando desconhecido!");
			}
		}
		System.out.println("Saindo.......");
		System.exit(0);
	}            
}