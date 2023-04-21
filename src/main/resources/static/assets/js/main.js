//메인상단 고정
$(function(){
	var SCROLL_TOP = $(window).scrollTop();
	var hdTit = $(".tit_calendar").offset().top;
	var hdHeight = $(".header").outerHeight();

	$(window).scroll(function(){
		SCROLL_TOP = $(window).scrollTop();		
		if(SCROLL_TOP > hdTit - hdHeight){
			$(".tit_calendar").addClass("scroll");
			console.log("aa");
		} else {
			$(".tit_calendar").removeClass("scroll");
			console.log("bb");
		}
	});
})


//카드내역 상세
