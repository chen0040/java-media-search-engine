package com.github.chen0040.tensorflow.classifiers.images.models.inception;

import com.github.chen0040.tensorflow.classifiers.images.models.ImageClassifier;
import com.github.chen0040.tensorflow.classifiers.images.utils.ImageUtils;
import com.github.chen0040.tensorflow.classifiers.images.utils.InputStreamUtils;
import com.github.chen0040.tensorflow.classifiers.images.utils.ResourceUtils;
import com.github.chen0040.tensorflow.classifiers.images.utils.TensorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InceptionImageClassifier implements ImageClassifier, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(InceptionImageClassifier.class);
    private Graph graph = new Graph();
    private List<String> labels = new ArrayList<>();
    public InceptionImageClassifier() {

    }

    @Override
    public void load_model() throws IOException {
        InputStream inputStream = ResourceUtils.getInputStream("tf_models/tensorflow_inception_graph.pb");
        load_model(inputStream);
        inputStream = ResourceUtils.getInputStream("tf_models/imagenet_comp_graph_label_strings.txt");
        load_labels(inputStream);
    }

    public void load_model(InputStream inputStream) throws IOException {
        byte[] bytes = InputStreamUtils.getBytes(inputStream);
        graph.importGraphDef(bytes);
    }

    public void load_labels(InputStream inputStream) {
        labels.clear();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while((line = reader.readLine()) != null) {
                labels.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String predict_image(BufferedImage image) {
        return predict_image(image, 224, 224);
    }

    @Override
    public String predict_image(BufferedImage image, int imgWidth, int imgHeight){

        float[] predicted = encode_image(image, imgWidth, imgHeight);

        if(predicted == null) {
            return "unknown";
        }

        int nlabels = predicted.length;
        int argmax = 0;
        float max = predicted[0];
        for(int i=1; i < nlabels; ++i) {
            if(max < predicted[i]) {
                max = predicted[i];
                argmax = i;
            }
        }

        if(argmax >= 0 && argmax < labels.size()) {
            return labels.get(argmax);
        } else {
            return "unknown";
        }
    }

    @Override
    public float[] encode_image(BufferedImage image){
        return encode_image(image, 224, 224);
    }

    @Override
    public float[] encode_image(BufferedImage image, int imgWidth,int imgHeight) {
        image = ImageUtils.resizeImage(image, imgWidth, imgHeight);

        Tensor<Float> imageTensor = TensorUtils.getImageTensor(image, imgWidth, imgHeight);

        try (Session sess = new Session(graph);
             Tensor<Float> result =
                     sess.runner().feed("input", imageTensor)
                             .fetch("output").run().get(0).expect(Float.class)) {
            final long[] rshape = result.shape();
            if (result.numDimensions() != 2 || rshape[0] != 1) {
                throw new RuntimeException(
                        String.format(
                                "Expected model to produce a [1 N] shaped tensor where N is the number of labels, instead it produced one with shape %s",
                                Arrays.toString(rshape)));
            }
            int nlabels = (int) rshape[1];
            return result.copyTo(new float[1][nlabels])[0];
        } catch(Exception ex) {
            logger.error("Failed to predict image", ex);
        }

        return null;
    }

    @Override
    public void close() throws Exception {
        if(graph != null) {
            graph.close();
            graph = null;
        }
    }
}
