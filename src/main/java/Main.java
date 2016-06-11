import neuralNetwork.NetworkHelper;

/**
 * Created by bruno on 10/06/16.
 */
public class Main {

    public static void main (String args[]){
        NetworkHelper networkHelper = new NetworkHelper();

        networkHelper.recognition();

        networkHelper.getPlaca();
    }
}
