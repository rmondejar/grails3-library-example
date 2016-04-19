package grails3.library.example

import org.springframework.boot.autoconfigure.transaction.jta.*
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

@EnableAutoConfiguration(exclude = [JtaAutoConfiguration])
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}