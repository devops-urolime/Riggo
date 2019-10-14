var rig = rig || {};

(function($) {
var ns = rig;

function st_load() {
	show_loader($('#ajax_load'));
}

function st_stop() {
	hide_loader($('#ajax_load'));
}

function show_loader(el) {
	if ($(el).spin) {
		$(el).show();
		$(el).spin();
	}
}

function hide_loader(el) {
	if ($(el).spin) {
		$(el).spin(false);
		$(el).hide();
	}
}

function selectAll(cb,row) {
	$("[id*=recSelected]").each(function() {
		this.checked = cb.checked;
	});
}

function checkSelectAll(cb) {
	$("[id*=pageSelected]").get(0).checked =
			($("[id*=recSelected]:checked").length == $("[id*=recSelected]").length);
}

ns.$                = $;
ns.st_load			= st_load;
ns.st_stop			= st_stop;
ns.selectAll		= selectAll;
ns.checkSelectAll	= checkSelectAll;

})(jQuery.noConflict(true));