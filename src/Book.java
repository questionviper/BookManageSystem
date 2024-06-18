import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Book {
    String Name;
    String Writer;
    String IsbnNum;
    String Publisher;
    boolean BorrowStatement;
    String Borrower;
    String BorrowDate;
    int Id;
}
