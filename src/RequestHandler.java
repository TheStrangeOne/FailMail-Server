import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aslan
 */
public class RequestHandler extends Thread{
    Socket socket;
    BufferedReader in;
    Server server;
    OutputStreamWriter out;
    public RequestHandler(Socket sock) {
        socket=sock;
    }

    
    @Override
    public void run() {
        
         try {
                in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
         
        try {
            out = new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        while(true){
           
            String client="";
            
            try {
                
               client=in.readLine();
               
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            //Separate the "header" from the other part
            String[] msg = client.split(":");
            msg[0] = msg[0].toLowerCase();     
            
            if(msg[0].compareTo("hello")==0){
                try {
                    
                    out.write("Hi\n");
                    out.flush();
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(msg[0].compareTo("file")==0){
                addInfo(msg);
            }
            else if(msg[0].compareTo("search")==0){
                try {
                    findFiles(msg[1]);
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(msg[0].startsWith("bye")){
                removeFiles(Integer.valueOf(msg[1]));
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
                
            //System.out.println(Server.data.toString());
            
            //System.out.println(client);
            
        }
    }
    
        synchronized void  addInfo(String[] file){
        Hashtable<String, HashSet<String>> list = Server.data;
        HashSet<String> files = list.get(file[1]);
        if(files==null){
            files = new HashSet();
        }
        String toPut="";
        for(int i=1;i<file.length;i++){
            toPut = toPut.concat(file[i]);
            if(file.length-i!=1){
                toPut = toPut.concat(":");
            }
        }
        files.add(toPut);
        list.put(file[1], files);
                  System.out.println(Server.data.toString());
        
    }
   synchronized void findFiles(String toFind) throws IOException{
                  //Getting all the keys to look through them
                  Enumeration<String> str = Server.data.keys();
                  boolean found=false;
                    
                  while(str.hasMoreElements()){
                      String key = str.nextElement();
                      if(key.toLowerCase().contains(toFind.toLowerCase())){
                        //If we find a file, send info about it  
                        if(!found){
                            out.write("Found\n");
                        }
                        found=true;
                        HashSet<String> info = Server.data.get(key);
                        Iterator<String> setIter = info.iterator();
                        while(setIter.hasNext()){
                        String searchResults  =setIter.next();
                        out.write(searchResults+"\n");
                        out.flush();
                          
                        }
                        }
                  }
                  if(!found){
                      out.write("Not Found\n");
                  }
                  //If no more elemnts, stop sending out the data
                  out.write("end\n");
                  out.flush();
                 // System.out.println(Server.data.toString());
    }
   synchronized void removeFiles(int port){
       System.out.println("remove");
        Enumeration<String> str = Server.data.keys();
        boolean deleted=false;
        while(str.hasMoreElements()){
            String key = str.nextElement();
            HashSet<String> files = Server.data.get(key);
            
            Iterator<String> iter = files.iterator();
            while(iter.hasNext()){
                String file = iter.next();
                System.out.println(file);
                if(file.endsWith(""+port)){
                    iter.remove();
                    if(files.isEmpty()){
                        Server.data.remove(key);
                        deleted=true;
                    }
                }
            }
            System.out.println(files.toString());
            if(deleted==false){
            Server.data.put(key, files);
            }
        }
        System.out.println(Server.data.toString());
   }

}
