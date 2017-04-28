import { browser, by, element } from 'protractor';

describe('Login', () => {

  beforeEach(() => {
    browser.get('/login/');
  });

  it('should have a title', () => {
    const subject = browser.getTitle();
    const result  = 'Tiatus Timing System';
    expect(subject).toEqual(result);
  });

  it('should have a form called loginForm', () => {
    expect(element(by.id('loginForm')).isPresent()).toBe(true);
  });

  it('should have a form input called name', () => {
    expect(element(by.id('name')).isPresent()).toBe(true);
  });

  it('should have a form input called password', () => {
    expect(element(by.id('password')).isPresent()).toBe(true);
  });

  it('should have submit button with text', () => {
    const subject = element(by.id('submit'));
    const text = subject.getText();
    const result  = 'Login';
    expect(text).toEqual(result);
  });

  it('should have submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should have error fields hidden', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    expect(element(by.id('userRequired')).isPresent()).toBeFalsy();
    expect(element(by.id('userMinLength')).isPresent()).toBeFalsy();
    expect(element(by.id('userMaxLength')).isPresent()).toBeFalsy();
    expect(element(by.id('pwdRequired')).isPresent()).toBeFalsy();
    expect(element(by.id('pwdMinLength')).isPresent()).toBeFalsy();
    expect(element(by.id('pwdMaxLength')).isPresent()).toBeFalsy();
  });

  it('should have warning on user name being required and submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    const subject = element(by.id('userRequired'));
    expect(subject.isPresent()).toBeFalsy();
    element(by.id('name')).click();
    element(by.id('password')).click();
    expect(subject.isPresent()).toBeTruthy();
    const text = subject.getText();
    expect(text).toEqual('User name is required');
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should have warning on not enough chars in user name and submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    const subject = element(by.id('userMinLength'));
    expect(subject.isPresent()).toBeFalsy();
    element(by.id('name')).sendKeys('1');
    element(by.id('password')).click();
    expect(subject.isPresent()).toBeTruthy();
    const text = subject.getText();
    expect(text).toEqual('User name is required to have 5 or more chars');
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should have warning on too many chars in user name and submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    const subject = element(by.id('userMaxLength'));
    expect(subject.isPresent()).toBeFalsy();
    element(by.id('name')).sendKeys('01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567891');
    element(by.id('password')).click();
    expect(subject.isPresent()).toBeTruthy();
    const text = subject.getText();
    expect(text).toEqual('User name is required to have less than 250 chars');
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should have warning on password being required and submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    const subject = element(by.id('pwdRequired'));
    expect(subject.isPresent()).toBeFalsy();
    element(by.id('password')).click();
    element(by.id('name')).click();
    expect(subject.isPresent()).toBeTruthy();
    const text = subject.getText();
    expect(text).toEqual('Password is required');
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should have warning on not enough chars in password and submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    const subject = element(by.id('pwdMinLength'));
    expect(subject.isPresent()).toBeFalsy();
    element(by.id('password')).sendKeys('1');
    element(by.id('name')).click();
    expect(subject.isPresent()).toBeTruthy();
    const text = subject.getText();
    expect(text).toEqual('Password is required to have 8 or more chars');
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should have warning on too many chars in password and submit button disabled', () => {
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    const subject = element(by.id('pwdMaxLength'));
    expect(subject.isPresent()).toBeFalsy();
    element(by.id('password')).sendKeys('01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567891');
    element(by.id('name')).click();
    expect(subject.isPresent()).toBeTruthy();
    const text = subject.getText();
    expect(text).toEqual('Password is required to have less than 250 chars');
    expect(element(by.id('submit')).isEnabled()).toBe(false);
  });

  it('should enable submit button', () => {
    expect(element(by.css('button')).isEnabled()).toBe(false);
    element(by.id('name')).sendKeys('admin');
    element(by.id('password')).sendKeys('12345678');
    expect(element(by.id('submit')).isEnabled()).toBe(true);
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
