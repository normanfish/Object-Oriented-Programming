/**
 * This class represents a patron, who has a name, literary critic attributes, and a minimum threshold for enjoying books.
 */
class Patron {

    /**
     * patron's first name.
     */
    final String firstName;

    /**
     * patron's last name.
     */
    final String lastName;

    /**
     * patron's comic critic attribute.
     */
    int comicAttribute;

    /**
     * patron's dramatic critic attribute.
     */
    int dramaticAttribute;

    /**
     * patron's educational critic attribute.
     */
    int educationalAttribute;

    /**
     * the patron's minimum threshold for enjoying a book. Based on the ook's literary value, and the weight the patron
     * gives to each aspect (comic, dramatic and educational).
     */
    int minThreshold;

    /*----=  Constructors  =-----*/

    /**
     * Creates a new Patron with the given characteristics.
     *
     * @param patronFirstName          The first name of the patron.
     * @param patronLastName           The last name of the patron.
     * @param comicTendency            The weight the patron assigns to the comic aspects of books.
     * @param dramaticTendency         The weight the patron assigns to the dramatic aspects of books.
     * @param educationalTendency      The weight the patron assigns to the educational aspects of books.
     * @param patronEnjoymentThreshold The minimal literary value a book must have for this patron to enjoy it.
     */
    Patron(String patronFirstName, String patronLastName, int comicTendency, int dramaticTendency,
           int educationalTendency, int patronEnjoymentThreshold) {
        firstName = patronFirstName;
        lastName = patronLastName;
        comicAttribute = comicTendency;
        dramaticAttribute = dramaticTendency;
        educationalAttribute = educationalTendency;
        minThreshold = patronEnjoymentThreshold;
    }


    /*----=  Getters and Setters  =-----*/

    /**
     *
     * @return the patron's first name
     */
    String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return the patron's last name
     */
    String getLastName() {
        return lastName;
    }

    /**
     *
     * @return The weight the patron assigns to the comic aspects of books.
     */
    int getComicAttribute() {
        return comicAttribute;
    }

    /**
     *
     * @param comicAttribute sets the weight the patron assigns to the comic aspects of books to comicAttribute.
     */
    void setComicAttribute(int comicAttribute) {
        this.comicAttribute = comicAttribute;
    }

    /**
     *
     * @return The weight the patron assigns to the dramatic aspects of books.
     */
    int getDramaticAttribute() {
        return dramaticAttribute;
    }

    /**
     *
     * @param dramaticAttribute sets the weight the patron assigns to the dramatic aspects of books to dramaticAttribute.
     */
    void setDramaticAttribute(int dramaticAttribute) {
        this.dramaticAttribute = dramaticAttribute;
    }

    /**
     *
     * @return The weight the patron assigns to the educational aspects of books.
     */
    int getEducationalAttribute() {
        return educationalAttribute;
    }

    /**
     *
     * @param educationalAttribute sets the weight the patron assigns to the educational aspects of books to
     *                            educationalAttribute.
     */
    void setEducationalAttribute(int educationalAttribute) {
        this.educationalAttribute = educationalAttribute;
    }

    /**
     *
     * @returns the minimal literary value a book must have for this patron to enjoy it.
     */
    int getMinThreshold() {
        return minThreshold;
    }

    /**
     *
     * @param minThreshold sets the threshold for literary value a book must have for the patron to enjoy it as minThreshold
     */
    void setMinThreshold(int minThreshold) {
        this.minThreshold = minThreshold;
    }


    /*----=  Instance Methods  =-----*/

    /**
     * Returns a string representation of the patron, which is a sequence of its first and last name,
     * separated by a single white space. For example, if the patron's first name is "Ricky" and his last name
     * is "Bobby", this method will return the String "Ricky Bobby".
     * @return the String representation of this patron.
     */
    String stringRepresentation() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the literary value this patron assigns to the given book.
     * @param book The book to assess.
     * @return the literary value this patron assigns to the given book.
     */
    int getBookScore(Book book) {
        return this.comicAttribute * book.getComicValue() +
                this.dramaticAttribute * book.getDramaticValue() +
                this.educationalAttribute * book.getEducationalValue();
    }

    /**
     * Returns true of this patron will enjoy the given book, false otherwise.
     * @param book The book to assess.
     * @return true of this patron will enjoy the given book, false otherwise.
     */
    boolean willEnjoyBook(Book book) {
        return getBookScore(book) > this.minThreshold;
    }
}