var __certimport=function(c){var p=function(g){function h(b){if(!b)return!1;b=b||window.event;b=b.which||b.keyCode;var a=String.fromCharCode(b);return 8===b?!0:isNaN(a)?!1:32===b?!1:!0}function e(b,a){if(!b||!a)return!0;b=b||window.event;b=b.which||b.keyCode;if(16==b||9==b)return!0;"unicrsv3-auth3"!=a.id?4===a.value.length&&("unicrsv3-auth1"===a.id?document.getElementById("unicrsv3-auth2").focus():document.getElementById("unicrsv3-auth3").focus()):"unicrsv3-auth3"==a.id&&5===a.value.length&&document.getElementById("unicrsv3-confirm").focus();return!0}function f(b){if(!b)return!1;if(b){var a=[2,3,4,5,6,7,8,9,2,3,4,5];var d=[48,49,50,51,52,53,54,55,56,57];a=(11-(d[parseInt(b.substring(0,1))]*a[0]+d[parseInt(b.substring(1,2))]*a[1]+d[parseInt(b.substring(2,3))]*a[2]+d[parseInt(b.substring(3,4))]*a[3]+d[parseInt(b.substring(4,5))]*a[4]+d[parseInt(b.substring(5,6))]*a[5]+d[parseInt(b.substring(6,7))]*a[6]+d[parseInt(b.substring(7,8))]*a[7]+d[parseInt(b.substring(8,9))]*a[8]+d[parseInt(b.substring(9,10))]*a[9]+d[parseInt(b.substring(10,11))]*a[10]+d[parseInt(b.substring(11,12))]*a[11])%11)%10}else a=-1;if(-1===a)return!1;b=parseInt(b.substring(12,13));return a===b?!0:!1}function k(b){if(!b)return!1;c.nimservice().Init(c.ESVS.TransServerIP,c.ESVS.TransServerPort,g.args.license,function(a,d,e){if(0!=a)return c.ERROR.Code=a,c.ERROR.Message=d,c.errPopup(c.ERROR.Code,c.ERROR.Message),!1;a=c.bsUtil().GetKeyboardType();d=0;if(""!=a)switch(a){case "ahnlab":d=3;break;case "touchen":d=8;break;default:0}if(0<d){var k={type:d,value:{pageID:$ASTX2.getE2EPageID()+"",inputID:{num1:$ASTX2.getE2EInputID(document.getElementById("unicrsv3-auth1")),num2:$ASTX2.getE2EInputID(document.getElementById("unicrsv3-auth2")),num3:$ASTX2.getE2EInputID(document.getElementById("unicrsv3-auth3"))}}};c.nimservice().VerifyCertNum(e,k,function(a,d){if(0!=a)return-1310==a?c.uiUtil().msgBox(b.IDS_UI_ERR_AUTH_GETTING):-1311==a?c.uiUtil().msgBox(b.IDS_UI_ERR_AUTH_LENGTH):c.uiUtil().msgBox(b.IDS_UI_ERR_AUTH_NUMBER),!1;g.onConfirm(k,e);return!0})}else{a=document.getElementById("unicrsv3-auth1");d=document.getElementById("unicrsv3-auth2");var m=document.getElementById("unicrsv3-auth3");a=""+a.value+d.value+m.value;if(13!=a.length)return c.uiUtil().msgBox(b.IDS_UI_ERR_AUTH_LENGTH),!1;if(a.match(/([^0-9])/))return c.uiUtil().msgBox(b.IDS_UI_ERR_ONLY_NUMBER),!1;if(!f(a))return c.uiUtil().msgBox(b.IDS_UI_ERR_AUTH_NUMBER),!1;g.onConfirm(a,e);return!0}})}var n=function(){var b=window.XMLHttpRequest?new window.XMLHttpRequest:new ActiveXObject("MSXML2.XMLHTTP.3.0");b.open("GET",c.ESVS.SRCPath+"unisignweb/rsrc/layout/certimport.html?version="+c.ver,!1);b.send(null);return b.responseText};return function(){var b=c.CustomEval(n),a=c.transferLang();c.ESVS.TargetObj.innerHTML=b();b=document.getElementById("unicrsv3-import");b.style.backgroundImage="url("+c.ESVS.SRCPath+"unisignweb/rsrc/img/main_bg.jpg)";document.getElementById("unicrsv3-title").appendChild(document.createTextNode(a.IDS_TRANS_TITLE));document.getElementById("unicrsv3-log").setAttribute("src",c.ESVS.SRCPath+"unisignweb/rsrc/img/main_title.jpg",0);document.getElementById("unicrsv3-code-text").appendChild(document.createTextNode(a.IDS_TRANS_CODE));var d=document.getElementById("unicrsv3-auth1");d.maxLength=4;d.onkeypress=function(a){return h(a?a:event)};d.onkeyup=function(a){return e(a?a:event,this)};d=document.getElementById("unicrsv3-auth2");d.maxLength=4;d.onkeypress=function(a){return h(a?a:event)};d.onkeyup=function(a){return e(a?a:event,this)};d=document.getElementById("unicrsv3-auth3");d.maxLength=5;d.onkeypress=function(a){return h(a?a:event)};d.onkeyup=function(a){return e(a?a:event,this)};d=document.getElementById("unicrsv3-noti-title");var f=document.getElementById("unicrsv3-noti-sub1"),l=document.getElementById("unicrsv3-noti-sub2");c.ESVS.g2b?(d.appendChild(document.createTextNode(a.IDS_TRANS_G2B_NOTI_TITLE)),f.appendChild(document.createTextNode(a.IDS_TRANS_G2B_IMPORT_NOTI_SUB_1)),l.appendChild(document.createTextNode(a.IDS_TRANS_G2B_IMPORT_NOTI_SUB_2))):c.ESVS.toss?(d.style.display="none",f.appendChild(document.createTextNode(a.IDS_TRANS_TOSS_IMPORT)),b.style.height="250px"):(d.appendChild(document.createTextNode(a.IDS_TRANS_NOTI_TITLE)),f.appendChild(document.createTextNode(a.IDS_TRANS_NOTI_SUB_1)),l.appendChild(document.createTextNode(a.IDS_TRANS_IMPORT_NOTI_SUB_2)));(d=document.getElementById("unicrsv3-ver-text"))&&d.appendChild(document.createTextNode("Version ( UI : "+c.ver+", Server : "+c.nimVersion+" )"));d=document.getElementById("unicrsv3-confirm");d.setAttribute("value",a.IDS_TRANS_CONFIRM,0);d.setAttribute("title",a.IDS_TRANS_CONFIRM+a.IDS_TRANS_BUTTON,0);d.onclick=function(){k(a)};d=document.getElementById("unicrsv3-cancel");d.setAttribute("value",a.IDS_TRANS_CANCEL,0);d.setAttribute("title",a.IDS_TRANS_CANCEL+a.IDS_TRANS_BUTTON,0);d.onclick=function(){g.onCancel()};return b}()};return function(g){var h=c.uiLayerLevel,e=c.uiUtil().getOverlay(h),f=p({type:g.type,args:g.args,onConfirm:g.onConfirm,onCancel:g.onCancel});f.style.zIndex=h+1;document.body.insertBefore(e,document.body.firstChild);return{show:function(){draggable(f,document.getElementById("unicrsv3-title"));e.style.display="block";f.style.top=c.uiUtil().getScrollTop()+(c.uiUtil().getViewportHeight()-c.uiUtil().getNumSize(c.uiUtil().getStyle(f,"height","height")))/3+"px";f.style.left=c.uiUtil().getScrollLeft()+(c.uiUtil().getViewportWidth()-c.uiUtil().getNumSize(c.uiUtil().getStyle(f,"width","width")))/2+"px";f.style.display="block";c.uiLayerLevel+=10;c.ESVS.TabIndex+=20;c.bsUtil().ahnlabInit();setTimeout(function(){var c=f.getElementsByTagName("input");if(0<c.length)for(var e=0;e<c.length;e++)"unicrsv3-auth1"==c[e].id&&c[e].focus()},10)},hide:function(){e.style.display="none";f.style.display="none"},dispose:function(){var g=f.parentNode;g&&g.removeChild(f);e&&e.parentNode&&e.parentNode.removeChild(e);c.uiLayerLevel-=10;c.ESVS.TabIndex-=20}}}};