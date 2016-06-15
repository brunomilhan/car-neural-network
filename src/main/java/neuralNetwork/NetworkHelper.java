package neuralNetwork;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ImageRecognitionPlugin;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * Created by bruno on 10/06/16.
 */
public class NetworkHelper {
    private ImageRecognitionPlugin imgRecPlugin;
    private String bestKey = "";
    private String placa = "";
    private int placaChar = 0;

    private String imagesPath;
    // implementar


    public NetworkHelper() {
        imagesPath = getImagesPath();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = Class.class.getClassLoader();
            }
            InputStream netInputStream = classLoader.getResourceAsStream("placa_network.nnet");;
            NeuralNetwork neuralNetwork = NeuralNetwork.load(netInputStream);
            imgRecPlugin = (ImageRecognitionPlugin) neuralNetwork.getPlugin(ImageRecognitionPlugin.class);
            System.out.println("Carregou rede!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recognition() {
        File file;
        for (int i = 0; i<8; i ++){
            try {
                file = new File(imagesPath + i + "_letra.bmp");
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
        System.out.println("=======================================");
        System.out.println("Possiveis caracteres placa carro: "+ this.placa);
        return this.placa;
    }

    public String getImagesPath(){
        String path = NetworkHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = "";
        String[] pathOrigin;
        String imagesPath  = "";
        try {
            decodedPath = URLDecoder.decode(path, "UTF-8");
            System.out.println("Path: "+ decodedPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("images path: " + decodedPath);
        pathOrigin = decodedPath.split("/");
        System.out.println("images path: " + pathOrigin[pathOrigin.length-2]);
        if (pathOrigin[pathOrigin.length - 1].equals("classes")){
            imagesPath = "/home/bruno/pdi/PDI/PDI/projeto_final/imagens/processada/letras/";
        }
        if (pathOrigin[pathOrigin.length - 2].equals("rede")) {
            System.out.println("entrou if");
            for (int i = 0; i < (pathOrigin.length - 2); i++){
                imagesPath += "/";
                imagesPath += pathOrigin[i];
            }
            imagesPath += "/imagens/processada/letras/";
        }
        System.out.println("images path: " + imagesPath);

        return imagesPath;

    }
}
