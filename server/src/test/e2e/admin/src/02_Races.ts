import { NightwatchTests } from 'nightwatch';

const races: NightwatchTests = {

  'Races': () => {
    browser
    .url('http://127.0.0.1:8080/admin/')
    .waitForElementVisible('body', 1000)
    .useXpath()
    .click("//a[text()='Race']")
    .pause(1000)
    .assert.textContains('//races', 'Add Race')
  }
};

export default races;