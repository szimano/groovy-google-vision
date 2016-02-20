application {
    title = 'groovy-google-vision'
    startupGroups = ['groovyGoogleVision']
    autoShutdown = true
}
mvcGroups {
    // MVC Group for "groovyGoogleVision"
    'groovyGoogleVision' {
        model      = 'org.szimano.GroovyGoogleVisionModel'
        view       = 'org.szimano.GroovyGoogleVisionView'
        controller = 'org.szimano.GroovyGoogleVisionController'
    }
}