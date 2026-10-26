package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketHandler implements Runnable{
    private final Socket s;
    private static HashMap<String, Integer> mappaBiglietti = new HashMap<>();
    private static ArrayList<String> arrayUsers = new ArrayList<>();
    private final Object lock = new Object();
    
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
                    out.println("ERR --> username già in uso");
                } else {
                    out.println("OK --> login effettuato");
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
                        String acquisto = in.readLine();
                        String parti[] = acquisto.split(" ", 2);

                        synchronized (lock) {
                            if(parti[0].toLowerCase() != "gold" && parti[0].toLowerCase() != "pit" && parti[0].toLowerCase() != "parterre"){
                                out.println("ERR UNKNOWNTYPE --> tipologia inserita non esistente o mancante");
                            } else {
                                if(parti.length == 1)
                                    parti[1] = "1";
                    
                                try {
                                    if(mappaBiglietti.get(parti[0]) >= Integer.parseInt(parti[1])){
                                        mappaBiglietti.put(parti[0], mappaBiglietti.get(parti[0]) - Integer.parseInt(parti[1]));
                                        out.println("OK --> hai acquistato: " + parti[1] + " biglitti di tipo " + parti[0]);
                                    } else {
                                        out.println("KO --> non sono rimasti abbastanza biglietti");
                                    }
                                } catch (Exception e) {
                                    out.println("ERR SYNTAX --> la quantità numerica inserita è incorretta");
                                }
                            }
                        }
                        
                    break;
                    
                    case "QUIT":
                        out.println("BYE");
                        return;
                
                    default:
                        out.println("ERR UNKNOWNCMD --> comando inserito sconosciuto");
                        break;
                }
            } 
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
