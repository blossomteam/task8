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
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/foundation.css" />
<link rel="stylesheet" href="css/foundation-icons.css" />

<script src="js/vendor/modernizr-2.6.2.min.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/foundation.min.js"></script>
<script src="js/foundation.js"></script>

<style>
.dropdown-menu>li>a:hover {
	background: none;
	color: #fff;
}

.nav .open>a {
	background: none !important;
	color: #fff !important;
}

.nav>li>a:hover {
	background: none;
}

.dropdown ul.dropdown-menu {
	margin-top: -10px;
}

.navbar-nav>li>a {
	text-align: left !important;
}
</style>
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
	<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
	<!-- SITE TOP -->
	<div class="site-top">
		<div class="site-header clearfix">
			<div class="container">


				<div class="site-brand pull-left">
					<ul class="nav navbar-nav">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown"> ${user.getUserName() }</a>
							<ul class="dropdown-menu">
								<li><a href="#">Account Settings </a></li>
								<li><a href="#">Messages </a></li>
								<li><a href="#">Sign Out </a></li>
							</ul></li>
					</ul>
				</div>




				<div class="row">
					<div class="col-md-4 col-md-offset-8">
						<form action="search-photo.do" class="search-form">
							<div class="form-group has-feedback">
								<label for="search" class="sr-only">Search</label> <input
									type="text" class="form-control" name="keyword" id="search"
									placeholder="search"> <span
									class="glyphicon glyphicon-search form-control-feedback"></span>
							</div>
						</form>
					</div>
				</div>

			</div>
		</div>
		<!-- .site-header -->

	

	<!-- .site-header -->
		<div class="site-banner">
			<div class="container">
				<div class="row">
					<div class="col-md-offset-2 col-md-8 text-center">
						<h2>
							PETAGRAM <span class="blue">colorful</span><span class="green">life</span>
						</h2>
					</div>
				</div>
			</div>
		</div>
		<!-- .site-banner -->
	</div>	
	<div class="site-body">
		<div class="row">
			<div class="container">
				<div class="col-md-12" style="text-align: center">
					<img src=""/>
				</div>
			</div>
		</div>
		<div>
			<br />
		</div>
		<hr>
		<div class="container">
			<div class="row">
				<div class="col-md-6">
					<div class="testimonial-wrap">
						<div class="row">
							<div class="col-md-12 testimonial">
								<div class="quote">
									<h4>Title</h4>
									<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.
										In ultrices, elit sed faucibus pharetra, diam mauris bibendum
										orci, sit amet ullamcorper purus dui sit amet augue. Donec
										aliquet diam ut neque mattis, eu tristique sem rutrum.</p>
								</div>
								<div class="student">
									<div class="photo"></div>
									<p>John Doe</p>
									<p>Important person, some Company</p>
								</div>
							</div>
						</div>
					</div>
					<hr>
					<div class="hug">
						<a href="#" class="button">Give it a Hug!</a>
					</div>
					<div class="row social-button">
						<ul class="button-group even-3">
							<li><i class="fi-eye large"></i><span>938794</span></li>
							<li><i class="fi-comments large"></i><span>94</span></li>
							<li><i class="fi-heart large"></i><span>894</span></li>
						</ul>
					</div>
				</div>
				<div class="col-md-1"></div>
				<div class="col-md-5">
					<h4>Comments:</h4>
					<div class="row">
					<div class="col-md-12">
						<div class="subscribe-form">
							<div class="subscribe-form">
								<form accept-charset="UTF-8" method="POST" action="comment.do"
									enctype="multipart/form-data">
									<textarea class="form-control counted" name="text"
										placeholder="Your comments: ..." rows="4"
										style="margin-bottom: 5px; background: transparent;"
										maxlength="150"></textarea>
									<div class="pull-right">
										<button class="btn btn-info" type="submit">Comment</button>
									</div>
									<!--                                      <h6 id="counter">320 characters remaining</h6> -->
								</form>
							</div>
						</div>
					</div>
					</div>
					<div class="comment">
						<section class="viewer">
							<h6 class="byline">
								<a href="#"><i class="icon"></i>Caleb Winters</a> <small>said
									<span class="data"> 4h Ago </span>
								</small>
							</h6>
						</section>
						<section class="content">
							<p>You've got the look!</p>
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>



	<!-- FOOTER -->
	<footer class="site-footer">
		<div class="container">
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

