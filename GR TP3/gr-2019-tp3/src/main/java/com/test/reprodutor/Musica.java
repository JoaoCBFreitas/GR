package com.test.reprodutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Musica implements Serializable{
    int index;
    String tipo;
    String artista;
    String album;
    String parte;
    String titulo;
    String formato;
    String data;
    int ordem;
    String path;
	ReprodutorMusical rpm;

	public Musica(int i, String t, String a, String al, String p, String title, String f, String d,int o,String pa){
		this.index=i;
		this.tipo=t;
		this.artista=a;
		this.album=al;
		this.parte=p;
		this.titulo=title;
		this.formato=f;
		this.data=d;
		this.ordem=o;
		this.path=pa;
	}
    public Musica(File fich,int i,ReprodutorMusical rpm) throws InterruptedException{
		Media media = new Media(fich.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		while(mediaPlayer.getStatus()!=MediaPlayer.Status.READY){
			Thread.sleep(10);
		}
		ObservableMap<String,Object> metadata=media.getMetadata();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        this.index=i;
		this.tipo=valCheck((String)metadata.get("genre"));
        this.artista=valCheck((String)metadata.get("artist"));
        this.album=valCheck((String)metadata.get("album"));

        String parteaux=setParte(fich,metadata);
        if(parteaux==null){
            this.parte="CD0";
        }else{
            this.parte=parteaux;
        }
        
        this.titulo=setNome(fich);
        this.formato=setFormato(fich);
		this.data=formatter.format(date);
		
        int ordaux=setOrdem(fich);
        if(ordaux!=-1){
            this.ordem=ordaux;
        }else{
            this.ordem=0;
        }
        
        this.path=fich.getAbsolutePath();
        this.rpm=rpm;
    }
    public String valCheck(String str){
        if(str==null){
            str="N/A";    
        }
        return str;
    }
    public String setParte(File fich,ObservableMap<String,Object> metadata){
		String resp=null;
		String aux=(String)metadata.get("album");
		Pattern pattern = Pattern.compile("(CD [0-9]+)|(CD[0-9]+)");
		if(aux==null){
			return resp;
		}
		Matcher matcher = pattern.matcher(aux);
		if (matcher.find()){
    		resp=matcher.group(1);
		}
		return resp;
    }
    public int setOrdem(File musicpath){
		int resp=-1;
		String nome=musicpath.getName();
		String t[]=nome.split("\\.");
		if(t.length>2){
			resp=Integer.parseInt(t[0]);
		}
		return resp;
	}
	public String setNome(File fich){
		String nome=fich.getName();
		String n[]=nome.split("\\.");
		String resp="";
		if(n.length>2){
			resp=n[n.length-2];
		}else{
			resp=n[0];
		}
		return resp; 
	}
	public String setFormato(File fich){
		String nome=fich.getName();
		String formato[]=nome.split("\\.");
		return formato[formato.length -1];
	}
	public int getIndex(){
		return this.index;
	}
	public String getTipo(){
		return this.tipo;
	}
	public String getArtista(){
		return this.artista;
	}
	public String getAlbum(){
		return this.album;
	}
	public String getParte(){
		return this.parte;
	}
	public String getMusica(){
		return this.titulo;
	}
	public String getFormato(){
		return this.formato;
	}
	public String getDataMod(){
		return this.data;
	}
	public int getOrdem(){
		return this.ordem;
	}
    public String getPath(){
        return this.path;
    }
    public ReprodutorMusical getRPM(){
        return this.rpm;
    }
    public void playMedia(String filepath,ReprodutorMusical rpm,String title){
		try{
			File musicpath=new File(filepath);
			if(musicpath.exists()){
				Media media = new Media(musicpath.toURI().toString());  
				MediaPlayer mediaPlayer = new MediaPlayer(media);
				BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
				String op = "";
				mediaPlayer.play();
				while(op!="-1"){
					rpm.printMusica();
					op = teclado.readLine();
					switch(op){
						case "1":
							if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING) {
								System.out.println("Music paused "+title);
								mediaPlayer.pause();
							}else{
								System.out.println("Error-Music not playing");
							}
							break;
						case "2":
							if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING){
								System.out.println("Error-Music already Playing");
							}else{
								System.out.println("Starting music "+title);
								mediaPlayer.play();
							}
							break;
						case "3":
							if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING){
								System.out.println("Stopping music "+title);
								mediaPlayer.stop();
							}else{
								System.out.println("Error-Music not playing");
							}
							break;
						case "0":
							System.out.println("Skipping music "+title);
							if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING) mediaPlayer.stop();
							op="-1";
							break;
						default:
							System.out.println("     Comando desconhecido!");
					}
				}
			}else{
				System.out.println("Can't find File: "+filepath);
			}
		}catch(Exception e){
			e.printStackTrace();
        }
    }
}