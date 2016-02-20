package org.szimano

import griffon.core.artifact.GriffonModel
import griffon.transform.FXObservable
import griffon.metadata.ArtifactProviderFor

@ArtifactProviderFor(GriffonModel)
class GroovyGoogleVisionModel {
    @FXObservable String clickCount = "0"
}