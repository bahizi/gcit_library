package com.gcit.lms;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.BookCopies;
import com.gcit.lms.domain.BookLoan;
import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
import com.gcit.lms.domain.Publisher;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeController {

	@Autowired
	AuthorDAO authorDAO;

	@Autowired
	BookDAO bookDAO;
	@Autowired
	BorrowerDAO borrowerDAO;
	@Autowired
	LibraryBranchDAO branchDAO;
	@Autowired
	PublisherDAO pubDAO;
	@Autowired
	GenreDAO genreDAO;
	@Autowired
	BookLoanDAO bookLoanDAO;
	@Autowired
	BookCopiesDAO bookCopiesDAO;


	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate );

		return "home";
	}
//AUTHORS
	@RequestMapping(value="/author/get", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public List<Author> getAuthors(){
		try {
			return authorDAO.readAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	@RequestMapping(value="/authors/search/{query}/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<Author> searchAuthors(@PathVariable String query, @PathVariable int pageNum){
		if(query.equals("*")){
			query ="";	
		}
		return authorDAO.search(query, pageNum);	
	}
	@Transactional
	@RequestMapping(value="/authors/add", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> addAuthor(@RequestBody Author author){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			authorDAO.create(author);
			res.put("ok", true);;
			res.put("id", author.getAuthorId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;		 
	}	
	
	@Transactional
	@RequestMapping(value="/author/edit", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> editAuthor(@RequestBody Author author){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			authorDAO.update(author);
			System.out.println("Editing "+author.getAuthorId() +" :" +author.getAuthorName());
			res.put("ok", true);;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;
		
	}
	
	@Transactional
	@RequestMapping(value="/author/delete/{id}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public Boolean deleteAuthor(@PathVariable int id){
		try {
			Author toDel = new Author();
			toDel.setAuthorId(id);
			authorDAO.delete(toDel);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	

	@Transactional
	@RequestMapping(value="/book/add", method={RequestMethod.GET, RequestMethod.POST} , consumes="application/json")
	public String addBook(@RequestBody Book book){
		bookDAO.create(book);
		return "Book added sucessfully";

	}

	@RequestMapping(value="/author/getOne", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json", consumes="application/json")
	public Author getOneAuthor(@RequestBody Author author){
		return authorDAO.readOne(author.getAuthorId());

	}

	@Transactional
	@RequestMapping(value="/author/update", method={RequestMethod.GET, RequestMethod.POST} , consumes="application/json", produces="application/json")
	public String updateAuthor(@RequestBody Author author){
		authorDAO.update(author);
		return "Author updated sucessfully";

	}
	
	
	//BOOKS
	@RequestMapping(value="/books/get/{bookId}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")

	public Book getAuthor(@PathVariable int bookId){
		return bookDAO.readOne(bookId);	
	}
	
	
	@RequestMapping(value="/books/search/{query}/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<Book> searchBooks(@PathVariable String query, @PathVariable int pageNum){
			if(query.equals("*")){
				query ="";	
			}
			System.out.println("Searching books");
			return bookDAO.search(query, pageNum);	
		}
	@Transactional
	@RequestMapping(value="/book/edit", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> editBook(@RequestBody Book book){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			bookDAO.update(book);
			System.out.println("Editing "+book.getBookId() +" :" +book.getTitle());
			res.put("ok", true);;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;
		
	}
	@Transactional
	@RequestMapping(value="/books/delete/{id}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public HashMap<Object,Object> deleteBook(@PathVariable int id){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			Book toDel = new Book();
			toDel.setBookId(id);
			bookDAO.delete(toDel);
			res.put("ok", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.put("ok", false);
			res.put("error", e.getMessage());
			
		}
		return res;
		
	}
	

//BORROWERS
	@RequestMapping(value="/borrowers/get", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public List<Borrower> getBorrowers(){
		return borrowerDAO.readAll();

	}
	@RequestMapping(value="/borrowers/search/{query}/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<Borrower> searchBorrowers(@PathVariable String query, @PathVariable int pageNum){
		if(query.equals("*")){
			query ="";	
		}
		return borrowerDAO.search(query, pageNum);	  
		
	}
	@Transactional
	@RequestMapping(value="/borrowers/add", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> addBorrower(@RequestBody Borrower bor){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			System.out.println(bor.getName());
			System.out.println(bor.getAddress());
			System.out.println(bor.getPhone());
			borrowerDAO.create(bor);
			res.put("ok", true);;
			res.put("id", bor.getCardNo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;
		
	}
	@Transactional
	@RequestMapping(value="/borrowers/edit", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> editBorrower(@RequestBody Borrower bor){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			borrowerDAO.update(bor);
			res.put("ok", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;
		
	}
	
	@Transactional
	@RequestMapping(value="/borrowers/delete/{id}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public HashMap<Object,Object> deleteBorrower(@PathVariable int id){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			Borrower toDel = new Borrower();
			toDel.setCardNo(id);
			borrowerDAO.delete(toDel);
			res.put("ok", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.put("ok", false);
			res.put("error", e.getMessage());
			
		}
		return res;
		
	}
	
	//BRANCHES	
	@RequestMapping(value="/branches/get", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public List<LibraryBranch> getBrances(){
		return branchDAO.readAll();

	}
	@RequestMapping(value="/branches/search/{query}/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<LibraryBranch> searchBranches(@PathVariable String query, @PathVariable int pageNum){
		if(query.equals("*")){
			query ="";	
		}
		return branchDAO.search(query, pageNum);	
	}
	
	@Transactional
	@RequestMapping(value="/branches/add", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> addBranch(@RequestBody LibraryBranch branch){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			branchDAO.create(branch);
			res.put("ok", true);;
			res.put("id", branch.getBranchId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;
		
	}
	@Transactional
	@RequestMapping(value="/branches/edit", method= RequestMethod.POST , produces="application/json")
	public HashMap<Object,Object> editBranch(@RequestBody LibraryBranch branch){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			branchDAO.update(branch);
			res.put("ok", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			res.put("ok", false);
			res.put("error", e.getMessage());
			e.printStackTrace();
			
		}
		return res;
		
	}
	@Transactional
	@RequestMapping(value="/branches/delete/{id}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public HashMap<Object,Object> deleteBranch(@PathVariable int id){
		HashMap<Object,Object> res = new HashMap<Object,Object>();
		try {
			LibraryBranch toDel = new LibraryBranch();
			toDel.setBranchId(id);
			branchDAO.delete(toDel);
			res.put("ok", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.put("ok", false);
			res.put("error", e.getMessage());			
		}
		return res;
		
	}

	
	//PUBLISHERS
	@RequestMapping(value="/publishers/get", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public List<Publisher> getPublishers(){
		return pubDAO.readAll();

	}
	@RequestMapping(value="/publishers/search/{query}/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<Publisher> searchPublishers(@PathVariable String query, @PathVariable int pageNum){
		if(query.equals("*")){
			query ="";	
		}
		return pubDAO.search(query, pageNum);	
	}
	
	//GENRES
	@RequestMapping(value="/genres/get", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public List<Genre> getGenres(){
		return genreDAO.readAll();

	}	

	@RequestMapping(value="/genres/search/{query}/{pageNum}", method = {RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<Genre> searchGenres(@PathVariable String query, @PathVariable int pageNum){
		if(query.equals("*")){
			query ="";	
		}
		return genreDAO.search(query, pageNum);

	}


	
	//BOOKLOAN
	@RequestMapping(value="/bookLoan/get/{bookId}/{libId}/{cardNo}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")

	public BookLoan getBookLoan(@PathVariable int bookId,@PathVariable int libId, @PathVariable int cardNo){
		return bookLoanDAO.readOne(bookId,libId,cardNo);	
	}
	@RequestMapping(value="/bookLoan/canBorrow/{bookId}/{libId}/{cardNo}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")

	public Boolean canBorrow(@PathVariable int bookId,@PathVariable int libId, @PathVariable int cardNo){
		return bookCopiesDAO.canBorrow(libId, bookId, cardNo);
		
	}
	@RequestMapping(value="/checkLoan/{bookId}/{libId}/{cardNo}", method={RequestMethod.GET, RequestMethod.POST} , produces="application/json")
	public HashMap<String,Boolean> checkBookLoan(@PathVariable int bookId,@PathVariable int libId, @PathVariable int cardNo){
		HashMap<String,Boolean> res = new HashMap<String, Boolean>();
		res.put("canBorrow",bookCopiesDAO.canBorrow(libId, bookId, cardNo));
		res.put("canReturn", bookCopiesDAO.canReturn(libId, bookId, cardNo));
		return res;
		
	}

}
