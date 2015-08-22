import auth.Role
import auth.User
import auth.UserRole
import library.Author
import library.Book

class BootStrap {

    def init = { servletContext ->

        Author author1 = new Author(name:'Philip K',surname:'Dick')
        def books1 = [new Book(title: 'The Man in the High Castle', genre:Book.genres[3], releaseDate: Date.parse("yyyy", "1962")),
                      new Book(title: 'The Three Stigmata of Palmer Eldritch', genre:Book.genres[3], releaseDate: Date.parse("yyyy", "1965")),
                      new Book(title: 'Flow My Tears, the Policeman Said', genre:Book.genres[3], releaseDate: Date.parse("yyyy", "1974")),
                      new Book(title: 'Do Androids Dream of Electric Sheep?', genre:Book.genres[3], releaseDate: Date.parse("yyyy", "1968")),
                      new Book(title: 'A Scanner Darkly', genre:Book.genres[3], releaseDate: Date.parse("yyyy", "1977")),
                      new Book(title: 'VALIS', genre:Book.genres[3], releaseDate: Date.parse("yyyy", "1980"))
        ]
        books1.each {Book b -> author1.addToBooks(b)}
        author1.save flush:true, failOnError: true

        Author author2 = new Author(name:'Stephen',surname:'King')
        def books2 = [new Book(title: 'Carrie', genre:Book.genres[2], releaseDate: Date.parse("yyyy", "1974")),
                      new Book(title: 'Salem\'s Lot', genre:Book.genres[2], releaseDate: Date.parse("yyyy", "1975")),
                      new Book(title: 'The Shining', genre:Book.genres[2], releaseDate: Date.parse("yyyy", "1977")),
                      new Book(title: 'The Dead Zone', genre:Book.genres[2], releaseDate: Date.parse("yyyy", "1979")),
                      new Book(title: 'Misery', genre:Book.genres[2], releaseDate: Date.parse("yyyy", "1987"))
        ]
        books2.each {Book b -> author2.addToBooks(b)}
        author2.save flush:true, failOnError: true

        //auth init
        def adminRole = new Role(authority: 'ROLE_ADMIN').save flush: true, failOnError: true
        def userRole = new Role(authority: 'ROLE_USER').save flush: true, failOnError: true

        def testUser = new User(username: 'admin@grails.org', password: 'admin')
        testUser.save flush: true, failOnError: true

        UserRole.create(testUser, adminRole, true)

        assert User.count() == 1
        assert Role.count() == 2
        assert UserRole.count() == 1

    }
    def destroy = {
    }
}