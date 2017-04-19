import { browser, by, element } from 'protractor';

describe('Setup', () => {

  beforeEach(() => {
    browser.get('/');
  });

  it('should have a title', () => {
    let subject = browser.getTitle();
    let result  = 'Tiatus Timing System';
    expect(subject).toEqual(result);
  });

  it('should have a form input called name', () => {
    expect(element(by.id('name')).isPresent()).toBe(true);
  });

  it('should have a form input called password', () => {
    expect(element(by.id('password')).isPresent()).toBe(true);
  });

  it('should have submit button with text', () => {
    let subject = element(by.id('submit'));
    let text = subject.getText();
    let result  = 'Complete setup';
    expect(text).toEqual(result);
  });

  it('should have submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should enable submit button', () => {
    expect(element(by.css('button')).isEnabled()).toBe(false);
    element(by.id('name')).sendKeys('admin');
    element(by.id('password')).sendKeys('12345678');
    expect(element(by.id('submit')).isEnabled()).toBe(true);
  });

  it('should enable go to login on sumbmit', () => {
    expect(element(by.css('button')).isEnabled()).toBe(false);
    element(by.id('name')).sendKeys('admin');
    element(by.id('password')).sendKeys('12345678');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    return browser.driver.wait(() => {
      return browser.driver.getCurrentUrl().then((url) => {
        return /login/.test(url);
      });
    }, 10000);
  });
});
