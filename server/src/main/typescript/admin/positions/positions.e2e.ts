import { browser, by, element, ExpectedConditions } from 'protractor';

describe('Positions', () => {

  beforeEach(() => {
    browser.waitForAngularEnabled(false);
    browser.get('/admin/positions');
  });

  it('should have a form input called name', () => {
    expect(element(by.css('input[formcontrolname=newName]')).isPresent()).toBe(true);
  });

  it('should create new position on sumbmit', () => {
    expect(element(by.id('submit')).isPresent()).toBe(true);
    expect(element(by.id('submit')).isEnabled()).toBe(false);

    const table = element(by.id('positions'));
    expect(table.isPresent()).toBe(true);
    const rows = table.$$('tr');
    expect(rows.count()).toBe(0);

    element(by.css('input[formcontrolname=newName]')).sendKeys('Position 1');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    browser.wait(ExpectedConditions.visibilityOf(element(by.id('name-0'))), 5000) ;
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    expect(element(by.id('name-0')).getAttribute('value')).toBe('Position 1');
  });

  it('should create a second new position on sumbmit', () => {
    expect(element(by.id('submit')).isPresent()).toBe(true);
    expect(element(by.id('submit')).isEnabled()).toBe(false);

    const table = element(by.id('positions'));
    expect(table.isPresent()).toBe(true);
    const rows = table.$$('tr');
    expect(rows.count()).toBe(1);

    element(by.css('input[formcontrolname=newName]')).sendKeys('Position 2');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    browser.wait(ExpectedConditions.visibilityOf(element(by.id('name-1'))), 5000) ;
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    expect(element(by.id('name-1')).getAttribute('value')).toBe('Position 2');
  });

  it('should create a third new position on sumbmit', () => {
    expect(element(by.id('submit')).isPresent()).toBe(true);
    expect(element(by.id('submit')).isEnabled()).toBe(false);

    const table = element(by.id('positions'));
    expect(table.isPresent()).toBe(true);
    const rows = table.$$('tr');
    expect(rows.count()).toBe(2);

    element(by.css('input[formcontrolname=newName]')).sendKeys('Position 3');
    expect(element(by.id('submit')).isEnabled()).toBe(true);

    element(by.id('submit')).click();
    browser.wait(ExpectedConditions.visibilityOf(element(by.id('name-2'))), 5000) ;
    expect(element(by.id('submit')).isEnabled()).toBe(false);
    expect(element(by.id('name-2')).getAttribute('value')).toBe('Position 3');
  });
});
