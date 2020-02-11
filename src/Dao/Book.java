package Dao;

import javafx.beans.property.SimpleStringProperty;

public class Book {
    private SimpleStringProperty code;
    private SimpleStringProperty bookname;

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

    public String getWriter() {
        return writer.get();
    }

    public SimpleStringProperty writerProperty() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer.set(writer);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getLoan() {
        return loan.get();
    }

    public SimpleStringProperty loanProperty() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan.set(loan);
    }



    private SimpleStringProperty writer;
    private SimpleStringProperty publisher;
    private SimpleStringProperty loan;


    public Book(String code, String bookname, String writer, String publisher, String loan){
        this.code=new SimpleStringProperty(code);
        this.bookname=new SimpleStringProperty(bookname);
        this.writer=new SimpleStringProperty(writer);
        this.publisher=new SimpleStringProperty(publisher);
        this.loan=new SimpleStringProperty(loan);


    }






}
