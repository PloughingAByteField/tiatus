import { browser, by, element, ExpectedConditions } from 'protractor';

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

  it('should create new race on sumbmit', () => {
    expect(element(by.id('submit')).isPresent()).toBe(true);
    expect(element(by.id('submit')).isEnabled()).toBe(false);

    const table = element(by.id('races'));
    expect(table.isPresent()).toBe(true);
    const rows = table.$$('tr');
    expect(rows.count()).toBe(0);

    element(by.css('input[formcontrolname=name]')).sendKeys('Race 1');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    browser.wait(ExpectedConditions.visibilityOf(element(by.id('name-0'))), 5000) ;
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    expect(element(by.css('input[formcontrolname=raceOrder]')).getAttribute('value')).toBe('2');
    expect(element(by.id('name-0')).getAttribute('value')).toBe('Race 1');
    expect(element(by.id('order-0')).getText()).toBe('1');
  });

  it('should create a second new race on sumbmit', () => {
    expect(element(by.id('submit')).isPresent()).toBe(true);
    expect(element(by.id('submit')).isEnabled()).toBe(false);

    const table = element(by.id('races'));
    expect(table.isPresent()).toBe(true);
    const rows = table.$$('tr');
    expect(rows.count()).toBe(1);

    element(by.css('input[formcontrolname=name]')).sendKeys('Race 2');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    browser.wait(ExpectedConditions.visibilityOf(element(by.id('name-1'))), 5000) ;
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    expect(element(by.css('input[formcontrolname=raceOrder]')).getAttribute('value')).toBe('3');
    expect(element(by.id('name-1')).getAttribute('value')).toBe('Race 2');
    expect(element(by.id('order-1')).getText()).toBe('2');
  });
});
