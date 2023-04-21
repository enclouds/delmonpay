//팝업 열기
function openLayerPopup(id){
	var $obj = $("#" +id);
	
	$(".layerPopup").hide();
	$("body").addClass("popfix");
	
	if (!$obj.hasClass("ty2")){
		$obj.fadeIn(100);
		$obj.before("<div class='popup_dim ty1'></div>");
	} else {
		$("#" +id).fadeIn(100, function(){
			$(this).find(".popup").animate({bottom:'0'}, 200);
		});
		$obj.before("<div class='popup_dim'></div>");
	}
	


	$(".b-close, .popup_dim").on("click", function(){
		if (!$obj.hasClass("ty2")){
			$obj.hide();
		} else {
			$obj.hide(0, function(){
				$(this).find(".popup").animate({bottom:'-100%'}, 100);
			});
		}

		$("body").removeClass("popfix");
		$(".popup_dim").remove();
	});
}



//팝업 -alert
function openLayerPopup2(id){
	var $obj = $("#" +id);
	
	$obj.fadeIn(100,function(){
		$(this).find(".popup").animate({top:'50%'}, 200);
		$("body").addClass("popfix");
	});
	
	$obj.before("<div class='popup_dim2'></div>");

	$(".b-close2, .popup_dim2").on("click", function(){
		$obj.hide();
		$obj.find(".popup").css("top","0");
		if (!$(".layerPopup").is(":visible")){
			$("body").removeClass("popfix");
		}		
		$(".popup_dim2").remove();
	});
}

//팝업 -toste
function openLayerPopup3(id){
	var $obj = $("#" +id);
	
	$obj.fadeIn(100,function(){
		$(this).find(".popup").animate({top:'0'}, 200);
	});

	$(".b-close3").on("click", function(){
		$obj.hide();
		$obj.find(".popup").css("top","-50%");
	});
}


$(function(){
	$(document).on('change', ('.upload-hidden'), function(){
		var $target  = $(this);
		var $targetbtn = $target.siblings('.btn_file_del');

		if(window.FileReader){
			var filePath = $target.val();
			var fileName = $target[0].files[0].name;
		} else {
			var filePath = $target.val();
			var fileName = $target.val().split('/').pop().split('\\').pop();
		}

		//파일명 공백체크
		if (fileName && /\s/gi.test(fileName)) {
			alert("파일명에 공백이 들어가있습니다.\n파일명에 공백을 제거 후 다시 등록해주세요.");
			fileReset($target);
			return false;
		}
		
		$target.siblings('.inp_txt').val(fileName);// 추출한 파일명 삽입

		$targetbtn.show();
		$targetbtn.on("click", function(){
			$targetbtn.hide();
			fileReset($target);
		});
	});
});


//첨부파일박스삭제
function fileReset($target) {
	if ($target != $(this)){
		$target = $($target);
	}

	$target.siblings('.inp_txt').val("");
	$target.siblings('.btn_file_del').hide();

	if (navigator.appName.indexOf("Explorer") > -1) {
		$target.replaceWith( $target.clone(true) );
	} else {
		$target.val("");
	}

	return false;
}


  // 천단위 콤마 (소수점포함)
  function numberWithCommas(num) {
    var parts = num.toString().split(".");
    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
  }

  // 숫자 체크(숫자 이외 값 모두 제거)
  function chkNumber(obj){
    var tmpValue = $(obj).val().replace(/[^0-9,]/g,'');
    tmpValue = tmpValue.replace(/[,]/g,'');
    // 천단위 콤마 처리 후 값 강제변경
    obj.value = numberWithCommas(tmpValue);
  }

  function isEmpty(str){
    if(typeof str == "undefined" || str == null || str == "")
        return true;
    else
        return false ;
}