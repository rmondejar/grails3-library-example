package library

import grails.test.mixin.*
import spock.lang.*

@TestFor(AuthorController)
@Mock(Author)
class AuthorControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params.name='Philip K'
        params.surname='Dick'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.authorList
            model.authorCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.author!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def author = new Author()
            author.validate()
            controller.save(author)

        then:"The create view is rendered again with the correct model"
            model.author!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            author = new Author(params)

            controller.save(author)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/author/show/1'
            controller.flash.message != null
            Author.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def author = new Author(params)
            controller.show(author)

        then:"A model is populated containing the domain instance"
            model.author == author
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def author = new Author(params)
            controller.edit(author)

        then:"A model is populated containing the domain instance"
            model.author == author
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/author/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def author = new Author()
            author.validate()
            controller.update(author)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.author == author

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            author = new Author(params).save(flush: true)
            controller.update(author)

        then:"A redirect is issued to the show action"
            author != null
            response.redirectedUrl == "/author/show/$author.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/author/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def author = new Author(params).save(flush: true)

        then:"It exists"
            Author.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(author)

        then:"The instance is deleted"
            Author.count() == 0
            response.redirectedUrl == '/author/index'
            flash.message != null
    }
}
