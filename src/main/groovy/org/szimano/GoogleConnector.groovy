package org.szimano

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionScopes
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.Feature
import com.google.api.services.vision.v1.model.Image
import com.google.api.services.vision.v1.model.ImageContext
import com.google.common.collect.Lists
import groovy.transform.TypeChecked
import org.apache.commons.io.IOUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@TypeChecked
class GoogleConnector {

    Vision vision

    public GoogleConnector() {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Build service account credential.
        GoogleCredential credential = GoogleCredential.fromStream(
                GoogleConnector.class.getResourceAsStream("/google.json"))
                .createScoped(VisionScopes.all())

        vision = new Vision.Builder(httpTransport, jsonFactory, credential).build()
    }

    public BatchAnnotateImagesResponse annotate(BufferedImage bufferedImage, String format) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream()

        ImageIO.write(bufferedImage, format, bos)

        AnnotateImageRequest ar = new AnnotateImageRequest()
                .setImageContext(new ImageContext())
                .setImage(new Image().encodeContent(bos.toByteArray()))
                .setFeatures(Lists.asList(
                new Feature().setType("FACE_DETECTION").setMaxResults(5),
                new Feature().setType("LABEL_DETECTION").setMaxResults(5)
        ))
        BatchAnnotateImagesRequest req = new BatchAnnotateImagesRequest().setRequests(Lists.asList(ar))
        Vision.Images.Annotate ann = vision.images().annotate(req)

        BatchAnnotateImagesResponse imagesResponse = ann.execute()

        return imagesResponse
    }
}
