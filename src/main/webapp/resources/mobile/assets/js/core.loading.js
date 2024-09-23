/**
 * 페이지 로딩
 * @author JSCHOI
 * @since  2020
 * @version 1.0
 * @see
 * Copyright (C)  All right reserved.
*/

var coreLoading = {
    show : function(msg,animation) {
    	if(animation != null)
    	    $('body').loadingModal({ text: msg }).loadingModal('animation', animation);
    	else
    	    $('body').loadingModal({ text: msg }).loadingModal('animation', 'wave');
    },		
    hide : function() {
	$('body').loadingModal('destroy') ;
    },			 		
        	
};