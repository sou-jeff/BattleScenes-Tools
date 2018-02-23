/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package impressaocards;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
*
* @author sou-jeff
*/
public class ImpressaoCards {

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) throws IOException {
          
        //FazerCopiasCards();

        //JuntarFotos();
        
        //Redimensionar();
        
        MontarFolhas("Biah");
    }

    public static void GerarPlanilha() throws IOException {
         String[] cards = new File("C:\\cards").list();
        
        FileWriter arquivoSaida = new FileWriter("C:\\cards\\listaCards.csv");
        PrintWriter gravarArq = new PrintWriter(arquivoSaida);
        
        gravarArq.println("Nome;Versão;Nº Coleção;Nome Coleção;");
        
        for (int i = 0; i < cards.length; i++) {
            String nome = "";
            String versao = "";
            String numeroColecao = "";
            String nomeColecao = "";
            
            int indexOfVersao = 0;
            
            if (cards[i].indexOf("v1") > -1)
                indexOfVersao = cards[i].indexOf("v1");
            else if (cards[i].indexOf("v2") > -1)
                indexOfVersao = cards[i].indexOf("v2");
            else if (cards[i].indexOf("v3") > -1)
                indexOfVersao = cards[i].indexOf("v3");
            else if (cards[i].indexOf("v4") > -1)
                indexOfVersao = cards[i].indexOf("v4");
            
            System.out.println(i);
            System.out.println(indexOfVersao);
            
            nome = cards[i].substring(0, indexOfVersao).replace('-', ' ');
            versao = cards[i].substring(indexOfVersao, indexOfVersao + 2 );
            numeroColecao = cards[i].substring(indexOfVersao + 3, indexOfVersao + 5 );
            nomeColecao = cards[i].substring(indexOfVersao + 6).replace('-', ' ').replaceAll("deck", "").replaceAll(".png", "").replaceAll("[0-9]","");
            
            gravarArq.println(nome + ";" + versao + ";" + numeroColecao + ";" + nomeColecao + ";");
            
        }
                
        arquivoSaida.close();
    }
    
    public static void Redimensionar() throws IOException {
        String[] cards = new File("C:\\cards\\tamanhoOriginal").list();
        
        for (int i = 0; i < cards.length; i++) {
            
            System.out.println(cards[i]);
            
            if (!cards[i].contains(".png"))
                continue;
            
            int widResize = 811;
            int heightResize = 1117;
            
            BufferedImage img = ImageIO.read(new File("C:\\cards\\tamanhoOriginal\\"+cards[i]));
            BufferedImage newImage = new BufferedImage(widResize,heightResize, BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g2 = newImage.createGraphics();
            Color oldColor = g2.getColor();
            //fill background
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, widResize, heightResize);
            //draw image
            g2.setColor(oldColor);
            
            g2.drawImage(img, 0, 0, widResize, heightResize, null);
            g2.dispose();
            
            boolean success = ImageIO.write(newImage, "png", new File("C:\\cards\\Redimensionado\\"+cards[i]));
            
            System.out.println("saved success? "+success);
        }
    }
    
    public static void FazerCopiasCards() throws FileNotFoundException, IOException {
        BufferedReader planilha = new BufferedReader(new FileReader("C:\\cards\\listaCards.csv"));
        
        String[] titulo = planilha.readLine().split(";");	
        String linha = planilha.readLine();
        
        while (linha != null) {
            
            String[] colunas = linha.split(";");

            int numeroCopiasBiah = Integer.parseInt(colunas[6].isEmpty() ? "0" : colunas[6]);
            int numeroCopiasNando = Integer.parseInt(colunas[7].isEmpty() ? "0" : colunas[7]);
            int numeroCopiasJeff = Integer.parseInt(colunas[8].isEmpty() ? "0" : colunas[8]);
            int numeroCopiasLady = Integer.parseInt(colunas[9].isEmpty() ? "0" : colunas[9]);
            
            Copiar(numeroCopiasBiah, colunas[10], titulo[6]);
            Copiar(numeroCopiasNando, colunas[10], titulo[7]);
            Copiar(numeroCopiasJeff, colunas[10], titulo[8]);
            Copiar(numeroCopiasLady, colunas[10], titulo[9]);
            
            linha = planilha.readLine();
        }
    }    
    
    public static void Copiar(int numeroCopias, String nomeCard, String pasta) throws FileNotFoundException, IOException {
        if (numeroCopias == 0)
            return;
        
        System.out.println(numeroCopias);
        
        for (int j = 0; j < numeroCopias; j++) {
            FileInputStream card = new FileInputStream("C:\\cards\\" + nomeCard);
            FileOutputStream copia = new FileOutputStream("C:\\cards\\" + pasta + "\\" + nomeCard.replaceAll(".png", "") + "_" + j + ".png");

            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = card.read(buf)) > 0) {
                    copia.write(buf, 0, len);
            }

            copia.close();
            card.close();
        }                
    }
    
    public static void MontarFolhas(String pasta) {
        try {
            String[] cards = new File("C:\\cards\\"+pasta).list();
            
            int qtdeCards = 0;
            int numeroFolha = 0;
            
            ArrayList<BufferedImage> imagens = new ArrayList<BufferedImage>();
            
            System.out.println("length " + cards.length);
            
            for (int i = 0; i < cards.length; i++) {
                if (!cards[i].contains(".png"))
                    continue;
                
                System.out.println(cards[i]);
                
                qtdeCards++;
                
                BufferedImage img = ImageIO.read(new File("C:\\cards\\"+pasta+"\\"+cards[i]));
                
                imagens.add(img);
                
                System.out.println(i);
                
                if (qtdeCards == 9 || i == cards.length - 2) {
                    System.out.println(imagens.size());
                    
                    numeroFolha++;
                    qtdeCards = 0;
                    
                    BufferedImage joinedImg = joinBufferedImage(imagens);
                    boolean success = ImageIO.write(joinedImg, "png", new File("C:\\cards\\"+pasta+"\\folhas\\folha_" + numeroFolha + ".png"));

                    System.out.println("saved success? "+success);
                    
                    imagens.clear();                    
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static BufferedImage joinBufferedImage(ArrayList<BufferedImage> imagens) {
        //do some calculate first
        int wid = 2480;
        int height = 3508;
        
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        g2.setColor(oldColor);
        
        int offset = 25;
        int quantidadeCards = 0;
        int fileira = 0;
        int coluna = 0;
        
        for (int i = 0; i < imagens.size(); i++) {
            quantidadeCards++;
            
            g2.drawImage(imagens.get(i), null, offset + imagens.get(i).getWidth() * coluna, offset + imagens.get(i).getHeight() * fileira);
                
            if (quantidadeCards%3 == 0) 
                fileira++;
            
            if (coluna == 2)
                coluna = 0;
            else
                coluna++;
        }
                
        g2.dispose();
        return newImage;
    }
}
