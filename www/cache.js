window.SecureCache = new (function() {
	var exec = function(action, cb, args)
	{
		cordova.exec(cb, function(err) { cb(err); }, 'SimpleCache', action, args);
	};

	this.foreground = function(cb)
	{
		exec('foreground', cb, []);
	};

	this.unforeground = function(cb)
	{
		exec('unforeground', cb, []);
	};

	this.set = function(data, cb)
	{
		exec('set', cb, [data]);
	};

	this.get = function(cb)
	{
		exec('get', cb, []);
	};
});

