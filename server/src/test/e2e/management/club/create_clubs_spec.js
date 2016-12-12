describe('src.test.e2e.management.club.create_clubs_spec.js', function() {
  it('should be to get to page', function() {
    browser.get('/management/management.html#/club');
    return browser.driver.wait(function() {
      return browser.driver.getCurrentUrl().then(function(url) {
        return /club/.test(url);
      });
    }, 5000);
  });

  it('should have a title', function() {
    expect(browser.getTitle()).toEqual('Clubs');
  });


  it('should create club', function() {
    var table = element(by.id('clubs'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(3);

    element(by.model('ctrl.club.clubName')).sendKeys('Club 1');
    element(by.id('createClub')).click();

    rows = table.$$('tr');
    expect(rows.count()).toBe(4);
    var club = element(by.repeater('club in ctrl.clubs').row(1));
    expect(club.element(by.model('club.clubName')).getAttribute('value')).toEqual('Club 1');
  });

  it('should create club', function() {
    var table = element(by.id('clubs'));
    expect(table.isPresent());
    var rows = table.$$('tr');
    expect(rows.count()).toBe(4);

    element(by.model('ctrl.club.clubName')).sendKeys('Club 3');
    element(by.id('createClub')).click();

    rows = table.$$('tr');
    expect(rows.count()).toBe(5);
    var club = element(by.repeater('club in ctrl.clubs').row(2));
    expect(club.element(by.model('club.clubName')).getAttribute('value')).toEqual('Club 3');
  });


});