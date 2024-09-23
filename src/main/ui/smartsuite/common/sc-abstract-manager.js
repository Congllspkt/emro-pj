/**
* SCAbstractManager
*/
(function(window) {
	
	SCAbstractManager = {
	
		/** Ready Callback Function */
		_callbacks : [],
	
		/** Availability */
		onReady : function(callback) {
			if(this._readied) {
				return callback();
			}
			this._callbacks.push(callback);
		},
		
		/** After object initialization is complete, the registered callback function is executed */
		onCompleted : function(){
			var callbacks = this._callbacks;
			this._readied = true;
			while((fn = callbacks.shift(0))) {
				if(fn && Polymer.$Util.isFunction(fn)){
					this.onReady(fn);
				}
			}
		}
	};
	
}(window));