'use strict';
define(function(require, exports) {

    exports.testCommon = function(it, EnumService) {
        if (!runCase('common')) {
            return;
        }

        it("the EnumService should be defined", function() {
            expect(EnumService).not.to.be(undefined);
        });

        it("the font file should be accessable", function(done) {
            $.ajax({
			    url: 'lib/font-awesome/font/fontawesome-webfont.woff',
			    type: 'GET'
			}).then(function(res) {
			    done();
			}, function() {
				throw new Error('the font file is not accessable.');
			});
        });

        
    };

});