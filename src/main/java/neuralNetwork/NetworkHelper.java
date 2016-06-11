package neuralNetwork;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ImageRecognitionPlugin;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bruno on 10/06/16.
 */
public class NetworkHelper {
    private static String NETWORK = "/home/bruno/pdi/PDI/PDI/projeto_final/rede/placa_network.nnet";
    private static String IMGS_PATH = "/home/bruno/pdi/PDI/PDI/projeto_final/imagens/processada/letras/";
    InputStream netInputStream;
    NeuralNetwork neuralNetwork;
    ImageRecognitionPlugin imgRecPlugin;
    private String bestKey = "";
    private String placa = "";
    private int placaChar = 0;

    public NetworkHelper() {
        try {
            InputStream netInputStream = new FileInputStream(NETWORK);
            NeuralNetwork neuralNetwork = NeuralNetwork.load(netInputStream);
            imgRecPlugin = (ImageRecognitionPlugin) neuralNetwork.getPlugin(ImageRecognitionPlugin.class);
            System.out.println("Carregou rede");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void recognition(String fileName) {
        File file = new File(IMGS_PATH + fileName);
        BufferedImage img1 = null;
        HashMap<String, Double> output = null;
        try {
            img1 = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output = imgRecPlugin.recognizeImage(img1);

            System.out.println(output.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
        sort(output);
    }

    public void recognition() {
        File file;
        for (int i = 0; i<8; i ++){
            try {
                file = new File(IMGS_PATH + i + "_letra.bmp");
                BufferedImage img1 = null;
                HashMap<String, Double> output = null;
                try {
                    img1 = ImageIO.read(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    output = imgRecPlugin.recognizeImage(img1);

                    System.out.println(output.toString());
                } catch (Exception e) {
                    System.out.println(e);
                }
                sort(output);
            }catch ( Exception e){
                System.out.println("File not found.");
            }
        }
    }

    public void sort(HashMap<String, Double> hashmap) {
        HashMap<String, Double> newMap = new HashMap<String, Double>();
        double bestValue = 0;
        String bestKey = "";

        // words
        for (int i = 97; i < 123; i++) {
            char word = (char) i;
            hashmap.containsKey(word);
            //lista.add(String.valueOf(a));
            double value = 0;
            int aux;
            if (hashmap.containsKey(String.valueOf(word)))
                value = hashmap.get(String.valueOf(word));

            if (value > 1) {
                System.out.println("value wrong: " + value);
                aux = (int) value;
                value = value - aux;
                System.out.println("value corrected: " + value);
            }
            newMap.put(String.valueOf(word), value);
            if (bestValue < value){
                bestValue = value;
                bestKey = String.valueOf(word);
            }
        }
        // numbers
        for (int i = 0; i < 10; i++) {
            char word = (char) i;
            double value = 0;
            int aux;
            if (hashmap.containsKey(String.valueOf(i)))
                value = hashmap.get(String.valueOf(i));
            if (value > 1) {
                System.out.println("value wrong: " + value);
                aux = (int) value;
                value = value - aux;
                System.out.println("value corrected: " + value);
            }
            newMap.put(String.valueOf(i), value);
            if (bestValue < value){
                bestValue = value;
                bestKey = String.valueOf(i);
            }
        }
        if (bestValue > 0.5){
            System.out.println("value sorted: " + bestValue + " key>: " + bestKey);
            this.bestKey = bestKey;
            mountPlaca(bestKey);

        }
    }

    public String getBestKey() {
        return bestKey;
    }

    public void mountPlaca(String bestKey){
       placaChar++;
       this.placa += bestKey;
    }

    public String getPlaca(){
        System.out.println("Possiveis caracteres placa carro: "+ this.placa);
        return this.placa;
    }
}
