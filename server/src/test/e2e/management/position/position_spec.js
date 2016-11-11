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
        element(by.model('ctrl.position.order')).sendKeys('1');
        element(by.model('ctrl.position.timing')).click();
        element(by.model('ctrl.position.active')).click();

        element(by.id('createPosition')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(3);
        var position = element(by.repeater('position in ctrl.positions').row(0));
        expect(position.element(by.binding('position.name')).getText()).toEqual('Position 1');
        expect(position.element(by.binding('position.order')).getText()).toBe('1');
        expect(position.element(by.model('position.active')).isSelected()).toBe(true);
        expect(position.element(by.model('position.canStart')).isSelected()).toBe(false);
        expect(position.element(by.model('position.showAllEntries')).isSelected()).toBe(false);
    });

    it('should have remove position', function() {
        var table = element(by.id('positions'));
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.repeater('position in ctrl.positions').row(0)).element(by.css('.delete-position')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });

    it('should have update position', function() {
        var table = element(by.id('positions'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);

        element(by.model('ctrl.position.name')).sendKeys('Position 1');
        element(by.model('ctrl.position.order')).sendKeys('1');
        element(by.model('ctrl.position.timing')).click();
        element(by.model('ctrl.position.active')).click();

        element(by.id('createPosition')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(3);
        var position = element(by.repeater('position in ctrl.positions').row(0));
        var name = position.element(by.binding('position.name'));
        var order = position.element(by.binding('position.order'));
        var active = position.element(by.model('position.active'));
        var canStart = position.element(by.model('position.canStart'));
        var showAllEntries = position.element(by.model('position.showAllEntries'));
        expect(name.getText()).toEqual('Position 1');
        expect(order.getText()).toBe('1');
        expect(active.isSelected()).toBe(true);
        expect(canStart.isSelected()).toBe(false);
        expect(showAllEntries.isSelected()).toBe(false);

        active.click();
        canStart.click();
        showAllEntries.click();
        position.element(by.css('.update-position')).click();
        expect(name.getText()).toEqual('Position 1');
        expect(order.getText()).toBe('1');
        expect(active.isSelected()).toBe(false);
        expect(canStart.isSelected()).toBe(true);
        expect(showAllEntries.isSelected()).toBe(true);
    });

    it('should trigger name check directive', function() {
        var table = element(by.id('positions'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.model('ctrl.position.name')).sendKeys('Position 1');
        element(by.model('ctrl.position.order')).sendKeys('2');

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
        element(by.model('ctrl.position.order')).clear();
    });

    it('should trigger order check directive', function() {
        var table = element(by.id('positions'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.model('ctrl.position.name')).sendKeys('Position 2');
        element(by.model('ctrl.position.order')).sendKeys('1');

        var errors = $('.errors.order-error-message');
        expect(errors.isPresent()).toBe(true);

        expect(errors.$('[ng-message=positionOrderExists]').isPresent()).toBe(true);
        expect(errors.$('[ng-message=required]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=max]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=min]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=positionNameExists]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=minlength]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=maxlength]').isPresent()).toBe(false);

        element(by.model('ctrl.position.name')).clear();
        element(by.model('ctrl.position.order')).clear();
    });
});