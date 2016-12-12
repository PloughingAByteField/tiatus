describe('src.test.e2e.management.entry.entry_spec.js', function() {
  it('should be to get to page', function() {
    browser.get('/management/management.html#/entry');
    return browser.driver.wait(function() {
      return browser.driver.getCurrentUrl().then(function(url) {
        return /entry/.test(url);
      });
    }, 5000);
  });

  it('should have a title', function() {
    expect(browser.getTitle()).toEqual('Entries');
  });

  it('should have empty table', function() {
    var table = element(by.id('entries'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(2);
  });

  it('should be able to click on create entry', function() {
    element(by.id('addEntry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry_details");
    browser.get('/management/management.html#/entry');
  });

  it('should be able to create an entry', function() {
    element(by.id('addEntry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry_details");
    element(by.cssContainingText('option', 'Event 1')).click();
    element(by.cssContainingText('option', 'Club 1')).click();
    element(by.id('addEntry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry");
    var table = element(by.id('entries'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(3);
    var entries = element.all(by.repeater('entry in $data'));
    expect(entries.count()).toBe(1);
    var createdEntry = element(by.repeater('entry in $data').row(0).column('entry.club'));
    expect(createdEntry.getText()).toEqual('Club 1');
  });

  it('should be able to create a multi club entry', function() {
    element(by.id('addEntry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry_details");
    element(by.cssContainingText('option', 'Event 2')).click();
    element(by.cssContainingText('option', 'Club 1')).click();
    element(by.cssContainingText('option', 'Club 2')).click();
    element(by.id('addEntry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry");
    var table = element(by.id('entries'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(4);
    var entries = element.all(by.repeater('entry in $data'));
    expect(entries.count()).toBe(2);
    var createdEntry = element(by.repeater('entry in $data').row(1).column('entry.club'));
    expect(createdEntry.getText()).toContain('Club 1');
    expect(createdEntry.getText()).toContain('Club 2');
  });

  it('should be able to remove an entry', function() {
    expect(browser.driver.getCurrentUrl()).toContain("#/entry");
    var entries = element.all(by.repeater('entry in $data'));
    expect(entries.count()).toBe(2);
    element(by.repeater('entry in $data').row(0)).element(by.css('.remove-entry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry");
    var table = element(by.id('entries'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(3);
    var entries = element.all(by.repeater('entry in $data'));
    expect(entries.count()).toBe(1);
  });

  it('should be able to update an entry', function() {
    expect(browser.driver.getCurrentUrl()).toContain("#/entry");
    var entries = element.all(by.repeater('entry in $data'));
    expect(entries.count()).toBe(1);

    var rowEntry = element(by.repeater('entry in $data').row(0).column('entry.crew'));
    expect(rowEntry.getText()).toEqual('');
    element(by.repeater('entry in $data').row(0)).element(by.css('.update-entry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry_details");
    element(by.model('ctrl.entry.crew')).sendKeys('A');
    element(by.id('updateEntry')).click();
    expect(browser.driver.getCurrentUrl()).toContain("#/entry");
    var table = element(by.id('entries'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(3);
    var entries = element.all(by.repeater('entry in $data'));
    expect(entries.count()).toBe(1);
    var rowEntry = element(by.repeater('entry in $data').row(0).column('entry.crew'));
    expect(rowEntry.getText()).toEqual('A');
  });
});