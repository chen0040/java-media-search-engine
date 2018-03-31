package com.github.chen0040.tensorflow.classifiers.images;

import com.github.chen0040.tensorflow.classifiers.images.models.cifar10.Cifar10ImageClassifier;
import com.github.chen0040.tensorflow.classifiers.images.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Cifar10ImageClassifierDemo {

    private static final Logger logger = LoggerFactory.getLogger(Cifar10ImageClassifierDemo.class);

    public static void main(String[] args) throws IOException {



        Cifar10ImageClassifier classifier = new Cifar10ImageClassifier();
        classifier.load_model();

        String[] image_names = new String[] {
                "airplane1",
                "airplane2",
                "airplane3",
                "automobile1",
                "automobile2",
                "automobile3",
                "bird1",
                "bird2",
                "bird3",
                "cat1",
                "cat2",
                "cat3"
        };

        for(String image_name :image_names) {
            String image_path = "images/cifar10/" + image_name + ".png";
            BufferedImage img = ResourceUtils.getImage(image_path);
            String predicted_label = classifier.predict_image(img);
            logger.info("predicted class for {}: {}", image_name, predicted_label);
        }
    }
}
