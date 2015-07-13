package com.gcit.lms.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gcit.lms.domain.Author;
import com.gcit.lms.domain.Book;
import com.gcit.lms.domain.Borrower;
import com.gcit.lms.domain.Genre;
import com.gcit.lms.domain.LibraryBranch;
import com.gcit.lms.domain.Publisher;
import com.gcit.lms.service.AdministrativeService;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addAuthor", "/addPublisher", "/viewAuthors", "/deleteAuthor",
		"/editAuthor","/addBook", "/addBorrower" , "/deleteBook",
		"/searchAuthors", "/searchBooks", "/searchBorrowers", "/searchLibraries", "/searchPublishers", "/searchGenres"})
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		///deleteAuthor(request, response);
		System.out.println("Received a GET Request!");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Received a POST Request!");

		String reqUrl = request.getRequestURI().substring(
				request.getContextPath().length(),
				request.getRequestURI().length());
		switch (reqUrl) {
		case "/addAuthor":
			createAuthor(request, response);
			break;
		case "/addBorrower":
			createBorrower(request, response);
			break;

		case "/addPublisher":
			createPublisher(request, response);
			break;
			
		case "/addBook": 
			createBook(request, response);
			break;
		case "/editAuthor": 
			System.out.println("YAHOOOO");
			editAuthor(request, response);
			break;
		case "/viewAuthors":
			viewAuthors(request, response);
			break;
		case "/searchAuthors": 
			searchAuthors(request, response);
			break;
		case "/searchBooks": 
			searchBooks(request, response);
			break;
		case "/searchBorrowers": 
			searchBorrowers(request, response);
			break;
		case "/searchLibraries": 
			searchLibraries(request, response);
			break;
		case "/searchPublishers": 
			searchPublishers(request, response);
			break;
		case "/searchGenres": 
			searchGenres(request, response);
			break;
		case "/deleteBook": {
			editAuthor(request, response);
			break;
		}
		default:
			break;
		}
	}

	private void searchGenres(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Object> rawData= new ArrayList<Object>();	
		try {
			int pageNum = Integer.parseInt(request.getParameter("pageNum"));
			String query = request.getParameter("searchString");
			AdministrativeService service = new AdministrativeService();
			List<Genre> genres = service.searchGenres(query, pageNum);
			int pageCount = service.getGenrePageCount(query);
			rawData.add(genres);
			rawData.add(pageCount);
			rawData.add(getJSONStatus(true));
		} catch (Exception e) {
			rawData.add(getJSONStatus(false));
			e.printStackTrace();
		}
		response.getWriter().write(toJSON(rawData));
	}

	private void searchPublishers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Object> rawData= new ArrayList<Object>();	
		try {
			int pageNum = Integer.parseInt(request.getParameter("pageNum"));
			System.out.println("Received page "+ pageNum);
			String query = request.getParameter("searchString");	
			AdministrativeService service = new AdministrativeService();
			List<Publisher> genres = service.searchPublishers(query, pageNum);
			rawData.add(genres);
			rawData.add(service.getPublisherPageCount(query));
			rawData.add(getJSONStatus(true));
		} catch (Exception e) {
			rawData.add(getJSONStatus(false));
			e.printStackTrace();
		}
		response.getWriter().write(toJSON(rawData));
		
	}

	private void searchLibraries(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Object> rawData= new ArrayList<Object>();	
		try {
			int pageNum = Integer.parseInt(request.getParameter("pageNum"));
			String query = request.getParameter("searchString");		
			AdministrativeService service = new AdministrativeService();
			List<LibraryBranch> genres = service.searchLibraries(query, pageNum);
			rawData.add(genres);
			rawData.add(service.getLibraryPageCount(query));
			rawData.add(getJSONStatus(true));
		} catch (Exception e) {
			rawData.add(getJSONStatus(false));
			e.printStackTrace();
		}
		response.getWriter().write(toJSON(rawData));
		
	}

	private void searchBorrowers(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Object> rawData= new ArrayList<Object>();	
		try {
			int pageNum = Integer.parseInt(request.getParameter("pageNum"));
			String query = request.getParameter("searchString");			
			System.out.println("Searching for borrower: "+query);
			AdministrativeService service = new AdministrativeService();
			List<Borrower> genres = service.searchBorrowers(query, pageNum);
			rawData.add(genres);
			rawData.add(service.getBookPageCount(query));
			rawData.add(getJSONStatus(true));
		} catch (Exception e) {
			rawData.add(getJSONStatus(false));
			e.printStackTrace();
		}
		response.getWriter().write(toJSON(rawData));
		
	}

	private void searchBooks(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Object> rawData= new ArrayList<Object>();	
		try {
			int pageNum = Integer.parseInt(request.getParameter("pageNum"));
			String query = request.getParameter("searchString");
			AdministrativeService service = new AdministrativeService();
			List<Book> genres = service.searchBooks(query, pageNum);
			rawData.add(genres);
			rawData.add(service.getBookPageCount(query));
			rawData.add(getJSONStatus(true));
		} catch (Exception e) {
			rawData.add(getJSONStatus(false));
			e.printStackTrace();
		}
		response.getWriter().write(toJSON(rawData));
		
	}

	private void searchAuthors(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArrayList<Object> rawData= new ArrayList<Object>();	
		try {
			int pageNum = Integer.parseInt(request.getParameter("pageNum"));
			
			String query = request.getParameter("searchString");
			AdministrativeService service = new AdministrativeService();
			List<Author> genres = service.searchAuthors(query, pageNum);			
			rawData.add(genres);
			rawData.add(service.getAuthorPageCount(query));
			rawData.add(getJSONStatus(true));
		} catch (Exception e) {
			rawData.add(getJSONStatus(false));
			e.printStackTrace();
		}
		response.getWriter().write(toJSON(rawData));
		
	}
		
	private HashMap<String,Boolean> getJSONStatus(boolean b){
		HashMap<String, Boolean > res = new HashMap<String, Boolean>();
		res.put("ok", b);
		return res;
	}
	private String toJSON(ArrayList<Object> object){
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json ="";
		try {
			json = ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			json ="{\"ok\": false}";
			e.printStackTrace();
			
		}
		return json;
	}

	private void editAuthor(HttpServletRequest request,
			HttpServletResponse response) {
		String authorName = request.getParameter("authorName");
		int authorId = Integer.parseInt(request.getParameter("authorId"));
		Author a = new Author();
		a.setAuthorName(authorName);
		a.setAuthorId(authorId);
		AdministrativeService adminService = new AdministrativeService();
		try {
			adminService.updateAuthor(a);
			request.setAttribute("result", "Author updated Successfully");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("result",
					"Author update failed " + e.getMessage());
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/viewAuthors.jsp");
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createAuthor(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String authorName = request.getParameter("authorName");
		Author a = new Author();
		a.setAuthorName(authorName);
		AdministrativeService adminService = new AdministrativeService();
		try {
			adminService.createAuthor(a);
			request.setAttribute("result", "Author Added Successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("result",
					"Author add failed " + e.getMessage());
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/admin.jsp");
		rd.forward(request, response);
	}
	
	private void createBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		String title = request.getParameter("title");
		String[] authors = request.getParameter("authors").split(",");
		String pub = request.getParameter("publisher");
		String[] genres = request.getParameter("genres").split(",");
		System.out.println("Title1: "+title);
		System.out.println("Authors1:  "+authors[0]);
		System.out.println("Publisher1:  "+pub);
		System.out.println("Genres1:  "+genres[0]);
		Book book = new Book();
		book.setTitle(title);		 
		AdministrativeService adminService = new AdministrativeService();		
		try {
			adminService.createBook(book,authors,pub,genres);
			request.setAttribute("result", "Book added Successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("result",
					"Book add failed " + e.getMessage());
		}		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/admin.jsp");
		rd.forward(request, response);
		
	}

	private void createPublisher(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String publisherName = request.getParameter("publisherName");
		String publisherAddress = request.getParameter("publisherAddress");
		String publisherPhone = request.getParameter("publisherPhone");
		Publisher p = new Publisher();
		p.setPublisherName(publisherName);
		p.setPublisherAddress(publisherAddress);
		p.setPublisherPhone(publisherPhone);
		AdministrativeService adminService = new AdministrativeService();
		try {
			adminService.createPublisher(p);
			request.setAttribute("result", "Publisher added Successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("result",
					"Publisher add failed " + e.getMessage());
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/admin.jsp");
		rd.forward(request, response);
	}
	private void createBorrower(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String borName = request.getParameter("borName");
		String borAddress = request.getParameter("borAddress");
		String borPhone = request.getParameter("borPhone");
		Borrower b = new Borrower();
		b.setName(borName);
		b.setAddress(borAddress);
		b.setPhone(borPhone);
		AdministrativeService adminService = new AdministrativeService();
		try {
			adminService.createBorrower(b);
			request.setAttribute("result", "Borrower added Successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("result",
					"Borrower add failed " + e.getMessage());
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/admin.jsp");
		rd.forward(request, response);
	}

	private List<Author> viewAuthors(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			return new AdministrativeService().readAuthors(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void deleteAuthor(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String authorId = request.getParameter("authorId");
		Author author = new Author();
		author.setAuthorId(Integer.parseInt(authorId));

		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/viewAuthors.jsp");
		try {
			new AdministrativeService().deleteAuthor(author);

			request.setAttribute("result", "Author Deleted Succesfully!");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("result",
					"Author Delete Failed because: " + e.getMessage());
		}

		rd.forward(request, response);
	}
}
