var SecureCache = (function() {
	var exec = function(action, cb, args)
	{
		cordova.exec(cb, function(err) { cb(err); }, 'SecureCache', action, args);
	};

	this.foreground = function(title, text, cb)
	{
		exec('foreground', cb, [title, text]);
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

module.exports = new SecureCache();

