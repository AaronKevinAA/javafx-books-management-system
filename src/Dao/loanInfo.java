package Dao;

import javafx.beans.property.SimpleStringProperty;

public class loanInfo {
    private SimpleStringProperty code;

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getBookname() {
        return bookname.get();
    }

    public SimpleStringProperty booknameProperty() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname.set(bookname);
    }

    public String getReturndate() {
        return returndate.get();
    }

    public SimpleStringProperty returndateProperty() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate.set(returndate);
    }

    public String getLoandate() {
        return loandate.get();
    }

    public SimpleStringProperty loandateProperty() {
        return loandate;
    }

    public void setLoandate(String loandate) {
        this.loandate.set(loandate);
    }

    private SimpleStringProperty bookname;
    private SimpleStringProperty returndate;
    private SimpleStringProperty loandate;

    public loanInfo(String code, String bookname,String loandate,String returndate) {
        this.code = new SimpleStringProperty(code);
        this.bookname = new SimpleStringProperty(bookname);
        this.returndate = new SimpleStringProperty(returndate);
        this.loandate = new SimpleStringProperty(loandate);
    }
}
