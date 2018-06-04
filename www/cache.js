var SecureCache = (function() {
	var exec = function(action, cb, args) {
		cordova.exec(function(res) { cb(null, res); }, function(err) { cb(err, null); }, 'SecureCache', action, args);
	};

	this.start = function(cb) {
		exec('start', cb, []);
	};

	this.set = function(data, cb) {
		exec('set', cb, [data]);
	};

	this.wipe = function(cb) {
		exec('wipe', cb, []);
	}

	this.get = function(cb) {
		exec('get', cb, []);
	};

	this.stop = function(cb) {
		exec('stop', cb, []);
	}
});

module.exports = new SecureCache();

