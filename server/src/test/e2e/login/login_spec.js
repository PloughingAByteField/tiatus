describe('src.test.e2e.login.login_spec.js', function() {
 it('should get to login', function() {
    browser.get('/');
    return browser.driver.wait(function() {
        return browser.driver.getCurrentUrl().then(function(url) {
            return /login/.test(url);
        });
    }, 10000);

  });

 it('should login', function() {
    browser.get('/login.html');
    expect(element(by.id('login')).isEnabled()).toBe(false);
    element(by.model('ctrl.user.userName')).sendKeys('admin');
    element(by.model('ctrl.user.password')).sendKeys('12345678');

    expect(element(by.id('login')).isEnabled()).toBe(true);
    element(by.id('login')).click();

    return browser.driver.wait(function() {
        return browser.driver.getCurrentUrl().then(function(url) {
            return /management/.test(url);
        });
    }, 10000);

  });

});