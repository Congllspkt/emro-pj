/**
 	******************************************************************************************
	** @Program-name 	: 엑셀 다운로드 Util (DataLudi용 엑셀 다운로드 스크립트 소스)
	** @Description		: 
	** @Author 			: swAhn
	** @Create Date 	: 2018.03.13
	** @History 		: 2017.11.27 swAhn 최종 수정
	******************************************************************************************
 */
window.DataGridExporter = (function(){
	var exporter = function (){
		 
	};
	
	exporter.prototype.excludeColumnNames = ['__dummy__'];
	
	exporter.prototype.excludeHiddenColumns = false;
	
	exporter.prototype.getDepth = function(columns, depth) {
		
		var depth = depth != null ? depth : 1, 
			currentDepth = depth,
			nvc = 0;
		for (var i = 0,
				len = columns.length,
				column, children; i < len; i++) {
			column = columns[i];
			children = null;
			if (column.columns && column.columns().length > 0) {
				children = column.columns();
				if (children.length > 0) {
					var d = arguments.callee(children, currentDepth + 1);
					if (d > depth) {
						depth = d;
					}
				}
			}
		}
		
		return depth;
	};
	
	exporter.prototype._getHorizontalMergeRange = function(column){
		var children = column.columns && column.columns();
		if(children && children.length > 0){
			var range = 0;
			for(var i = 0 ; i < children.length; i++){
				if(this.excludeHiddenColumns === true && children[i].visible && children[i].visible() === false){
					continue;
				}
				range += arguments.callee(children[i]);
			}
			return range;
		}
		else{
			return 1;
		}
	};
	
	exporter.prototype._getEmptyCell = function(){
		return {
			rows : 1,
			cols : 1
		};
	};
	
	exporter.prototype._getHeaderInfo = function(grid, column){
		
		if(column.isEmptyCell === true){
			return this._getEmptyCell();
		}
		
		if(column.columns && column.columns().length > 0){
			var headerText = column.header().text();
			return {
				headerText : headerText ,
				cols : this._getHorizontalMergeRange(column)
			};
		}
		
		var textAlign = 'left';
		var textAlignmentStyle = column.getProperty('styles').textAlignment();
		if(textAlignmentStyle === 'near'){
			textAlign = 'left';
		}else if(textAlignmentStyle === 'far'){
			textAlign = 'right';
		}else if(textAlignmentStyle === 'center'){
			textAlign = 'center';
		}
		
		var result = {
			dataIndex : column.fieldName(),
			headerText : column.header().text(),
			textAlign : textAlign,
			cols : 1,
			width : column.width()+''
		};
		
		if(column.dropDown){
			result.dropDown = column.dropDown; 
			delete column.dropDown;
		}
		
		if(column.fieldStyle){
			result.fieldStyle = column.fieldStyle;
			delete column.fieldStyle;
		}
		
		return result;
	};
	
	exporter.prototype._getLowerDepthColumns = function(columns){
		var lowerLevelCols = [];
		for(var i = 0 ; i < columns.length; i++){
			var column = columns[i];
			
			if(this.excludeHiddenColumns === true && column.visible() === false){
				continue;
			}
			if(column.visible() !== false && column.columns && column.columns().length > 0){
				lowerLevelCols = lowerLevelCols.concat(column.columns());
			}else{
				
				
				var cellInfo = {
					isEmptyCell : true,
					visible : function(){return true;}
				};
				
				
				
				lowerLevelCols.push(cellInfo);
			}
		}
		return lowerLevelCols;
	};
	
	exporter.prototype._addDropDowns = function(columns, dropDowns){
		
		for(var i = 0; i < columns.length; i++){
			if(dropDowns[columns[i].name()]){
				columns[i].dropDown = dropDowns[columns[i].name()];
			}
			if(columns[i].columns && columns[i].columns().length > 0){
				arguments.callee.call(this, columns[i].columns(), dropDowns);
			}
		}
	};
	
	exporter.prototype._addFieldStyles = function(columns, fieldStyles){
		for(var i = 0; i< columns.length; i++){
			if(this.isObject(fieldStyles[columns[i].name()])){
				columns[i].fieldStyle = JSON.stringify(fieldStyles[columns[i].name()]);
			}
			if(columns[i].columns && columns[i].columns().length > 0){
				arguments.callee.call(this, columns[i].columns(), fieldStyles);
			}
		}
	};
	
	
	exporter.prototype.isObject= function(o){
		return Object.prototype.toString.call(o) === '[object Object]';
	};
	
	exporter.prototype.isArray= function(o){
		return  Object.prototype.toString.call(o) === '[object Array]';
	};
	
	exporter.prototype.isExcludingColumn = function(col){
		if(col && col.name && col.name()){
			var colName = col.name();
			for(var i= 0; i< this.excludeColumnNames.length; i++){
				if(this.excludeColumnNames[i] === colName){
					return true;
				}
			}
		}
		return false;
	};
	
	exporter.prototype._getExportColumns = function(grid, options){
		var cols = grid.getVisibleColumns(),
			headerInfo = [],
			maxDepth = this.getDepth(cols);
		
		for(var i = 0; i < cols.length ; i++){
			if(this.isExcludingColumn(cols[i]) === true){
				cols.splice(i, 1);
				i--;
			}
		}
		
		if(this.isObject(options)){
			if(this.isObject(options.dropDowns)){
				var dropDowns = {};
				
				for(var key in options.dropDowns){
					
					var value = options.dropDowns[key];
					
					if(this.isArray(value)){
						var result = ''; 
						for(var i = 0; i < value.length; i++){
							if(i !== 0){
								result +=",";
							}
							result += value[i];
						}
						value = result;
					}
					dropDowns[key] = value;
				}
				
				this._addDropDowns(cols, dropDowns);
			}
			
			if(this.isObject(options.fieldStyles)){
				this._addFieldStyles(cols, options.fieldStyles);
			}
		}
		
		if(maxDepth > 1){
			var depthColumns = cols;
			for(var i = 0; i < maxDepth; i++){
				var currentDepthHeaderInfo = [];
				for(var j = 0; j< depthColumns.length; j++){
					if(this.excludeHiddenColumns === true && (depthColumns[j].visible() === false)){
						continue;
					}
					
					var depthColumnHeaderInfo = this._getHeaderInfo(grid, depthColumns[j]);
					currentDepthHeaderInfo.push(depthColumnHeaderInfo);
					if(depthColumnHeaderInfo.cols > 1){
						for(var di = 1; di < depthColumnHeaderInfo.cols; di++){
							currentDepthHeaderInfo.push(this._getEmptyCell());
						}
					}
					
					if(depthColumns[j].isEmptyCell || (!(this.excludeHiddenColumns === true && depthColumns[j].hiddenChildHeaders && depthColumns[j].hiddenChildHeaders() === true) && (depthColumns[j].columns && depthColumns[j].columns().length > 0))){
						depthColumnHeaderInfo.rows = 1;
					}else{
						depthColumnHeaderInfo.rows = (maxDepth - i);
					}
					
				}
				headerInfo.push(currentDepthHeaderInfo);
				depthColumns = this._getLowerDepthColumns(depthColumns);
			}
		}
		// 그루핑되지 않았을 경우
		else{
			for(var i = 0; i < cols.length; i++){
				if(this.excludeHiddenColumns === true && cols[i].visible() === false){
					continue;
				}
				
				headerInfo.push(this._getHeaderInfo(grid, cols[i]));
			}
		}
		return headerInfo;
	};
	
	exporter.prototype._getExportUtil = function(){
		return new Export_LargeExportUtil();
	};
	
	/*
	 * options = {
	 *		// not required
	 * 		exportHeaders : [
	 * 			{	// required
	 * 				dataIndex : 'key1',
	 *				// required
	 * 				headerText : 'header1',
	 * 				// notRequired
	 * 				width : 100
	 * 			}
	 * 
	 * 		],
	 * 
	 * 		// required
	 * 		params : {
	 * 			param1 : "param1",
	 * 			param2 : "param2"
	 * 
	 * 		}
	 * 
	 * 
	 * 
	 * }
	 */
	exporter.prototype.doExport = function(grid, options){
		
		options.headers = options.exportHeaders ? options.exportHeaders : this._getExportColumns(grid, options);
		
		this._getExportUtil().doExport(options);
	};
	
	return exporter;
}());