package org.szimano

import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor
import griffon.transform.Threading

@ArtifactProviderFor(GriffonController)
class GroovyGoogleVisionController {
    GroovyGoogleVisionModel model
    GroovyGoogleVisionView view

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    void click() {
        int count = model.clickCount.toInteger()
        model.clickCount = String.valueOf(count + 1)
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    void chooseImage() {
        view.selectFile()
    }
}