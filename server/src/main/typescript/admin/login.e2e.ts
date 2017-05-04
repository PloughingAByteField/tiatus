import { browser, by, element } from 'protractor';

describe('Login', () => {

  beforeEach(() => {
    browser.get('/login/');
  });

  it('should enable go to admin on sumbmit', () => {
    expect(element(by.css('button')).isEnabled()).toBe(false);
    element(by.id('name')).sendKeys('admin');
    element(by.id('password')).sendKeys('12345678');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    return browser.driver.wait(() => {
      return browser.driver.getCurrentUrl().then((url) => {
        return /admin/.test(url);
      });
    }, 10000);
  });
});
