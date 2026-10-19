package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketHandler implements Runnable{
    private final Socket s;
    private HashMap<String, Integer> mappaBiglietti = new HashMap<>();
    private static ArrayList<String> arrayUsers = new ArrayList<>();
    
    public SocketHandler(Socket s) {
        this.s = s;

        mappaBiglietti.put("gold", 10);
        mappaBiglietti.put("pit", 30);
        mappaBiglietti.put("parterre", 60);
    }



    @Override
    public void run(){
        String user;
        Boolean var = true;
        String scelta;

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Socket autoClosablesSocket = s;
        ) {
            out.println("WELCOME");
            
            do {
                out.println("Inserisci l'username per effettuare il login");
                user = in.readLine();

                if(arrayUsers.contains(user)){
                    out.println("ERR: username già in uso");
                } else {
                    out.println("OK: login effettuato");
                    arrayUsers.add(user);
                    var = false;
                }
            } while (var);

            while(true) {
                scelta = in.readLine();

                switch (scelta) {
                    case "N":
                        String str = "";
                        for (String s : mappaBiglietti.keySet()) {
                            str += s + " <" + mappaBiglietti.get(s) + "> \n";
                        }
                        out.println(str);
                        str = "";
                    break;

                    case "BUY":
                        
                    break;
                    
                    case "QUIT":
                    
                    break;
                
                    default:
                        break;
                }
            }

            


            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
