package org.szimano

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.FaceAnnotation
import griffon.core.artifact.GriffonView
import griffon.metadata.ArtifactProviderFor
import javafx.scene.control.TextArea
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Polygon
import javafx.stage.FileChooser
import javafx.stage.Window
import org.imgscalr.Scalr

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@ArtifactProviderFor(GriffonView)
class GroovyGoogleVisionView {
    FactoryBuilderSupport builder
    GroovyGoogleVisionModel model
    FileChooser fileChooser
    ImageView imageViewHandler
    TextArea textAreaHandler
    GoogleConnector googleConnector
    AnchorPane anchorPaneHandler

    void initUI() {
        fileChooser = new FileChooser()
        googleConnector = new GoogleConnector()
        builder.application(title: application.configuration['application.title'],
            sizeToScene: true, centerOnScreen: true, name: 'mainWindow') {
            scene(fill: WHITE, width: 1000, height: 600) {
                gridPane {
                    anchorPaneHandler = anchorPane(row: 0, column: 0) {
                        imageViewHandler = imageView (fitWidth: 500, fitHeight: 500, preserveRatio: true,
                                image: new Image(GroovyGoogleVisionView.class.getResourceAsStream("/kwach.jpg")))
                    }
                    textAreaHandler = textArea (row: 0, column: 1, width: 500, height: 500, wrapText: true)
                    label(id: 'clickLabel', row: 2, column: 1,
                          text: bind(model.clickCountProperty()))
                    button(row: 1, column: 1, prefWidth: 200,
                           id: 'clickActionTarget', clickAction)
                    button(row: 1, column: 0, prefWidth: 200,
                           id: 'chooseImageTarget', chooseImageAction)
                }
            }
        }
    }

    void selectFile() {
        println(imageViewHandler)
        File file = fileChooser.showOpenDialog(application.windowManager.startingWindow as Window)
        println(file)

        imageViewHandler.setImage(new Image(new FileInputStream(file)))

        println(imageViewHandler.getImage())

        BufferedImage img = ImageIO.read(file)
        BufferedImage scaledImg = Scalr.resize(img, 500);

        BatchAnnotateImagesResponse batchAnnotateImagesResponse = googleConnector
                .annotate(scaledImg, file.getName().split("\\.").last())

        textAreaHandler.setText(batchAnnotateImagesResponse.toPrettyString())

        anchorPaneHandler.getChildren().removeAll(anchorPaneHandler.getChildren().filtered{it instanceof Polygon})

        batchAnnotateImagesResponse.getResponses().each { resp ->
            resp.getFaceAnnotations().each { FaceAnnotation face ->
                List<List<Double>> points = face.getBoundingPoly().getVertices().collect {
                    [it.getX().doubleValue(), it.getY().doubleValue()]
                }

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(points.flatten() as List<Double>)

                polygon.setFill(null)
                polygon.setStroke(Color.RED);
                polygon.setStrokeWidth(2);
                anchorPaneHandler.getChildren().add(polygon);
            }
        }
    }
}