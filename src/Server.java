
import com.sun.jmx.remote.internal.ArrayQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Handler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aslan Assylkhanov, Altyn Zhelambayeva, Tair Maralov
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static int size=0;
    public static Hashtable<String, HashSet<String>> data = new Hashtable();
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket server = new ServerSocket(8080);
        
        
        System.out.println("FailMail server is now activated on port " + server.getLocalPort()
                +" and waiting "
                + "for the users to connect");
        
        
               
        while(true){
           // System.out.println(console.readLine());
            Socket user = server.accept();
            System.out.println("I've connection on port: "+user.getPort());
            new RequestHandler(user).start();
            
            
           
            
            
        }
        
        
    }
    
    
}
