<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Petagram</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="css/normalize.css">
<link rel="stylesheet" href="css/font-awesome.css">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/templatemo-style.css">
<script src="js/vendor/modernizr-2.6.2.min.js"></script>
</head>
<body>
	<!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->

	<div id="loader-wrapper">
		<div id="loader"></div>
	</div>

	<div class="content-bg"></div>
	<div class="bg-overlay"></div>

	<!-- SITE TOP -->
	<div class="site-top">
		<div class="site-header clearfix">
			<div class="container">
				<a href="#" class="site-brand pull-left"><strong>Petagram</strong></a>
				<div class="row">
					<div class="col-md-4 col-md-offset-6">
						<form action="" class="search-form">
							<div class="form-group has-feedback">
								<label for="search" class="sr-only">Search</label> <input
									type="text" class="form-control" name="search" id="search"
									placeholder="search"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<!-- .site-header -->
		<div class="site-banner">
			<div class="container">
				<div class="row">
					<div class="col-md-offset-2 col-md-8">
						<div class="subscribe-form">
							<div class="subscribe-form">
								<form accept-charset="UTF-8" action="" method="POST">
									<textarea class="form-control counted" name="message"
										placeholder="What's happening..." rows="4"
										style="margin-bottom: 5px; background: transparent;"></textarea>
									<div class="pull-right">
										<button class="btn btn-info" type="submit">Post</button>
									</div>
									<h6 id="counter">320 characters remaining</h6>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- .site-banner -->
	</div>
	<!-- .site-top -->
	<!-- MAIN POSTS -->
	<div class="main-posts">
		<div class="container">

			<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
			<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
			<c:choose>
				<c:when test="${users !=null}">
					<h3>Search Result:</h3>
					<br>
					<c:if test="${fn:length(users) == 0}">
					No results matched
				</c:if>
					<c:if test="${fn:length(users) != 0}">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>User Name</th>
									<th>Details</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="user" items="${users}">
									<tr>
										<td>${user.getUserName() }</td>
										<td><a
											href="view-user.do?user_name=${user.getUserName()}">view</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
				</c:when>
			</c:choose>
		</div>
	</div>

	<!-- FOOTER -->
	<footer class="site-footer">
		<div class="container">
			<div class="row">
				<div class="col-md-12 text-center">
					<div class="social-icons">
						<ul>
							<li><a href="#" class="fa fa-facebook"></a></li>
							<li><a href="#" class="fa fa-twitter"></a></li>
							<li><a href="#" class="fa fa-instagram"></a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 text-center">
					<p class="copyright-text">Copyright &copy; 2015 Blossom |
						eBusiness Technology</p>
				</div>
			</div>
		</div>
	</footer>

	<script src="js/jquery.min.js"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="js/vendor/jquery-1.10.2.min.js"><\/script>')
	</script>
	<script src="js/min/plugins.min.js"></script>
	<script src="js/min/main.min.js"></script>

	<!-- Preloader -->
	<script type="text/javascript">
		//<![CDATA[
		$(window).load(function() { // makes sure the whole site is loaded
			$('#loader').fadeOut(); // will first fade out the loading animation
			$('#loader-wrapper').delay(350).fadeOut('slow'); // will fade out the white DIV that covers the website.
			$('body').delay(350).css({
				'overflow-y' : 'visible'
			});
		})
		//]]>
	</script>
	<!-- templatemo 434 masonry -->
</body>
</html>