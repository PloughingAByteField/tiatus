describe('src.test.e2e.management.position.position_spec.js', function() {
    it('should be to get to page', function() {
        browser.get('/management/management.html#/position');
        return browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /position/.test(url);
            });
        }, 5000);
    });

    it('should have a title', function() {
        expect(browser.getTitle()).toEqual('Positions');
    });

    it('should have empty table', function() {
        var table = element(by.id('positions'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });

    it('should have create position', function() {
        var table = element(by.id('positions'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);

        element(by.model('ctrl.position.name')).sendKeys('Position 1');
        element(by.id('createPosition')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(3);
        var position = element(by.repeater('position in ctrl.positions').row(0));
        expect(position.element(by.binding('position.name')).getText()).toEqual('Position 1');
    });

    it('should have remove position', function() {
        var table = element(by.id('positions'));
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.repeater('position in ctrl.positions').row(0)).element(by.css('.delete-position')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });

    it('should trigger name check directive', function() {
        var table = element(by.id('positions'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);

        element(by.model('ctrl.position.name')).sendKeys('Position 1');

        var errors = $('.errors.name-error-message');
        expect(errors.isPresent()).toBe(true);

        expect(errors.$('[ng-message=positionOrderExists]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=required]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=max]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=min]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=positionNameExists]').isPresent()).toBe(true);
        expect(errors.$('[ng-message=minlength]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=maxlength]').isPresent()).toBe(false);

        element(by.model('ctrl.position.name')).clear();
    });

});