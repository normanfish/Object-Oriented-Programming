/**
 * This class represents a library, which hold a collection of books. Patrons can register at the library to be
 * able to check out books, if a copy of the requested book is available.
 */
class Library {

    /**
     * The maximal number of books this library can hold.
     */
    int bookCapacity;

    /**
     * The maximal number of books this library allows a single patron to borrow at the same time.
     */
    int maxBorrowedLimit;

    /**
     * The maximal number of registered patrons this library can handle.
     */
    int patronCapacity;

    /**
     * An array of the books currently held in the library. Starts off as an empty array. Each book's ID number is the
     * cell in which the Book is stored in the array.
     */
    Book[] libraryBooksArray;

    /**
     * The number of books in the library. Starts off at 0
     */
    int numBooksInLibrary;

    /**
     * An array of Patrons registered to the library. Each Patron's ID number is the
     * cell in which the Book is stored in the array.
     */
    Patron[] patronsArray;

    /**
     * The number of patrons currently in the library
     */
    int numPatronsInLibrary;

    /**
     * an array of a histogram type, in which each cells index represents a patron's id,
     * and each cell's content holds the amount of books that patron has borrowed.
     */
    int[] numPatronsBooksTaken;

    /*----=  Constructors  =-----*/

    /**
     *
     * @param maxBookCapacity       The maximal number of books this library can hold.
     * @param maxBorrowedBooks      The maximal number of books this library allows a single patron to borrow
     *                             at the same time.
     * @param maxPatronCapacity     The maximal number of registered patrons this library can handle.
     */
    Library(int maxBookCapacity, int maxBorrowedBooks, int maxPatronCapacity) {
        bookCapacity = maxBookCapacity;
        maxBorrowedLimit = maxBorrowedBooks;
        patronCapacity = maxPatronCapacity;
        libraryBooksArray = new Book[maxBookCapacity];
        patronsArray = new Patron[maxPatronCapacity];
        numPatronsBooksTaken = new int[maxPatronCapacity];
        numBooksInLibrary = 0;
        numPatronsInLibrary = 0;
        this.initializeArrays();
    }

    /*----=  Instance Methods  =-----*/

    /**
     * The method initializes the 3 arrays in the class variables to be empty arrays, of the size that is defined by
     * the limit of books in the library and the number of patrons that can be registered. This method is only called
     * upon when the library is first created.
     */
    void initializeArrays() {
        for (int i = 0; i < this.bookCapacity; i++) {
            this.libraryBooksArray[i] = null;
        }
        for (int i = 0; i < this.patronCapacity; i++) {
            this.patronsArray[i] = null;
            this.numPatronsBooksTaken[i] = 0;
        }
    }

    /**
     * Adds the given book to this library, if there is place available, and it isn't already in the library.
     * @param book The book to add to this library.
     * @return a non-negative id number for the book if there was a spot and the book was successfully added,
     * or if the book was already in the library; a negative number otherwise.
     */
    int addBookToLibrary(Book book) {
        // first, we'll check if the book already exists in the library. if it does, then we'll return it's id
        for (int i = 0; i < numBooksInLibrary; i++) {
            if (this.libraryBooksArray[i] == book) {
                return i;
            }
        }
        // next, we'll check if there is any room in the library
        if (this.numBooksInLibrary == this.bookCapacity) {
            return -1;
        }
        // now, when we are certain the book isn't already registered in the library and there in room left in the library, we can add the book
        this.libraryBooksArray[numBooksInLibrary] = book;
        this.numBooksInLibrary++;
        return numBooksInLibrary - 1;
    }

    /**
     * Returns true if the given number is an id of some book in the library, false otherwise.
     * @param bookId The id to check.
     * @return true if the given number is an id of some book in the library, false otherwise.
     */
    boolean isBookIdValid(int bookId) {
        if (bookId >= this.numBooksInLibrary || bookId < 0) {
            return false;
        }
        if (this.libraryBooksArray[bookId] == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns the non-negative id number of the given book if he is owned by this library, -1 otherwise.
     * @param book The book for which to find the id number.
     * @return a non-negative id number of the given book if he is owned by this library, -1 otherwise.
     */
    int getBookId(Book book) {
        for (int i = 0; i < this.bookCapacity; i++) {
            if (this.libraryBooksArray[i] == book) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns true if the book with the given id is available, false otherwise.
     * @param bookId Returns true if the book with the given id is available, false otherwise.
     * @return true if the book with the given id is available, false otherwise.
     */
    boolean isBookAvailable(int bookId) {
        if (!this.isBookIdValid(bookId)) {
            return false;
        }
        if (this.libraryBooksArray[bookId].getCurrentBorrowerId() == -1) {
            return true;
        }
        return false;
    }

    /**
     * Registers the given Patron to this library, if there is a spot available.
     * @param patron The patron to register to this library.
     * @return a non-negative id number for the patron if there was a spot and the patron was successfully registered,
     * a negative number otherwise.
     */
    int registerPatronToLibrary(Patron patron) {
        // first, we'll check if the patron is already registered to the library
        for (int i = 0; i < this.numPatronsInLibrary; i++) {
            if (patron == this.patronsArray[i])
            {
                return i;
            }
        }
        //next, we'll check if we have room for more patrons in the library
        if (this.numPatronsInLibrary == this.patronCapacity) {
            return -1;
        }
        //register the patron
        this.patronsArray[numPatronsInLibrary] = patron;
        this.numPatronsInLibrary++;
        return this.numPatronsInLibrary - 1;
    }

    /**
     * Returns true if the given number is an id of a patron in the library, false otherwise.
     * @param patronId The id to check.
     * @return true if the given number is an id of a patron in the library, false otherwise.
     */
    boolean isPatronIdValid(int patronId) {
        if (patronId >= numPatronsInLibrary || patronId < 0) {
            return false;
        }
        if (this.patronsArray[patronId] == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns the non-negative id number of the given patron if he is registered to this library, -1 otherwise.
     * @param patron  The patron for which to find the id number.
     * @return a non-negative id number of the given patron if he is registered to this library, -1 otherwise.
     */
    int getPatronId(Patron patron) {
        for (int i = 0; i < this.patronCapacity; i++) {
            if (this.patronsArray[i] == patron) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Marks the book with the given id number as borrowed by the patron with the given patron id, if this book is
     * available, the given patron isn't already borrowing the maximal number of books allowed, and if the patron
     * will enjoy this book.
     * @param bookId The id number of the book to borrow.
     * @param patronId The id number of the patron that will borrow the book.
     * @return true if the book was borrowed successfully, false otherwise.
     */
    boolean borrowBook(int bookId, int patronId) {
        // checks if the book id and parton id are legit
        if (!this.isBookIdValid(bookId) || (!this.isPatronIdValid(patronId))) {
            return false;
        }
        // checks if the book is available
        if (!this.isBookAvailable(bookId)) {
            return false;
        }
        //checks if the patron already maxed out his book rentals
        if (this.numPatronsBooksTaken[patronId] >= maxBorrowedLimit) {
            return false;
        }
        //checks if the patron will enjoy the book
        if (!this.patronsArray[patronId].willEnjoyBook(this.libraryBooksArray[bookId])){
            return false;
        }
        // everything checks out - we'll take the book!
        this.numPatronsBooksTaken[patronId]++;
        this.libraryBooksArray[bookId].setBorrowerId(patronId);
        return true;
    }

    /**
     * Return the given book.
     * @param bookId bookId - The id number of the book to return.
     */
    void returnBook(int bookId) {
        if (this.isBookIdValid(bookId)) {
            int borrowerID = libraryBooksArray[bookId].getCurrentBorrowerId();
            if (borrowerID != -1) {
                libraryBooksArray[bookId].returnBook();
                this.numPatronsBooksTaken[borrowerID]--;
            }
        }
    }

    /**
     * Suggest the patron with the given id the book he will enjoy the most, out of all available books he will enjoy,
     * if any such exist.
     * @param patronId The id number of the patron to suggest the book to.
     * @return The available book the patron with the given will enjoy the most. Null if no book is available.
     */
    Book suggestBookToPatron(int patronId) {
        boolean flag = true;
        if (!this.isPatronIdValid(patronId)) {
            return null;
        }
        int mostLikelyToEnjoyIndex = -1;
        int mostLikelyToEnjoyScore = 0;
        Patron patron = this.patronsArray[patronId];
        for (int i = 0; i < this.bookCapacity; i++) {
            if (this.libraryBooksArray[i] == null) {
                break;
            }
            if (patron.getBookScore(this.libraryBooksArray[i]) > mostLikelyToEnjoyScore &&
                    this.isBookAvailable(i)) {
                mostLikelyToEnjoyIndex = i;
                mostLikelyToEnjoyScore = patron.getBookScore(this.libraryBooksArray[i]);
            }
        }
        if ((mostLikelyToEnjoyIndex == -1) || (mostLikelyToEnjoyScore <= patron.getMinThreshold())) {
            flag = false;
        }
        if (flag) {
            return this.libraryBooksArray[mostLikelyToEnjoyIndex];
        } else {
            return null;
        }

    }

}