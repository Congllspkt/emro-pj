/**
 * SCModuleManager
 */
SCModuleManager = new (function() {
	
	var AJAXSETTINGS = SCPreloader.ajaxSettings(),
		CONVERT      = SCPreloader.convert;
	
	function ModuleManagerImpl() {
		this.modules = [];
		this.ccModules = [];
		this.modulePropertiesMap = {};
		this._module();
	}
	
	ModuleManagerImpl.prototype._module = function() {
		return $.ajax('getModules.do', AJAXSETTINGS).then(function(response) {
			return CONVERT(response);
		}).done(function(moduleInfo) {
			this.modules = moduleInfo.modules;
			this.ccModules = moduleInfo.ccModules;
			this.modulePropertiesMap = moduleInfo.modulePropertiesMap;
		}.bind(this)).fail(function() {
			console.error("모듈 조회중 오류가 발생하였습니다.");
		});
	};
	
	ModuleManagerImpl.prototype.getModules = function() {
		return this.modules;
	};
	
	ModuleManagerImpl.prototype.exist = function(module) {
		return this.modules.indexOf(module.toUpperCase()) > -1;
	};
	
	ModuleManagerImpl.prototype.getCcModules = function() {
		return this.ccModules;
	}
	
	ModuleManagerImpl.prototype.getModuleProperties = function(module) {
		return this.modulePropertiesMap[module.toUpperCase()];
	}
	
	ModuleManagerImpl.prototype.getModulePropertyValues = function(module, prop) {
		var propertiesMap = this.modulePropertiesMap[module.toUpperCase()] || {};
		return propertiesMap[prop];
	}
	
	return ModuleManagerImpl;
}());