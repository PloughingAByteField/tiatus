import { browser, by, element } from 'protractor';

describe('Races', () => {

  beforeEach(() => {
    browser.waitForAngularEnabled(false);
    browser.get('/admin/races');
  });

  it('should have a form input called name', () => {
    expect(element(by.css('input[formcontrolname=name]')).isPresent()).toBe(true);
  });

  it('should have a form input called raceOrder', () => {
    expect(element(by.css('input[formcontrolname=raceOrder]')).isPresent()).toBe(true);
  });
});
