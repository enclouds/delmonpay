$(function(){

	//은행선택
	if ($(".lst_swiper.ty2").length > 0){
		swiper0 = new Swiper(".lst_swiper", {
			direction: "vertical",
			centeredSlides: true,
			slidesPerView: 5,
			observer: true,
			observeParents: true,
			on:{
			}
		});
		swiper0.slideTo($(".lst_swiper .current").index(), 1000, false);
	}


	//날짜선택
	if ($(".swiper1").length > 0){
		var swiper1 = new Swiper(".swiper1", {
			direction: "vertical",
			centeredSlides: true,
			slidesPerView: 3,
			observer: true,
			observeParents: true,
		});
		swiper1.slideTo($(".swiper1 .current").index(), 1000, false);

		var swiper2 = new Swiper(".swiper2", {
			direction: "vertical",
			centeredSlides: true,
			slidesPerView: 3,
			observer: true,
			observeParents: true,
		});
		swiper2.slideTo($(".swiper2 .current").index(), 1000, false);

		var swiper3 = new Swiper(".swiper3", {
			direction: "vertical",
			centeredSlides: true,
			slidesPerView: 3,
			observer: true,
			observeParents: true,
		});
		swiper3.slideTo($(".swiper3 .current").index(), 1000, false);
	}


	//계약
	textnum();
	$(".textNum").on('keydown', function(e){
		textnum();
	});

	$(".textNum").on('focus', function(e){
		var value = $(".textNum").val();
		if (value > 0){
			$(".textNum").val("")
		}
		return false;
	});


	//도움말
	$(".btn_i").on("click", function(){
		$(this).parents("div").addClass("on");
	});
	$(document).on("click", function(e){
		if($(".box_i").has(e.target).length === 0){
			$(".box_i").removeClass("on");
		}
		textnum();
	});

	
});


function textnum(){
	var value = $(".textNum").val();
	$("#virtual_dom").text(value);

	var inputWidth =  $('#virtual_dom').width() + 20; 
	$(".textNum").css('width', inputWidth); 
}



//송금일 선택 드랍박스 영역 나왓다안나왓다
$(document).ready( function() {
$("#forms3").hide();
$("#input4").change(function () {
	$("#forms3").hide();
	$('#form' + $(this).find('option:selected').attr('id')).show();
});
});

