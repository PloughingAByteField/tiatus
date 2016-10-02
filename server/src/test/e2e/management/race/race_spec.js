describe('src.test.e2e.management.race.race_spec.js', function() {
    it('should be to get to page', function() {
        browser.get('/management/management.html#/race');
        return browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /race/.test(url);
            });
        }, 5000);
    });

    it('should have a title', function() {
        expect(browser.getTitle()).toEqual('Races');
    });

    it('should have empty table', function() {
        var table = element(by.id('races'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });
});