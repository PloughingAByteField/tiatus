describe('src.test.e2e.setup_spec.js', function() {
 it('should get to setup', function() {
    browser.get('/');
    return browser.driver.wait(function() {
        return browser.driver.getCurrentUrl().then(function(url) {
            return /setup/.test(url);
        });
    }, 5000);
  });

  it('should check both inputs are required and button disabled', function() {
    browser.get('setup/setup.html');
    var nameError = element(by.id('nameError'));
    var passwordError = element(by.id('passwordError'));
    expect(nameError.getText()).toBe('User name is required');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);
  });

  it('should check min length of username', function() {
    browser.get('setup/setup.html');
    var nameError = element(by.id('nameError'));
    var passwordError = element(by.id('passwordError'));
    expect(nameError.getText()).toBe('User name is required');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);

    element(by.model('ctrl.user.userName')).sendKeys('ad');
    expect(nameError.getText()).toBe('User name is required to have 5 or more chars');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);
  });

  it('should check max length of username', function() {
    browser.get('setup/setup.html');
    var nameError = element(by.id('nameError'));
    var passwordError = element(by.id('passwordError'));
    expect(nameError.getText()).toBe('User name is required');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);

    element(by.model('ctrl.user.userName')).sendKeys('12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890');
    expect(nameError.getText()).toBe('User name is required to have less than 256 chars');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);
  });

  it('should check min length of password', function() {
    browser.get('setup/setup.html');
    var nameError = element(by.id('nameError'));
    var passwordError = element(by.id('passwordError'));
    expect(nameError.getText()).toBe('User name is required');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);

    element(by.model('ctrl.user.userName')).sendKeys('admin');
    expect(nameError.getText()).toBe('');

    element(by.model('ctrl.user.password')).sendKeys('1');
    expect(passwordError.getText()).toBe('Password is required to have 8 or more chars');
    expect(element(by.id('create')).isEnabled()).toBe(false);
  });

  it('should check max length of password', function() {
    browser.get('setup/setup.html');
    var nameError = element(by.id('nameError'));
    var passwordError = element(by.id('passwordError'));
    expect(nameError.getText()).toBe('User name is required');
    expect(passwordError.getText()).toBe('Password is required');
    expect(element(by.id('create')).isEnabled()).toBe(false);

    element(by.model('ctrl.user.userName')).sendKeys('admin');
    expect(nameError.getText()).toBe('');

    element(by.model('ctrl.user.password')).sendKeys('123456789012345678901234567890');
    expect(passwordError.getText()).toBe('Password is required to have less than 20 chars');
    expect(element(by.id('create')).isEnabled()).toBe(false);
  });

  it('should create user', function() {
    browser.get('setup/setup.html');

    expect(element(by.id('create')).isEnabled()).toBe(false);
    element(by.model('ctrl.user.userName')).sendKeys('admin');
    element(by.model('ctrl.user.password')).sendKeys('12345678');

    expect(element(by.id('create')).isEnabled()).toBe(true);
    element(by.id('create')).click();

//     wait for redirect to login
    return browser.driver.wait(function() {
      return browser.driver.getCurrentUrl().then(function(url) {
        return /login/.test(url);
      });
    }, 5000);
  });
});
