package library

class Book {
    static def genres = ['Drama', 'Romance', 'Terror', 'SciFi', 'Comedy']

    String title
    String genre
    Date releaseDate

    static belongsTo = [author:Author]

    String getReleaseYear() {
        if (releaseDate) {
            Calendar cal = Calendar.getInstance()
            cal.setTime(releaseDate)
            cal.get(Calendar.YEAR)
        }
    }

    String toString() {
        "$title ($releaseYear)"
    }

    static constraints = {
        title maxSize: 150, blank: false, nullable: false
        genre blank: false, nullable: false, inList: Book.genres
        releaseDate max: new Date()
    }
}

