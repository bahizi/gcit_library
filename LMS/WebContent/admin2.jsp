<%@include file="main.html"%>
${result}
<div class="container">
	<div class="well bs-component">
	
	
		
		<div class="bs-example" data-example-id="simple-nav-justified">
    <ul class="nav nav-tabs nav-justified">
      <li role="presentation" class="active"><a class = "viewAuthors" href="#">Authors</a></li>
      <li role="presentation"><a class = "viewBooks" href="#">Books</a></li>
      <li role="presentation"><a class = "viewBorrowers" href="#">Borrowers</a></li>
      <li role="presentation"><a class = "viewPublishers" href="#">Publishers</a></li>
      <li role="presentation"><a class = "viewGenres" href="#">Genres</a></li>
      <li role="presentation"><a class = "viewLoans" href="#">Loans</a></li>
    </ul>
    <br>    
  </div>
		
<div class="jumbotron">
		<table  id = "myTable" class="table table-striped table-hover">
		</table>
			

	</div>
</div>




<script type="text/javascript">
	jQuery(function($) {
		$('.panel-heading span.clickable')
				.on(
						"click",
						function(e) {
							if ($(this).hasClass('panel-collapsed')) {
								// expand the panel
								$(this).parents('.panel').find('.panel-body')
										.slideDown();
								$(this).removeClass('panel-collapsed');
								$(this).find('i').removeClass(
										'glyphicon-chevron-down').addClass(
										'glyphicon-chevron-up');
							} else {
								// collapse the panel
								$(this).parents('.panel').find('.panel-body')
										.slideUp();
								$(this).addClass('panel-collapsed');
								$(this).find('i').removeClass(
										'glyphicon-chevron-up').addClass(
										'glyphicon-chevron-down');
							}
						});
	});
</script>

<script src="./assets/js/admin.js"></script>